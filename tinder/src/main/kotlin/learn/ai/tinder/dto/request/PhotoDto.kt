package learn.ai.tinder.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern

/**
 * DTO for photo data in requests.
 */
data class PhotoDto(
    @field:NotBlank(message = "Photo URL is required")
    @field:Pattern(
        regexp = "^https?://.+\\.(jpg|jpeg|png|gif|webp)$",
        flags = [Pattern.Flag.CASE_INSENSITIVE],
        message = "Photo URL must be a valid image URL (jpg, jpeg, png, gif, or webp)"
    )
    val url: String,
    
    val isPrimary: Boolean = false
)
