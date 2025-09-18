# Movie Rating Service

A Spring Boot application that provides a RESTful API for managing movies and their ratings.

## Overview

Movie Rating Service is a web application that allows users to:
- Browse and search for movies
- Create new movies (admin functionality)
- Rate movies (authenticated users)
- View top-rated movies
- Register and authenticate users

## Technologies

- Java 21
- Spring Boot 3.3.7
- Spring Security with JWT authentication
- Spring Data JPA
- MySQL database
- Flyway for database migrations
- OpenAPI for API documentation
- Docker and Docker Compose for containerization
- ELK Stack (Elasticsearch, Logstash, Kibana) for logging and monitoring
- Spring Boot Actuator and Prometheus for application monitoring and metrics collection with percentile support
- Testcontainers for integration testing
- ArchUnit for architecture testing

## Prerequisites

- Java 21 or higher
- Docker and Docker Compose (for containerized deployment)

## Getting Started

### Running with Docker

The easiest way to run the application is using Docker Compose:

```bash
# Build the application
mvn clean package

# Start all services
docker-compose up -d --build
```

This will start:
- MySQL database
- Movie Rating Service application
- Elasticsearch
- Kibana
- Filebeat for log collection

The application will be available at http://localhost:8080

## API Documentation

The API is documented using OpenAPI. When the application is running, you can access the API documentation at:

- Swagger UI: http://localhost:8080/swagger-ui.html

### Main Endpoints

- `GET /api/v1/movies` - List all movies (paginated)
- `POST /api/v1/movies` - Create a new movie (admin only)
- `POST /api/v1/movies/{movieId}/ratings` - Rate a movie
- `DELETE /api/v1/movies/{movieId}/ratings` - Delete a movie rating
- `GET /api/v1/movies/top-rated` - Get top-rated movies
- `POST /api/v1/users/register` - Register a new user
- `POST /api/v1/auth/login` - Authenticate and get JWT token

## Database

The application uses MySQL as its database. The schema is managed using Flyway migrations, which are automatically applied when the application starts.

### Initial Data

When the application starts, it automatically loads initial data through Flyway migrations (see `src/main/resources/db/migration/V2__insert_initial_data.sql`). This includes:

- **Movies**: 17 pre-populated movies with names, rating counts, and average ratings
- **Roles**: Two roles are defined:
  - `ROLE_ADMIN`: Has full access to all features
  - `ROLE_USER`: Has limited access to features
- **Privileges**: Four privileges are defined:
  - `RATE_MOVIE`: Ability to rate movies
  - `UPDATE_MOVIE_RATING`: Ability to update movie ratings
  - `DELETE_MOVIE_RATING`: Ability to delete movie ratings
  - `MANAGE_MOVIES`: Ability to manage movies (admin only)
- **Users**: Six pre-configured users:
  - Admin users: `admin1@example.com`, `admin2@example.com`, `admin3@example.com`
  - Regular users: `user1@example.com`, `user2@example.com`, `user3@example.com`
  - All users have the same password: `Password123!`

Testers can use these pre-configured users to test different access levels and functionality without needing to create new accounts.

## Logging

The application uses the ELK stack for centralized logging:
- Logs are collected by Filebeat
- Stored in Elasticsearch
- Visualized in Kibana (available at http://localhost:5601).

## Development

### Project Structure

The project follows a clean architecture approach with a clear separation of concerns:

- `com.sky.movieratingservice.domain` - Core domain models and interfaces (innermost layer)
  - Contains business entities and repository interfaces
  - Has no dependencies on other layers or frameworks

- `com.sky.movieratingservice.usecases` - Application business rules (middle layer)
  - Contains use case implementations that orchestrate the flow of data
  - Depends only on the domain layer
  - Independent of external concerns like databases or UI

- `com.sky.movieratingservice.interfaces` - Interface adapters (outermost layer)
  - Contains controllers, repository implementations, and external service adapters
  - Converts data between the format most convenient for use cases and entities
  - Depends on both domain and use case layers

This architecture ensures:
- High testability through dependency inversion
- Separation of concerns with clear boundaries
- Protection of business rules from external changes
- Flexibility to change frameworks or databases with minimal impact

### Building

```bash
mvn clean package
```

### Testing

```bash
mvn test
```

The project employs several testing strategies:

#### Unit Testing
- Tests individual components in isolation
- Uses JUnit 5 and Spring's testing framework
- Mocks dependencies using Spring's testing utilities

#### Integration Testing with Testcontainers
- Tests interactions between components and external systems
- Uses Testcontainers to spin up real MySQL databases in Docker containers
- Ensures database interactions work as expected in a production-like environment
- Provides isolated, reproducible test environments
- Base integration test class sets up the MySQL container automatically

#### Architecture Testing with ArchUnit
- Enforces clean architecture principles through automated tests
- Verifies that dependency rules between layers are maintained:
  - Domain layer doesn't depend on any other layer
  - Use case layer only depends on domain layer
  - Interface layer can depend on both domain and use case layers
- Prevents architectural erosion over time
- Runs as part of the regular test suite

## Security

The application uses JWT (JSON Web Tokens) for authentication. To access protected endpoints:

1. Register a user or login with existing credentials
2. Use the returned JWT token in the Authorization header for subsequent requests:
   ```
   Authorization: Bearer <your-jwt-token>
   ```
