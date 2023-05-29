package blu3berry.why.avalon.dal.services

import blu3berry.why.avalon.dal.interfaces.ILoggingService
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Service
class LoggingService : ILoggingService {

    var logger: Logger = LoggerFactory.getLogger("LoggingServiceImpl")

    override fun displayReq(request: HttpServletRequest?, body: Any?) {
        val reqMessage = StringBuilder()
        val parameters:Map<String, String>  = getParameters(request)

        reqMessage.append("\u001B[35mREQUEST\u001B[0m\t")
        reqMessage.append(" method = [")
            .append(request?.method.let {
                        when(it){
                            "GET" -> "\u001B[32m$it\u001B[0m" //green
                            "POST" -> "\u001B[34m$it\u001B[0m"  // blue
                            "PUT" -> "\u001B[33m$it\u001B[0m"//yellow
                            "DELETE" -> "\u001B[31m$it\u001B[0m" // red
                            else -> it
                        }
                    })
            .append("]")
        reqMessage.append(" path = [").append(request?.requestURI.let {
            "\u001B[36m$it\u001B[0m"
        }).append("] ")

        if(parameters.isNotEmpty()) {
            reqMessage.append("parameters = [").append(parameters).append("] ");
        }

        if(body != null) {
            reqMessage.append(" body = [").append(body).append("]");
        }

        logger.info("log Request:  {}", reqMessage.toString())
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

        respMessage.append("\u001B[32mRESPONSE\u001B[0m\t")
        respMessage.append(" method = [").append(request?.method).append("]")


        respMessage.append(" status = [")
            .append(response?.status?.let {
                if (it > 299){
                    "\u001B[31m$it\u001B[0m"
                }else{
                    "\u001B[32m$it\u001B[0m"
                }
            })
            .append("]")



        if(headers.isNotEmpty()) {
            respMessage.append(" ResponseHeaders = [").append(headers).append("]")
        }
        respMessage.append(" responseBody = [").append(body).append("]")

        logger.info("log Response: {}",respMessage.toString())
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