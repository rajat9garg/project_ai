package learn.ai.tinder.service

import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.kotest.common.runBlocking
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import learn.ai.tinder.dto.request.LocationDto
import learn.ai.tinder.dto.request.PhotoDto
import learn.ai.tinder.dto.request.UserRegistrationRequest
import learn.ai.tinder.dto.response.PhotoResponse
import learn.ai.tinder.dto.response.UserResponse
import learn.ai.tinder.exception.UserAlreadyExistsException
import learn.ai.tinder.model.Gender
import learn.ai.tinder.model.User
import learn.ai.tinder.model.UserPhoto
import learn.ai.tinder.repository.UserRepository
import learn.ai.tinder.service.impl.UserServiceImpl
import learn.ai.tinder.util.UserMapper
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.data.mongodb.core.geo.GeoJsonPoint
import org.springframework.security.crypto.password.PasswordEncoder
import java.time.Instant
import java.time.LocalDate
import java.util.*

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(MockKExtension::class)
class UserServiceTest {

    @MockK
    private lateinit var userRepository: UserRepository

    @MockK
    private lateinit var passwordEncoder: PasswordEncoder

    @MockK
    private lateinit var userMapper: UserMapper

    @InjectMockKs
    private lateinit var userService: UserServiceImpl

    private val testUserId = "507f1f77bcf86cd799439011"
    private val testEmail = "test@example.com"
    private val testPassword = "Password123!"
    private val encodedPassword = "encodedPassword123"
    private val testName = "Test User"
    private val testDateOfBirth = LocalDate.now().minusYears(25)
    private val testGender = Gender.MALE
    private val testGenderPreference = setOf(Gender.FEMALE, Gender.NON_BINARY)
    private val testBio = "Test bio"
    private val testInterests = setOf("Hiking", "Photography")
    private val testPhotoUrl = "https://example.com/photo.jpg"
    private val testLongitude = 12.492507
    private val testLatitude = 41.889938

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        
        // Common stubs
        every { passwordEncoder.encode(any()) } returns encodedPassword
    }

    @Test
    fun `registerUser should save and return user when email is not taken`() = runTest {
        // Given
        val registrationRequest = createValidRegistrationRequest()
        val expectedUser = createTestUser()
        val expectedResponse = createTestUserResponse()
        
        every { userRepository.existsByEmail(testEmail) } returns false
        every { userMapper.toUser(registrationRequest) } returns expectedUser
        every { userRepository.save(expectedUser) } returns expectedUser
        every { userMapper.toUserResponse(expectedUser) } returns expectedResponse
        
        // When
        val result = userService.registerUser(registrationRequest)
        
        // Then
        assertThat(result).isNotNull
        assertThat(result.email).isEqualTo(testEmail)
        assertThat(result.name).isEqualTo(testName)
        
        verify(exactly = 1) { userRepository.existsByEmail(testEmail) }
        verify(exactly = 1) { userMapper.toUser(registrationRequest) }
        verify(exactly = 1) { userRepository.save(expectedUser) }
        verify(exactly = 1) { userMapper.toUserResponse(expectedUser) }
    }
    
    @Test
    fun `registerUser should throw exception when email is already taken`() = runTest {
        // Given
        val registrationRequest = createValidRegistrationRequest()
        
        every { userRepository.existsByEmail(testEmail) } returns true
        
        // When / Then
        assertThatThrownBy {
            runBlocking {
                userService.registerUser(registrationRequest) 
            }
        }.isInstanceOf(UserAlreadyExistsException::class.java)
        
        verify(exactly = 1) { userRepository.existsByEmail(testEmail) }
        verify(exactly = 0) { userRepository.save(any()) }
    }
    
    @Test
    fun `findUserById should return user when found`() = runTest {
        // Given
        val user = createTestUser()
        
        every { userRepository.findById(testUserId) } returns Optional.of(user)
        every { userMapper.toUserResponse(user) } returns createTestUserResponse()
        
        // When
        val result = userService.findUserById(testUserId)
        
        // Then
        assertThat(result).isNotNull
        assertThat(result?.email).isEqualTo(testEmail)
        
        verify(exactly = 1) { userRepository.findById(testUserId) }
    }
    
    @Test
    fun `findUserById should return null when user not found`() = runTest {
        // Given
        every { userRepository.findById(testUserId) } returns Optional.empty()
        
        // When
        val result = userService.findUserById(testUserId)
        
        // Then
        assertThat(result).isNull()
        
        verify(exactly = 1) { userRepository.findById(testUserId) }
    }
    
    @Test
    fun `findUserByEmail should return user when found`() = runTest {
        // Given
        val user = createTestUser()
        
        every { userRepository.findByEmail(testEmail) } returns user
        every { userMapper.toUserResponse(user) } returns createTestUserResponse()
        
        // When
        val result = userService.findUserByEmail(testEmail)
        
        // Then
        assertThat(result).isNotNull
        assertThat(result?.email).isEqualTo(testEmail)
        
        verify(exactly = 1) { userRepository.findByEmail(testEmail) }
    }
    
    @Test
    fun `findUserByEmail should return null when user not found`() = runTest {
        // Given
        every { userRepository.findByEmail(testEmail) } returns null
        
        // When
        val result = userService.findUserByEmail(testEmail)
        
        // Then
        assertThat(result).isNull()
        
        verify(exactly = 1) { userRepository.findByEmail(testEmail) }
    }
    
    private fun createValidRegistrationRequest() = UserRegistrationRequest(
        email = testEmail,
        password = testPassword,
        name = testName,
        dateOfBirth = testDateOfBirth,
        gender = testGender,
        genderPreference = testGenderPreference,
        bio = testBio,
        interests = testInterests,
        location = createTestLocationDto(),
        photos = listOf(createTestPhotoDto())
    )
    
    private fun createTestLocationDto() = LocationDto(
        longitude = testLongitude,
        latitude = testLatitude
    )
    
    private fun createTestPhotoDto() = PhotoDto(
        url = testPhotoUrl,
        isPrimary = true
    )
    
    private fun createTestUser() = User(
        id = testUserId,
        email = testEmail,
        password = encodedPassword,
        name = testName,
        dateOfBirth = testDateOfBirth,
        gender = testGender,
        genderPreference = testGenderPreference,
        bio = testBio,
        interests = testInterests,
        location = GeoJsonPoint(testLongitude, testLatitude),
        photos = listOf(
            UserPhoto(
                url = testPhotoUrl,
                isPrimary = true,
                uploadedAt = Instant.now()
            )
        ),
        isActive = true,
        lastActive = Instant.now(),
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )
    
    private fun createTestUserResponse() = UserResponse(
        id = testUserId,
        email = testEmail,
        name = testName,
        bio = testBio,
        age = 25,
        gender = testGender,
        genderPreference = testGenderPreference,
        interests = testInterests,
        photos = listOf(
            PhotoResponse(
                url = testPhotoUrl,
                isPrimary = true,
                uploadedAt = Instant.now()
            )
        ),
        isActive = true,
        lastActive = Instant.now(),
        createdAt = Instant.now()
    )
}
