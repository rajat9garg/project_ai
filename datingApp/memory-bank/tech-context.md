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
- **Primary**: MongoDB 6.0
- **Cache**: Redis 7.0
- **Search**: Elasticsearch 8.7.0

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
