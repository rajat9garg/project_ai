# Dating App

A modern dating application built with Spring Boot, Kotlin, MongoDB, and Redis.

## Prerequisites

- Java 21 or later
- Docker and Docker Compose
- Gradle 8.5

## Tech Stack

- **Backend**: Spring Boot 3.2.0
- **Language**: Kotlin 1.9.20
- **Database**: MongoDB 7.0
- **Caching**: Redis 7.2
- **Build Tool**: Gradle 8.5

## Getting Started

### Local Development

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd datingApp
   ```

2. **Start services with Docker Compose**
   ```bash
   docker-compose up -d
   ```
   This will start:
   - MongoDB on port 27017
   - Redis on port 6379
   - The application on port 8080

3. **Access the application**
   - API will be available at: http://localhost:8080/api
   - Swagger UI: http://localhost:8080/api/swagger-ui.html

### Building and Running

1. **Build the application**
   ```bash
   ./gradlew clean build
   ```

2. **Run the application**
   ```bash
   java -jar build/libs/datingApp.jar
   ```

## API Documentation

API documentation is available using Swagger UI:
- Swagger UI: http://localhost:8080/api/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api/v3/api-docs

## Configuration

Application configuration can be found in:
- `src/main/resources/application.yml` - Main configuration
- Environment variables can override any property

## Development

### Code Style

This project uses ktlint for code style enforcement. To check the code style:

```bash
./gradlew ktlintCheck
```

To automatically fix code style issues:

```bash
./gradlew ktlintFormat
```

### Testing

Run tests:

```bash
./gradlew test
```

Run tests with coverage:

```bash
./gradlew test jacocoTestReport
```

## Deployment

### Building a Docker Image

```bash
docker build -t dating-app:latest .
```

### Running with Docker Compose

```bash
docker-compose up -d
```

## Monitoring

- Actuator endpoints: http://localhost:8080/api/actuator
- Health check: http://localhost:8080/api/actuator/health
- Metrics: http://localhost:8080/api/actuator/metrics

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
