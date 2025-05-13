package com.learn.elasticsearch.model

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime

@Document(collection = "users")
data class User(
    @Id
    val id: String? = null,
    val username: String,
    val email: String,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val updatedAt: ZonedDateTime = ZonedDateTime.now()
) 