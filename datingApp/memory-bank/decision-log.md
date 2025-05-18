# Decision Log

This document records major decisions made during the project, including the context, alternatives considered, and rationale.

## Format
Each entry should include:
- **Date**: When the decision was made
- **Decision**: What was decided
- **Status**: Proposed/Approved/Deprecated/Superseded
- **Context**: What problem are we solving?
- **Decision**: What we decided to do
- **Consequences**: What does this decision affect?
- **Alternatives Considered**: What other options were considered
- **Related**: Links to related decisions or documentation

---

## 2025-05-19 - UserController Implementation
- **Status**: Approved
- **Context**: Need to implement the REST API endpoints for user management in the dating application
- **Decision**: Implemented UserController with the following design decisions:
  - RESTful endpoints following REST principles
  - Proper HTTP status codes for different scenarios
  - Pagination and sorting support for user listing
  - Input validation using Jakarta Validation
  - Proper error handling and response formats
  - Clean separation of concerns with DTOs and mappers
- **Consequences**:
  - Consistent API design across the application
  - Better maintainability and testability
  - Improved developer experience with clear contracts
  - Easier client integration with standardized responses
- **Alternatives Considered**:
  - GraphQL: More flexible but added complexity
  - gRPC: Better performance but less web-friendly
  - Traditional MVC: Simpler but less flexible for API evolution
- **Related**: [API Documentation](./tech-context.md#api-documentation)

## 2025-05-18 - Test Data Generation Script
- **Status**: Approved
- **Context**: Need realistic test data for development and testing of the dating application
- **Decision**: Created a Kotlin script to generate 5000 realistic user profiles with the following characteristics:
  - 50/50 male/female distribution
  - Realistic names and email addresses using JavaFaker
  - Ages between 18 and 45
  - Locations from popular cities worldwide with optional geocoordinates
  - Realistic preferences with sensible age ranges
  - 80% of users have a default hashed password ("password")
  - 30% of users have geolocation coordinates
- **Consequences**:
  - Enables realistic testing of matching algorithms
  - Provides data for performance testing
  - Ensures consistent test data across environments
- **Related**: #testing #test-data #development

## 2025-05-18 - Registration Endpoint Validation
- **Status**: Approved
- **Context**: Need to ensure proper validation and error handling for user registration endpoint
- **Decision`: 
  - Implemented comprehensive validation in UserRegistrationRequest DTO
  - Added detailed error messages for validation failures
  - Enhanced logging for better debugging
  - Fixed endpoint URL to respect server context path
- **Consequences**:
  - More robust input validation
  - Better error messages for API consumers
  - Easier debugging with detailed logs
- **Related**: #user-registration #validation #error-handling

## 2025-05-18 - Optional Password in User Registration
- **Status**: Approved
- **Context**: Need to support user registration with optional password for social login scenarios
- **Decision**: Made password field optional in User model and registration DTO
- **Consequences**: 
  - Users can register without a password (for social login)
  - Email is now the primary identifier
  - Password validation remains when password is provided
- **Alternatives Considered**:
  - Requiring password for all registrations (rejected as too restrictive)
  - Separate registration flows for email/password and social (rejected for UX complexity)
- **Related**: #user-registration #authentication

## 2025-05-18 - MongoDB Configuration
- **Status**: Approved
- **Context**: Need to configure MongoDB for the application with optimal settings
- **Decision**: Use standard synchronous MongoDB driver instead of reactive
- **Consequences**:
  - Simpler codebase
  - Easier to test and debug
  - Adequate performance for current scale
- **Alternatives Considered**:
  - **Reactive MongoDB**:
    - Pros: Better scalability, non-blocking I/O
    - Cons: Steeper learning curve, more complex error handling
  - **JPA/Hibernate with PostgreSQL**:
    - Pros: Strong consistency, ACID transactions
    - Cons: Less flexible schema, more complex for hierarchical data
- **Related**: [Database Configuration](./tech-context.md#database-configuration)

## 2025-05-18 - Authentication Mechanism
- **Status**: Approved
- **Context**: Need secure user authentication for the dating app
- **Decision**: Use JWT (JSON Web Tokens) for stateless authentication
- **Consequences**: 
  - Stateless server architecture
  - Need token refresh mechanism
  - Simpler horizontal scaling
- **Alternatives Considered**:
  - Session-based auth: Requires server-side session storage
  - OAuth 2.0: More complex, but could be added later for social logins
- **Related**: [Auth Service](./system-patterns.md#authentication-service)

## 2025-05-18 - Database Selection
- **Status**: Approved
- **Context**: Need a flexible, scalable database for user profiles and matches
- **Decision**: Use MongoDB as the primary database
- **Consequences**:
  - Flexible schema for evolving user profiles
  - Native geospatial queries for location-based matching
  - Eventual consistency model
- **Alternatives Considered**:
  - PostgreSQL: More rigid schema, better for transactions
  - Cassandra: Better for write-heavy workloads but more complex
- **Related**: [Database Schema](./system-patterns.md#database-schema)

## 2025-05-18 - Frontend Framework
- **Status**: Approved
- **Context**: Need a cross-platform mobile and web solution
- **Decision**: Use React Native for mobile and React for web
- **Consequences**:
  - Code sharing between web and mobile
  - Large community and ecosystem
  - Need to handle platform-specific code
- **Alternatives Considered**:
  - Flutter: Good for mobile, but newer ecosystem
  - Native development: Better performance but higher maintenance
- **Related**: [Frontend Architecture](./tech-context.md#frontend-architecture)
