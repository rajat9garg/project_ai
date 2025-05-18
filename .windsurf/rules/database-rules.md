---
trigger: always_on
---

# MongoDB Database Rules & Best Practices

## Connection Configuration

### Connection String
- Always use environment variables for MongoDB connection strings
- Use the format: `mongodb://[username:password@]host1[:port1][,...hostN[:portN]]/database`
- Example: `mongodb://user:password@localhost:27017/datingApp`
- For local development, use: `mongodb://localhost:27017/datingApp`
- For production, use environment variables: `${MONGO_URI:mongodb://localhost:27017/datingApp}`

### Connection Pool Settings
- Configure appropriate connection pool size based on your application needs
- Default pool size is typically sufficient for most applications (default: 100 connections)
- Adjust based on your expected concurrent database operations
- Configure timeouts:
  - Connect timeout: 5000ms
  - Socket timeout: 5000ms
  - Server selection timeout: 30000ms

## Spring Data MongoDB Configuration

### Repository Configuration
- Extend `MongoRepository` for standard CRUD operations
- Use `@Document` annotation for entity classes
- Use `@Id` for the document ID field
- Configure auto-index creation in application.yml:
  ```yaml
  spring:
    data:
      mongodb:
        auto-index-creation: true
  ```

### Query Methods
- Use method name query derivation for simple queries
- Use `@Query` annotation for complex queries
- Prefer `findBy` methods over `@Query` when possible for better readability
- Use `existsBy` for existence checks
- Use `countBy` for counting documents

### Transactions
- Use `@Transactional` for multi-document operations
- Keep transactions short and focused
- Be aware of the performance impact of transactions
- Handle transaction exceptions appropriately

## Schema Design

### Document Structure
- Design documents to be self-contained but avoid unbounded growth
- Embed related data that is frequently accessed together
- Reference data that is shared across many documents
- Keep document size under 16MB (MongoDB limit)

### Indexing
- Create indexes for fields commonly used in queries
- Use compound indexes for queries that filter on multiple fields
- Be mindful of index size and performance impact on write operations
- Monitor query performance using MongoDB's explain()
- Example index definition:
  ```kotlin
  @Document(collection = "users")
  @CompoundIndex(def = "{email: 1}", unique = true)
  data class User(
      @Id val id: String,
      val email: String,
      // other fields
  )
  ```
- For text search, create text indexes:
  ```kotlin
  @Document(collection = "users")
  @TextIndexed(weight = 2)
  data class User(
      @Id val id: String,
      @TextIndexed val name: String,
      @TextIndexed(weight = 3) val bio: String
  )
  ```

## Data Access

### Repository Layer
- Use Spring Data MongoDB repositories for standard CRUD operations
- Create custom repository interfaces for complex queries
- Use `@Query` annotations for custom MongoDB JSON queries
- Implement proper error handling for database operations

### Transactions
- Use `@Transactional` for operations that require atomicity
- Keep transactions short and focused
- Be aware of the performance impact of transactions

## Security

### Authentication
- Always enable authentication in production
- Use strong, randomly generated credentials
- Rotate credentials regularly

### Network Security
- Enable TLS/SSL for all connections in production
- Configure IP whitelisting where possible
- Use VPC peering or private networking in cloud environments

## Performance

### Query Optimization
- Use projection to return only necessary fields
- Avoid `$where` and `$regex` with leading wildcards
- Use covered queries when possible
- Monitor slow queries using MongoDB profiler

### Write Concerns
- Use appropriate write concern based on your durability requirements
- Default to majority write concern for critical data
- Consider performance impact of higher write concerns

## Monitoring & Maintenance

### Monitoring
- Set up monitoring for key metrics (CPU, memory, connections, etc.)
- Configure alerts for critical conditions
- Monitor replication lag in replica sets

### Backup & Recovery
- Implement regular backup procedures
- Test restore procedures periodically
- Consider point-in-time recovery for critical data

## Best Practices

### Naming Conventions
- Use camelCase for field names (MongoDB convention)
- Use plural collection names (e.g., `users`, `profiles`)
- Be consistent with naming across the application

### Data Types
- Use appropriate BSON types for your data
- Be consistent with data types for the same field across documents
- Consider timezone handling for dates

### Error Handling
- Implement proper error handling for database operations
- Log meaningful error messages
- Handle connection failures gracefully

## Versioning
- Document schema changes
- Consider migration strategies for breaking changes
- Use schema validation for data integrity

## Development vs Production
- Use different connection strings for different environments
- Configure appropriate security settings for production
- Monitor and tune performance in production-like environments

## References
- [MongoDB Documentation](https://docs.mongodb.com/)
- [Spring Data MongoDB Reference](https://docs.spring.io/spring-data/mongodb/docs/current/reference/html/)
- [MongoDB Best Practices](https://www.mongodb.com/basics/best-practices)
