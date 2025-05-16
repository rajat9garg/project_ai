package learn.ai.tinder.config

import com.mongodb.reactivestreams.client.MongoClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.ReactiveMongoTransactionManager
import org.springframework.data.mongodb.config.EnableReactiveMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

/**
 * Configuration for MongoDB connection and repositories.
 * Enables reactive MongoDB repositories and auditing.
 */
@Configuration
@EnableReactiveMongoRepositories(basePackages = ["learn.ai.tinder.repository"])
@EnableReactiveMongoAuditing
class MongoConfig(
    private val mongoClient: MongoClient
) {
    /**
     * Creates a transaction manager for reactive MongoDB operations.
     * Note: Transactions require a replica set configuration in MongoDB.
     */
    @Bean
    fun reactiveMongoTransactionManager(dbFactory: ReactiveMongoDatabaseFactory): ReactiveMongoTransactionManager {
        return ReactiveMongoTransactionManager(dbFactory)
    }
}
