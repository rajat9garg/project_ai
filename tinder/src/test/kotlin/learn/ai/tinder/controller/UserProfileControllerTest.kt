package learn.ai.tinder.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import learn.ai.tinder.dto.UserProfileDto
import learn.ai.tinder.model.Gender
import learn.ai.tinder.model.UserProfile
import learn.ai.tinder.service.UserProfileService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBody
import java.time.LocalDate

class UserProfileControllerTest {

    private lateinit var userProfileService: UserProfileService
    private lateinit var webTestClient: WebTestClient
    private lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setUp() {
        userProfileService = mockk()
        val userProfileController = UserProfileController(userProfileService)
        
        webTestClient = WebTestClient
            .bindToController(userProfileController)
            .build()
            
        objectMapper = ObjectMapper()
            .registerKotlinModule()
            .registerModule(JavaTimeModule())
    }

    @Test
    fun `createUserProfile should return created profile`() {
        // Arrange
        val userProfileDto = createSampleUserProfileDto()
        val createdProfile = userProfileDto.toModel().copy(id = "generatedId")
        
        coEvery { userProfileService.createUserProfile(any()) } returns createdProfile

        // Act & Assert
        webTestClient.post()
            .uri("/api/profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(userProfileDto)
            .exchange()
            .expectStatus().isCreated
            .expectBody<UserProfileDto>()
            .consumeWith { response ->
                val responseBody = response.responseBody
                assert(responseBody != null)
                assert(responseBody?.userId == userProfileDto.userId)
                assert(responseBody?.name == userProfileDto.name)
            }
            
        coVerify(exactly = 1) { userProfileService.createUserProfile(any()) }
    }

    @Test
    fun `getUserProfileById should return profile when it exists`() {
        // Arrange
        val userProfile = createSampleUserProfile().copy(id = "profile1")
        
        coEvery { userProfileService.getUserProfileById("profile1") } returns userProfile

        // Act & Assert
        webTestClient.get()
            .uri("/api/profiles/profile1")
            .exchange()
            .expectStatus().isOk
            .expectBody<UserProfileDto>()
            .consumeWith { response ->
                val responseBody = response.responseBody
                assert(responseBody != null)
                assert(responseBody?.userId == userProfile.userId)
                assert(responseBody?.name == userProfile.name)
            }
            
        coVerify(exactly = 1) { userProfileService.getUserProfileById("profile1") }
    }

    @Test
    fun `getAllUserProfiles should return all profiles`() {
        // Arrange
        val profile1 = createSampleUserProfile().copy(id = "profile1", userId = "user1")
        val profile2 = createSampleUserProfile().copy(id = "profile2", userId = "user2")
        
        every { userProfileService.getAllUserProfiles() } returns flowOf(profile1, profile2)

        // Act & Assert
        webTestClient.get()
            .uri("/api/profiles")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(UserProfileDto::class.java)
            .hasSize(2)
            .contains(UserProfileDto.fromModel(profile1), UserProfileDto.fromModel(profile2))
            
        coVerify(exactly = 1) { userProfileService.getAllUserProfiles() }
    }

    @Test
    fun `updateUserProfile should return updated profile`() {
        // Arrange
        val userProfileDto = createSampleUserProfileDto()
        val updatedProfile = userProfileDto.toModel().copy(id = "profile1")
        
        coEvery { userProfileService.updateUserProfile(eq("profile1"), any()) } returns updatedProfile

        // Act & Assert
        webTestClient.put()
            .uri("/api/profiles/profile1")
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(userProfileDto)
            .exchange()
            .expectStatus().isOk
            .expectBody<UserProfileDto>()
            .consumeWith { response ->
                val responseBody = response.responseBody
                assert(responseBody != null)
                assert(responseBody?.userId == userProfileDto.userId)
                assert(responseBody?.name == userProfileDto.name)
            }
            
        coVerify(exactly = 1) { userProfileService.updateUserProfile(eq("profile1"), any()) }
    }

    @Test
    fun `deleteUserProfile should return no content`() {
        // Arrange
        coEvery { userProfileService.deleteUserProfile("profile1") } returns Unit

        // Act & Assert
        webTestClient.delete()
            .uri("/api/profiles/profile1")
            .exchange()
            .expectStatus().isNoContent
            .expectBody().isEmpty
            
        coVerify(exactly = 1) { userProfileService.deleteUserProfile("profile1") }
    }

    private fun createSampleUserProfileDto() = UserProfileDto(
        userId = "user123",
        name = "Test User",
        birthDate = LocalDate.of(1990, 1, 1),
        gender = Gender.MALE,
        bio = "Test bio",
        interests = listOf("hiking", "reading", "travel")
    )
    
    private fun createSampleUserProfile() = UserProfile(
        userId = "user123",
        name = "Test User",
        birthDate = LocalDate.of(1990, 1, 1),
        gender = Gender.MALE,
        bio = "Test bio",
        interests = listOf("hiking", "reading", "travel")
    )
}
