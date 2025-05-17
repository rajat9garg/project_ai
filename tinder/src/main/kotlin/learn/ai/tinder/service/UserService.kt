package learn.ai.tinder.service

import learn.ai.tinder.dto.request.UserRegistrationRequest
import learn.ai.tinder.dto.response.UserResponse

/**
 * Service interface for user-related operations.
 */
interface UserService {
    
    /**
     * Registers a new user.
     * @param request The user registration request
     * @return The created user's data
     * @throws UserAlreadyExistsException if a user with the given email already exists
     */
    suspend fun registerUser(request: UserRegistrationRequest): UserResponse
    
    /**
     * Finds a user by ID.
     * @param id The user ID
     * @return The user data if found, null otherwise
     */
    suspend fun findUserById(id: String): UserResponse?
    
    /**
     * Finds a user by email.
     * @param email The user's email
     * @return The user data if found, null otherwise
     */
    suspend fun findUserByEmail(email: String): UserResponse?
}
