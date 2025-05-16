package learn.ai.tinder.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime

/**
 * User profile document stored in MongoDB.
 * Contains user information and preferences for matching.
 */
@Document(collection = "user_profiles")
data class UserProfile(
    @Id
    val id: String? = null,
    
    @Indexed(unique = true)
    val userId: String,
    
    val name: String,
    
    val birthDate: LocalDate,
    
    val gender: Gender,
    
    val bio: String? = null,
    
    val location: GeoLocation? = null,
    
    val photos: List<String> = emptyList(),
    
    val interests: List<String> = emptyList(),
    
    val preferences: MatchPreferences = MatchPreferences(),
    
    @CreatedDate
    val createdAt: LocalDateTime? = null,
    
    @LastModifiedDate
    val updatedAt: LocalDateTime? = null,
    
    @Version
    val version: Long = 0
)

/**
 * Gender enum for user profiles.
 */
enum class Gender {
    MALE, FEMALE, NON_BINARY, OTHER
}

/**
 * Geographic location with latitude and longitude.
 */
data class GeoLocation(
    val latitude: Double,
    val longitude: Double
)

/**
 * User preferences for matching.
 */
data class MatchPreferences(
    val genderPreferences: List<Gender> = listOf(Gender.MALE, Gender.FEMALE),
    val minAge: Int = 18,
    val maxAge: Int = 100,
    val maxDistance: Int = 50, // in kilometers
    val showMe: Boolean = true
)
