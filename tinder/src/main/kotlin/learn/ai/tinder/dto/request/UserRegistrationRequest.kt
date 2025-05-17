package learn.ai.tinder.dto.request

import jakarta.validation.Valid
import jakarta.validation.constraints.*
import learn.ai.tinder.model.Gender
import java.time.LocalDate

/**
 * DTO for user registration request.
 */
data class UserRegistrationRequest(
    @field:NotBlank(message = "Email is required")
    @field:Email(message = "Invalid email format")
    val email: String,
    
    @field:NotBlank(message = "Password is required")
    @field:Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
    @field:Pattern(
        regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$",
        message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, one special character, and no whitespace"
    )
    val password: String,
    
    @field:NotBlank(message = "Name is required")
    @field:Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    val name: String,
    
    @field:NotNull(message = "Date of birth is required")
    @field:Past(message = "Date of birth must be in the past")
    val dateOfBirth: LocalDate,
    
    @field:NotNull(message = "Gender is required")
    val gender: Gender,
    
    @field:NotEmpty(message = "At least one gender preference is required")
    val genderPreference: Set<@NotNull Gender>,
    
    @field:Size(max = 500, message = "Bio cannot exceed 500 characters")
    val bio: String? = null,
    
    val interests: Set<@NotBlank String> = emptySet(),
    
    @field:Valid
    @field:NotNull(message = "Location is required")
    val location: LocationDto,
    
    @field:Valid
    @field:NotEmpty(message = "At least one photo is required")
    val photos: List<PhotoDto>
) {
    /**
     * Validates that the user is at least 18 years old.
     */
    @AssertTrue(message = "User must be at least 18 years old")
    fun isOfLegalAge(): Boolean {
        val today = LocalDate.now()
        val minDate = today.minusYears(18)
        return dateOfBirth <= minDate
    }
    
    /**
     * Validates that at least one photo is marked as primary.
     */
    @AssertTrue(message = "At least one photo must be marked as primary")
    fun hasPrimaryPhoto(): Boolean {
        return photos.any { it.isPrimary }
    }
}