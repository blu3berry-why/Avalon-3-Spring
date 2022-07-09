package blu3berry.why.avalon.api.errorhandling.handler

import blu3berry.why.avalon.api.errorhandling.exceptions.ConflictException
import blu3berry.why.avalon.api.errorhandling.exceptions.ResourceNotFoundException
import blu3berry.why.avalon.model.network.Message
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController


@ControllerAdvice
@RestController
class ExceptionHandlerController {
    @ExceptionHandler(value = [ResourceNotFoundException::class])
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleResourceNotFound(e: ResourceNotFoundException): Message =
        Message(e.message ?: "The requested resource could not be found.")



    @ExceptionHandler(value = [ConflictException::class])
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleConflict(e: ConflictException): Message =
            Message(e.message ?: "The requested operation can not be executed due to conflicts.")

}