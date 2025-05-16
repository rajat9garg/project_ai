# Docker Setup for Tinder Application

This guide explains how to use Docker to run the Tinder application along with all its dependencies.

## Prerequisites

- Docker and Docker Compose installed on your machine
- JDK 21 for local development
- Gradle for building the application

## Docker Configuration Files

The Docker setup consists of two main files:

1. `docker-compose.yml` - Orchestrates all services needed for the application
2. `Dockerfile` - Builds the Spring Boot application container

## Services Included

The Docker Compose configuration includes the following services:

- **Spring Boot Application** - The Tinder application itself
- **MongoDB** - Document database for storing user profiles
- **Cassandra** - Wide-column store for high-throughput data
- **Redis** - In-memory data store for caching
- **Elasticsearch** - Search engine for user profile search
- **Kafka & Zookeeper** - Event streaming platform for messaging
- **Kafka UI** - Web interface for managing Kafka

## Running the Application with Docker

### Starting All Services

```bash
# Start all services in detached mode
docker-compose up -d

# View logs from all containers
docker-compose logs -f
```

### Accessing Services

- **Tinder Application**: http://localhost:8080
- **MongoDB**: localhost:27017
- **Cassandra**: localhost:9042
- **Redis**: localhost:6379
- **Elasticsearch**: http://localhost:9200
- **Kafka**: localhost:9092 (internal), localhost:29092 (external)
- **Kafka UI**: http://localhost:8090

### Stopping Services

```bash
# Stop all services but keep volumes
docker-compose down

# Stop all services and remove volumes (will delete all data)
docker-compose down -v
```

## Development Workflow

### Building the Application

```bash
# Build the application without running tests
./gradlew build -x test

# Build the application with tests
./gradlew build
```

### Rebuilding the Docker Image

```bash
# Rebuild just the application container
docker-compose build app

# Restart the application container
docker-compose up -d --no-deps app
```

## Initializing Databases

### Cassandra

The Cassandra database needs to be initialized with the keyspace before the application can use it:

```bash
# Connect to the Cassandra container
docker exec -it tinder-cassandra cqlsh -u cassandra -p cassandra

# Create the keyspace
CREATE KEYSPACE IF NOT EXISTS tinder 
WITH REPLICATION = { 'class' : 'SimpleStrategy', 'replication_factor' : 1 };

# Exit cqlsh
exit
```

### Elasticsearch

Elasticsearch will automatically create indices when the application starts, but you can verify it's running:

```bash
# Check Elasticsearch status
curl http://localhost:9200
```

### Kafka Topics

Kafka will auto-create topics, but you can manually create them if needed:

```bash
# Connect to the Kafka container
docker exec -it tinder-kafka bash

# Create topics
kafka-topics --create --topic tinder-profiles --bootstrap-server kafka:9092 --partitions 3 --replication-factor 1
kafka-topics --create --topic tinder-matches --bootstrap-server kafka:9092 --partitions 3 --replication-factor 1
kafka-topics --create --topic tinder-messages --bootstrap-server kafka:9092 --partitions 3 --replication-factor 1

# Exit the container
exit
```

## Environment Variables

The application container is configured with environment variables that connect to the other services. If you need to modify these connections, update the environment section in the `docker-compose.yml` file.

## Volumes

All data is persisted using Docker volumes:

- `mongodb-data`: MongoDB data
- `cassandra-data`: Cassandra data
- `redis-data`: Redis data
- `elasticsearch-data`: Elasticsearch data
- `kafka-data`: Kafka data

These volumes ensure that your data persists even when containers are stopped or removed.

## Troubleshooting

### Service Dependencies

Services are configured with dependencies, but sometimes you might need to wait for services to fully initialize. The application container includes a wait-for-it script that can be used to wait for dependent services.

### Checking Logs

```bash
# View logs for a specific service
docker-compose logs -f app
docker-compose logs -f mongodb
docker-compose logs -f kafka
```

### Restarting a Service

```bash
# Restart a specific service
docker-compose restart app
docker-compose restart mongodb
```

## Production Considerations

This setup is primarily designed for development. For production, consider:

1. Using separate Docker Compose files for different environments
2. Implementing proper security measures (passwords, TLS, etc.)
3. Setting up proper monitoring and logging
4. Configuring appropriate resource limits
5. Using Docker Swarm or Kubernetes for orchestration
