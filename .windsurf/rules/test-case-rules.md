---
trigger: always_on
---

# Test Case Rules and Guidelines

## Table of Contents
- [1. Test Structure](#1-test-structure)
- [2. Naming Conventions](#2-naming-conventions)
- [3. Test Data Management](#3-test-data-management)
- [4. Mocking Guidelines](#4-mocking-guidelines)
- [5. Assertions](#5-assertions)
- [6. Test Isolation](#6-test-isolation)
- [7. Integration Tests](#7-integration-tests)
- [8. Test Coverage](#8-test-coverage)
- [9. Best Practices](#9-best-practices)

## 1. Test Structure

### 1.1 Test Class Organization
- Place test classes in the same package as the class under test
- Use `*Test` suffix for unit test classes (e.g., `UserServiceTest`)
- Use `*IT` suffix for integration test classes (e.g., `UserControllerIT`)
- Group related test methods using nested classes with `@Nested` annotation

### 1.2 Test Method Structure
Follow the Arrange-Act-Assert (AAA) pattern:

```kotlin
@Test
fun `methodName_shouldExpectedBehavior_whenCondition`() {
    // Arrange
    val testData = createTestData()
    every { mockDependency.method() } returns expectedValue
    
    // Act
    val result = serviceUnderTest.methodUnderTest(testData)
    
    // Assert
    assertThat(result).isEqualTo(expectedResult)
    verify { mockDependency.method() wasCalled 1 }
}
```

## 2. Naming Conventions

### 2.1 Test Method Names
Use descriptive names in the format:
- `methodName_shouldExpectedBehavior_whenCondition`
- `shouldExpectedBehavior_whenCondition` (for BDD style)
- `methodName_shouldThrowException_whenInvalidInput` (for exception cases)

### 2.2 Test Data Variables
- Use descriptive names for test data (e.g., `validUser`, `invalidEmail`)
- Prefix mock objects with `mock` (e.g., `mockUserRepository`)
- Use `expected` prefix for expected values

## 3. Test Data Management

### 3.1 Test Data Factories
- Use a `TestDataFactory` to create test objects
- Make factory methods reusable and configurable
- Use default values that make sense for most test cases

```kotlin
object TestDataFactory {
    fun createUser(
        id: String = UUID.randomUUID().toString(),
        name: String = "Test User",
        email: String = "test@example.com",
        password: String? = "password123"
    ) = User(
        id = id,
        name = name,
        email = email,
        password = password
    )
}
```

### 3.2 Test Containers
- Use Testcontainers for integration tests requiring external services
- Configure test containers in a base test class
- Use `@Testcontainers` and `@Container` annotations

## 4. Mocking Guidelines

### 4.1 MockK Usage
- Use `mockk<T>()` to create mocks
- Use `every` to define mock behavior
- Use `verify` to verify interactions
- Prefer relaxed mocks (`relaxed = true`) when return values aren't important

```kotlin
private val mockRepository = mockk<Repository>(relaxed = true)
private val service = Service(mockRepository)

@Test
fun `should call repository`() {
    // Given
    val user = TestDataFactory.createUser()
    every { mockRepository.save(any()) } returns user
    
    // When
    val result = service.createUser(user)
    
    // Then
    verify { mockRepository.save(user) }
    assertThat(result).isEqualTo(user)
}
```

### 4.2 Argument Captors
Use `slot` to capture arguments for verification:

```kotlin
val userSlot = slot<User>()
every { mockRepository.save(capture(userSlot)) } returns mockk()

// Test code

val capturedUser = userSlot.captured
assertThat(capturedUser.name).isEqualTo("Test User")
```

## 5. Assertions

### 5.1 AssertJ for Kotlin
- Use `assertThat(actual).isEqualTo(expected)` for equality checks
- Use `assertThatThrownBy` for exception assertions
- Use `assertThatCollection` for collection assertions

```kotlin
// Collection assertions
assertThat(users)
    .hasSize(2)
    .extracting("name")
    .containsExactlyInAnyOrder("Alice", "Bob")

// Exception assertion
assertThatThrownBy { service.methodThatThrows() }
    .isInstanceOf(ValidationException::class.java)
    .hasMessage("Invalid input")
```

## 6. Test Isolation

### 6.1 Fresh State
- Each test should be independent
- Reset mocks between tests using `clearMocks()`
- Use `@BeforeEach` for common setup
- Avoid sharing state between tests

## 7. Integration Tests

### 7.1 Spring Boot Tests
- Use `@SpringBootTest` for integration tests
- Use `@Testcontainers` for external services
- Use `@AutoConfigureMockMvc` for controller tests

```kotlin
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIT {
    
    @Autowired
    private lateinit var mockMvc: MockMvc
    
    @Test
    fun `should return user by id`() {
        mockMvc.perform(get("/api/users/1"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Test User"))
    }
}
```

## 8. Test Coverage

### 8.1 Coverage Goals
- Aim for at least 80% line coverage
- Focus on business logic, not getters/setters
- Use `@Disabled` with a reason for temporarily disabled tests

## 9. Best Practices

### 9.1 Test Readability
- Keep tests short and focused
- Use descriptive variable names
- Add comments for complex test setups
- Group related assertions

### 9.2 Test Performance
- Keep unit tests fast (under 50ms each)
- Use mocks for external dependencies
- Run tests in parallel when possible

### 9.3 Test Maintenance
- Update tests when behavior changes
- Delete or update obsolete tests
- Refactor tests when refactoring production code

### 9.4 Flaky Tests
- Fix or delete flaky tests immediately
- Use retries only when absolutely necessary
- Document the reason for any test retries

## Conclusion

Following these guidelines will help maintain a robust, maintainable test suite that provides confidence in the application's behavior. Remember that tests are production code and should be treated with the same care and attention to quality.
