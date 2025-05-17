package learn.ai.tinder.dto.response

import learn.ai.tinder.model.Gender
import java.time.Instant
import java.util.*

/**
 * DTO for user response data.
 */
data class UserResponse(
    val id: String,
    val email: String,
    val name: String,
    val bio: String?,
    val age: Int,
    val gender: Gender,
    val genderPreference: Set<Gender>,
    val interests: Set<String>,
    val photos: List<PhotoResponse>,
    val isActive: Boolean,
    val lastActive: Instant,
    val createdAt: Instant
)

/**
 * DTO for photo data in responses.
 */
data class PhotoResponse(
    val id: String = UUID.randomUUID().toString(),
    val url: String,
    val isPrimary: Boolean = false,
    val uploadedAt: Instant = Instant.now()
)
