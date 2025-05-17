package learn.ai.tinder.dto.request

import jakarta.validation.constraints.DecimalMax
import jakarta.validation.constraints.DecimalMin
import jakarta.validation.constraints.NotNull

/**
 * DTO for location data.
 */
data class LocationDto(
    @field:NotNull(message = "Longitude is required")
    @field:DecimalMin(
        value = "-180.0",
        message = "Longitude must be between -180 and 180 degrees"
    )
    @field:DecimalMax(
        value = "180.0",
        message = "Longitude must be between -180 and 180 degrees"
    )
    val longitude: Double,
    
    @field:NotNull(message = "Latitude is required")
    @field:DecimalMin(
        value = "-90.0",
        message = "Latitude must be between -90 and 90 degrees"
    )
    @field:DecimalMax(
        value = "90.0",
        message = "Latitude must be between -90 and 90 degrees"
    )
    val latitude: Double
)
