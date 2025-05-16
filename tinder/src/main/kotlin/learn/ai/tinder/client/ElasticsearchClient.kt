package learn.ai.tinder.client

import co.elastic.clients.elasticsearch.ElasticsearchClient
import kotlinx.coroutines.reactor.awaitSingle
import learn.ai.tinder.model.Gender
import learn.ai.tinder.model.UserProfile
import org.slf4j.LoggerFactory
import org.springframework.data.elasticsearch.client.elc.ReactiveElasticsearchTemplate
import org.springframework.data.elasticsearch.core.SearchHit
import org.springframework.data.elasticsearch.core.query.*
import org.springframework.stereotype.Component
import reactor.core.publisher.Flux
import java.time.LocalDate
import java.time.Period

/**
 * Client utility for Elasticsearch operations related to user profiles.
 * Provides methods for searching and filtering user profiles.
 */
@Component
class ElasticsearchClient(
    private val elasticsearchClient: ElasticsearchClient,
    private val reactiveElasticsearchTemplate: ReactiveElasticsearchTemplate
) {
    private val logger = LoggerFactory.getLogger(ElasticsearchClient::class.java)
    
    companion object {
        const val USER_PROFILE_INDEX = "user_profiles"
    }
    
    /**
     * Search for potential matches based on user preferences.
     *
     * @param userId The ID of the user searching for matches
     * @param userProfile The user profile containing preferences
     * @param page The page number (0-based)
     * @param size The page size
     * @return A Flux of matching user profiles
     */
    suspend fun findPotentialMatches(
        userId: String,
        userProfile: UserProfile,
        page: Int = 0,
        size: Int = 20
    ): Flux<SearchHit<UserProfile>> {
        logger.debug("Finding potential matches for user: $userId")
        
        // Create a simple query using StringQuery
        val queryString = """
            {
                "bool": {
                    "must_not": {"term": {"userId": "$userId"}},
                    "must": [
                        {"terms": {"gender": ${userProfile.preferences.genderPreferences.map { "\"${it.name}\"" }}},
                        {"range": {"birthDate": {"lte": "now-${userProfile.preferences.minAge}y/y", "gte": "now-${userProfile.preferences.maxAge}y/y"}}},
                        {"term": {"preferences.showMe": true}}
                    ]
                }
            }
        """.trimIndent()
        
        val query = StringQuery(queryString, org.springframework.data.domain.PageRequest.of(page, size))
        
        return reactiveElasticsearchTemplate.search(query, UserProfile::class.java)
    }
    
    /**
     * Search for user profiles by interests.
     *
     * @param interests The list of interests to search for
     * @param page The page number (0-based)
     * @param size The page size
     * @return A Flux of matching user profiles
     */
    suspend fun searchByInterests(
        interests: List<String>,
        page: Int = 0,
        size: Int = 20
    ): Flux<SearchHit<UserProfile>> {
        logger.debug("Searching for profiles with interests: $interests")
        
        val queryString = """
            {
                "query": {
                    "terms": {
                        "interests": ${interests.map { "\"${it.lowercase()}\"" }}
                    }
                }
            }
        """.trimIndent()
        
        val query = StringQuery(
            queryString,
            org.springframework.data.domain.PageRequest.of(page, size)
        )
        
        return reactiveElasticsearchTemplate.search(query, UserProfile::class.java)
    }
    
    /**
     * Index a user profile in Elasticsearch.
     *
     * @param userProfile The user profile to index
     * @return True if indexing was successful
     */
    suspend fun indexUserProfile(userProfile: UserProfile): Boolean {
        try {
            val result = reactiveElasticsearchTemplate.save(userProfile).awaitSingle()
            logger.debug("Indexed user profile: ${result.id}")
            return true
        } catch (e: Exception) {
            logger.error("Error indexing user profile: ${userProfile.id}", e)
            return false
        }
    }
    
    /**
     * Delete a user profile from the Elasticsearch index.
     *
     * @param id The ID of the user profile to delete
     * @return True if deletion was successful
     */
    suspend fun deleteUserProfile(id: String): Boolean {
        try {
            reactiveElasticsearchTemplate.delete(id, UserProfile::class.java).awaitSingle()
            logger.debug("Deleted user profile from index: $id")
            return true
        } catch (e: Exception) {
            logger.error("Error deleting user profile from index: $id", e)
            return false
        }
    }
}
