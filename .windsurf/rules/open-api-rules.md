---
trigger: always_on
---

# OpenAPI Specification Guidelines (Windsurf)

## Table of Contents
- [File Structure](#file-structure)
- [Specification Format](#specification-format)
- [Versioning](#versioning)
- [Documentation Standards](#documentation-standards)
- [Validation](#validation)
- [Best Practices](#best-practices)

## File Structure

- Store specs in `src/main/resources/openapi/`
- Main file: `api.yaml`
- Use `$ref` to split large specs
- Place all schema definitions in `schemas/`
- File naming: kebab-case
- Schema property names: snake_case
- Constants: UPPER_SNAKE_CASE
- Schema names: PascalCase

## Specification Format

```yaml
openapi: 3.0.3
info:
  title: Service Name API
  description: API documentation
  version: 1.0.0
servers:
  - url: /api/v1
paths:
  /example:
    get:
      summary: Example endpoint
      responses:
        '200':
          description: OK
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Example'
components:
  schemas:
    Example:
      type: object
      properties:
        id:
          type: string
          format: uuid
          example: 123e4567-e89b-12d3-a456-426614174000
Versioning
API version in path (e.g., /api/v1/)

Semantic versioning: MAJOR.MINOR.PATCH

Update info.version on each release

Use Git tags and maintain a changelog

Documentation Standards
Use clear, short endpoint descriptions

Document all request/response schemas with field descriptions

Include:

Status codes and meaning

Request/response content types

Examples with realistic values

Required vs optional fields

Format constraints (UUID, email, etc.)

Enums and their meanings

Default values (if any)

Pagination, filtering, sorting fields

Deprecation notes when applicable

Validation
Validate all inputs and outputs against schema

Use correct HTTP status codes (400 for invalid requests)

Show descriptive error messages

Enforce required fields, formats, patterns

Best Practices
Keep specs DRY using $ref

Group related endpoints with tags

Use consistent naming (snake_case, kebab-case)

Include examples for all models

Reuse schemas for consistent structure

Use enums for fixed sets of values

Keep descriptions concise (<200 chars)

Document default values

Reactive Design
All APIs must generate reactive interfaces (e.g., using Mono, Flux)

Models should be compatible with reactive programming

Do not use blocking types or imperative signatures