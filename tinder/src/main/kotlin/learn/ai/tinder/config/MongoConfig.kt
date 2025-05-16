package learn.ai.tinder.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.MongoDatabaseFactory
import org.springframework.data.mongodb.MongoTransactionManager
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean


@Configuration
@EnableMongoRepositories(basePackages = ["learn.ai.tinder.repository.mongo"])
class MongoConfig {

    @Bean
    fun mongoTransactionManager(dbFactory: MongoDatabaseFactory): MongoTransactionManager {
        return MongoTransactionManager(dbFactory)
    }

    @Bean
    fun mongoCustomConversions(): MongoCustomConversions {
        return MongoCustomConversions(emptyList<Any>())
    }

    @Bean
    fun validatingMongoEventListener(validator: LocalValidatorFactoryBean): ValidatingMongoEventListener {
        return ValidatingMongoEventListener(validator).apply {
            validator.afterPropertiesSet()
        }
    }
}
