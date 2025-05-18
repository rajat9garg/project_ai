package learn.ai.dating.controller

import jakarta.validation.Valid
import learn.ai.dating.dto.*
import learn.ai.dating.mapper.UserMapper
import learn.ai.dating.model.User
import learn.ai.dating.service.UserService
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api/v1")
@Validated
class UserController(
    private val userService: UserService,
    private val userMapper: UserMapper
) {

    /**
     * Get a user by ID
     * @param id The user ID
     * @return The user if found, or 404 if not found
     */
    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable id: String): ResponseEntity<UserResponse> {
        val user = userService.getUserById(id) ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(userMapper.toDto(user))
    }

    /**
     * Get a paginated list of users with sorting options
     * @param page Page number (zero-based)
     * @param size Number of items per page
     * @param sort Optional sorting parameters (format: field or -field for descending)
     * @return Paginated list of users
     */
    @GetMapping("/users")
    fun getUsers(
        @RequestParam(defaultValue = "0") page: Int,
        @RequestParam(defaultValue = "20") size: Int,
        @RequestParam(required = false) sort: List<String>?
    ): ResponseEntity<Page<UserResponse>> {
        // Create sort object from parameters
        val sortObj = sort?.let { sortParams -> 
            Sort.by(sortParams.map { order -> 
                val direction = if (order.startsWith("-")) Sort.Direction.DESC else Sort.Direction.ASC
                val property = if (order.startsWith("-") || order.startsWith("+")) order.substring(1) else order
                Sort.Order(direction, property)
            })
        } ?: Sort.by(Sort.Direction.DESC, "createdAt")
        
        // Create pageable from parameters
        val pageable = PageRequest.of(page, size, sortObj)
        
        // Get users from service and map to response
        val usersPage = userService.getUsers(pageable)
        val userResponses = usersPage.map { userMapper.toDto(it) }
        
        return ResponseEntity.ok(userResponses)
    }

    /**
     * Register a new user
     * @param userRegistrationRequest User registration data
     * @return Created user with 201 status code
     */
    @PostMapping(
        value = ["/users/register"],
        produces = [MediaType.APPLICATION_JSON_VALUE],
        consumes = [MediaType.APPLICATION_JSON_VALUE]
    )
    fun registerUser(
        @Valid @RequestBody userRegistrationRequest: UserRegistrationRequest
    ): ResponseEntity<UserResponse> {
        val user = userMapper.toEntity(userRegistrationRequest)
        val createdUser = userService.registerUser(user)
        val userResponse = userMapper.toDto(createdUser)
        
        val location: URI = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdUser.id)
            .toUri()
            
        return ResponseEntity.created(location).body(userResponse)
    }

    /**
     * Update an existing user
     * @param id User ID to update
     * @param userUpdateDto Updated user data
     * @return Updated user or 404 if not found
     */
    @PutMapping("/users/{id}")
    fun updateUser(
        @PathVariable id: String,
        @Valid @RequestBody userUpdateDto: UserUpdateDto
    ): ResponseEntity<UserResponse> {
        // Check if user exists
        userService.getUserById(id) ?: return ResponseEntity.notFound().build()
        
        // Update user fields based on the update DTO
        val updatedUser = userService.updateUser(id, userUpdateDto)
        
        return ResponseEntity.ok(userMapper.toDto(updatedUser))
    }

    /**
     * Delete a user by ID
     * @param id User ID to delete
     * @return 204 No Content if successful, 404 if not found
     */
    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id: String): ResponseEntity<Void> {
        // Check if user exists
        userService.getUserById(id) ?: return ResponseEntity.notFound().build()
        
        userService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }
}