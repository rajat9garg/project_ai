package com.learn.elasticsearch.util

import com.mongodb.client.MongoClient
import org.springframework.stereotype.Component

@Component
class MongoClientUtil(private val mongoClient: MongoClient) {
    fun isMongoDbUp(): Boolean = try {
        mongoClient.listDatabaseNames().firstOrNull() != null
    } catch (ex: Exception) {
        false
    }
} 