package learn.ai.tinder.repository

import learn.ai.tinder.model.User
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.mongodb.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : MongoRepository<User, String> {
    
    fun existsByEmail(email: String): Boolean
    
    fun findByEmail(email: String): User?
    
    /**
     * Finds users near a specific location within a certain distance.
     * @param longitude The longitude of the center point
     * @param latitude The latitude of the center point
     * @param distance The maximum distance in meters
     * @param excludeUserId The user ID to exclude from results
     * @return List of users within the specified distance from the point
     */
    @Query(value = "{ 'location': { '\$nearSphere': { '\$geometry': { 'type': 'Point', 'coordinates': [?0, ?1] }, '\$maxDistance': ?2 } }, 'isActive': true, '_id': { '\$ne': ?3 } }")
    fun findNearLocation(
        longitude: Double,
        latitude: Double,
        distance: Double,
        excludeUserId: String
    ): List<User>
}