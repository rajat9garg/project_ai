Comprehensive Spring Boot Project Setup Instructions

STEP 1: GATHER REQUIREMENTS [ASK THE HUMAN FOR THIS ALWAYS]
Before beginning the setup process, clearly identify:
1. Project Folder Name
    - Name your main project folder: Example (ElasticSearch)
    - This folder will serve as the root directory for the entire project structure. Ensure the name reflects the core functionality or technology of the project.
Your Project Name 
Your Package Name (e.g., "com.example.elastic")
Your Group ID (e.g., "com.example")
Specific technologies you need from these categories:

Databases: PostgreSQL, MySQL, MongoDB, Casandra
Cache: Redis
Search: Elasticsearch
Message Queue: RabbitMQ, Kafka
Other custom requirements



STEP 2: CREATE BASE PROJECT USING SPRING INITIALIZR

Visit Spring Initializr in your web browser
Set the following options:

Project: Select "Gradle - Kotlin"
Language: Select "Kotlin"
Spring Boot: Choose version 3.2.3 (or latest stable version)
Project Metadata:

Group: Enter your Group ID
Artifact: Enter your Project Name
Name: Same as Artifact
Description: Brief description of your project
Package name: Enter your Package Name
Packaging: Select "Jar"
Java: Select version 21




Add these essential dependencies:

Spring Web
Spring Boot Actuator
Spring Boot DevTools (optional for development)


Click "GENERATE" to download the ZIP file
Extract the ZIP file to your desired location
Open the extracted project in your preferred IDE

STEP 3: ENHANCE THE PROJECT STRUCTURE

Create these additional directories to organize your code:

src/main/kotlin/your/package/path/config
src/main/kotlin/your/package/path/controller
src/main/kotlin/your/package/path/service
src/main/kotlin/your/package/path/repository
src/main/kotlin/your/package/path/model
src/main/kotlin/your/package/path/dto
src/main/kotlin/your/package/path/exception
src/main/kotlin/your/package/path/util
src/main/kotlin/your/package/path/client
src/test/kotlin/your/package/path/service
src/test/kotlin/your/package/path/controller
src/test/kotlin/your/package/path/repository
src/test/resources



STEP 4: ADD REQUIRED DEPENDENCIES
Edit the build.gradle.kts file to add these dependencies:

Kotlin Coroutines Dependencies:

kotlinx-coroutines-core
kotlinx-coroutines-reactor
kotlinx-coroutines-reactive
reactor-kotlin-extensions
kotlinx-serialization-json


Database Dependencies (select based on need):

For PostgreSQL:

spring-boot-starter-data-jpa
postgresql driver
Optional: flyway for migrations


For MySQL:

spring-boot-starter-data-jpa
mysql-connector-j


For MongoDB:

spring-boot-starter-data-mongodb
For reactive MongoDB: spring-boot-starter-data-mongodb-reactive


Cache Dependencies (if needed):

For Redis:

spring-boot-starter-data-redis
For reactive Redis: spring-boot-starter-data-redis-reactive

Search Dependencies (if needed):

For Elasticsearch:

spring-boot-starter-data-elasticsearch
elasticsearch-java client

Message Queue Dependencies (if needed):

For Kafka:
spring-kafka
reactor-kafka for reactive support

For RabbitMQ:
spring-boot-starter-amqp




Testing Dependencies:

spring-boot-starter-test (included by default)
kotlinx-coroutines-test
mockk (for Kotlin mocking)
kotest (optional, for BDD-style testing)



STEP 5: CONFIGURE APPLICATION PROPERTIES
Create or modify src/main/resources/application.yml with the following sections:

Core Application Settings:

Application name
Server port
Logging configurations


Database Configuration (based on selection):

For PostgreSQL/MySQL: connection URL, username, password, JPA settings
For MongoDB: connection URI, database name


Cache Configuration (if Redis selected):

Redis host, port, password (if required)


Search Configuration (if Elasticsearch selected):

Elasticsearch URIs, credentials (if required)


Message Queue Configuration:

For Kafka: bootstrap servers, consumer group ID, topics
For RabbitMQ: host, port, username, password, virtual host, exchanges, queues



STEP 6: CREATE NECESSARY CONFIGURATION CLASSES
Create configuration classes in the config directory for each selected technology:

Database Configurations:

For PostgreSQL/MySQL: Create a JPA/DataSource configuration class
For MongoDB: Create a MongoDB connection configuration class


Cache Configuration:

For Redis: Create a Redis connection configuration class


Search Configuration:

For Elasticsearch: Create an Elasticsearch client configuration class


Message Queue Configuration:

For Kafka: Create Producer/Consumer configuration classes
For RabbitMQ: Create connection, exchange, and queue configuration classes


Async Configuration:

Create a class to configure thread pools for async operations
Set up coroutine context providers



STEP 7: IMPLEMENT CLIENT UTILITIES
Create utility classes in the client directory for interacting with selected technologies:

Database Clients:

Repository interfaces extending appropriate Spring Data interfaces


Cache Client:

Redis operations wrapper if needed beyond Spring Data Redis


Search Client:

Elasticsearch operations wrapper class


Message Queue Clients:

Kafka producer/consumer wrapper classes
RabbitMQ sender/receiver wrapper classes



STEP 8: SET UP COROUTINES SUPPORT

Configure main application class with @EnableAsync annotation
Define application-wide CoroutineScope beans
Set up proper exception handlers for coroutines

STEP 9: IMPLEMENT SERVICE CLASSES
Create service classes with suspend functions for asynchronous operations:

Pattern for database services with coroutines
Pattern for cache services with coroutines
Pattern for search services with coroutines
Pattern for message queue producers/consumers with coroutines

STEP 10: IMPLEMENT CONTROLLERS
Create REST controllers using coroutines:

Pattern for suspending controller methods
Use of Flows for streaming data
Proper error handling with global exception handlers

STEP 11: WRITE TESTS
Create test classes for your components:

Unit tests for services with coroutines
Integration tests for repositories
End-to-end tests for controllers

STEP 12: RUN AND VERIFY

Start the application with ./gradlew bootRun
Test endpoints with Postman or curl
Monitor application health using Actuator endpoints

DATABASE CONNECTIVITY GUIDELINES
PostgreSQL/MySQL Guidelines

Configure the datasource properties in application.yml
Create entity classes with proper annotations
Create repository interfaces extending JpaRepository or CrudRepository
Use repository methods from service classes
For reactive access, use R2DBC with appropriate configuration

MongoDB Guidelines

Configure MongoDB connection properties in application.yml
Create document classes with @Document annotation
Create repository interfaces extending MongoRepository or ReactiveMongoRepository
Use repository methods from service classes
For reactive access, use ReactiveMongoRepository and Flux/Mono

CACHE CONNECTIVITY GUIDELINES
Redis Guidelines

Configure Redis connection properties in application.yml
Create model classes for cached objects
Use RedisTemplate or ReactiveRedisTemplate for operations
Implement caching strategies in service classes
For reactive access, use ReactiveRedisTemplate with Flux/Mono

SEARCH CONNECTIVITY GUIDELINES
Elasticsearch Guidelines

Configure Elasticsearch connection properties in application.yml
Create index model classes with proper annotations
Create repository interfaces extending ElasticsearchRepository
Use high-level client for complex queries
For reactive access, use ReactiveElasticsearchTemplate

MESSAGE QUEUE CONNECTIVITY GUIDELINES
Kafka Guidelines

Configure Kafka connection properties in application.yml
Create producer classes using KafkaTemplate or reactive KafkaSender
Create consumer classes using @KafkaListener or KafkaReceiver
Implement proper serialization/deserialization
For reactive access, use reactor-kafka with Flux/Mono

RabbitMQ Guidelines

Configure RabbitMQ connection properties in application.yml
Create producer classes using RabbitTemplate
Create consumer classes using @RabbitListener
Define exchanges, queues, and bindings in configuration
For reactive access, use Spring AMQP with reactor adapters

ASYNCHRONOUS PROGRAMMING GUIDELINES
Kotlin Coroutines Guidelines

Use suspend functions for asynchronous operations
Use appropriate dispatchers (IO for I/O operations, Default for CPU-intensive tasks)
Use structured concurrency with coroutineScope and supervisorScope
Use Flow for reactive streams
Handle exceptions properly with try-catch or supervisorScope

Reactor Integration Guidelines

Convert between Reactor types (Mono/Flux) and Kotlin coroutines using awaitSingle(), awaitFirst(), etc.
Convert collections to Flux using .asFlux() extension
Convert Flow to Flux using .asFlux() extension
Handle backpressure properly in Flow and Flux operations
Use proper error handling with onErrorResume, onErrorMap, etc.

TESTING GUIDELINES
Coroutines Testing Guidelines

Use runTest for testing suspend functions
Use TestCoroutineDispatcher for controlling execution
Use runBlockingTest for testing flows
Verify interactions with mockk's coVerify for suspend functions
Use turbine for testing Flow emissions

Integration Testing Guidelines

Use @SpringBootTest for full context loading
Use @DataJpaTest for repository tests
Use TestContainers for integration tests with real databases/services
Mock external services using MockWebServer or WireMock
Use proper test profiles with different configurations