package learn.ai.dating.service

import learn.ai.dating.dto.*
import learn.ai.dating.exception.ValidationException
import learn.ai.dating.mapper.UserMapper
import learn.ai.dating.model.*
import learn.ai.dating.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

/**
 * Service class for user-related operations.
 */
@Service
@Transactional(readOnly = true)
class UserService(
    private val userRepository: UserRepository,
    private val userMapper: UserMapper,
) {
    
    /**
     * Register a new user
     */
    @Transactional
    fun registerUser(request: UserRegistrationRequest): UserResponse {
        // Validate request
        validateRegistrationRequest(request)
        
        // Check if email is already registered
        if (userRepository.existsByEmailIgnoreCase(request.email)) {
            throw ValidationException("Email ${request.email} is already registered")
        }
        
        // Map to entity
        val user = userMapper.toEntity(request).copy(
            createdAt = LocalDateTime.now()
        )
        
        // Save and return response
        val savedUser = userRepository.save(user)
        return userMapper.toResponse(savedUser)
    }
    
    /**
     * Get a user by ID
     */
    fun getUserById(id: String): UserResponse {
        val user = userRepository.findById(id)
            .orElseThrow { NoSuchElementException("User not found with id: $id") }
        return userMapper.toResponse(user)
    }
    
    /**
     * Get a user by email
     */
    fun getUserByEmail(email: String): UserResponse {
        val user = userRepository.findByEmailIgnoreCase(email)
            ?: throw NoSuchElementException("User not found with email: $email")
        return userMapper.toResponse(user)
    }
    
    /**
     * Update user information
     */
    @Transactional
    fun updateUser(id: String, request: UserUpdateDto): UserResponse {
        val existingUser = userRepository.findById(id)
            .orElseThrow { NoSuchElementException("User not found with id: $id") }
        
        val updatedLocation = request.location?.let { locationDto ->
            existingUser.location.copy(
                city = locationDto.city ?: existingUser.location.city,
                country = locationDto.country ?: existingUser.location.country,
                coordinates = if (locationDto.latitude != null && locationDto.longitude != null) {
                    Coordinates(
                        latitude = locationDto.latitude,
                        longitude = locationDto.longitude
                    )
                } else {
                    existingUser.location.coordinates
                }
            )
        } ?: existingUser.location
        
        val updatedPreferences = request.preferences?.let { prefDto ->
            existingUser.preferences.copy(
                interestedIn = prefDto.interestedIn ?: existingUser.preferences.interestedIn,
                ageRange = if (prefDto.minAge != null && prefDto.maxAge != null) {
                    prefDto.minAge..prefDto.maxAge
                } else {
                    existingUser.preferences.ageRange
                },
                maxDistance = prefDto.maxDistance ?: existingUser.preferences.maxDistance
            )
        } ?: existingUser.preferences
        
        val updatedUser = existingUser.copy(
            name = request.name ?: existingUser.name,
            age = request.age ?: existingUser.age,
            gender = request.gender ?: existingUser.gender,
            location = updatedLocation,
            preferences = updatedPreferences
        )
        
        val savedUser = userRepository.save(updatedUser)
        return userMapper.toResponse(savedUser)
    }
    
    /**
     * Delete a user by ID
     */
    @Transactional
    fun deleteUser(id: String) {
        if (!userRepository.existsById(id)) {
            throw NoSuchElementException("User not found with id: $id")
        }
        userRepository.deleteById(id)
    }
    
    /**
     * Search users by various criteria
     */
    fun searchUsers(
        gender: String? = null,
        minAge: Int? = null,
        maxAge: Int? = null,
        city: String? = null,
        country: String? = null,
        interests: List<String>? = null,
        pageable: Pageable
    ): Page<UserResponse> {
        return when {
            gender != null && minAge != null && maxAge != null && city != null && interests != null -> {
                userRepository.findByGenderIgnoreCaseAndAgeBetweenAndLocationCityIgnoreCaseAndPreferencesInterestedInIn(
                    gender, minAge, maxAge, city, interests, pageable
                ).map(userMapper::toResponse)
            }
            city != null -> {
                userRepository.findByLocationCityIgnoreCase(city, pageable)
                    .map(userMapper::toResponse)
            }
            country != null -> {
                userRepository.findByLocationCountryIgnoreCase(country, pageable)
                    .map(userMapper::toResponse)
            }
            gender != null -> {
                userRepository.findByGenderIgnoreCase(gender, pageable)
                    .map(userMapper::toResponse)
            }
            interests != null -> {
                userRepository.findByPreferencesInterestedInContainingIgnoreCase(
                    interests.first(),
                    pageable
                ).map(userMapper::toResponse)
            }
            else -> {
                userRepository.findAll(pageable).map(userMapper::toResponse)
            }
        }
    }
    
    /**
     * Validate the registration request
     */
    private fun validateRegistrationRequest(request: UserRegistrationRequest) {
        if (request.preferences.maxAge <= request.preferences.minAge) {
            throw ValidationException("Max age must be greater than min age")
        }
        
        if (request.age < request.preferences.minAge || request.age > request.preferences.maxAge) {
            throw ValidationException("Your age must be within your specified age range")
        }
    }
}
