---
description: Kotlin Spring Boot Project Setup
---

# Kotlin Spring Boot Project Setup

This workflow sets up a new Kotlin Spring Boot project with support for multiple technologies including MongoDB, PostgreSQL, MySQL, Redis, Elasticsearch, RabbitMQ, and Kafka.

## Prerequisites

- Java 17 or later
- Docker and Docker Compose
- Gradle 8.5 or later (will be installed if not present)

## Inputs

### Required
- `project_name`: Name of the project (e.g., my-awesome-app)
- `package_name`: Base package name (e.g., com.example.app)
- `group_name`: Group ID for the project (e.g., com.example)

### Optional with Defaults
- `spring_boot_version`: Spring Boot version (default: 3.2.0)
- `kotlin_version`: Kotlin version (default: 1.9.20)
- `java_version`: Java version to target (default: 21)
- `mongodb_version`: MongoDB version (default: 7.0)
- `postgresql_version`: PostgreSQL version (default: 15-alpine)
- `mysql_version`: MySQL version (default: 8.0)
- `redis_version`: Redis version (default: 7.2-alpine)
- `elasticsearch_version`: Elasticsearch version (default: 8.9.0)
- `rabbitmq_version`: RabbitMQ version (default: 3.11-management-alpine)
- `kafka_version`: Kafka version (default: 7.4.0)

### Technology Selection
- `include_mongodb`: Include MongoDB (default: true)
- `include_postgresql`: Include PostgreSQL (default: false)
- `include_mysql`: Include MySQL (default: false)
- `include_redis`: Include Redis (default: true)
- `include_elasticsearch`: Include Elasticsearch (default: false)
- `include_rabbitmq`: Include RabbitMQ (default: false)
- `include_kafka`: Include Kafka (default: false)

## Steps

1. **Create Project Structure**
   ```bash
   mkdir -p ${project_name}/src/main/kotlin/$(echo ${package_name//.//})/{config,controller,service,repository,model,util}
   mkdir -p ${project_name}/src/test/kotlin/$(echo ${package_name//.//})
   mkdir -p ${project_name}/src/main/resources
   mkdir -p ${project_name}/.cursor
   mkdir -p ${project_name}/docker
   ```

2. **Initialize Gradle Wrapper**
   ```bash
   cd ${project_name}
   gradle wrapper --gradle-version 8.5 --distribution-type=bin
   chmod +x gradlew
   ./gradlew --version
   ```

3. **Create build.gradle.kts** with all necessary dependencies:
   ```kotlin
   // Build configuration will be generated dynamically based on selected technologies
   ```

4. **Create application.yml** with configurations for all selected technologies:
   ```yaml
   # Application configuration will be generated dynamically
   ```

5. **Create Docker Compose file** with selected services:
   ```yaml
   # Docker Compose configuration will be generated dynamically
   ```

6. **Create main application class**:
   ```kotlin
   package ${package_name}
   
   import org.springframework.boot.autoconfigure.SpringBootApplication
   import org.springframework.boot.runApplication
   
   @SpringBootApplication
   class ${project_name^}Application
   
   fun main(args: Array<String>) {
       runApplication<${project_name^}Application>(*args)
   }
   ```

7. **Create .gitignore** with standard exclusions for Kotlin/Java projects

8. **Create README.md** with project documentation and setup instructions

## Example Usage

To create a new project with MongoDB and Redis:

```bash
windsurf run .windsurf/workflows/kotlin-spring-setup.md \
  --project-name myapp \
  --package-name com.example.myapp \
  --group-name com.example \
  --include-mongodb true \
  --include-redis true
```

To include all technologies:

```bash
windsurf run .windsurf/workflows/kotlin-spring-setup.md \
  --project-name myapp \
  --package-name com.example.myapp \
  --group-name com.example \
  --include-mongodb true \
  --include-postgresql true \
  --include-mysql true \
  --include-redis true \
  --include-elasticsearch true \
  --include-rabbitmq true \
  --include-kafka true
```

## Notes

- The workflow will generate all necessary configuration files with placeholders that will be replaced with actual values
- Database credentials and other sensitive information should be managed using environment variables
- The generated project includes proper health checks and monitoring endpoints
- All services are configured with sensible defaults but can be customized as needed

## Output

After running the workflow, you'll have a fully configured Kotlin Spring Boot project with:

- Gradle build configuration
- Docker Compose setup for infrastructure services
- Application properties for all selected technologies
- Basic project structure following best practices
- Documentation and setup instructions
