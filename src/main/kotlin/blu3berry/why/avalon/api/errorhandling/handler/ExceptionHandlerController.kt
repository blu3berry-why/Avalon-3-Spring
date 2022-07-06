package blu3berry.why.avalon.api.errorhandling.handler

import blu3berry.why.avalon.api.errorhandling.exceptions.ConflictException
import blu3berry.why.avalon.api.errorhandling.exceptions.ResourceNotFoundException
import blu3berry.why.avalon.model.network.Message
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
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
        if (e.message == null)
            Message("The requested resource could not be found.")
        else
            Message(e.message)

    @ExceptionHandler(value = [ConflictException::class])
    @ResponseStatus(HttpStatus.CONFLICT)
    fun handleConflict(e: ConflictException): Message =
        if (e.message == null)
            Message("The requested operation can not be executed due to conflicts.")
        else
            Message(e.message)
}