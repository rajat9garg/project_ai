package com.learn.elasticsearch.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig {

    @Value("\${spring.redis.host}")
    private lateinit var host: String

    @Value("\${spring.redis.port}")
    private var port: Int = 6379

    @Bean
    fun redisConnectionFactory(): RedisConnectionFactory {
        val redisConfig = RedisStandaloneConfiguration(host, port)
        return LettuceConnectionFactory(redisConfig)
    }

    @Bean
    fun redisTemplate(): RedisTemplate<String, Any> {
        val template = RedisTemplate<String, Any>()
        template.connectionFactory = redisConnectionFactory()
        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = GenericJackson2JsonRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = GenericJackson2JsonRedisSerializer()
        template.afterPropertiesSet()
        return template
    }

    @Bean
    fun cacheManager(): CacheManager {
        val cacheConfig = RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(60))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
            .disableCachingNullValues()

        return RedisCacheManager.builder(redisConnectionFactory())
            .cacheDefaults(cacheConfig)
            .withCacheConfiguration("users", 
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30)))
            .withCacheConfiguration("products", 
                RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofHours(1)))
            .build()
    }

    @Bean
    fun redisCacheConfiguration(): RedisCacheConfiguration {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(60))
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(StringRedisSerializer()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(GenericJackson2JsonRedisSerializer()))
            .disableCachingNullValues()
    }
} 