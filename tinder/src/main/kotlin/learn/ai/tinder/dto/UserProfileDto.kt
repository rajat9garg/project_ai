package learn.ai.tinder.dto

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer
import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Past
import jakarta.validation.constraints.Size
import kotlinx.serialization.Serializable
import learn.ai.tinder.model.Gender
import learn.ai.tinder.model.GeoLocation
import learn.ai.tinder.model.MatchPreferences
import learn.ai.tinder.model.UserProfile
import java.time.LocalDate

/**
 * Data Transfer Object for creating and updating user profiles.
 */
@Serializable
data class UserProfileDto(
    @field:NotBlank(message = "User ID is required")
    val userId: String,
    
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,
    
    @field:NotNull(message = "Birth date is required")
    @field:Past(message = "Birth date must be in the past")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @JsonSerialize(using = LocalDateSerializer::class)
    val birthDate: LocalDate,
    
    @field:NotNull(message = "Gender is required")
    val gender: Gender,
    
    @field:Size(max = 500, message = "Bio must not exceed 500 characters")
    val bio: String? = null,
    
    val location: GeoLocationDto? = null,
    
    val photos: List<String> = emptyList(),
    
    val interests: List<String> = emptyList(),
    
    val preferences: MatchPreferencesDto = MatchPreferencesDto()
) {
    /**
     * Convert DTO to domain model.
     */
    fun toModel(): UserProfile = UserProfile(
        userId = userId,
        name = name,
        birthDate = birthDate,
        gender = gender,
        bio = bio,
        location = location?.toModel(),
        photos = photos,
        interests = interests,
        preferences = preferences.toModel()
    )
    
    companion object {
        /**
         * Convert domain model to DTO.
         */
        fun fromModel(model: UserProfile): UserProfileDto = UserProfileDto(
            userId = model.userId,
            name = model.name,
            birthDate = model.birthDate,
            gender = model.gender,
            bio = model.bio,
            location = model.location?.let { GeoLocationDto.fromModel(it) },
            photos = model.photos,
            interests = model.interests,
            preferences = MatchPreferencesDto.fromModel(model.preferences)
        )
    }
}

/**
 * DTO for geographic location.
 */
@Serializable
data class GeoLocationDto(
    @field:NotNull(message = "Latitude is required")
    @field:Min(value = -90, message = "Latitude must be between -90 and 90")
    @field:Max(value = 90, message = "Latitude must be between -90 and 90")
    val latitude: Double,
    
    @field:NotNull(message = "Longitude is required")
    @field:Min(value = -180, message = "Longitude must be between -180 and 180")
    @field:Max(value = 180, message = "Longitude must be between -180 and 180")
    val longitude: Double
) {
    /**
     * Convert DTO to domain model.
     */
    fun toModel(): GeoLocation = GeoLocation(
        latitude = latitude,
        longitude = longitude
    )
    
    companion object {
        /**
         * Convert domain model to DTO.
         */
        fun fromModel(model: GeoLocation): GeoLocationDto = GeoLocationDto(
            latitude = model.latitude,
            longitude = model.longitude
        )
    }
}

/**
 * DTO for match preferences.
 */
@Serializable
data class MatchPreferencesDto(
    val genderPreferences: List<Gender> = listOf(Gender.MALE, Gender.FEMALE),
    
    @field:Min(value = 18, message = "Minimum age must be at least 18")
    @field:Max(value = 100, message = "Maximum age must not exceed 100")
    val minAge: Int = 18,
    
    @field:Min(value = 18, message = "Minimum age must be at least 18")
    @field:Max(value = 100, message = "Maximum age must not exceed 100")
    val maxAge: Int = 100,
    
    @field:Min(value = 1, message = "Maximum distance must be at least 1")
    @field:Max(value = 500, message = "Maximum distance must not exceed 500")
    val maxDistance: Int = 50,
    
    val showMe: Boolean = true
) {
    /**
     * Convert DTO to domain model.
     */
    fun toModel(): MatchPreferences = MatchPreferences(
        genderPreferences = genderPreferences,
        minAge = minAge,
        maxAge = maxAge,
        maxDistance = maxDistance,
        showMe = showMe
    )
    
    companion object {
        /**
         * Convert domain model to DTO.
         */
        fun fromModel(model: MatchPreferences): MatchPreferencesDto = MatchPreferencesDto(
            genderPreferences = model.genderPreferences,
            minAge = model.minAge,
            maxAge = model.maxAge,
            maxDistance = model.maxDistance,
            showMe = model.showMe
        )
    }
}
