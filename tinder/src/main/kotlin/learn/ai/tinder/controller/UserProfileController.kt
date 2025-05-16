package learn.ai.tinder.controller

import jakarta.validation.Valid
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import learn.ai.tinder.dto.UserProfileDto
import learn.ai.tinder.service.UserProfileService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

/**
 * REST controller for user profile operations.
 * Uses coroutines for asynchronous request handling.
 */
@RestController
@RequestMapping("/api/profiles")
class UserProfileController(
    private val userProfileService: UserProfileService
) {
    /**
     * Create a new user profile.
     *
     * @param userProfileDto The user profile data
     * @return The created user profile
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    suspend fun createUserProfile(@Valid @RequestBody userProfileDto: UserProfileDto): UserProfileDto {
        val createdProfile = userProfileService.createUserProfile(userProfileDto.toModel())
        return UserProfileDto.fromModel(createdProfile)
    }

    /**
     * Get a user profile by ID.
     *
     * @param id The profile ID
     * @return The user profile
     */
    @GetMapping("/{id}")
    suspend fun getUserProfileById(@PathVariable id: String): UserProfileDto {
        val profile = userProfileService.getUserProfileById(id)
        return UserProfileDto.fromModel(profile)
    }

    /**
     * Get a user profile by user ID.
     *
     * @param userId The user ID
     * @return The user profile
     */
    @GetMapping("/user/{userId}")
    suspend fun getUserProfileByUserId(@PathVariable userId: String): UserProfileDto {
        val profile = userProfileService.getUserProfileByUserId(userId)
        return UserProfileDto.fromModel(profile)
    }

    /**
     * Get all user profiles.
     *
     * @return A flow of user profiles
     */
    @GetMapping
    fun getAllUserProfiles(): Flow<UserProfileDto> {
        return userProfileService.getAllUserProfiles()
            .map { UserProfileDto.fromModel(it) }
    }

    /**
     * Update a user profile.
     *
     * @param id The profile ID
     * @param userProfileDto The updated profile data
     * @return The updated user profile
     */
    @PutMapping("/{id}")
    suspend fun updateUserProfile(
        @PathVariable id: String,
        @Valid @RequestBody userProfileDto: UserProfileDto
    ): UserProfileDto {
        val updatedProfile = userProfileService.updateUserProfile(id, userProfileDto.toModel())
        return UserProfileDto.fromModel(updatedProfile)
    }

    /**
     * Delete a user profile.
     *
     * @param id The profile ID
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    suspend fun deleteUserProfile(@PathVariable id: String) {
        userProfileService.deleteUserProfile(id)
    }
}
