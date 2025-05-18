package learn.ai.dating.mapper

import learn.ai.dating.dto.*
import learn.ai.dating.model.Coordinates
import learn.ai.dating.model.Location
import learn.ai.dating.model.Preferences
import learn.ai.dating.model.User
import org.springframework.stereotype.Component

@Component
class UserMapper {
    
    fun toEntity(request: UserRegistrationRequest): User {
        return User(
            name = request.name,
            email = request.email.lowercase(),
            // Password will be set in the service layer
            age = request.age,
            gender = request.gender.uppercase(),
            location = Location(
                city = request.location.city,
                country = request.location.country,
                coordinates = request.location.let { 
                    if (it.latitude != null && it.longitude != null) {
                        Coordinates(it.latitude, it.longitude)
                    } else {
                        null
                    }
                }
            ),
            preferences = Preferences(
                interestedIn = request.preferences.interestedIn.map { it.uppercase() },
                ageRange = request.preferences.minAge..request.preferences.maxAge,
                maxDistance = request.preferences.maxDistance
            )
        )
    }
    
    fun toResponse(user: User): UserResponse {
        return UserResponse(
            id = user.id,
            name = user.name,
            age = user.age,
            gender = user.gender,
            location = LocationResponse(
                city = user.location.city,
                country = user.location.country,
                coordinates = user.location.coordinates?.let {
                    CoordinatesResponse(
                        latitude = it.latitude,
                        longitude = it.longitude
                    )
                }
            ),
            preferences = PreferencesResponse(
                interestedIn = user.preferences.interestedIn,
                ageRange = AgeRangeResponse(
                    min = user.preferences.ageRange.first,
                    max = user.preferences.ageRange.last
                ),
                maxDistance = user.preferences.maxDistance
            ),
            createdAt = user.createdAt.toString()
        )
    }
}
