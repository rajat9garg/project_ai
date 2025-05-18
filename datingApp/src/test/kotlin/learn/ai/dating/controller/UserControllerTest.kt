package learn.ai.dating.controller

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import learn.ai.dating.dto.UserResponse
import learn.ai.dating.exception.ValidationException
import learn.ai.dating.factory.TestDataFactory
import learn.ai.dating.service.UserService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus

class UserControllerTest {
    
    private val userService: UserService = mockk(relaxed = true)
    private val controller = UserController(userService)
    
    @Test
    fun `registerUser should return 201 when valid request`() {
        // Given
        val request = TestDataFactory.createUserRegistrationRequest()
        val expectedResponse = TestDataFactory.createUserResponse(TestDataFactory.createUser())
        
        every { userService.registerUser(any()) } returns expectedResponse
        
        // When
        val response = controller.registerUser(request)
        
        // Then
        assertThat(response.statusCode).isEqualTo(HttpStatus.CREATED)
        assertThat(response.body).isEqualTo(expectedResponse)
        verify(exactly = 1) { userService.registerUser(request) }
    }
    
    @Test
    fun `registerUser should return 400 when business validation fails`() {
        // Given
        val request = TestDataFactory.createUserRegistrationRequest()
        val errorMessage = "Business validation failed"
        
        every { userService.registerUser(any()) } throws ValidationException(errorMessage)
        
        // When / Then
        val exception = org.junit.jupiter.api.assertThrows<ValidationException> {
            controller.registerUser(request)
        }
        
        assertThat(exception.message).isEqualTo(errorMessage)
        verify(exactly = 1) { userService.registerUser(request) }
    }
}
