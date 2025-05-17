package learn.ai.tinder.controller

import jakarta.validation.Valid
import learn.ai.tinder.dto.request.UserRegistrationRequest
import learn.ai.tinder.dto.response.UserResponse
import learn.ai.tinder.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

/**
 * REST controller for user-related operations.
 */
@RestController
@RequestMapping(
    path = ["/api/v1/users"],
    produces = [MediaType.APPLICATION_JSON_VALUE]
)
class UserController(
    private val userService: UserService
) {

    private val logger = LoggerFactory.getLogger(javaClass)

    /**
     * Registers a new user.
     * 
     * @param request The user registration request
     * @return The created user's data
     */
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun registerUser(
        @Valid @RequestBody request: UserRegistrationRequest
    ): ResponseEntity<UserResponse> {
        logger.info("Received registration request for email: {}", request.email)
        
        val userResponse = userService.registerUser(request)
        
        logger.info("Successfully registered user with ID: {}", userResponse.id)
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(userResponse)
    }

    /**
     * Retrieves a user by ID.
     * 
     * @param id The user ID
     * @return The user data if found, 404 otherwise
     */
    @GetMapping("/{id}")
    suspend fun getUserById(@PathVariable id: String): ResponseEntity<UserResponse> {
        logger.debug("Fetching user with ID: {}", id)
        
        return userService.findUserById(id)
            ?.let { user ->
                logger.debug("Found user with ID: {}", id)
                ResponseEntity.ok(user)
            }
            ?: run {
                logger.warn("User not found with ID: {}", id)
                ResponseEntity.notFound().build()
            }
    }

    /**
     * Retrieves a user by email.
     * 
     * @param email The user's email
     * @return The user data if found, 404 otherwise
     */
    @GetMapping
    suspend fun getUserByEmail(
        @RequestParam("email") email: String
    ): ResponseEntity<UserResponse> {
        logger.debug("Fetching user with email: {}", email)
        
        return userService.findUserByEmail(email)
            ?.let { user ->
                logger.debug("Found user with email: {}", email)
                ResponseEntity.ok(user)
            }
            ?: run {
                logger.warn("User not found with email: {}", email)
                ResponseEntity.notFound().build()
            }
    }
}
