package learn.ai.tinder.config

import jakarta.validation.constraints.Max
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.StringRedisSerializer
import org.springframework.validation.annotation.Validated

@Configuration
@EnableConfigurationProperties(RedisConfig.RedisConnectionProperties::class)
class RedisConfig(
    private val properties: RedisConfig.RedisConnectionProperties
) {
    
    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory =
        RedisStandaloneConfiguration()
            .apply {
                hostName = properties.host
                port = properties.port
                database = properties.database
            }
            .let(::LettuceConnectionFactory)
            .apply {
                afterPropertiesSet()
            }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> =
        RedisTemplate<String, Any>().apply {
            connectionFactory = redisConnectionFactory()
            keySerializer = StringRedisSerializer()
            valueSerializer = GenericJackson2JsonRedisSerializer()
            hashKeySerializer = StringRedisSerializer()
            hashValueSerializer = GenericJackson2JsonRedisSerializer()
            afterPropertiesSet()
        }

    @ConfigurationProperties(prefix = "spring.data.redis")
    @Validated
    data class RedisConnectionProperties(
        @field:NotBlank
        val host: String = "localhost",
        
        @field:Min(1)
        @field:Max(65535)
        val port: Int = 6379,
        
        @field:Min(0)
        val database: Int = 0,
        
        val timeout: String = "5000ms"
    )
}
