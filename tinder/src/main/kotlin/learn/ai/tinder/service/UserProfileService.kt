package learn.ai.tinder.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.reactive.awaitSingle
import learn.ai.tinder.exception.ResourceNotFoundException
import learn.ai.tinder.model.UserProfile
import learn.ai.tinder.repository.UserProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * Service for managing user profiles.
 * Uses coroutines for asynchronous operations.
 */
@Service
class UserProfileService(
    private val userProfileRepository: UserProfileRepository
) {
    /**
     * Create a new user profile.
     *
     * @param userProfile The user profile to create
     * @return The created user profile
     */
    @Transactional
    suspend fun createUserProfile(userProfile: UserProfile): UserProfile {
        val exists = userProfileRepository.existsByUserId(userProfile.userId).awaitSingle()
        if (exists) {
            throw IllegalArgumentException("User profile with userId ${userProfile.userId} already exists")
        }
        return userProfileRepository.save(userProfile).awaitSingle()
    }

    /**
     * Get a user profile by its ID.
     *
     * @param id The profile ID
     * @return The user profile
     * @throws ResourceNotFoundException if the profile is not found
     */
    suspend fun getUserProfileById(id: String): UserProfile {
        return userProfileRepository.findById(id).awaitFirstOrNull()
            ?: throw ResourceNotFoundException("User profile not found with id: $id", "/api/profiles/$id")
    }

    /**
     * Get a user profile by userId.
     *
     * @param userId The unique user identifier
     * @return The user profile
     * @throws ResourceNotFoundException if the profile is not found
     */
    suspend fun getUserProfileByUserId(userId: String): UserProfile {
        return userProfileRepository.findByUserId(userId).awaitFirstOrNull()
            ?: throw ResourceNotFoundException("User profile not found with userId: $userId", "/api/profiles/user/$userId")
    }

    /**
     * Get all user profiles.
     *
     * @return A flow of user profiles
     */
    fun getAllUserProfiles(): Flow<UserProfile> {
        return userProfileRepository.findAll().asFlow()
    }

    /**
     * Update a user profile.
     *
     * @param id The profile ID
     * @param userProfile The updated user profile
     * @return The updated user profile
     * @throws ResourceNotFoundException if the profile is not found
     */
    @Transactional
    suspend fun updateUserProfile(id: String, userProfile: UserProfile): UserProfile {
        val existingProfile = userProfileRepository.findById(id).awaitFirstOrNull()
            ?: throw ResourceNotFoundException("User profile not found with id: $id", "/api/profiles/$id")
        
        val updatedProfile = userProfile.copy(
            id = existingProfile.id,
            userId = existingProfile.userId,
            createdAt = existingProfile.createdAt,
            version = existingProfile.version
        )
        
        return userProfileRepository.save(updatedProfile).awaitSingle()
    }

    /**
     * Delete a user profile by its ID.
     *
     * @param id The profile ID
     * @throws ResourceNotFoundException if the profile is not found
     */
    @Transactional
    suspend fun deleteUserProfile(id: String) {
        val exists = userProfileRepository.existsById(id).awaitSingle()
        if (!exists) {
            throw ResourceNotFoundException("User profile not found with id: $id", "/api/profiles/$id")
        }
        userProfileRepository.deleteById(id).awaitFirstOrNull()
    }
}
