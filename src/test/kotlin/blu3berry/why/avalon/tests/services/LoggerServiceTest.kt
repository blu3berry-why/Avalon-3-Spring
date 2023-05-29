package blu3berry.why.avalon.tests.services

import blu3berry.why.avalon.dal.services.LoggingService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.slf4j.Logger
import org.springframework.boot.test.context.SpringBootTest
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@SpringBootTest
class LoggerServiceTest{

    @Test
    fun `displayReq should construct log message with request details`() {
        val loggingService = LoggingService()
        val request = mock(HttpServletRequest::class.java)
        val logger = mock(Logger::class.java)
        loggingService.logger = logger

        `when`(request.method).thenReturn("GET")
        `when`(request.requestURI).thenReturn("/api/users")

        val parameterNames = mock(Enumeration::class.java) as Enumeration<String>
        `when`(parameterNames.hasMoreElements()).thenReturn(true, true, false)
        `when`(parameterNames.nextElement()).thenReturn("param1", "param2")
        `when`(request.parameterNames).thenReturn(parameterNames)
        `when`(request.getParameter("param1")).thenReturn("value1")
        `when`(request.getParameter("param2")).thenReturn("value2")

        val body = null // Assuming no request body

        loggingService.displayReq(request, body)

        val expectedLogMessage = "\u001B[35mREQUEST\u001B[0m\t method = [\u001B[32mGET\u001B[0m] path = [\u001B[36m/api/users\u001B[0m] parameters = [{param1=value1, param2=value2}] "
        verify(logger).info("log Request:  {}", expectedLogMessage)
    }


    @Test
    fun displayResp_shouldConstructLogMessageWithResponseDetails() {
        // Create mock HttpServletRequest, HttpServletResponse, and body
        val request = mock(HttpServletRequest::class.java)
        val response = mock(HttpServletResponse::class.java)
        val body = "Sample response body"

        // Create an instance of the LoggingService
        val loggingService = LoggingService()

        // Mock the logger
        val logger = mock(Logger::class.java)
        loggingService.logger = logger

        // Call the displayResp method
        loggingService.displayResp(request, response, body)

        // Verify that the logger's info method was called with the expected log message
        verify(logger).info("log Response: {}", "\u001B[32mRESPONSE\u001B[0m\t method = [${request.method}] status = [\u001B[32m${response.status}\u001B[0m] responseBody = [$body]")
    }

}