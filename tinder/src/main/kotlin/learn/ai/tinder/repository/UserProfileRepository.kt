package learn.ai.tinder.repository

import learn.ai.tinder.model.UserProfile
import org.springframework.data.mongodb.repository.ReactiveMongoRepository
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono

/**
 * Repository interface for UserProfile documents.
 * Extends ReactiveMongoRepository to provide reactive MongoDB operations.
 */
@Repository
interface UserProfileRepository : ReactiveMongoRepository<UserProfile, String> {
    
    /**
     * Find a user profile by userId.
     * 
     * @param userId The unique user identifier
     * @return A Mono containing the user profile if found
     */
    fun findByUserId(userId: String): Mono<UserProfile>
    
    /**
     * Check if a user profile exists by userId.
     * 
     * @param userId The unique user identifier
     * @return A Mono containing true if the profile exists, false otherwise
     */
    fun existsByUserId(userId: String): Mono<Boolean>
}
