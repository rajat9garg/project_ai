package learn.ai.tinder.service

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.test.runTest
import learn.ai.tinder.exception.ResourceNotFoundException
import learn.ai.tinder.model.Gender
import learn.ai.tinder.model.UserProfile
import learn.ai.tinder.repository.UserProfileRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.LocalDate

class UserProfileServiceTest {

    private lateinit var userProfileRepository: UserProfileRepository
    private lateinit var userProfileService: UserProfileService

    @BeforeEach
    fun setUp() {
        userProfileRepository = mockk()
        userProfileService = UserProfileService(userProfileRepository)
    }

    @Test
    fun `createUserProfile should save and return the profile when userId doesn't exist`() = runTest {
        // Arrange
        val userProfile = createSampleUserProfile()
        
        coEvery { userProfileRepository.existsByUserId(any()) } returns Mono.just(false)
        coEvery { userProfileRepository.save(any()) } returns Mono.just(userProfile.copy(id = "generatedId"))

        // Act
        val result = userProfileService.createUserProfile(userProfile)

        // Assert
        assertNotNull(result)
        assertEquals("generatedId", result.id)
        assertEquals("user123", result.userId)
        
        coVerify(exactly = 1) { userProfileRepository.existsByUserId("user123") }
        coVerify(exactly = 1) { userProfileRepository.save(userProfile) }
    }

    @Test
    fun `createUserProfile should throw exception when userId already exists`() = runTest {
        // Arrange
        val userProfile = createSampleUserProfile()
        
        coEvery { userProfileRepository.existsByUserId(any()) } returns Mono.just(true)

        // Act & Assert
        val exception = assertThrows<IllegalArgumentException> {
            userProfileService.createUserProfile(userProfile)
        }
        
        assertEquals("User profile with userId user123 already exists", exception.message)
        
        coVerify(exactly = 1) { userProfileRepository.existsByUserId("user123") }
        coVerify(exactly = 0) { userProfileRepository.save(any()) }
    }

    @Test
    fun `getUserProfileById should return profile when it exists`() = runTest {
        // Arrange
        val userProfile = createSampleUserProfile().copy(id = "profile1")
        
        coEvery { userProfileRepository.findById("profile1") } returns Mono.just(userProfile)

        // Act
        val result = userProfileService.getUserProfileById("profile1")

        // Assert
        assertNotNull(result)
        assertEquals("profile1", result.id)
        assertEquals("user123", result.userId)
        
        coVerify(exactly = 1) { userProfileRepository.findById("profile1") }
    }

    @Test
    fun `getUserProfileById should throw ResourceNotFoundException when profile doesn't exist`() = runTest {
        // Arrange
        coEvery { userProfileRepository.findById("nonexistent") } returns Mono.empty()

        // Act & Assert
        val exception = assertThrows<ResourceNotFoundException> {
            userProfileService.getUserProfileById("nonexistent")
        }
        
        assertEquals("User profile not found with id: nonexistent", exception.message)
        assertEquals("/api/profiles/nonexistent", exception.path)
        
        coVerify(exactly = 1) { userProfileRepository.findById("nonexistent") }
    }

    @Test
    fun `getAllUserProfiles should return all profiles`() = runTest {
        // Arrange
        val profile1 = createSampleUserProfile().copy(id = "profile1", userId = "user1")
        val profile2 = createSampleUserProfile().copy(id = "profile2", userId = "user2")
        
        coEvery { userProfileRepository.findAll() } returns Flux.just(profile1, profile2)

        // Act
        val results = userProfileService.getAllUserProfiles().toList()

        // Assert
        assertEquals(2, results.size)
        assertEquals("profile1", results[0].id)
        assertEquals("profile2", results[1].id)
        
        coVerify(exactly = 1) { userProfileRepository.findAll() }
    }

    @Test
    fun `updateUserProfile should update and return the profile when it exists`() = runTest {
        // Arrange
        val existingProfile = createSampleUserProfile().copy(
            id = "profile1",
            name = "Old Name",
            createdAt = java.time.LocalDateTime.now().minusDays(1)
        )
        
        val updatedProfileData = createSampleUserProfile().copy(
            name = "New Name",
            bio = "Updated bio"
        )
        
        val expectedSavedProfile = updatedProfileData.copy(
            id = "profile1",
            userId = existingProfile.userId,
            createdAt = existingProfile.createdAt,
            version = existingProfile.version
        )
        
        coEvery { userProfileRepository.findById("profile1") } returns Mono.just(existingProfile)
        coEvery { userProfileRepository.save(any()) } returns Mono.just(expectedSavedProfile)

        // Act
        val result = userProfileService.updateUserProfile("profile1", updatedProfileData)

        // Assert
        assertNotNull(result)
        assertEquals("profile1", result.id)
        assertEquals("New Name", result.name)
        assertEquals("Updated bio", result.bio)
        assertEquals(existingProfile.createdAt, result.createdAt)
        
        coVerify(exactly = 1) { userProfileRepository.findById("profile1") }
        coVerify(exactly = 1) { userProfileRepository.save(any()) }
    }

    @Test
    fun `deleteUserProfile should delete the profile when it exists`() = runTest {
        // Arrange
        coEvery { userProfileRepository.existsById("profile1") } returns Mono.just(true)
        coEvery { userProfileRepository.deleteById("profile1") } returns Mono.empty()

        // Act
        userProfileService.deleteUserProfile("profile1")

        // Assert
        coVerify(exactly = 1) { userProfileRepository.existsById("profile1") }
        coVerify(exactly = 1) { userProfileRepository.deleteById("profile1") }
    }

    @Test
    fun `deleteUserProfile should throw ResourceNotFoundException when profile doesn't exist`() = runTest {
        // Arrange
        coEvery { userProfileRepository.existsById("nonexistent") } returns Mono.just(false)

        // Act & Assert
        val exception = assertThrows<ResourceNotFoundException> {
            userProfileService.deleteUserProfile("nonexistent")
        }
        
        assertEquals("User profile not found with id: nonexistent", exception.message)
        
        coVerify(exactly = 1) { userProfileRepository.existsById("nonexistent") }
        coVerify(exactly = 0) { userProfileRepository.deleteById(any()) }
    }

    private fun createSampleUserProfile() = UserProfile(
        userId = "user123",
        name = "Test User",
        birthDate = LocalDate.of(1990, 1, 1),
        gender = Gender.MALE,
        bio = "Test bio",
        interests = listOf("hiking", "reading", "travel")
    )
}
