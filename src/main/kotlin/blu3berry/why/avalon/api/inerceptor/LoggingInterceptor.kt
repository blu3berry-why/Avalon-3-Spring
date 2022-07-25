package blu3berry.why.avalon.api.inerceptor

import blu3berry.why.avalon.dal.services.LoggingService
import io.swagger.v3.oas.models.PathItem
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.ModelAndView
import java.lang.Exception
import java.time.LocalDateTime
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoggingInterceptor(val loggingService: LoggingService) : HandlerInterceptor{

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
            if(request.method == PathItem.HttpMethod.GET.name
                || request.method == PathItem.HttpMethod.DELETE.name
                || request.method == PathItem.HttpMethod.PUT.name
            )    {
                loggingService.displayReq(request,null)
            }
            return true
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