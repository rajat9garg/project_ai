package learn.ai.tinder.service.impl

import learn.ai.tinder.dto.request.UserRegistrationRequest
import learn.ai.tinder.dto.response.UserResponse
import learn.ai.tinder.exception.UserAlreadyExistsException
import learn.ai.tinder.model.User
import learn.ai.tinder.repository.UserRepository
import learn.ai.tinder.service.UserService
import learn.ai.tinder.util.UserMapper
import org.slf4j.LoggerFactory
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.*
/**
 * Implementation of the UserService interface.
 */
@Service
@Transactional
class UserServiceImpl(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val userMapper: UserMapper
) : UserService {

    private val logger = LoggerFactory.getLogger(javaClass)
    private val MINIMUM_AGE = 18

    override suspend fun registerUser(request: UserRegistrationRequest): UserResponse {
        logger.info("Registering new user with email: {}", request.email)
        
        // Check if user with email already exists
        if (userRepository.existsByEmail(request.email.lowercase())) {
            logger.warn("Registration failed - User with email {} already exists", request.email)
            throw UserAlreadyExistsException(request.email)
        }

        // Validate user age
        val age = calculateAge(request.dateOfBirth)
        if (age < MINIMUM_AGE) {
            throw IllegalArgumentException("User must be at least $MINIMUM_AGE years old")
        }

        // Convert DTO to domain model, hash password, and save
        val user = userMapper.toUser(request).copy(
            password = passwordEncoder.encode(request.password)
        )
        
        val savedUser = userRepository.save(user)
        
        logger.info("User registered successfully with ID: {}", savedUser.id)
        return userMapper.toUserResponse(savedUser)
    }

    @Transactional(readOnly = true)
    override suspend fun findUserById(id: String): UserResponse? {
        logger.debug("Finding user by ID: {}", id)
        return userRepository.findById(id)
            .map { userMapper.toUserResponse(it) }
            .orElse(null)
    }

    @Transactional(readOnly = true)
    override suspend fun findUserByEmail(email: String): UserResponse? {
        logger.debug("Finding user by email: {}", email)
        return userRepository.findByEmail(email.lowercase())
            ?.let { userMapper.toUserResponse(it) }
    }

    private fun calculateAge(birthDate: LocalDate): Int {
        return Period.between(birthDate, LocalDate.now()).years
    }
}