package com.learn.elasticsearch.service

import com.learn.elasticsearch.model.User
import com.learn.elasticsearch.repository.UserRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Service
import java.time.ZonedDateTime

@Service
class UserService(
    private val userRepository: UserRepository,
    private val redisTemplate: RedisTemplate<String, Any>
) {
    @Cacheable(value = ["users"], key = "#id")
    fun getUserById(id: String): User? {
        return userRepository.findById(id).orElse(null)
    }

    @Cacheable(value = ["users"], key = "#email")
    fun getUserByEmail(email: String): User? {
        return userRepository.findByEmail(email)
    }

    @CacheEvict(value = ["users"], allEntries = true)
    fun createUser(user: User): User {
        val now = ZonedDateTime.now()
        val newUser = user.copy(
            createdAt = now,
            updatedAt = now
        )
        return userRepository.save(newUser)
    }

    @CacheEvict(value = ["users"], allEntries = true)
    fun updateUser(id: String, user: User): User? {
        return userRepository.findById(id).map { existingUser ->
            val updatedUser = existingUser.copy(
                username = user.username,
                email = user.email,
                updatedAt = ZonedDateTime.now()
            )
            userRepository.save(updatedUser)
        }.orElse(null)
    }

    @CacheEvict(value = ["users"], allEntries = true)
    fun deleteUser(id: String) {
        userRepository.deleteById(id)
    }

    fun getAllUsers(): List<User> {
        return userRepository.findAll().toList()
    }
} 