package learn.ai.dating.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.*

@Document(collection = "users")
data class User(
    @Id
    val id: String = ObjectId.get().toString(),
    
    // Basic Information
    val firstName: String,
    val lastName: String,
    val email: String,
    val password: String? = null,
    val dateOfBirth: LocalDate,
    val gender: String,
    val bio: String? = null,
    val profilePictureUrl: String? = null,
    
    // Account Status
    val isVerified: Boolean = false,
    val isActive: Boolean = true,
    
    // Timestamps
    val lastActiveAt: LocalDateTime = LocalDateTime.now(),
    
    @CreatedDate
    val createdAt: LocalDateTime = LocalDateTime.now(),
    
    @LastModifiedDate
    val updatedAt: LocalDateTime = LocalDateTime.now(),
    
    val deletedAt: LocalDateTime? = null,
    
    // Relationships
    @DBRef
    val location: Location? = null,
    
    @DBRef
    val preferences: Preferences? = null
)

data class Location(
    val id: String = ObjectId.get().toString(),
    val address: String? = null,
    val city: String,
    val state: String? = null,
    val country: String,
    val postalCode: String? = null,
    val coordinates: Coordinates? = null,
    val isActive: Boolean = true,
    val isPrimary: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class Coordinates(
    val type: String = "Point",
    val coordinates: List<Double> = emptyList() // [longitude, latitude]
)

data class Preferences(
    val id: String = ObjectId.get().toString(),
    val genderPreferences: List<String> = emptyList(),
    val ageRange: IntRange = 18..100,
    val maxDistance: Int = 100, // in kilometers
    val interestedIn: List<String> = emptyList(),
    val notificationPreferences: NotificationPreferences = NotificationPreferences(),
    val privacySettings: PrivacySettings = PrivacySettings(),
    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime = LocalDateTime.now()
)

data class NotificationPreferences(
    val emailNotifications: Boolean = true,
    val pushNotifications: Boolean = true,
    val smsNotifications: Boolean = false,
    val matchNotifications: Boolean = true,
    val messageNotifications: Boolean = true,
    val promotionalEmails: Boolean = false
)

data class PrivacySettings(
    val showAge: Boolean = true,
    val showLocation: Boolean = true,
    val showLastActive: Boolean = true,
    val profileVisibility: ProfileVisibility = ProfileVisibility.PUBLIC
)

enum class ProfileVisibility {
    PUBLIC,
    FRIENDS_OF_FRIENDS,
    FRIENDS,
    PRIVATE
}
