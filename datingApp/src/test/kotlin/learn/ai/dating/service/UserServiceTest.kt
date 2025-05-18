package learn.ai.dating.service

import io.mockk.*
import io.mockk.junit5.MockKExtension
import learn.ai.dating.dto.UserRegistrationRequest
import learn.ai.dating.exception.ValidationException
import learn.ai.dating.factory.TestDataFactory
import learn.ai.dating.mapper.UserMapper
import learn.ai.dating.model.User
import learn.ai.dating.repository.UserRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.assertThrows
import org.springframework.test.util.ReflectionTestUtils

@ExtendWith(MockKExtension::class)
class UserServiceTest {
    
    private val userRepository: UserRepository = mockk()
    private val userMapper: UserMapper = mockk()
    
    private lateinit var userService: UserService
    
    @BeforeEach
    fun setup() {
        userService = UserService(userRepository, userMapper)
        clearMocks(userRepository, userMapper)
    }
    
    @Test
    fun `registerUser should save user when valid request provided`() {
        // Given
        val request = TestDataFactory.createUserRegistrationRequest()
        val user = TestDataFactory.createUser()
        val expectedResponse = TestDataFactory.createUserResponse(user)
        
        every { userRepository.existsByEmailIgnoreCase(any()) } returns false
        every { userMapper.toEntity(any()) } returns user
        every { userRepository.save(any()) } returns user
        every { userMapper.toResponse(any()) } returns expectedResponse
        
        // When
        val result = userService.registerUser(request)
        
        // Then
        assertThat(result).isEqualTo(expectedResponse)
        verify(exactly = 1) { userRepository.existsByEmailIgnoreCase(request.email) }
        verify(exactly = 1) { userRepository.save(any()) }
        verify(exactly = 1) { userMapper.toEntity(request) }
        verify(exactly = 1) { userMapper.toResponse(user) }
    }
    
    @Test
    fun `registerUser should throw ValidationException when maxAge is less than minAge`() {
        // Given
        val request = TestDataFactory.createUserRegistrationRequest().copy(
            preferences = TestDataFactory.createUserRegistrationRequest().preferences.copy(
                minAge = 30,
                maxAge = 20
            )
        )
        
        // When / Then
        val exception = assertThrows<ValidationException> {
            userService.registerUser(request)
        }
        
        assertThat(exception.message).isEqualTo("Max age must be greater than min age")
        verify(exactly = 0) { userRepository.save(any()) }
    }
    
    @Test
    fun `registerUser should throw ValidationException when age is outside preference range`() {
        // Given
        val request = TestDataFactory.createUserRegistrationRequest(
            age = 40
        ).copy(
            preferences = TestDataFactory.createUserRegistrationRequest().preferences.copy(
                minAge = 20,
                maxAge = 30
            )
        )
        
        // When / Then
        val exception = assertThrows<ValidationException> {
            userService.registerUser(request)
        }
        
        assertThat(exception.message).isEqualTo("Your age must be within your specified age range")
        verify(exactly = 0) { userRepository.save(any()) }
    }
    
    @Test
    fun `registerUser should throw ValidationException when email already exists`() {
        // Given
        val request = TestDataFactory.createUserRegistrationRequest()
        
        every { userRepository.existsByEmailIgnoreCase(any()) } returns true
        
        // When / Then
        val exception = assertThrows<ValidationException> {
            userService.registerUser(request)
        }
        
        assertThat(exception.message).isEqualTo("Email ${request.email} is already registered")
        verify(exactly = 0) { userRepository.save(any()) }
        verify(exactly = 1) { userRepository.existsByEmailIgnoreCase(request.email) }
    }
}
