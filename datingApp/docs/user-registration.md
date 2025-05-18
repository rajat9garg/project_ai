# User Registration Module

## Architecture Overview
We'll implement a simple, scalable user registration flow using:
- **Spring Boot** for the backend
- **MongoDB** for data storage
- **REST API** for communication

## Component Breakdown

### 1. Data Model
```kotlin
// User.kt
data class User(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
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
```

### 2. Repository Layer
```kotlin
// UserRepository.kt
@Repository
interface UserRepository : MongoRepository<User, String> {
    fun existsByEmail(email: String): Boolean
    // Add custom queries as needed
}
```

### 3. Service Layer
```kotlin
// UserService.kt
@Service
class UserService(
    private val userRepository: UserRepository
) {
    fun registerUser(request: UserRegistrationRequest): User {
        // Validate request
        // Map to domain model
        // Save to database
        // Return created user
    }
}
```

### 4. REST API
```kotlin
// UserController.kt
@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody request: UserRegistrationRequest): ResponseEntity<User> {
        val user = userService.registerUser(request)
        return ResponseEntity.status(HttpStatus.CREATED).body(user)
    }
}

// DTOs
data class UserRegistrationRequest(
    val name: String,
    val age: Int,
    val gender: String,
    val location: LocationRequest,
    val preferences: PreferencesRequest
)

data class LocationRequest(
    val city: String,
    val country: String,
    val latitude: Double? = null,
    val longitude: Double? = null
)

data class PreferencesRequest(
    val interestedIn: List<String>,
    val minAge: Int,
    val maxAge: Int,
    val maxDistance: Int? = null
)
```

## API Contract

### Register User
- **Endpoint**: `POST /api/v1/users/register`
- **Request Body**:
```json
{
  "name": "John Doe",
  "age": 28,
  "gender": "MALE",
  "location": {
    "city": "New York",
    "country": "USA",
    "latitude": 40.7128,
    "longitude": -74.0060
  },
  "preferences": {
    "interestedIn": ["FEMALE", "NON_BINARY"],
    "minAge": 23,
    "maxAge": 35,
    "maxDistance": 50
  }
}
```

- **Success Response (201 Created)**:
```json
{
  "id": "123e4567-e89b-12d3-a456-426614174000",
  "name": "John Doe",
  "age": 28,
  "gender": "MALE",
  "location": {
    "city": "New York",
    "country": "USA",
    "coordinates": {
      "latitude": 40.7128,
      "longitude": -74.0060
    }
  },
  "preferences": {
    "interestedIn": ["FEMALE", "NON_BINARY"],
    "ageRange": {
      "start": 23,
      "endInclusive": 35
    },
    "maxDistance": 50
  },
  "createdAt": "2025-05-18T15:07:42"
}
```

## Test Cases

### Unit Tests

#### UserServiceTest.kt
```kotlin
@ExtendWith(MockKExtension::class)
class UserServiceTest {
    
    @MockK
    private lateinit var userRepository: UserRepository
    
    private lateinit var userService: UserService
    
    @BeforeEach
    fun setup() {
        MockKAnnotations.init(this)
        userService = UserService(userRepository)
    }
    
    @Test
    fun `registerUser should save user when valid request provided`() {
        // Given
        val request = UserRegistrationRequest(
            name = "Test User",
            age = 25,
            gender = "MALE",
            location = LocationRequest("Test City", "Test Country"),
            preferences = PreferencesRequest(
                interestedIn = listOf("FEMALE"),
                minAge = 20,
                maxAge = 30
            )
        )
        
        every { userRepository.save(any()) } returnsArgument 0
        
        // When
        val result = userService.registerUser(request)
        
        // Then
        assertThat(result.name).isEqualTo("Test User")
        assertThat(result.age).isEqualTo(25)
        verify(exactly = 1) { userRepository.save(any()) }
    }
    
    @Test
    fun `registerUser should validate age range`() {
        // Given
        val request = UserRegistrationRequest(
            name = "Test User",
            age = 17, // Invalid age
            gender = "MALE",
            location = LocationRequest("Test City", "Test Country"),
            preferences = PreferencesRequest(
                interestedIn = listOf("FEMALE"),
                minAge = 18,
                maxAge = 30
            )
        )
        
        // When / Then
        assertThrows<ValidationException> {
            userService.registerUser(request)
        }
    }
}
```

### Integration Tests

#### UserControllerIT.kt
```kotlin
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class UserControllerIT {
    
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @MockkBean
    private lateinit var userService: UserService
    
    @Test
    fun `registerUser should return 201 when valid request`() {
        // Given
        val request = """
            {
                "name": "Test User",
                "age": 25,
                "gender": "MALE",
                "location": {
                    "city": "Test City",
                    "country": "Test Country"
                },
                "preferences": {
                    "interestedIn": ["FEMALE"],
                    "minAge": 20,
                    "maxAge": 30
                }
            }
        """.trimIndent()
        
        val expectedUser = User(
            id = "123",
            name = "Test User",
            age = 25,
            gender = "MALE",
            location = Location("Test City", "Test Country"),
            preferences = Preferences(
                interestedIn = listOf("FEMALE"),
                ageRange = 20..30
            )
        )
        
        every { userService.registerUser(any()) } returns expectedUser
        
        // When / Then
        mockMvc.perform(
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(request)
        )
        .andExpect(status().isCreated)
        .andExpect(jsonPath("$.name").value("Test User"))
        .andExpect(jsonPath("$.age").value(25))
    }
    
    @Test
    fun `registerUser should return 400 when invalid request`() {
        // Given
        val invalidRequest = """
            {
                "name": "", // Invalid: empty name
                "age": 17,    // Invalid: under 18
                "gender": "INVALID",
                "location": {},
                "preferences": {}
            }
        """.trimIndent()
        
        // When / Then
        mockMvc.perform(
            post("/api/v1/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidRequest)
        )
        .andExpect(status().isBadRequest)
        .andExpect(jsonPath("$.errors").isArray)
        .andExpect(jsonPath("$.errors[0].field").exists())
    }
}
```

### Test Data Factory
```kotlin
object TestDataFactory {
    
    fun createUser(
        id: String = UUID.randomUUID().toString(),
        name: String = "Test User",
        age: Int = 25,
        gender: String = "MALE"
    ) = User(
        id = id,
        name = name,
        age = age,
        gender = gender,
        location = Location("Test City", "Test Country"),
        preferences = Preferences(
            interestedIn = listOf("FEMALE"),
            ageRange = 20..30,
            maxDistance = 50
        )
    )
    
    fun createUserRegistrationRequest(
        name: String = "Test User",
        age: Int = 25,
        gender: String = "MALE"
    ) = UserRegistrationRequest(
        name = name,
        age = age,
        gender = gender,
        location = LocationRequest("Test City", "Test Country"),
        preferences = PreferencesRequest(
            interestedIn = listOf("FEMALE"),
            minAge = 20,
            maxAge = 30,
            maxDistance = 50
        )
    )
}
```

## Implementation Plan

1. **Domain Models**
   - Create User, Location, Coordinates, and Preferences data classes
   - Add validation annotations
   
2. **Repository Layer**
   - Implement UserRepository interface
   - Add custom query methods as needed
   
3. **Service Layer**
   - Implement UserService with business logic
   - Add validation logic
   - Handle data transformation
   
4. **REST API**
   - Create UserController with register endpoint
   - Implement request/response DTOs
   - Add exception handling
   
5. **Testing**
   - Write unit tests for service layer
   - Write integration tests for controller
   - Add test data factories
   
6. **Documentation**
   - Add API documentation
   - Update README with setup instructions

Would you like me to proceed with implementing any specific part of this plan?
