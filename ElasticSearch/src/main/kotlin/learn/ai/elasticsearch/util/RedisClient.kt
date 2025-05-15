package learn.ai.elasticsearch.util

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import mu.KotlinLogging
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

private val logger = KotlinLogging.logger {}

@Component
class RedisClient(private val redisTemplate: ReactiveRedisTemplate<String, Any>) {

    suspend fun set(key: String, value: Any): Boolean {
        logger.debug { "Setting key: $key in Redis" }
        return redisTemplate.opsForValue().set(key, value).awaitFirst()
    }

    suspend fun set(key: String, value: Any, expiration: Duration): Boolean {
        logger.debug { "Setting key: $key with expiration: $expiration in Redis" }
        return redisTemplate.opsForValue().set(key, value, expiration).awaitFirst()
    }

    suspend fun get(key: String): Any? {
        logger.debug { "Getting value for key: $key from Redis" }
        return redisTemplate.opsForValue().get(key).awaitFirstOrNull()
    }

    suspend fun delete(key: String): Boolean {
        logger.debug { "Deleting key: $key from Redis" }
        return redisTemplate.delete(key).awaitFirst() > 0
    }

    suspend fun hasKey(key: String): Boolean {
        logger.debug { "Checking if key exists: $key in Redis" }
        return redisTemplate.hasKey(key).awaitFirst()
    }

    suspend fun expire(key: String, timeout: Duration): Boolean {
        logger.debug { "Setting expiration for key: $key to: $timeout in Redis" }
        return redisTemplate.expire(key, timeout).awaitFirst()
    }

    suspend fun increment(key: String): Long {
        logger.debug { "Incrementing value for key: $key in Redis" }
        return redisTemplate.opsForValue().increment(key).awaitFirst()
    }

    suspend fun increment(key: String, delta: Long): Long {
        logger.debug { "Incrementing value for key: $key by delta: $delta in Redis" }
        return redisTemplate.opsForValue().increment(key, delta).awaitFirst()
    }

    suspend fun hSet(key: String, hashKey: String, value: Any): Boolean {
        logger.debug { "Setting hash key: $hashKey for key: $key in Redis" }
        return redisTemplate.opsForHash<String, Any>().put(key, hashKey, value).awaitFirst()
    }

    suspend fun hGet(key: String, hashKey: String): Any? {
        logger.debug { "Getting hash value for key: $key, hash key: $hashKey from Redis" }
        return redisTemplate.opsForHash<String, Any>().get(key, hashKey).awaitFirstOrNull()
    }

    suspend fun hDelete(key: String, hashKey: String): Boolean {
        logger.debug { "Deleting hash key: $hashKey for key: $key from Redis" }
        return redisTemplate.opsForHash<String, Any>().remove(key, hashKey).awaitFirst() > 0
    }

    suspend fun hHasKey(key: String, hashKey: String): Boolean {
        logger.debug { "Checking if hash key exists: $hashKey for key: $key in Redis" }
        return redisTemplate.opsForHash<String, Any>().hasKey(key, hashKey).awaitFirst()
    }
}
