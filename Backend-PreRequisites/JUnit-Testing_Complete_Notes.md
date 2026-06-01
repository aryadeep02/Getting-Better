# JUnit 5 Complete Notes for Java Full-Stack Developers

## What is JUnit?

JUnit is the most popular testing framework used in Java applications. It helps developers verify that their code behaves as expected by writing automated tests.

### Why Use JUnit?

* Detect bugs early
* Improve code quality
* Enable safe refactoring
* Support CI/CD pipelines
* Increase confidence before deployment

---

# JUnit vs JUnit 5

| JUnit                            | JUnit 5                      |
| -------------------------------- | ---------------------------- |
| Testing framework family         | Latest version of JUnit      |
| Includes all versions            | Modern testing platform      |
| Older projects may use JUnit 3/4 | Recommended for new projects |

When developers say "JUnit" today, they usually mean **JUnit 5**.

---

# JUnit 5 Architecture

JUnit 5 consists of three main components:

## 1. JUnit Platform

Provides:

* Test discovery
* Test execution
* IDE integration
* Build tool integration

Supports:

* IntelliJ IDEA
* Eclipse
* Maven
* Gradle

---

## 2. JUnit Jupiter

The modern testing API used for writing tests.

Provides:

* Annotations
* Assertions
* Lifecycle methods
* Parameterized tests

Most of your work will be done using Jupiter.

---

## 3. JUnit Vintage

Allows execution of legacy:

* JUnit 3 tests
* JUnit 4 tests

Useful when migrating older projects.

---

# Maven Dependency

```xml
<dependency>
    <groupId>org.junit.jupiter</groupId>
    <artifactId>junit-jupiter</artifactId>
    <version>5.10.2</version>
    <scope>test</scope>
</dependency>
```

---

# Project Structure

```text
src
├── main
│   └── java
│       └── Calculator.java
│
└── test
    └── java
        └── CalculatorTest.java
```

Production code belongs inside:

```text
src/main/java
```

Test code belongs inside:

```text
src/test/java
```

---

# First JUnit 5 Test

## Calculator.java

```java
public class Calculator {

    public int add(int a, int b) {
        return a + b;
    }
}
```

## CalculatorTest.java

```java
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    @Test
    void shouldAddNumbers() {

        Calculator calculator = new Calculator();

        int result = calculator.add(2, 3);

        assertEquals(5, result);
    }
}
```

---

# Important Annotations

## @Test

Marks a method as a test method.

```java
@Test
void testMethod() {

}
```

---

## @BeforeEach

Runs before every test.

```java
@BeforeEach
void setup() {

}
```

---

## @AfterEach

Runs after every test.

```java
@AfterEach
void cleanup() {

}
```

---

## @BeforeAll

Runs once before all tests.

```java
@BeforeAll
static void initialize() {

}
```

---

## @AfterAll

Runs once after all tests.

```java
@AfterAll
static void destroy() {

}
```

---

## @DisplayName

Provides a readable test name.

```java
@DisplayName("Testing Login Functionality")
```

---

## @Disabled

Temporarily skips a test.

```java
@Disabled
@Test
void testFeature() {

}
```

---

# Assertions

Assertions compare expected and actual results.

## assertEquals()

```java
assertEquals(10, result);
```

## assertNotEquals()

```java
assertNotEquals(10, result);
```

## assertTrue()

```java
assertTrue(age >= 18);
```

## assertFalse()

```java
assertFalse(isEmpty);
```

## assertNull()

```java
assertNull(user);
```

## assertNotNull()

```java
assertNotNull(user);
```

## assertThrows()

```java
assertThrows(
    ArithmeticException.class,
    () -> calculator.divide(5, 0)
);
```

---

# AAA Pattern

Professional tests follow:

## Arrange

Prepare test data.

## Act

Execute the method.

## Assert

Verify results.

Example:

```java
@Test
void shouldAddNumbers() {

    // Arrange
    Calculator calculator = new Calculator();

    // Act
    int result = calculator.add(2, 3);

    // Assert
    assertEquals(5, result);
}
```

---

# Parameterized Tests

Allows multiple inputs for a single test.

```java
@ParameterizedTest
@CsvSource({
        "1,2,3",
        "5,5,10",
        "10,20,30"
})
void testAdd(int a, int b, int expected) {

    Calculator calculator = new Calculator();

    assertEquals(expected, calculator.add(a, b));
}
```

---

# Nested Tests

Used for organizing test groups.

```java
@Nested
class LoginTests {

}
```

Example:

```text
AuthenticationTests
├── LoginTests
├── SignupTests
└── TokenTests
```

---

# Exception Testing

```java
@Test
void shouldThrowException() {

    Calculator calculator = new Calculator();

    assertThrows(
        ArithmeticException.class,
        () -> calculator.divide(10, 0)
    );
}
```

---

# Timeout Testing

```java
assertTimeout(
        Duration.ofSeconds(2),
        () -> {
            Thread.sleep(1000);
        }
);
```

Useful for:

* APIs
* Database operations
* AI model calls

---

# Dynamic Tests

Tests generated at runtime.

```java
@TestFactory
Collection<DynamicTest> dynamicTests() {

}
```

Used in:

* Automation systems
* AI testing
* Large datasets

---

# JUnit 5 with Spring Boot

Dependency:

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

Includes:

* JUnit 5
* Mockito
* AssertJ
* Spring Test

---

# Mockito Overview

Mockito creates fake objects called mocks.

Benefits:

* Faster tests
* No real database required
* No external APIs required

Example:

```java
UserRepository repository = mock(UserRepository.class);

when(repository.findById(1))
        .thenReturn("Aryadeep");
```

---

# API Testing with MockMvc

```java
@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnUser() throws Exception {

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk());
    }
}
```

---

# Code Coverage

Measures how much code is tested.

Popular Tool:

* JaCoCo

Recommended Coverage:

* 70% to 85%

Remember:

High coverage does not guarantee bug-free code.

---

# Testing Pyramid

```text
          UI Tests
     Integration Tests
          Unit Tests
```

Most tests should be Unit Tests because they are:

* Fast
* Reliable
* Easy to maintain

---

# Best Practices

* Write small focused tests
* Use meaningful test names
* Follow AAA pattern
* Keep tests independent
* Test edge cases
* Test exceptions
* Mock external dependencies
* Automate tests in CI/CD

---

# Common Interview Questions

### What is JUnit?

A Java testing framework used for automated testing.

### What is JUnit Jupiter?

The primary API used for writing JUnit 5 tests.

### Difference between JUnit 4 and JUnit 5?

JUnit 5 is modular, more powerful, and supports advanced features like nested tests and parameterized tests.

### What is Mocking?

Creating fake dependencies for testing.

### What is Unit Testing?

Testing a single unit of code in isolation.

### What is Integration Testing?

Testing multiple components working together.

---

# Learning Roadmap

## Beginner

* @Test
* Assertions
* Lifecycle methods

## Intermediate

* Parameterized Tests
* Nested Tests
* Exception Testing

## Advanced

* Mockito
* Spring Boot Testing
* MockMvc
* Integration Testing
* Testcontainers

## Professional

* CI/CD Pipelines
* GitHub Actions
* Jenkins
* Code Coverage
* Enterprise Testing Strategies

---

# Final Takeaway

JUnit 5 is the industry-standard testing framework for modern Java development.

Every Java Full-Stack Developer should be comfortable with:

* Writing unit tests
* Testing Spring Boot applications
* Using Mockito
* Automating tests in CI/CD pipelines

Testing is not an optional skill—it is a core part of professional software development.
