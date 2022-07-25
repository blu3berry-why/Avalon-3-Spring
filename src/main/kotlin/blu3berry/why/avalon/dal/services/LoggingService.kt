package blu3berry.why.avalon.dal.services

import blu3berry.why.avalon.dal.services.interfaces.ILoggingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import kotlin.collections.HashMap


@Service
class LoggingService : ILoggingService{

    val logger: Logger = LoggerFactory.getLogger("LoggingServiceImpl")

    override fun displayReq(request: HttpServletRequest?, body: Any?) {
        val reqMessage = StringBuilder()
        val parameters:Map<String, String>  = getParameters(request)

        reqMessage.append("REQUEST ")
        reqMessage.append("method = [").append(request?.method).append("]")
        reqMessage.append(" path = [").append(request?.requestURI).append("] ")

        if(parameters.isNotEmpty()) {
            reqMessage.append(" parameters = [").append(parameters).append("] ");
        }

        if(body != null) {
            reqMessage.append(" body = [").append(body).append("]");
        }

        logger.info("log Request: {}", reqMessage)
}
private fun getParameters(request: HttpServletRequest?): Map<String, String> {
        val parameters: MutableMap<String,String> = mutableMapOf()
        val params = request?.parameterNames ?: return parameters
        while (params.hasMoreElements()){
            val paramName = params.nextElement()
            val paramValue = request.getParameter(paramName)
            parameters.put(paramName, paramValue)
        }
        return parameters
    }

    override fun displayResp(request: HttpServletRequest?, response: HttpServletResponse?, body: Any?) {
        val respMessage = StringBuilder()
        val headers = getHeaders(response)

        respMessage.append("RESPONSE ")
        respMessage.append(" method = [").append(request?.method).append("]")
        respMessage.append(" status = [").append(response?.status).append("]")

        if(headers.isNotEmpty()) {
            respMessage.append(" ResponseHeaders = [").append(headers).append("]")
        }
        respMessage.append(" responseBody = [").append(body).append("]")

        logger.info("logResponse: {}",respMessage)
    }

    private fun getHeaders(response: HttpServletResponse?): Map<String,String> {
        val headers: MutableMap<String,String> = mutableMapOf()
        val headerMap = response?.headerNames ?: return headers
        headerMap.forEach { key ->
            headers.put(key, response.getHeader(key))
        }
        return headers
    }
}