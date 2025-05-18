# Kotlin Workspace Coding Rules

## Overview
You are an expert in Kotlin programming, Spring Boot, Spring Framework, Gradle, jOOQ, and related JVM technologies.

## Code Style and Structure
- Write clean, efficient, and well-documented Kotlin code with accurate Spring Boot examples.
- Use Spring Boot best practices and conventions throughout your code.
- Implement RESTful API design patterns when creating web services.
- Use descriptive method and variable names following camelCase convention.
- Structure Spring Boot applications: controllers, services, repositories, models, configurations.
- Follow SOLID principles to ensure code is maintainable and extensible.
- Keep classes small and focused on a single responsibility.

## Kotlin-Specific Best Practices
- Prefer val over var to create immutable references.
- Utilize Kotlin's null safety features to prevent null pointer exceptions.
- Use data classes for DTOs and immutable data structures.
- Leverage Kotlin's extension functions to enhance existing classes without inheritance.
- Use sealed classes for representing restricted class hierarchies.
- Implement Kotlin's scope functions (let, apply, run, with, also) appropriately.
- Leverage Kotlin's coroutines for asynchronous programming.
- use constructor based dependency injection
- use kotlin specific best practices

## Spring Boot Specifics
- Use Spring Boot starters for quick project setup and dependency management.
- Implement proper use of annotations (e.g., @SpringBootApplication, @RestController, @Service).
- Utilize Spring Boot's auto-configuration features effectively.
- Implement proper exception handling using @ControllerAdvice and @ExceptionHandler.
- Keep controllers thin; business logic belongs in services.

## Naming Conventions
- Use PascalCase for class names (e.g., UserController, OrderService).
- Use camelCase for method and variable names (e.g., findUserById, isOrderValid).
- Use ALL_CAPS for constants (e.g., MAX_RETRY_ATTEMPTS, DEFAULT_PAGE_SIZE).
- Use meaningful and descriptive names that reflect purpose.

## Configuration and Properties
- Use application.yml over application.properties for better structure.
- Implement environment-specific configurations using Spring Profiles.
- Use @ConfigurationProperties for type-safe configuration properties.
- Keep configuration organized by concern.

## Dependency Injection and IoC
- Use constructor injection over field injection for better testability.
- Properly scope beans (@Singleton, @Prototype, etc.).
- Use appropriate stereotypes (@Service, @Repository, @Component, @Controller).
- Avoid circular dependencies.

## Testing
- Write unit tests using JUnit 5 and Spring Boot Test.
- Follow AAA pattern (Arrange-Act-Assert) in tests.
- Use MockK for mocking in Kotlin tests.
- Implement integration tests using @SpringBootTest.
- Use TestContainers for database tests.
- Test edge cases and error conditions.

## Security
- Implement Spring Security for authentication and authorization.
- Use proper password encoding (e.g., BCrypt).
- Validate all user inputs.
- Use parameterized queries to prevent SQL injection.
- Follow OWASP security guidelines.

## Logging and Monitoring
- Use SLF4J with Logback for logging.
- Implement proper log levels (ERROR, WARN, INFO, DEBUG).
- Don't log sensitive information.
- Use Spring Boot Actuator for application monitoring and metrics.

## API Documentation
- Use Springdoc OpenAPI for API documentation.
- Document all public APIs with KDoc.
- Include examples for complex methods.

## Design Patterns Implementation
- Implement Builder Pattern for complex object construction.
- Use Factory Method for creating objects without specifying exact class.
- Implement Strategy Pattern for selecting algorithm at runtime.
- Use Spring Events (Observer Pattern) for loose coupling.
- Implement Decorator Pattern for adding responsibilities dynamically.

## Gradle Configuration
- Organize build.gradle.kts logically.
- Use version catalogs for dependency management.
- Configure proper test tasks.
- Define custom tasks for project-specific operations.
- Configure Detekt for static code analysis.

## Performance Optimization
- Prefer non-blocking operations.
- Use caching appropriately.
- Configure connection pooling properly.
- Optimize database queries.
- Profile performance-critical code.

## Error Handling
- Implement comprehensive exception handling.
- Use Kotlin's Result type for more functional error handling.
- Create custom exceptions for domain-specific errors.
- Provide meaningful error messages.

## Build and Deployment
- Use Gradle for dependency management and build processes.
- Implement proper profiles for different environments (dev, test, prod).
- Use Docker for containerization.
- Set up CI/CD pipelines with proper testing stages.

## Best Practices for Specific Areas
- RESTful API design (proper use of HTTP methods, status codes, etc.).
- Microservices architecture (if applicable).
- Asynchronous processing using Kotlin coroutines.

## MongoDB Query Best Practices
- Use single quotes for MongoDB JSON keys and values in @Query annotations
- Properly escape MongoDB operators with backslash (e.g., '\$near', '\$geometry')
- Use the 'value' parameter explicitly in @Query annotations for better readability
- Prefer '\$nearSphere' over '\$near' for geospatial queries on Earth-like spheres
- Use appropriate parameter types (Double for coordinates, not String)

## Exception Handling Guidelines
- Standardize error response format across all exception handlers
- Include request paths in log messages for better traceability
- Generate unique error IDs for unexpected exceptions
- Properly handle specific exceptions like DataIntegrityViolationException
- Structure field errors in a consistent, readable format

## Kotlin Coroutines Testing
- Use runTest for testing suspend functions in unit tests
- Properly wrap assertions that test suspending functions with runBlocking when needed
- Ensure mock setup is compatible with coroutine execution
- Always mock ALL method calls that will occur during test execution, including chained calls
- For each mocked method that returns a value used later in the service, explicitly define behavior with `every { ... } returns ...`
- Verify all important mock interactions with `verify(exactly = n) { ... }`

## Data Structure Consistency
- Ensure DTOs, domain models, and repository queries align in structure
- Use Instant instead of LocalDateTime for timestamp fields when appropriate
- Verify parameter names and types match between DTOs and domain models

## Constructor Injection Best Practices
- Always use `val` for immutable dependencies
- Keep constructors concise and focused
- Use `@ConfigurationProperties` for type-safe configuration
- Prefer constructor injection for required dependencies
- Use `@Autowired` for optional dependencies or when using `lateinit var`
- Document complex dependency graphs with `@Qualifier`
- Keep the number of constructor parameters reasonable (consider refactoring if >5)
- Use `@ConfigurationProperties` for grouping related configuration values
- Always provide sensible defaults for configuration properties
- Use `@Value` only for simple property injection when `@ConfigurationProperties` is overkill
