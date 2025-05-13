package com.learn.elasticsearch

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ElasticSearchApplication

fun main(args: Array<String>) {
    // This is the main function that starts the Spring Boot application
    runApplication<ElasticSearchApplication>(*args)
} 