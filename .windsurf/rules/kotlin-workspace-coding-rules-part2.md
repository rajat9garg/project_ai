---
trigger: always_on
---

# Development Best Practices

## Design Patterns Implementation
- Implement Builder Pattern for complex object construction
- Use Factory Method for creating objects without specifying exact class
- Implement Strategy Pattern for selecting algorithm at runtime
- Use Spring Events (Observer Pattern) for loose coupling
- Implement Decorator Pattern for adding responsibilities dynamically
- Use Repository Pattern for data access abstraction
- Implement Command Pattern for operation encapsulation
- Use Adapter Pattern for interface compatibility

## Gradle Configuration
- Organize build.gradle.kts logically
- Use version catalogs for dependency management
- Configure proper test tasks
- Define custom tasks for project-specific operations
- Configure Detekt for static code analysis
- Use appropriate plugin application order
- Leverage Gradle's lazy configuration
- Use buildSrc for custom plugin development

## Performance Optimization
- Prefer non-blocking operations
- Use caching appropriately
- Configure connection pooling properly
- Optimize database queries
- Profile performance-critical code
- Use appropriate data structures
- Implement pagination for large result sets
- Consider bulk operations for batch processing

## MongoDB Query Best Practices
- Use single quotes for MongoDB JSON keys and values in @Query annotations
- Properly escape MongoDB operators with backslash (e.g., '\$near', '\$geometry')
- Use the 'value' parameter explicitly in @Query annotations for better readability
- Prefer '\$nearSphere' over '\$near' for geospatial queries on Earth-like spheres
- Use appropriate parameter types (Double for coordinates, not String)
- Create proper indexes for frequently queried fields
- Use projection to limit returned fields when appropriate

## Data Structure Consistency
- Ensure DTOs, domain models, and repository queries align in structure
- Use Instant instead of LocalDateTime for timestamp fields when appropriate
- Verify parameter names and types match between DTOs and domain models
- Maintain consistent naming conventions across layers
- Use appropriate validation annotations on DTOs
- Implement proper mapping between domain and persistence models

## Build and Deployment
- Use Gradle for dependency management and build processes
- Implement proper profiles for different environments (dev, test, prod)
- Use Docker for containerization
- Set up CI/CD pipelines with proper testing stages
- Implement automated testing in the build process
- Configure proper resource limits for deployments
- Use infrastructure as code for deployment configurations

## Best Practices for Specific Areas
- RESTful API design (proper use of HTTP methods, status codes, etc.)
- Microservices architecture (if applicable)
- Asynchronous processing using Kotlin coroutines
- Event-driven architecture patterns
- Caching strategies for performance improvement
- Database schema evolution and migration
