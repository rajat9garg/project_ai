package learn.ai.tinder.model

import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import java.time.Instant

/**
 * Represents a photo associated with a user's profile.
 * @property url The URL where the photo is stored
 * @property isPrimary Whether this is the user's primary/display photo
 * @property uploadedAt When the photo was uploaded
 */
data class UserPhoto(
    val url: String,
    val isPrimary: Boolean = false,
    val uploadedAt: Instant = Instant.now()
)
