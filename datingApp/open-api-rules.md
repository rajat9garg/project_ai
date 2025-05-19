# Kotlin OpenAPI Generation Rules

## Generator & Structure
- Use `kotlin-spring` as the OpenAPI generator.
- Set `interfaceOnly: true` to generate only interfaces.
- Set `apiPackage` to `learn.ai.generated.api` and `modelPackage` to `learn.ai.generated.model`.
- Output generated Kotlin sources to `build/generated/src/main/kotlin`.
- Add generated sources to the main source set:
  ```kotlin
  sourceSets.main {
    java.srcDirs("$buildDir/generated/src/main/kotlin")
  }
  ```
- All controllers must implement the generated Kotlin interfaces.
- All request/response types must use the generated Kotlin models.
- Do not generate Java code for API interfaces or models.

## OpenAPI Spec Best Practices
- Use OpenAPI 3.0.3 or above.
- Define clear `operationId` for each endpoint (e.g., `usersRegisterPost`).
- Use PascalCase for schema names and camelCase for properties.
- Reference schemas with `$ref` and keep them in a `schemas/` directory.

## Example Gradle Configuration
```kotlin
openApiGenerate {
    generatorName.set("kotlin-spring")
    inputSpec.set("$rootDir/src/main/resources/openapi/api.yaml")
    outputDir.set("$buildDir/generated")
    apiPackage.set("learn.ai.generated.api")
    modelPackage.set("learn.ai.generated.model")
    configOptions.set(mapOf(
        "interfaceOnly" to "true",
        "useSpringBoot3" to "true",
        "useBeanValidation" to "true"
    ))
}
sourceSets.main {
    java.srcDirs("$buildDir/generated/src/main/kotlin")
}
```

## Example OpenAPI Path
```yaml
paths:
  /users/register:
    post:
      operationId: usersRegisterPost
      summary: Register a new user
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: './schemas/UserRegistrationRequest.yaml'
      responses:
        '201':
          description: Created
          content:
            application/json:
              schema:
                $ref: './schemas/UserResponse.yaml'
```
