package blu3berry.why.avalon.api.configuration

import blu3berry.why.avalon.dal.services.LoggingService

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Type;


    @ControllerAdvice
    class RequestBodyInterceptor : RequestBodyAdviceAdapter() {
        @Autowired
        var logService: LoggingService? = null

        @Autowired
        var request: HttpServletRequest? = null
        override fun afterBodyRead(
            body: Any,
            inputMessage: HttpInputMessage,
            parameter: MethodParameter,
            targetType: Type,
            converterType: Class<out HttpMessageConverter<*>>
        ): Any {
            logService!!.displayReq(request!!, body)
            return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType)
        }

        override fun supports(
            methodParameter: MethodParameter,
            targetType: Type,
            converterType: Class<out HttpMessageConverter<*>>
        ): Boolean {
            return true
        }
    }
