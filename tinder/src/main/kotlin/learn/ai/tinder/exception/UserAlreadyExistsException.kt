package learn.ai.tinder.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

/**
 * Exception thrown when attempting to register a user with an email that already exists.
 */
@ResponseStatus(HttpStatus.CONFLICT)
class UserAlreadyExistsException(email: String) : 
    RuntimeException("User with email '$email' already exists")
