---
trigger: always_on
---

# OpenAPI Specification (OAS) Guidelines

## Table of Contents
- [File Structure](#file-structure)
- [Specification Format](#specification-format)
- [Versioning](#versioning)
- [Documentation Standards](#documentation-standards)
- [Code Generation](#code-generation)
- [Validation](#validation)
- [Security](#security)
- [Best Practices](#best-practices)

## File Structure

### Location
- Store OpenAPI specifications in: `src/main/resources/openapi/`
- Main specification file must be named: `api.yaml`
- Split large specs into multiple files using `$ref`
- Keep all schema definitions in a `schemas/` subdirectory

### Naming Conventions
- Use kebab-case for file names
- Use snake_case for schema property names
- Use UPPER_SNAKE_CASE for constants
- Use PascalCase for schema names

## Specification Format

### Required Structure
```yaml
openapi: 3.0.3
info:
  title: Service Name API
  description: Comprehensive API documentation
  version: 1.0.0
  contact:
    name: API Support
    email: support@example.com
  license:
    name: Proprietary
servers:
  - url: /api/v1
    description: Production server
paths:
  /resource:
    get:
      summary: Get resource
      responses:
        '200':
          description: Successful operation
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Resource'
components:
  schemas:
    Resource:
      type: object
      properties:
        id:
          type: string
          format: uuid
          example: 123e4567-e89b-12d3-a456-426614174000
```

## Versioning

### API Versioning
- Include API version in the URL path (e.g., `/api/v1/resource`)
- Use semantic versioning (MAJOR.MINOR.PATCH)
- Document breaking changes in release notes
- Support at least the current and previous major versions

### Specification Versioning
- Update version in `info.version` for each release
- Use Git tags for version control
- Include changelog with each version

## Documentation Standards

### Required Elements
- Clear, concise endpoint descriptions that explain the business purpose
- All request/response schemas with field-level documentation
- HTTP status codes and their meanings (success and error cases)
- Authentication requirements (e.g., JWT token, API key)
- Rate limiting information (headers, limits, and quotas)
- Error response formats and possible error codes
- Examples for all operations with realistic data
- Deprecation notices for old endpoints with migration guides
- Request/response content types (e.g., `application/json`)
- Required vs. optional fields clearly marked
- Format constraints (e.g., email, date-time, UUID)
- Default values where applicable
- Enum values and their meanings
- Pagination parameters and response headers
- Sorting and filtering capabilities
- Field selection (if supported)
- Versioning strategy and compatibility guarantees

### Best Practices
- Use markdown for rich text formatting
- Include examples for all schemas
- Document all possible error responses
- Use consistent terminology
- Keep descriptions under 200 characters when possible
- Use enums for fixed sets of values
- Document default values

## Code Generation

### Configuration
- Use OpenAPI Generator for code generation
- Configure in `build.gradle.kts` with appropriate plugins:
  ```kotlin
  plugins {
      id("org.openapi.generator") version "6.6.0"
  }
  
  openApiGenerate {
      generatorName.set("kotlin-spring")
      inputSpec.set("$rootDir/src/main/resources/openapi/api.yaml")
      outputDir.set("$buildDir/generated")
      modelPackage.set("${project.group}.generated.model")
      apiPackage.set("${project.group}.generated.api")
      configOptions.set(
          mapOf(
              "interfaceOnly" to "true",
              "useSpringBoot3" to "true",
              "useBeanValidation" to "true",
              "documentationProvider" to "none"
          )
      )
  }
  
  // Add generated sources to the main source set
  sourceSets.main {
      java.srcDirs("$buildDir/generated/src/main/kotlin")
  }
  
  // Ensure the openApiGenerate task runs before compilation
  tasks.compileKotlin {
      dependsOn("openApiGenerate")
  }
  ```
- Generate models, APIs, and documentation
- Keep generated code in `build/generated`
- Never modify generated code directly - update the OpenAPI spec instead
- Add `build/generated` to `.gitignore` to prevent committing generated code

### Client SDKs
- Generate for at least Java and TypeScript
- Include usage examples in README
- Publish to package repositories

## Validation

### Schema Validation
- Validate all requests against the schema
- Return 400 for invalid requests
- Include detailed error messages
- Validate enums and patterns
- Enforce required fields

### Contract Testing
- Test all API endpoints against the spec
- Validate request/response formats
- Test error conditions
- Include contract tests in CI/CD


## Best Practices

### General
- Keep the spec DRY (Don't Repeat Yourself)
- Use `$ref` for reusable components (e.g., `#/components/schemas/ErrorResponse`)
- Keep response objects consistent across similar endpoints
- Document all fields with descriptions and examples
- Use consistent naming conventions (snake_case for properties, kebab-case for paths)
- Include examples for all request/response schemas
- Document all possible error responses with appropriate HTTP status codes
- Use enums for fixed sets of values (e.g., user roles, status values)
- Document default values for optional parameters
- Include deprecation notices for old endpoints
- Use tags to group related endpoints (e.g., "Users", "Authentication")
