package learn.ai.dating.controller

import learn.ai.dating.dto.UserRegistrationRequest
import learn.ai.dating.dto.UserResponse
import learn.ai.dating.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody request: UserRegistrationRequest): ResponseEntity<UserResponse> {
        val userResponse = userService.registerUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(userResponse)
    }
}
