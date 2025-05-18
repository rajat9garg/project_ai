package learn.ai.dating.factory

import learn.ai.dating.dto.*
import learn.ai.dating.model.*
import java.time.LocalDateTime
import java.util.*

object TestDataFactory {
    
    fun createUser(
        id: String = UUID.randomUUID().toString(),
        name: String = "Test User",
        email: String = "test${UUID.randomUUID()}@example.com",
        password: String? = "password123",
        age: Int = 25,
        gender: String = "MALE",
        location: Location = Location("Test City", "Test Country"),
        preferences: Preferences = Preferences(
            interestedIn = listOf("FEMALE"),
            ageRange = 20..30,
            maxDistance = 50
        )
    ) = User(
        id = id,
        name = name,
        email = email,
        password = password,
        age = age,
        gender = gender,
        location = location,
        preferences = preferences,
        createdAt = LocalDateTime.now()
    )
    
    fun createUserRegistrationRequest(
        name: String = "Test User",
        email: String = "test${UUID.randomUUID()}@example.com",
        password: String? = "password123",
        age: Int = 25,
        gender: String = "MALE",
        location: LocationRequest = LocationRequest("Test City", "Test Country"),
        preferences: PreferencesRequest = PreferencesRequest(
            interestedIn = listOf("FEMALE"),
            minAge = 20,
            maxAge = 30,
            maxDistance = 50
        )
    ) = UserRegistrationRequest(
        name = name,
        email = email,
        password = password,
        age = age,
        gender = gender,
        location = location,
        preferences = preferences
    )
    
    fun createUserResponse(
        id: String = UUID.randomUUID().toString(),
        name: String = "Test User",
        age: Int = 25,
        gender: String = "MALE",
        location: LocationResponse = LocationResponse(
            city = "Test City",
            country = "Test Country",
            coordinates = null
        ),
        preferences: PreferencesResponse = PreferencesResponse(
            interestedIn = listOf("FEMALE"),
            ageRange = AgeRangeResponse(20, 30),
            maxDistance = 50
        ),
        createdAt: String = LocalDateTime.now().toString()
    ) = UserResponse(
        id = id,
        name = name,
        age = age,
        gender = gender,
        location = location,
        preferences = preferences,
        createdAt = createdAt
    )
    
    fun createUserResponse(user: User) = UserResponse(
        id = user.id,
        name = user.name,
        age = user.age,
        gender = user.gender,
        location = LocationResponse(
            city = user.location.city,
            country = user.location.country,
            coordinates = user.location.coordinates?.let { CoordinatesResponse(it.latitude, it.longitude) }
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
