package learn.ai.elasticsearch.util

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import mu.KotlinLogging
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.stereotype.Component

private val logger = KotlinLogging.logger {}

@Component
class MongoClient(private val mongoTemplate: ReactiveMongoTemplate) {

    suspend fun <T : Any> findById(id: String, entityClass: Class<T>): T? {
        logger.debug { "Finding document by id: $id in collection: ${entityClass.simpleName}" }
        return mongoTemplate.findById(id, entityClass).awaitFirstOrNull()
    }

    suspend fun <T : Any> findOne(query: Query, entityClass: Class<T>): T? {
        logger.debug { "Finding one document with query: $query in collection: ${entityClass.simpleName}" }
        return mongoTemplate.findOne(query, entityClass).awaitFirstOrNull()
    }

    fun <T : Any> findAll(entityClass: Class<T>): Flow<T> {
        logger.debug { "Finding all documents in collection: ${entityClass.simpleName}" }
        return mongoTemplate.findAll(entityClass).asFlow()
    }

    fun <T : Any> find(query: Query, entityClass: Class<T>): Flow<T> {
        logger.debug { "Finding documents with query: $query in collection: ${entityClass.simpleName}" }
        return mongoTemplate.find(query, entityClass).asFlow()
    }

    suspend fun <T : Any> save(entity: T): T {
        logger.debug { "Saving document to collection: ${entity!!::class.java.simpleName}" }
        return mongoTemplate.save(entity).awaitFirst()
    }

    suspend fun <T : Any> updateFirst(query: Query, update: Update, entityClass: Class<T>): Boolean {
        logger.debug { "Updating first document with query: $query, update: $update in collection: ${entityClass.simpleName}" }
        val result = mongoTemplate.updateFirst(query, update, entityClass).awaitFirst()
        return result.modifiedCount > 0
    }

    suspend fun <T : Any> updateMulti(query: Query, update: Update, entityClass: Class<T>): Long {
        logger.debug { "Updating multiple documents with query: $query, update: $update in collection: ${entityClass.simpleName}" }
        val result = mongoTemplate.updateMulti(query, update, entityClass).awaitFirst()
        return result.modifiedCount
    }

    suspend fun <T : Any> remove(entity: T): Boolean {
        logger.debug { "Removing document from collection: ${entity!!::class.java.simpleName}" }
        val result = mongoTemplate.remove(entity).awaitFirst()
        return result.deletedCount > 0
    }

    suspend fun <T : Any> remove(query: Query, entityClass: Class<T>): Long {
        logger.debug { "Removing documents with query: $query from collection: ${entityClass.simpleName}" }
        val result = mongoTemplate.remove(query, entityClass).awaitFirst()
        return result.deletedCount
    }

    suspend fun <T : Any> count(query: Query, entityClass: Class<T>): Long {
        logger.debug { "Counting documents with query: $query in collection: ${entityClass.simpleName}" }
        return mongoTemplate.count(query, entityClass).awaitFirst()
    }

    suspend fun <T : Any> exists(query: Query, entityClass: Class<T>): Boolean {
        logger.debug { "Checking if document exists with query: $query in collection: ${entityClass.simpleName}" }
        return mongoTemplate.exists(query, entityClass).awaitFirst()
    }
}
