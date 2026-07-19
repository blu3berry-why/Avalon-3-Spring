package blu3berry.why.avalon

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import java.io.File

/**
 * Phase 0.5 contract gate: boots the app, pulls the springdoc-generated OpenAPI, asserts the
 * KMP client contract shape, and writes the versioned `openapi.json` that Phase 1's kmpgen
 * consumes. Runs offline — Firebase init is swallowed and Mongo connects lazily, so no live
 * infra is needed (same as the existing @SpringBootTest context test).
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OpenApiExportTest {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Test
    fun `dump openapi spec and assert client contract shape`() {
        val json = restTemplate.getForObject("/v3/api-docs", String::class.java)
        assertNotNull(json, "springdoc did not serve /v3/api-docs")

        val mapper = ObjectMapper()
        val root = mapper.readTree(json)

        // Write the versioned artifact first so it is inspectable even if an assertion fails.
        File("openapi.json").writeText(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root) + "\n")

        // Assert on the structured spec, not raw-string substrings (which also match prose descriptions).
        val paths = root.path("paths").fieldNames().asSequence().toSet()
        val parameterNames = root.path("paths")
            .flatMap { pathItem -> pathItem }          // operations under each path
            .flatMap { op -> op.path("parameters") }   // parameters under each operation
            .map { it.path("name").asText() }
            .toSet()

        // Client-facing paths are present.
        assertTrue(paths.any { it.startsWith("/lobby/") }, "new REST lobby paths missing: $paths")
        assertTrue(paths.any { it.startsWith("/game/") }, "game paths missing: $paths")

        // Legacy + infra paths are hidden (F2 / F4).
        assertFalse("/createlobby" in paths, "legacy OldEndpointsController path leaked")
        assertFalse("/tryheader" in paths, "TestController endpoint leaked")
        assertFalse("/topic/subscription" in paths, "NotificationController endpoint leaked")

        // Gateway-injected identity header is stripped (F3) — the client never sends it.
        assertFalse(
            GATEWAY_USERNAME_HEADER in parameterNames,
            "gateway header leaked into the client contract as a parameter"
        )
    }

    private companion object {
        const val GATEWAY_USERNAME_HEADER = "Avalon-Header-Logged-In-User-Username"
    }
}
