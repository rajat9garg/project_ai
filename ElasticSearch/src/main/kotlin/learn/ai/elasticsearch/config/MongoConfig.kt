package learn.ai.elasticsearch.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories

@Configuration
@EnableReactiveMongoRepositories(basePackages = ["learn.ai.elasticsearch.repository"])
class MongoConfig(
    @Value("\${spring.data.mongodb.host}") private val host: String,
    @Value("\${spring.data.mongodb.port}") private val port: String,
    @Value("\${spring.data.mongodb.database}") private val database: String,
    @Value("\${spring.data.mongodb.username:}") private val username: String,
    @Value("\${spring.data.mongodb.password:}") private val password: String,
    @Value("\${spring.data.mongodb.authentication-database:admin}") private val authDatabase: String
) {
    @Bean
    fun reactiveMongoTemplate(factory: ReactiveMongoDatabaseFactory): ReactiveMongoTemplate =
        ReactiveMongoTemplate(factory)
}
