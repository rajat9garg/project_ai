package learn.ai.elasticsearch.service

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirstOrElse
import kotlinx.coroutines.reactive.awaitFirstOrNull
import learn.ai.elasticsearch.model.Document
import learn.ai.elasticsearch.repository.DocumentRepository
import learn.ai.elasticsearch.util.RedisClient
import mu.KotlinLogging
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.Instant

private val logger = KotlinLogging.logger {}
private const val CACHE_PREFIX = "document:"
private val CACHE_DURATION = Duration.ofMinutes(30)

@Service
class DocumentService(
    private val documentRepository: DocumentRepository,
    private val redisClient: RedisClient
) {

    suspend fun findById(id: String): Document? {
        // Try to get from cache first
        val cacheKey = "$CACHE_PREFIX$id"
        val cachedDocument = redisClient.get(cacheKey) as? Document
        
        if (cachedDocument != null) {
            logger.debug { "Cache hit for document id: $id" }
            return cachedDocument
        }
        
        logger.debug { "Cache miss for document id: $id, fetching from database" }
        val document = documentRepository.findById(id).awaitFirstOrNull()
        
        // Cache the document if found
        if (document != null) {
            redisClient.set(cacheKey, document, CACHE_DURATION)
        }
        
        return document
    }

    suspend fun save(document: Document): Document {
        val savedDocument = if (document.id == null) {
            // New document
            val newDocument = document.copy(
                createdAt = Instant.now(),
                updatedAt = Instant.now()
            )
            documentRepository.save(newDocument).awaitFirstOrElse { throw RuntimeException("Failed to save document") }
        } else {
            // Update existing document
            val existingDocument = documentRepository.findById(document.id).awaitFirstOrNull()
                ?: throw IllegalArgumentException("Document with id ${document.id} not found")
                
            val updatedDocument = document.copy(
                createdAt = existingDocument.createdAt,
                updatedAt = Instant.now()
            )
            
            documentRepository.save(updatedDocument).awaitFirstOrElse { throw RuntimeException("Failed to update document") }
        }
        
        // Update cache
        val cacheKey = "$CACHE_PREFIX${savedDocument.id}"
        redisClient.set(cacheKey, savedDocument, CACHE_DURATION)
        
        return savedDocument
    }

    suspend fun delete(id: String): Boolean {
        val document = documentRepository.findById(id).awaitFirstOrNull()
            ?: return false
            
        documentRepository.delete(document).awaitFirstOrNull()
        
        // Remove from cache
        val cacheKey = "$CACHE_PREFIX$id"
        redisClient.delete(cacheKey)
        
        return true
    }

    fun findAll(): Flow<Document> {
        return documentRepository.findAll().asFlow()
    }

    fun findByTitle(title: String): Flow<Document> {
        return documentRepository.findByTitleContaining(title).asFlow()
    }

    fun findByTag(tag: String): Flow<Document> {
        return documentRepository.findByTagsContaining(tag).asFlow()
    }
}
