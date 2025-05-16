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


DO NOT CREATE THE PROJECT FOLDER YOURSELF UNZIPPING THE FOLDER FROM SPRING INITIALER WILL DO THE JOB


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
Java: Select version 24


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

STEP 5:
- USE object mapper for serialization and deserialization

STEP 6:
- CREATE docker image for the project
- CREATE docker-compose.yml file for the project with the technologies specified in the step 1


STEP 7:
- create connection classes for the technologies specified in the step 1


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