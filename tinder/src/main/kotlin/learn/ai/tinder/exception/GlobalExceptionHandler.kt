package learn.ai.tinder.exception

import org.slf4j.LoggerFactory
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.Instant

/**
 * Global exception handler for the application.
 */
@ControllerAdvice
class GlobalExceptionHandler : ResponseEntityExceptionHandler() {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Handles validation exceptions for @Valid annotated objects.
     */
    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errors = ex.bindingResult.allErrors
            .filterIsInstance<FieldError>()
            .associate { it.field to (it.defaultMessage ?: "No error message available") }
            .map { "${it.key}: ${it.value}" }
            .sorted()

        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Validation Failed",
            message = "Validation failed for request",
            timestamp = Instant.now(),
            errors = errors
        )

        logger.warn("Validation failed - Path: {}, Errors: {} $errors")
        return ResponseEntity.badRequest().body(errorResponse)
    }

    /**
     * Handles JSON parse errors.
     */
    override fun handleHttpMessageNotReadable(
        ex: HttpMessageNotReadableException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any> {
        val errorMessage = ex.mostSpecificCause.message ?: "Invalid JSON format"
        val errorResponse = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = "Bad Request",
            message = "Malformed JSON request: $errorMessage",
            timestamp = Instant.now()
        )

        logger.warn("Malformed JSON request - Path: {}, Error: {} ${request.getDescription(false)}, $errorMessage")
        return ResponseEntity.badRequest().body(errorResponse)
    }

    /**
     * Handles UserAlreadyExistsException.
     */
    @ExceptionHandler(UserAlreadyExistsException::class)
    fun handleUserAlreadyExists(
        ex: UserAlreadyExistsException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Conflict",
            message = ex.message ?: "User already exists",
            timestamp = Instant.now()
        )

        logger.warn("Malformed JSON request - Path: {}, Error: {} ${request.getDescription(false)}")

        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }

    /**
     * Handles database constraint violations.
     */
    @ExceptionHandler(DataIntegrityViolationException::class)
    fun handleDataIntegrityViolation(
        ex: DataIntegrityViolationException,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val rootCause = ex.mostSpecificCause.message ?: "Database constraint violation"
        val errorResponse = ErrorResponse(
            status = HttpStatus.CONFLICT.value(),
            error = "Data Integrity Violation",
            message = "Database operation failed: $rootCause",
            timestamp = Instant.now()
        )
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse)
    }


    /**
     * Handles all other unhandled exceptions.
     */
    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(
        ex: Exception,
        request: WebRequest
    ): ResponseEntity<ErrorResponse> {
        val errorId = System.currentTimeMillis().toString()
        val errorResponse = ErrorResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR.value(),
            error = "Internal Server Error",
            message = "An unexpected error occurred. Error ID: $errorId",
            timestamp = Instant.now()
        )
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse)
    }
}