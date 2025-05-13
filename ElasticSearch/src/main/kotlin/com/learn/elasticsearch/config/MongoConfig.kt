package com.learn.elasticsearch.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClient
import com.mongodb.client.MongoClients
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.convert.MongoCustomConversions
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.Date

@Configuration
@EnableMongoRepositories(basePackages = ["com.learn.elasticsearch.repository"])
@EnableMongoAuditing
class MongoConfig {

    @Value("\${spring.data.mongodb.host}")
    private lateinit var host: String

    @Value("\${spring.data.mongodb.port}")
    private var port: Int = 27017

    @Value("\${spring.data.mongodb.database}")
    private lateinit var database: String

    @Bean
    fun mongoClient(): MongoClient {
        val connectionString = ConnectionString("mongodb://$host:$port")
        val mongoClientSettings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build()
        return MongoClients.create(mongoClientSettings)
    }

    @Bean
    fun mongoTemplate(): MongoTemplate {
        return MongoTemplate(mongoClient(), database)
    }

    @Bean
    fun validatingMongoEventListener(): ValidatingMongoEventListener {
        return ValidatingMongoEventListener(validator())
    }

    @Bean
    fun validator(): LocalValidatorFactoryBean {
        return LocalValidatorFactoryBean()
    }

    @Bean
    fun mongoCustomConversions(): MongoCustomConversions {
        return MongoCustomConversions(listOf(
            ZonedDateTimeToDateConverter(),
            DateToZonedDateTimeConverter()
        ))
    }
}

class ZonedDateTimeToDateConverter : org.springframework.core.convert.converter.Converter<ZonedDateTime, Date> {
    override fun convert(source: ZonedDateTime): Date {
        return Date.from(source.toInstant())
    }
}

class DateToZonedDateTimeConverter : org.springframework.core.convert.converter.Converter<Date, ZonedDateTime> {
    override fun convert(source: Date): ZonedDateTime {
        return source.toInstant().atZone(ZoneOffset.UTC)
    }
} 