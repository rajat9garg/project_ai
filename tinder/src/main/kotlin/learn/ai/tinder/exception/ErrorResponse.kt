package learn.ai.tinder.exception

import java.time.Instant

/**
 * Standard error response format for API errors.
 */
data class ErrorResponse(
    val status: Int,
    val error: String,
    val message: String,
    val timestamp: Instant,
    val errors: List<String> = emptyList()
)
