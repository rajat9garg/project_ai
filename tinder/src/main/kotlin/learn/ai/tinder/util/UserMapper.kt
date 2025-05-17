package learn.ai.tinder.util

import learn.ai.tinder.dto.request.PhotoDto
import learn.ai.tinder.dto.request.UserRegistrationRequest
import learn.ai.tinder.dto.response.PhotoResponse
import learn.ai.tinder.dto.response.UserResponse
import learn.ai.tinder.model.Gender
import learn.ai.tinder.model.User
import learn.ai.tinder.model.UserPhoto
import org.bson.types.ObjectId
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.time.Instant

/**
 * Mapper for converting between User domain objects and DTOs.
 */
@Component
class UserMapper(
    private val passwordEncoder: PasswordEncoder
) {

    
    /**
     * Converts a UserRegistrationRequest to a User domain object.
     */
    fun toUser(request: UserRegistrationRequest): User {
        return User(
            email = request.email.lowercase().trim(),
            password = passwordEncoder.encode(request.password),
            name = request.name.trim(),
            dateOfBirth = request.dateOfBirth,
            gender = request.gender,
            genderPreference = request.genderPreference,
            bio = request.bio?.trim(),
            interests = request.interests.map { it.trim() }.toSet(),
            location = GeoJsonPoint(request.location.longitude, request.location.latitude),
            photos = request.photos.mapIndexed { index, photo ->
                UserPhoto(
                    url = photo.url.trim(),
                    isPrimary = photo.isPrimary || index == 0 // First photo is primary if none specified
                )
            },
            isActive = true,
            lastActive = Instant.now(),
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
    }

    /**
     * Converts a User domain object to a UserResponse DTO.
     */
    fun toUserResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id ?: throw IllegalArgumentException("User must have an ID"),
            email = user.email,
            name = user.name,
            bio = user.bio,
            age = user.calculateAge(),
            gender = user.gender,
            genderPreference = user.genderPreference,
            interests = user.interests,
            photos = user.photos.map { toPhotoResponse(it) },
            isActive = user.isActive,
            lastActive = user.lastActive,
            createdAt = user.createdAt
        )
    }

    /**
     * Converts a UserPhoto domain object to a PhotoResponse DTO.
     */
    private fun toPhotoResponse(photo: UserPhoto): PhotoResponse {
        return PhotoResponse(
            url = photo.url,
            isPrimary = photo.isPrimary,
            uploadedAt = photo.uploadedAt
        )
    }

    /**
     * Updates a User domain object with values from a PhotoDto.
     */
    fun updatePhotoFromDto(photo: UserPhoto, dto: PhotoDto): UserPhoto {
        return photo.copy(
            url = dto.url.trim(),
            isPrimary = dto.isPrimary
        )
    }
}
