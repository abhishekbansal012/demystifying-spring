# Tech Stack

## Core

- **Language:** Java 17
- **Framework:** Spring Boot 3.4.4
- **Build System:** Gradle (Groovy DSL)
- **Dependency Management:** Spring Dependency Management Plugin 1.1.7

## Dependencies

- `spring-boot-starter` — core Spring Boot
- `spring-boot-starter-web` — REST/web support (embedded Tomcat)
- `spring-boot-starter-test` — testing (JUnit 5, Mockito, Spring Test)
- `junit-platform-launcher` — test runtime

## Common Commands

```bash
# Build the project
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Clean build artifacts
./gradlew clean
```

## Configuration

- Application config: `src/main/resources/application.properties`
- App name: `demystifying-spring`
- Default port: 8080 (Spring Boot default)
