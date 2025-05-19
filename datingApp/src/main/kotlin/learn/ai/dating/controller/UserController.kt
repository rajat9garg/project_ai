package learn.ai.dating.controller

import learn.ai.dating.service.UserService
import learn.ai.dating.dto.UserRegistrationRequest as DtoUserRegistrationRequest
import learn.ai.dating.dto.LocationRequest as DtoLocationRequest
import learn.ai.dating.dto.PreferencesRequest as DtoPreferencesRequest
import learn.ai.generated.api.UsersApi
import learn.ai.generated.model.UserRegistrationRequest
import learn.ai.generated.model.UserResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController
import java.net.URI
import java.time.OffsetDateTime
import java.util.*

@RestController
class UserController(
    private val userService: UserService
) : UsersApi {

    override fun usersRegisterPost(userRegistrationRequest: UserRegistrationRequest): ResponseEntity<UserResponse> {
        // Map generated model to internal DTO with required fields
        val dto = DtoUserRegistrationRequest(
            name = userRegistrationRequest.name ?: throw IllegalArgumentException("Name is required"),
            email = userRegistrationRequest.email ?: throw IllegalArgumentException("Email is required"),
            password = userRegistrationRequest.password ?: throw IllegalArgumentException("Password is required"),
            age = 18, // Default value
            gender = "Not specified", // Default value
            location = DtoLocationRequest("Unknown", "Unknown"),
            preferences = DtoPreferencesRequest(
                interestedIn = listOf("Everyone"),
                minAge = 18,
                maxAge = 99
            )
        )
        
        // Call service with internal DTO
        val serviceResponse = userService.registerUser(dto)
        
        // Create and return the response using the constructor
        val response = UserResponse(
            id = UUID.fromString(serviceResponse.id),
            name = serviceResponse.name,
            createdAt = OffsetDateTime.now()
        )
        
        return ResponseEntity
            .created(URI.create("/api/v1/users/${response.id}"))
            .body(response)
    }
}