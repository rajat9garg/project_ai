package learn.ai.dating.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import java.time.LocalDateTime

data class ErrorResponse(
    val timestamp: LocalDateTime = LocalDateTime.now(),
    val status: Int,
    val error: String,
    val message: String,
    val errors: List<FieldErrorResponse> = emptyList()
)

data class FieldErrorResponse(
    val field: String,
    val message: String
)

@ControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationExceptions(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val errors = ex.bindingResult.allErrors.map { error ->
            val fieldName = (error as? FieldError)?.field ?: ""
            val errorMessage = error.defaultMessage ?: ""
            FieldErrorResponse(fieldName, errorMessage)
        }
        
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Failed",
            message = "Validation failed for ${errors.size} field(s)",
            errors = errors
        )
        
        return ResponseEntity.badRequest().body(errorResponse)
    }
    
    @ExceptionHandler(ValidationException::class)
    fun handleValidationException(ex: ValidationException): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Failed",
            message = ex.message ?: "Invalid request"
        )
        
        return ResponseEntity.badRequest().body(errorResponse)
    }
}
