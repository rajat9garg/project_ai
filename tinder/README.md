# Tinder Clone Application

A modern dating application backend built with Kotlin, Spring Boot, and reactive programming using coroutines.

## Technologies

This application uses the following technologies:

- **Kotlin 1.9.24** with Coroutines for asynchronous programming
- **Spring Boot 3.3.0** for application framework
- **MongoDB** for primary data storage
- **Cassandra** for high-throughput data storage
- **Redis** for caching and real-time features
- **Elasticsearch** for powerful search capabilities
- **Kafka** for event streaming and asynchronous communication
- **Gradle** for build automation

## Project Structure

```
tinder/
├── src/
│   ├── main/
│   │   ├── kotlin/learn/ai/tinder/
│   │   │   ├── config/          # Configuration classes
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── service/         # Business logic
│   │   │   ├── repository/      # Data access interfaces
│   │   │   ├── model/           # Domain models
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── exception/       # Custom exceptions
│   │   │   ├── util/            # Utility classes
│   │   │   ├── client/          # External service clients
│   │   │   └── TinderApplication.kt  # Main application class
│   │   └── resources/
│   │       ├── application.yml  # Application configuration
│   │       ├── static/          # Static resources
│   │       └── templates/       # Template files
│   └── test/
│       ├── kotlin/learn/ai/tinder/
│       │   ├── service/         # Service tests
│       │   ├── controller/      # Controller tests
│       │   └── repository/      # Repository tests
│       └── resources/           # Test resources
└── build.gradle.kts             # Build configuration
```

## Features

- User profile management
- Matching algorithm based on preferences
- Real-time messaging
- Search capabilities for finding potential matches
- Event-driven architecture for scalability

## Getting Started

### Prerequisites

- JDK 21
- MongoDB
- Cassandra
- Redis
- Elasticsearch
- Kafka

### Running the Application

1. Clone the repository
2. Configure the connection details in `src/main/resources/application.yml`
3. Run the application:

```bash
./gradlew bootRun
```

### API Endpoints

#### User Profiles

- `POST /api/profiles` - Create a new user profile
- `GET /api/profiles/{id}` - Get a user profile by ID
- `GET /api/profiles/user/{userId}` - Get a user profile by user ID
- `GET /api/profiles` - Get all user profiles
- `PUT /api/profiles/{id}` - Update a user profile
- `DELETE /api/profiles/{id}` - Delete a user profile

## Development

### Building the Project

```bash
./gradlew build
```

### Running Tests

```bash
./gradlew test
```

## Architecture

This application follows a reactive architecture using Kotlin coroutines for asynchronous programming. The main components are:

- **Controllers**: Handle HTTP requests and responses
- **Services**: Implement business logic
- **Repositories**: Provide data access
- **Models**: Represent domain entities
- **DTOs**: Transfer data between layers
- **Clients**: Interact with external services

## Coroutines Usage

The application uses Kotlin coroutines for asynchronous programming:

- `suspend` functions for non-blocking operations
- `Flow` for reactive streams
- Structured concurrency with coroutine scopes
- Integration with reactive libraries through extension functions

## Data Storage

- **MongoDB**: Stores user profiles and application data
- **Cassandra**: Stores high-throughput data like messages and matches
- **Redis**: Caches frequently accessed data and manages real-time features
- **Elasticsearch**: Indexes user profiles for efficient searching

## Event Streaming

Kafka is used for event streaming with the following topics:

- `tinder-profiles`: User profile updates
- `tinder-matches`: Match events
- `tinder-messages`: Message events

## License

This project is licensed under the MIT License - see the LICENSE file for details.
