package learn.ai.tinder.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

/**
 * Configuration for Redis connection and operations.
 * Sets up reactive Redis templates for working with Redis.
 */
@Configuration
class RedisConfig(
    @Value("\${spring.data.redis.host}") private val redisHost: String,
    @Value("\${spring.data.redis.port}") private val redisPort: Int,
    @Value("\${spring.data.redis.database}") private val redisDatabase: Int
) {

    /**
     * Creates a reactive Redis connection factory using Lettuce.
     */
    @Bean
    fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory {
        val config = RedisStandaloneConfiguration(redisHost, redisPort)
        config.database = redisDatabase
        
        val clientConfig = LettuceClientConfiguration.builder()
            .commandTimeout(Duration.ofSeconds(5))
            .build()
            
        return LettuceConnectionFactory(config, clientConfig)
    }

    /**
     * Creates a generic reactive Redis template for String keys and any value type.
     */
    @Bean
    fun <T : Any> reactiveRedisTemplate(
        connectionFactory: ReactiveRedisConnectionFactory,
        valueClass: Class<T>
    ): ReactiveRedisTemplate<String, T> {
        val keySerializer = StringRedisSerializer()
        val valueSerializer = Jackson2JsonRedisSerializer<T>(valueClass)
        
        val serializationContext = RedisSerializationContext
            .newSerializationContext<String, T>(keySerializer)
            .value(valueSerializer)
            .build()
            
        return ReactiveRedisTemplate(connectionFactory, serializationContext)
    }
}
