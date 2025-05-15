package learn.ai.elasticsearch.config

import io.lettuce.core.ClientOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.connection.RedisPassword
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
class RedisConfig(
    @Value("\${spring.redis.host}") private val host: String,
    @Value("\${spring.redis.port}") private val port: Int,
    @Value("\${spring.redis.password:}") private val password: String,
    @Value("\${spring.redis.database:0}") private val database: Int,
    @Value("\${spring.redis.timeout:2000}") private val timeout: Long
) {
    @Bean
    fun reactiveRedisConnectionFactory(): ReactiveRedisConnectionFactory {
        val redisStandaloneConfig = RedisStandaloneConfiguration(host, port)
        if (password.isNotBlank()) {
            redisStandaloneConfig.password = RedisPassword.of(password)
        }
        redisStandaloneConfig.database = database

        val clientOptions = ClientOptions.builder()
            .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS)
            .autoReconnect(true)
            .build()

        val clientConfig = LettuceClientConfiguration.builder()
            .clientOptions(clientOptions)
            .commandTimeout(Duration.ofMillis(timeout))
            .build()

        return LettuceConnectionFactory(redisStandaloneConfig, clientConfig)
    }

    @Bean
    fun reactiveRedisTemplate(factory: ReactiveRedisConnectionFactory): ReactiveRedisTemplate<String, Any> {
        val stringSerializer = StringRedisSerializer()
        val jackson2JsonRedisSerializer = Jackson2JsonRedisSerializer(Any::class.java)
        val serializationContext = RedisSerializationContext
            .newSerializationContext<String, Any>()
            .key(stringSerializer)
            .value(jackson2JsonRedisSerializer)
            .build()
        return ReactiveRedisTemplate(factory, serializationContext)
    }
}
