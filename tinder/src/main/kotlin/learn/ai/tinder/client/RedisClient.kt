package learn.ai.tinder.client

import kotlinx.coroutines.reactive.awaitFirst
import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.stereotype.Component
import java.time.Duration

/**
 * Client utility for Redis operations.
 * Provides methods for caching and retrieving data with JSON serialization.
 */
@Component
class RedisClient(
    private val redisTemplate: ReactiveRedisTemplate<String, String>,
    private val json: Json = Json {
        prettyPrint = false
        ignoreUnknownKeys = true
        isLenient = true
    }
) {
    companion object {
        private val logger = LoggerFactory.getLogger(RedisClient::class.java)
    }

    /**
     * Set a value in Redis with an optional expiration time.
     *
     * @param key The cache key
     * @param value The value to cache (will be serialized to JSON)
     * @param expiration Optional expiration duration
     * @return True if the operation was successful
     */
    suspend fun <T> setValue(key: String, value: T, serializer: KSerializer<T>, expiration: Duration? = null): Boolean {
        return try {
            val jsonString = json.encodeToString(serializer, value)
            val result = if (expiration != null) {
                redisTemplate.opsForValue().set(key, jsonString, expiration).awaitFirst()
            } else {
                redisTemplate.opsForValue().set(key, jsonString).awaitFirst()
            }
            logger.debug("Set value in Redis for key: $key")
            result
        } catch (e: Exception) {
            logger.error("Error setting value in Redis for key: $key", e)
            false
        }
    }

    /**
     * Get a raw string value from Redis.
     *
     * @param key The cache key
     * @return The cached value as string or null if not found
     */
    suspend fun getValue(key: String): String? {
        return try {
            redisTemplate.opsForValue().get(key).awaitFirstOrNull()
        } catch (e: Exception) {
            logger.error("Error getting value from Redis for key: $key", e)
            null
        }
    }

    /**
     * Get a value from Redis and deserialize it to the specified type.
     *
     * @param key The cache key
     * @param serializer The serializer for the type
     * @return The deserialized object or null if not found
     */
    suspend fun <T> getValue(key: String, serializer: KSerializer<T>): T? {
        val value = getValue(key) ?: return null
        return try {
            json.decodeFromString(serializer, value)
        } catch (e: Exception) {
            logger.error("Error deserializing value from Redis for key: $key", e)
            null
        }
    }

    /**
     * Get a string value from Redis.
     *
     * @param key The cache key
     * @return The string value or null if not found
     */
    suspend fun getString(key: String): String? {
        return getValue(key, String.serializer())
    }

    /**
     * Get an integer value from Redis.
     *
     * @param key The cache key
     * @return The integer value or null if not found
     */
    suspend fun getInt(key: String): Int? {
        return getValue(key, Int.serializer())
    }

    /**
     * Get a long value from Redis.
     *
     * @param key The cache key
     * @return The long value or null if not found
     */
    suspend fun getLong(key: String): Long? {
        return getValue(key, Long.serializer())
    }

    /**
     * Get a boolean value from Redis.
     *
     * @param key The cache key
     * @return The boolean value or null if not found
     */
    suspend fun getBoolean(key: String): Boolean? {
        return getValue(key, Boolean.serializer())
    }

    /**
     * Delete a value from Redis.
     *
     * @param key The cache key
     * @return True if the deletion was successful
     */
    suspend fun deleteValue(key: String): Boolean {
        return try {
            val result = redisTemplate.delete(key).awaitFirst()
            logger.debug("Deleted value from Redis for key: $key")
            result > 0
        } catch (e: Exception) {
            logger.error("Error deleting value from Redis for key: $key", e)
            false
        }
    }

    /**
     * Check if a key exists in Redis.
     *
     * @param key The cache key
     * @return True if the key exists, false otherwise
     */
    suspend fun hasKey(key: String): Boolean {
        return try {
            redisTemplate.hasKey(key).awaitFirst()
        } catch (e: Exception) {
            logger.error("Error checking if key exists in Redis: $key", e)
            false
        }
    }

    /**
     * Set the time-to-live for a key in Redis.
     *
     * @param key The cache key
     * @param timeout The timeout duration
     * @return True if the operation was successful
     */
    suspend fun expire(key: String, timeout: Duration): Boolean {
        return try {
            redisTemplate.expire(key, timeout).awaitFirst()
        } catch (e: Exception) {
            logger.error("Error setting TTL for key in Redis: $key", e)
            false
        }
    }

    /**
     * Increment a counter in Redis.
     *
     * @param key The counter key
     * @return The new value of the counter, or -1 if an error occurred
     */
    suspend fun increment(key: String): Long {
        return try {
            redisTemplate.opsForValue().increment(key).awaitFirst()
        } catch (e: Exception) {
            logger.error("Error incrementing counter in Redis for key: $key", e)
            -1
        }
    }
}
