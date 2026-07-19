package blu3berry.why.avalon.api.configuration

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springdoc.core.customizers.OperationCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * OpenAPI contract config for the KMP client (Phase 0.5 of the KMP migration).
 *
 * Scope: this spec describes only the game/lobby/user endpoints that the mobile
 * client calls. Auth (login/register) lives in the separate ForwardAuth service
 * (see ForwardAuth/openapi.yaml in the deploy repo), not here.
 */
@Configuration
class OpenApiConfig {

    @Bean
    fun avalonOpenAPI(): OpenAPI = OpenAPI().info(
        Info()
            .title("Avalon Game API")
            .version("0.1.0")
            .description(
                "Game, lobby and user endpoints served by avalon-spring, consumed by the " +
                    "Avalon KMP client (contract-first via kmpgen).\n\n" +
                    "Auth is handled upstream by the ForwardAuth service: the client sends " +
                    "`Authorization: Bearer <jwt>` on every call, the gateway validates it and " +
                    "injects the `$GATEWAY_USERNAME_HEADER` header. That header is therefore " +
                    "omitted from this contract (the client never sets it), and avalon-spring " +
                    "itself declares no security scheme."
            )
    )

    /**
     * F3: strip the gateway-injected identity header from every operation. Controllers
     * declare it as a `@RequestHeader` because it is mandatory server-side (lobby
     * membership), but it is populated by the gateway from the validated JWT — the
     * client never sends it, so it must not appear in the generated client contract.
     */
    @Bean
    fun hideGatewayHeader(): OperationCustomizer = OperationCustomizer { operation, _ ->
        operation.parameters?.removeIf { it.name == GATEWAY_USERNAME_HEADER }
        operation
    }

    companion object {
        const val GATEWAY_USERNAME_HEADER = "Avalon-Header-Logged-In-User-Username"
    }
}
