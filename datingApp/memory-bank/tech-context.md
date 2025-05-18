# Dating App - Technical Context

## Technology Stack

### Backend
- **Framework**: Spring Boot 3.3.0
- **Language**: Kotlin 1.9.0
- **Build Tool**: Gradle with Kotlin DSL
- **Java Version**: 17

### Frontend
- **Mobile**: React Native 0.72.0
- **Web**: React 18.2.0
- **State Management**: Redux Toolkit
- **UI Library**: React Native Paper / Material-UI

### Database

#### MongoDB
- **Version**: 6.0
- **Configuration**:
  - Connection: Standard synchronous driver
  - Connection Pool: Default settings with environment variable overrides
  - Indexing: Automatic index creation enabled
  - Schema: Flexible schema with validation
- **Usage**:
  - Primary data store for user profiles, matches, and interactions
  - Uses Spring Data MongoDB repositories for data access
  - Implements proper connection pooling and error handling
- **Environment Variables**:
  ```env
  SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/datingapp
  SPRING_DATA_MONGODB_DATABASE=datingapp
  ```

#### Redis
- **Version**: 7.0
- **Usage**: Session management and caching

#### Search
- **Elasticsearch**: 8.7.0 (For advanced search capabilities)

### Infrastructure
- **Containerization**: Docker 23.0, Docker Compose
- **CI/CD**: GitHub Actions
- **Cloud Provider**: AWS
- **Monitoring**: Prometheus, Grafana

## Development Environment Setup

### Prerequisites
- JDK 17+
- Node.js 18+
- Docker Desktop
- IntelliJ IDEA / VS Code

### Local Development
1. Clone the repository
2. Set up environment variables (copy `.env.example` to `.env`)
3. Start required services:
   ```bash
   docker-compose up -d mongodb redis
   ```
4. Run the backend:
   ```bash
   ./gradlew bootRun
   ```
5. Start the frontend:
   ```bash
   cd frontend
   npm install
   npm start
   ```

### Environment Variables

#### Backend
```env
# Server
SERVER_PORT=8080
SERVER_SERVLET_CONTEXT_PATH=/api

# MongoDB
SPRING_DATA_MONGODB_URI=mongodb://localhost:27017/datingapp

# JWT
JWT_SECRET=your-jwt-secret
JWT_EXPIRATION_MS=86400000

# AWS
AWS_ACCESS_KEY_ID=your-access-key
AWS_SECRET_ACCESS_KEY=your-secret-key
AWS_REGION=us-east-1
S3_BUCKET_NAME=datingapp-media
```

## Build and Deployment

### Build Commands
```bash
# Build backend
./gradlew clean build

# Run tests
./gradlew test

# Build Docker image
docker build -t datingapp-backend .
```

### Deployment
Deployment is handled through GitHub Actions. The workflow includes:
1. Running tests
2. Building Docker images
3. Pushing to ECR
4. Deploying to ECS

## Testing Framework

### Testing Stack
- **Unit Testing**: JUnit 5, MockK
- **Integration Testing**: Spring Boot Test, Testcontainers
- **API Testing**: MockMvc, RestAssured
- **Assertions**: AssertJ, Kotlin Test

### Key Dependencies
```kotlin
testImplementation("io.mockk:mockk:1.13.8")
testImplementation("io.mockk:mockk-jvm:1.13.8")
testImplementation("io.mockk:mockk-agent-jvm:1.13.8")
testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.0") {
    exclude(group = "org.mockito")
    exclude(group = "org.junit.vintage")
}
testImplementation("org.testcontainers:testcontainers:1.18.3")
testImplementation("org.testcontainers:mongodb:1.18.3")
```

### Testing Best Practices
1. **Unit Tests**
   - Use MockK for mocking dependencies
   - Follow AAA pattern (Arrange-Act-Assert)
   - Test one behavior per test method
   - Use descriptive test method names

2. **Integration Tests**
   - Use Testcontainers for database integration tests
   - Annotate with `@SpringBootTest`
   - Use `@Testcontainers` and `@Container` for containerized tests
   - Reset database state between tests

3. **API Tests**
   - Use MockMvc for controller tests
   - Test both success and error scenarios
   - Validate response status, headers, and body
   - Test input validation

## Configuration Management

### Application Properties
Configuration is managed through:
- `application.yml` - Base configuration
- `application-dev.yml` - Development overrides
- `application-prod.yml` - Production overrides

### Feature Flags
Feature flags are managed through a combination of:
- Spring Cloud Config
- Database-driven configuration
- Environment variables

## Security Considerations

### Authentication
- JWT-based stateless authentication
- Refresh token mechanism
- Password hashing with bcrypt

### Authorization
- Role-based access control (RBAC)
- Method-level security with `@PreAuthorize`
- Resource ownership validation

### Data Protection
- Encryption at rest for sensitive data
- TLS 1.3 for all communications
- Input validation and sanitization
- Rate limiting on public endpoints

## Monitoring and Logging

### Logging
- Structured JSON logging
- Correlation IDs for request tracing
- Log levels: ERROR, WARN, INFO, DEBUG, TRACE

### Monitoring
- Spring Boot Actuator endpoints
- Prometheus metrics
- Health checks
- Custom business metrics

### Alerting
- Error rate thresholds
- Performance degradation detection
- Business metrics monitoring

## Testing Strategy

### Unit Tests
- JUnit 5
- MockK for mocking
- AssertJ for assertions
- Test coverage > 80%

### Integration Tests
- @SpringBootTest for full context tests
- Testcontainers for database tests
- @WebMvcTest for controller tests

### E2E Tests
- Cypress for web
- Detox for mobile
- API contract testing with Pact

## Documentation

### API Documentation
- SpringDoc OpenAPI 2.0
- Interactive API console
- Example requests/responses

### Architecture Decision Records (ADRs)
- Recorded in `/docs/adr`
- Template provided
- Required for significant decisions

### Runbook
- Deployment procedures
- Common issues and solutions
- Rollback procedures
- Contact information
