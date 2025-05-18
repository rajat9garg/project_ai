# Dating App - System Patterns

> **Related Documents**: 
> - [Technical Context](./tech-context.md) for implementation details
> - [Decision Log](./decision-log.md) for architectural decisions
> - [Open Questions](./open-questions.md) for pending technical decisions

## System Architecture

### High-Level Architecture
```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│                 │     │                 │     │                 │
│  Mobile Client  │ ◄──►│  API Gateway    │ ◄──►│  Auth Service   │
│                 │     │                 │     │                 │
└─────────────────┘     └─────────────────┘     └─────────────────┘                                       
         ▲                       ▲
         │                       │
         ▼                       ▼
┌─────────────────┐     ┌─────────────────┐
│                 │     │                 │
│  Web Client     │     │  User Service   │
│                 │     │                 │
└─────────────────┘     └─────────────────┘
                                               
```

### Technology Stack
- **Frontend**: React Native (mobile), React (web)
- **Backend**: Kotlin/Spring Boot
- **Database**: MongoDB (user data), Redis (caching)
- **Search**: Elasticsearch (for advanced matching)
- **Messaging**: WebSockets for real-time chat
- **Storage**: AWS S3 for media files

## Authentication Service

### Overview
The authentication service handles user registration, login, and session management. It's implemented as a separate microservice for better scalability and security.

### Key Components

1. **JWT Token Generation**
   - Uses asymmetric encryption (RS256)
   - Short-lived access tokens (15 minutes)
   - Long-lived refresh tokens (7 days)
   - Token blacklisting for logout

2. **Password Security**
   - BCrypt hashing with work factor 12
   - Rate limiting on login attempts
   - Account lockout after multiple failed attempts

3. **Session Management**
   - Redis-backed session storage
   - Device fingerprinting
   - Session invalidation on password change

### API Endpoints
- `POST /api/v1/auth/register` - Register new user
- `POST /api/v1/auth/login` - User login
- `POST /api/v1/auth/refresh` - Refresh access token
- `POST /api/v1/auth/logout` - Invalidate tokens

For more details, see the [Authentication Decision](./decision-log.md#2025-05-18---authentication-mechanism) in the decision log.

## Design Patterns

### Repository Pattern
- Used for data access abstraction
- Provides a clean API for data operations
- Makes testing easier by allowing mock implementations

### Service Layer Pattern
- Business logic is encapsulated in service classes
- Controllers remain thin, delegating to services
- Improves testability and maintainability

### Observer Pattern
- Used for real-time notifications
- Notifies users of new matches and messages
- Decouples the notification logic from core business logic

## API Specifications

### Base URL
`https://api.datingapp.com/v1`

### Authentication
- JWT-based authentication
- Token refresh mechanism
- Role-based access control

### Endpoints

#### Authentication
- `POST /auth/register` - Register new user
- `POST /auth/login` - User login
- `POST /auth/refresh` - Refresh access token

#### User Profile
- `GET /users/me` - Get current user profile
- `PUT /users/me` - Update profile
- `GET /users/{id}` - Get user by ID

#### Matching
- `GET /matches` - Get matches
- `POST /matches/{id}/like` - Like a user
- `POST /matches/{id}/pass` - Pass on a user

## Data Models

### User
```kotlin
data class User(
    val id: String,
    val email: String,
    val name: String,
    val birthDate: LocalDate,
    val gender: Gender,
    val bio: String?,
    val interests: List<String>,
    val location: GeoJsonPoint,
    val photos: List<Photo>,
    val preferences: UserPreferences,
    val isVerified: Boolean,
    val createdAt: Instant,
    val updatedAt: Instant
)
```

### Match
```kotlin
data class Match(
    val id: String,
    val user1Id: String,
    val user2Id: String,
    val matchedAt: Instant,
    val lastMessage: Message?,
    val isActive: Boolean
)
```

## Database Schema

### Users Collection
```
{
  _id: ObjectId,
  email: String,
  passwordHash: String,
  name: String,
  birthDate: Date,
  gender: String,
  bio: String,
  interests: [String],
  location: { type: "Point", coordinates: [longitude, latitude] },
  photos: [
    {
      url: String,
      isPrimary: Boolean,
      uploadedAt: Date
    }
  ],
  preferences: {
    ageRange: { min: Number, max: Number },
    maxDistance: Number,
    genderPreference: [String]
  },
  isVerified: Boolean,
  createdAt: Date,
  updatedAt: Date
}
```

### Matches Collection
```
{
  _id: ObjectId,
  users: [ObjectId],
  matchedAt: Date,
  lastMessage: {
    senderId: ObjectId,
    content: String,
    sentAt: Date
  },
  isActive: Boolean
}
```

## Caching Strategy
- User profiles: 1 hour TTL
- Matches: 5 minutes TTL
- User preferences: 24 hours TTL

## Security Considerations
- All endpoints require authentication except /auth/*
- Rate limiting on authentication endpoints
- Input validation on all endpoints
- Data encryption at rest and in transit
- Regular security audits

## Performance Considerations
- Pagination for large result sets
- Database indexing on frequently queried fields
- Caching strategy for read-heavy operations
- Asynchronous processing for non-critical operations
