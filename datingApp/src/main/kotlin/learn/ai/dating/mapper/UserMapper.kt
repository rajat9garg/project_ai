package learn.ai.dating.mapper

import learn.ai.dating.dto.*
import learn.ai.dating.model.Coordinates
import learn.ai.dating.model.Location
import learn.ai.dating.model.Preferences
import learn.ai.dating.model.User
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.Period

@Component
class UserMapper {
    
    fun toEntity(request: UserRegistrationRequest): User {
        return User(
            firstName = request.name.split(" ").firstOrNull() ?: "",
            lastName = request.name.split(" ").drop(1).joinToString(" "),
            email = request.email.lowercase(),
            password = request.password,
            dateOfBirth = LocalDate.now().minusYears(request.age.toLong()),
            gender = request.gender.uppercase(),
            location = Location(
                city = request.location.city,
                country = request.location.country,
                coordinates = request.location.let { 
                    if (it.latitude != null && it.longitude != null) {
                        Coordinates(
                            type = "Point",
                            coordinates = listOf(it.longitude, it.latitude)
                        )
                    } else {
                        null
                    }
                }
            ),
            preferences = Preferences(
                interestedIn = request.preferences.interestedIn.map { it.uppercase() },
                ageRange = request.preferences.minAge..request.preferences.maxAge,
                maxDistance = request.preferences.maxDistance ?: 100
            )
        )
    }
    
    fun toDto(user: User): UserResponse {
        val age = if (user.dateOfBirth != null) {
            Period.between(user.dateOfBirth, LocalDate.now()).years
        } else {
            0
        }
        
        return UserResponse(
            id = user.id,
            name = "${user.firstName} ${user.lastName}".trim(),
            age = age,
            gender = user.gender,
            location = LocationResponse(
                city = user.location?.city ?: "",
                country = user.location?.country ?: "",
                coordinates = user.location?.coordinates?.let {
                    if (it.coordinates.size >= 2) {
                        CoordinatesResponse(
                            latitude = it.coordinates[1],
                            longitude = it.coordinates[0]
                        )
                    } else {
                        null
                    }
                }
            ),
            preferences = PreferencesResponse(
                interestedIn = user.preferences?.interestedIn ?: emptyList(),
                ageRange = AgeRangeResponse(
                    min = user.preferences?.ageRange?.first ?: 18,
                    max = user.preferences?.ageRange?.last ?: 100
                ),
                maxDistance = user.preferences?.maxDistance
            ),
            createdAt = user.createdAt.toString()
        )
    }
}
