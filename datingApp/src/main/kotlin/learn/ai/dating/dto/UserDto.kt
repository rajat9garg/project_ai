package learn.ai.dating.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.*

// Request DTOs
data class UserRegistrationRequest(
    @field:NotBlank(message = "Name is required")
    val name: String,
    
    @field:Email(message = "Email should be valid")
    @field:NotBlank(message = "Email is required")
    val email: String,
    
    @field:Size(min = 8, message = "Password must be at least 8 characters long")
    val password: String? = null,
    
    @field:Min(value = 18, message = "Age must be at least 18")
    @field:Max(value = 120, message = "Age must be less than 120")
    val age: Int,
    
    @field:NotBlank(message = "Gender is required")
    val gender: String,
    
    @field:Valid
    val location: LocationRequest,
    
    @field:Valid
    val preferences: PreferencesRequest
)

data class LocationRequest(
    @field:NotBlank(message = "City is required")
    val city: String,
    
    @field:NotBlank(message = "Country is required")
    val country: String,
    
    val latitude: Double? = null,
    val longitude: Double? = null
)

data class PreferencesRequest(
    @field:NotEmpty(message = "At least one interest is required")
    val interestedIn: List<String>,
    
    @field:Min(value = 18, message = "Minimum age must be at least 18")
    val minAge: Int,
    
    @field:Max(value = 120, message = "Maximum age must be less than 120")
    val maxAge: Int,
    
    @field:Min(value = 1, message = "Maximum distance must be at least 1")
    val maxDistance: Int? = null
)

// Response DTO
data class UserResponse(
    val id: String,
    val name: String,
    val age: Int,
    val gender: String,
    val location: LocationResponse,
    val preferences: PreferencesResponse,
    val createdAt: String
)

data class LocationResponse(
    val city: String,
    val country: String,
    val coordinates: CoordinatesResponse?
)

data class CoordinatesResponse(
    val latitude: Double,
    val longitude: Double
)

data class PreferencesResponse(
    val interestedIn: List<String>,
    val ageRange: AgeRangeResponse,
    val maxDistance: Int?
)

data class AgeRangeResponse(
    val min: Int,
    val max: Int
)

// Update DTOs
data class UserUpdateDto(
    val name: String? = null,
    val age: Int? = null,
    val gender: String? = null,
    val location: LocationUpdateDto? = null,
    val preferences: PreferencesUpdateDto? = null
)

data class LocationUpdateDto(
    val city: String? = null,
    val country: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null
)

data class PreferencesUpdateDto(
    val interestedIn: List<String>? = null,
    val minAge: Int? = null,
    val maxAge: Int? = null,
    val maxDistance: Int? = null
)
