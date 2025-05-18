package learn.ai.dating.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime
import java.util.UUID

@Document(collection = "users")
data class User(
    @Id
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val email: String,
    val password: String? = null,
    val age: Int,
    val gender: String,
    val location: Location,
    val preferences: Preferences,
    val createdAt: LocalDateTime = LocalDateTime.now()
)

data class Location(
    val city: String,
    val country: String,
    val coordinates: Coordinates? = null
)

data class Coordinates(
    val latitude: Double,
    val longitude: Double
)

data class Preferences(
    val interestedIn: List<String>,
    val ageRange: IntRange,
    val maxDistance: Int? = null
)
