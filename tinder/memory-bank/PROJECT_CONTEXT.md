# Dating Application - Project Context

## Project Overview
- **Project Name**: tinder
- **Package Name**: learn.ai.tinder
- **Group ID**: learn.ai
- **Spring Boot Version**: 3.3.0
- **Java Version**: 24
- **Build Tool**: Gradle (Kotlin DSL)

## Technology Stack

### Core Technologies
- **Backend**: Spring Boot 3.3.0 with Kotlin
- **Databases**:
  - MongoDB (Primary Document Store)
  - Cassandra (For scalable data needs)
- **Caching**: Redis
- **Containerization**: Docker with Docker Compose

### Development Tools
- **Build Tool**: Gradle with Kotlin DSL
- **Testing**: JUnit 5, MockK, TestContainers
- **Code Quality**: ktlint, detekt (configured in build.gradle.kts)

## Project Structure
```
src/
├── main/
│   ├── kotlin/learn/ai/tinder/
│   │   ├── config/       # Configuration classes
│   │   ├── controller/   # REST controllers
│   │   ├── service/      # Business logic
│   │   ├── repository/   # Data access layer
│   │   ├── model/        # Domain models
│   │   ├── dto/          # Data Transfer Objects
│   │   ├── exception/    # Custom exceptions
│   │   └── util/         # Utility classes
│   └── resources/        # Application properties and static resources
└── test/                 # Test files
```

## Configuration

### Application Properties
Configuration supports environment variables with default values:

```yaml
# Server Configuration
server:
  port: ${SERVER_PORT:8080}
  servlet:
    context-path: ${SERVER_SERVLET_CONTEXT_PATH:/api}

# Database Configuration
spring:
  data:
    mongodb:
      host: ${MONGO_HOST:localhost}
      port: ${MONGO_PORT:27017}
      database: ${MONGO_DATABASE:tinder}
    redis:
      host: ${REDIS_HOST:localhost}
      port: ${REDIS_PORT:6379}
      database: ${REDIS_DATABASE:0}
  cassandra:
    contact-points: ${CASSANDRA_CONTACT_POINTS:localhost:9042}
    keyspace-name: ${CASSANDRA_KEYSPACE:tinder}
    local-datacenter: ${CASSANDRA_DATACENTER:datacenter1}
```

### Docker Services
- **MongoDB**: 27017 with health checks
- **Cassandra**: 9042 with health checks
- **Redis**: 6379 with health checks
- **Mongo Express**: 8081 (Web UI)
- **Redis Commander**: 8082 (Web UI)

### Health Check Endpoints
- MongoDB: `http://localhost:8080/api/actuator/health/mongodb`
- Cassandra: `http://localhost:8080/api/actuator/health/cassandra`
- Redis: `http://localhost:8080/api/actuator/health/redis`

## Development Setup

### Prerequisites
- Java 21 JDK
- Docker and Docker Compose
- Gradle 8.0+

### Getting Started
1. Start infrastructure:
   ```bash
   docker-compose up -d
   ```

2. Build the application:
   ```bash
   ./gradlew clean build
   ```

3. Run the application:
   ```bash
   ./gradlew bootRun
   ```

## Key Configuration Classes
1. **MongoConfig**: MongoDB configuration with transaction support
2. **CassandraConfig**: Cassandra keyspace and session configuration
3. **RedisConfig**: Redis connection and template configuration

## Development Guidelines

### Code Style
- Follow Kotlin coding conventions
- Use ktlint for code formatting
- Write meaningful commit messages

### Testing
- Write unit tests for all business logic
- Use TestContainers for integration tests
- Mock external dependencies in unit tests

### API Documentation
- Document all API endpoints
- Use OpenAPI/Swagger annotations
- Keep API versioning in mind

## Dependency Management

### Version Management
- **Kotlin**: 2.0.21
- **Spring Boot**: 3.3.0
- **Kotlin Coroutines**: 1.8.1
- **TestContainers**: 1.19.8
- **MockK**: 1.13.11
- **Kotest**: 5.9.0
- **SpringDoc OpenAPI**: 2.5.0

### Key Dependencies
- **Spring Boot Starters**: Web, Data (MongoDB, Cassandra, Redis), Validation, Actuator
- **Kotlin**: Coroutines, Serialization, Reflection
- **Testing**: Kotest, MockK, TestContainers
- **Documentation**: SpringDoc OpenAPI

### Updating Dependencies
1. Check for updates using:
   ```bash
   ./gradlew dependencyUpdates
   ```
2. Update versions in `build.gradle.kts`
3. Test thoroughly after updates
4. Commit version changes with a meaningful message

## Monitoring and Management
- **Actuator Endpoints**: /api/actuator
- **Health Checks**: /api/actuator/health
- **Metrics**: /api/actuator/metrics

## Troubleshooting

### Common Issues
1. **Port Conflicts**: Check if required ports are available
2. **Database Connections**: Verify Docker containers are running
3. **Build Failures**: Ensure all dependencies are properly synced

### Useful Commands
- Check container logs:
  ```bash
  docker-compose logs -f
  ```
- Access MongoDB shell:
  ```bash
  docker-compose exec mongodb mongosh
  ```
- Access Cassandra CQL shell:
  ```bash
  docker-compose exec cassandra cqlsh
  ```

## Kotlin Best Practices

### Constructor Injection
Always prefer constructor injection over field injection in Kotlin:

1. **Basic Constructor Injection**
   ```kotlin
   @Service
   class UserService(
       private val userRepository: UserRepository,
       private val emailService: EmailService
   ) {
       // Class implementation
   }
   ```

2. **With Configuration Properties**
   ```kotlin
   @ConfigurationProperties(prefix = "app.mail")
   data class MailProperties(
       val host: String,
       val port: Int,
       val username: String,
       val from: String = "noreply@example.com"
   )

   @Service
   class NotificationService(
       private val mailProperties: MailProperties
   ) {
       // Use mailProperties
   }
   ```

3. **With Primary Constructor and Secondary Constructor**
   ```kotlin
   @Service
   class PaymentService @Autowired constructor(
       private val paymentGateway: PaymentGateway,
       private val retryTemplate: RetryTemplate
   ) {
       // Primary constructor for required dependencies
       
       @Autowired
       constructor(
           paymentGateway: PaymentGateway
       ) : this(paymentGateway, RetryTemplate())
   }
   ```

4. **With Qualifiers**
   ```kotlin
   @Service
   class OrderService(
       @Qualifier("jpaOrderRepository") 
       private val orderRepository: OrderRepository,
       private val paymentService: PaymentService
   )
   ```

5. **With Lazy Initialization**
   ```kotlin
   @Service
   class ReportService(
       private val dataSource: DataSource,
       private val templateEngine: TemplateEngine
   ) {
       @Autowired
       private lateinit var reportExporter: ReportExporter
   }
   ```

### Best Practices
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

## Configuration Best Practices

### Environment Variables
- Always use environment variables with default values for configuration
- Follow UPPER_SNAKE_CASE naming convention for environment variables
- Document all available environment variables in this document

### YAML Formatting
- Use quotes around values with special characters
- Maintain consistent indentation (2 spaces)
- Group related properties together with comments

### Spring Boot Specific
- Use `@ConfigurationProperties` for type-safe configuration
- Add validation annotations to configuration properties
- Keep configuration classes focused and single-responsibility

### Docker Configuration
- Always include health checks for services
- Use `depends_on` with health conditions for service dependencies
- Set appropriate resource limits (CPU/memory)
- Use environment variables for sensitive data

## Common Issues and Solutions

### Property Deprecations
- `management.metrics.export.simple.enabled` → Use `management.simple.metrics.export.enabled`
- `management.metrics.web.server.request.autotime` → Configure at `ObservationRegistry` level

### Health Check Failures
1. **MongoDB**: Ensure the database is accessible and credentials are correct
2. **Cassandra**: Check if the keyspace exists and is accessible
3. **Redis**: Verify the Redis server is running and accessible

## Database Configuration

### MongoDB
- **Configuration Class**: `MongoConfig`
- **Base Package**: `learn.ai.tinder.repository.mongo`
- **Features**:
  - Transaction support
  - Custom type conversion
  - Validation integration
  - Index management

### Cassandra
- **Configuration Class**: `CassandraConfig`
- **Base Package**: `learn.ai.tinder.repository.cassandra`
- **Features**:
  - Keyspace auto-creation
  - Schema management
  - Type-safe configuration with `@ConfigurationProperties`
  - Health check integration

### Redis
- **Configuration Class**: `RedisConfig`
- **Features**:
  - Connection pooling
  - JSON serialization
  - Type-safe configuration
  - Health check integration

## Testing Strategy

### Unit Tests
- Use MockK for mocking dependencies
- Test business logic in isolation
- Follow Arrange-Act-Assert pattern

### Integration Tests
- Use TestContainers for database integration tests
- Test repository layer with real database instances
- Test service layer with mocked dependencies

### Test Configuration
- Test-specific properties in `application-test.yml`
- Test profiles for different scenarios
- Database test containers for consistent test environments

## API Design

### REST Endpoints
- Follow RESTful principles
- Use proper HTTP methods (GET, POST, PUT, DELETE, PATCH)
- Consistent URL naming (kebab-case, plural resources)
- Versioning strategy (URL path or headers)

### Error Handling
- Consistent error response format
- Proper HTTP status codes
- Detailed error messages for client applications
- Logging of server errors

## Security Considerations

### Data Protection
- Encrypt sensitive data at rest
- Use HTTPS for all communications
- Implement proper CORS policies
- Rate limiting for public APIs

### Authentication & Authorization
- JWT-based authentication
- Role-based access control (RBAC)
- OAuth2/OpenID Connect integration
- Token refresh mechanism

## Performance Optimization

### Database
- Proper indexing strategy
- Query optimization
- Connection pooling
- Caching strategy

### Application
- Asynchronous processing
- Caching layer
- Batch processing for bulk operations
- Monitoring and profiling

## Monitoring and Observability

### Logging
- Structured logging with JSON format
- Correlation IDs for request tracing
- Log levels configuration
- Log rotation and retention policy

### Metrics
- Application metrics with Micrometer
- Custom business metrics
- JVM metrics
- Database metrics

### Distributed Tracing
- Trace ID propagation
- Span creation for critical operations
- Integration with monitoring tools

## Deployment

### Containerization
- Multi-stage Docker builds
- Minimal base images
- Proper resource limits
- Health checks

### Orchestration
- Kubernetes manifests
- Helm charts
- Service discovery
- Auto-scaling configuration

## Future Considerations
- Implement API versioning
- Add API documentation with Swagger/OpenAPI
- Set up CI/CD pipeline
- Implement proper security (OAuth2/OpenID Connect)
- Add monitoring with Prometheus and Grafana
- Set up distributed tracing with Jaeger/Zipkin
- Implement circuit breakers with Resilience4j

## Development Workflow

### Branching Strategy
- Feature branches from `develop`
- Pull requests with code reviews
- Semantic versioning for releases
- Changelog maintenance

### Code Quality
- Static code analysis
- Code coverage requirements
- Automated code formatting
- Documentation standards

### Local Development
- Docker Compose for dependencies
- Database migration tools
- Test data generation
- Development-specific configuration

## Troubleshooting Guide

### Common Issues
1. **Database Connection Issues**
   - Verify database is running
   - Check connection strings and credentials
   - Verify network connectivity

2. **Test Failures**
   - Ensure test containers are properly configured
   - Check for port conflicts
   - Verify test data setup

3. **Performance Problems**
   - Check database queries with EXPLAIN
   - Monitor application metrics
   - Profile memory and CPU usage

## Lessons Learned

### Configuration Management
1. **Property Naming**
   - Always use the latest property names as per Spring Boot documentation
   - Example: `management.metrics.export.simple.enabled` is deprecated in favor of `management.simple.metrics.export.enabled`

2. **YAML Formatting**
   - Special characters in YAML keys should be properly escaped or quoted
   - Use consistent indentation (2 spaces)
   - Example:
     ```yaml
     # Bad
     org.springframework: INFO
     
     # Good
     'org.springframework': INFO
     ```

3. **Test Containers**
   - Always include proper waiting strategies for containers
   - Configure health checks for dependent services
   - Example:
     ```kotlin
     .waitingFor(Wait.forLogMessage(".*Startup complete.*\n", 1))
     ```

4. **Dependency Management**
   - Use BOM (Bill of Materials) for managing TestContainers versions
   - Example:
     ```kotlin
     testImplementation(platform("org.testcontainers:testcontainers-bom:$testContainersVersion"))
     testImplementation("org.testcontainers:testcontainers")
     testImplementation("org.testcontainers:junit-jupiter")
     ```

5. **Error Handling**
   - Add proper validation for configuration properties
   - Include meaningful error messages for configuration issues
   - Example:
     ```kotlin
     @ConfigurationProperties(prefix = "app.redis")
     @Validated
     data class RedisProperties(
         @field:NotBlank
         val host: String,
         
         @field:Min(1)
         @field:Max(65535)
         val port: Int
     )
     ```

## Maintenance

### Dependency Updates
- Regular security updates
- Major version upgrades
- Dependency vulnerability scanning
- License compliance

### Documentation
- API documentation
- Architecture decision records (ADRs)
- Runbooks for common operations
- Incident response procedures
