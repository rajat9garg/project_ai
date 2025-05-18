package learn.ai.dating.config

import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean

@Configuration
@EnableMongoRepositories(basePackages = ["learn.ai.dating.repository"])
class MongoConfig : AbstractMongoClientConfiguration() {

    @Value("\${spring.data.mongodb.uri:mongodb://localhost:27017/datingApp}")
    private lateinit var mongoUri: String

    @Value("\${spring.data.mongodb.database:datingApp}")
    private lateinit var databaseName: String

    override fun getDatabaseName(): String = databaseName

    @Bean
    override fun mongoClient(): MongoClient {
        return MongoClients.create(mongoUri)
    }

    @Bean
    fun mongoTemplate(): MongoTemplate {
        return MongoTemplate(mongoClient(), databaseName)
    }

    @Bean
    fun validator(): LocalValidatorFactoryBean {
        return LocalValidatorFactoryBean()
    }

    @Bean
    fun validatingMongoEventListener(validator: LocalValidatorFactoryBean): ValidatingMongoEventListener {
        return ValidatingMongoEventListener(validator)
    }
}
