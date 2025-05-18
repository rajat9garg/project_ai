package learn.ai.dating.repository

import learn.ai.dating.model.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

/**
 * Repository for managing User entities in MongoDB.
 * Provides methods for user management and querying using Spring Data's query derivation.
 */
@Repository
interface UserRepository : MongoRepository<User, String> {
    
    // Basic CRUD operations are inherited from MongoRepository
    
    /**
     * Check if a user with the given email exists
     */
    fun existsByEmail(email: String): Boolean
    
    /**
     * Find a user by email (case-insensitive)
     */
    fun findByEmailIgnoreCase(email: String): User?
    
    fun existsByEmailIgnoreCase(email: String): Boolean
    
    /**
     * Find users by their city (case-insensitive)
     */
    fun findByLocationCityIgnoreCase(city: String, pageable: Pageable): Page<User>
    
    /**
     * Find users by their country (case-insensitive)
     */
    fun findByLocationCountryIgnoreCase(country: String, pageable: Pageable): Page<User>
    
    /**
     * Find users within a certain age range
     */
    fun findByAgeBetween(minAge: Int, maxAge: Int, pageable: Pageable): Page<User>
    
    /**
     * Find users by gender (case-insensitive)
     */
    fun findByGenderIgnoreCase(gender: String, pageable: Pageable): Page<User>
    
    /**
     * Find users created after a specific date
     */
    fun findByCreatedAtAfter(date: LocalDateTime, pageable: Pageable): Page<User>
    
    /**
     * Find users who have specific interests (case-insensitive)
     * @param interest The interest to search for
     * @param pageable Pagination information
     */
    fun findByPreferencesInterestedInContainingIgnoreCase(interest: String, pageable: Pageable): Page<User>
    
    /**
     * Find users by multiple criteria using method name query derivation
     */
    fun findByGenderIgnoreCaseAndAgeBetweenAndLocationCityIgnoreCaseAndPreferencesInterestedInIn(
        gender: String,
        minAge: Int,
        maxAge: Int,
        city: String,
        interests: Collection<String>,
        pageable: Pageable
    ): Page<User>
}
