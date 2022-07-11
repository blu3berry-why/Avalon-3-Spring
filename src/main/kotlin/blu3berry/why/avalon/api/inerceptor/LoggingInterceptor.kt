package blu3berry.why.avalon.api.inerceptor

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class LoggingInterceptor : HandlerInterceptor{

    val logger: Logger = LoggerFactory.getLogger(LoggingInterceptor::class.java)

    fun createLog(methodType:String, path:String, username: String) : String{
        return StringBuilder().let {
            it.append("At : [")
            it.append(LocalDateTime.now())
            it.append("] Request: (")
            it.append(methodType)
            it.append(") on path \"")
            it.append(path)
            it.append("\" by user =")
            it.append(username)
            it.toString()
        }
    }

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        logger.info(createLog(request.method, request.pathInfo, "username"))
        return super.preHandle(request, response, handler)
    }

    override fun postHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        modelAndView: ModelAndView?
    ) {
        super.postHandle(request, response, handler, modelAndView)
    }

    override fun afterCompletion(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any,
        ex: Exception?
    ) {
        super.afterCompletion(request, response, handler, ex)
    }


}