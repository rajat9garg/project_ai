package learn.ai.tinder.model

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant
import java.time.LocalDate

/**
 * Represents a user in the Tinder-like application.
 */
@Document(collection = "users")
data class User(
    @Id
    val id: String? = null,
    
    @Indexed(unique = true)
    val email: String,
    
    val password: String,
    val name: String,
    val dateOfBirth: LocalDate,
    val gender: Gender,
    val genderPreference: Set<Gender>,
    val bio: String? = null,
    val interests: Set<String> = emptySet(),
    
    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    val location: GeoJsonPoint,
    
    val photos: List<UserPhoto>,
    val isActive: Boolean = true,
    val lastActive: Instant = Instant.now(),
    val createdAt: Instant = Instant.now(),
    val updatedAt: Instant = Instant.now()
) {
    /**
     * Calculates the user's age based on their date of birth.
     * @return The user's age in years
     */
    fun calculateAge(): Int {
        val now = LocalDate.now()
        var age = now.year - dateOfBirth.year
        
        // Adjust age if birthday hasn't occurred yet this year
        if (dateOfBirth.monthValue > now.monthValue || 
            (dateOfBirth.monthValue == now.monthValue && dateOfBirth.dayOfMonth > now.dayOfMonth)) {
            age--
        }
        
        return age
    }
}
