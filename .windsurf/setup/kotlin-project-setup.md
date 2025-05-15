# Project Setup Instructions

## Required Information
Before starting the setup, please provide:

1. Project Folder Name
   - Name your main project folder: Example (ElasticSearch)
   - This folder will serve as the root directory for the entire project structure. Ensure the name reflects the core functionality or technology of the project.

2. Package Name
   - Example: com.example.project
   - This will be used in your build files and package structure

3. Group Name
   - Example: com.example
   - This will be used for artifact identification

4. Required Technologies
   Select from the following:
   - Databases: PostgreSQL, MySQL, MongoDB
   - Cache: Redis
   - Search: Elasticsearch
   - Message Queue: RabbitMQ, Kafka
   - Other: Specify any additional requirements

4. Ask for any special setup like project structure etc

5. Ask for git initialization and remote repository URL if just skip

## Setup Process

1. Create Project Structure
   ```bash
   mkdir -p src/main/kotlin/[package_name]/{config,controller,service,repository,model,util}
   mkdir -p src/main/resources
   mkdir -p src/test/kotlin/[package_name]
   mkdir -p .cursor
   mkdir -p docker
   ```

2. Initialize Project
   ```bash
   touch .gitignore
   touch docker/docker-compose.yml
   touch .cursor/settings.json
   ```

3. Create Build File
   - For Maven: `touch pom.xml`
   - For Gradle: `touch build.gradle`

4. Configure Docker Services
   - Create docker-compose.yml based on selected technologies
   - Configure environment variables
   - Set up volumes and networks
   - Include Spring Boot service in docker-compose.yml with proper dependencies and environment variables
   - Create Dockerfile in project root for Spring Boot application

5. Start Development
   - Update package names
   - Configure build tool
   - Set up IDE
   - Start Docker services with: `docker-compose up -d`
   - This will start all services including Spring Boot application

6. Gradle Setup
   - check if gradle is installed if not install using sdkman `sdk install gradle 8.5`
   - Generate gradlew wrapper scripts: gradle wrapper --gradle-version 8.5
   - Make gradlew executable (Linux/macOS only): chmod +x gradlew
   - Download all dependencies: ./gradlew build --refresh-dependencies

7. Client Setup
   - Add dependencies and imports for the client for the technologies in the step 4
   - Set up client code for selected technologies in step 4 inside src/main/kotlin/[package_name]/util
   - For Redis: add Redis client config using Lettuce or Jedis in RedisClient.kt
   - For Elasticsearch: use RestHighLevelClient or ElasticsearchClient in ElasticsearchClient.kt
   - For PostgreSQL/MySQL/MongoDB: configure datasource properties in application.yml and define DatabaseConfig.kt
   - For Kafka: create KafkaProducerConfig.kt and KafkaConsumerConfig.kt with required serializers and topic settings
   - For RabbitMQ: add RabbitMQConfig.kt with ConnectionFactory, Queue, and RabbitTemplate beans
   - Keep each client config reusable and injectable via Spring's @Configuration and @Bean annotations
   - Validate each client by writing a basic health-check or ping method in a corresponding ClientHealthUtil.kt

   
Always Do these things
   - Use Java 24
   - Use gradle 8.5
   - download the latest version of all the depenedencies
   - test docker-compose by running it once and then shut it off
   - ensure docker-compose up brings up all services including Spring Boot application


## Notes
- Keep sensitive information in environment variables
- Update .gitignore for your project needs
- Add appropriate documentation
- Docker Compose should start all services including Spring Boot application
- Use environment variables in docker-compose.yml for service configuration

## Support
For any issues or questions, please refer to:
- Docker Documentation
- Spring Boot Documentation
- Maven/Gradle Documentation