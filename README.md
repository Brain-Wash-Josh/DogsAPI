# Dogs API

A RESTful API for managing police dog registrations, built with Micronaut 4.94, Java 21, and Gradle Kotlin.

## Table of Contents

- [Overview](#overview)
- [Technologies](#technologies)
- [Architecture](#architecture)
- [Getting Started](#getting-started)
- [API Endpoints](#api-endpoints)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Project Structure](#project-structure)

## Overview

The Dogs API provides a comprehensive system for managing police dog registrations. It supports full CRUD operations, soft deletion for audit purposes, pagination, and filtering capabilities. All endpoints are RESTful and consume/produce JSON.

### Key Features

- Complete CRUD operations for dog records
- Soft deletion for audit trail maintenance
- Pagination support for listing operations
- Advanced filtering by name, breed, and supplier
- Enumerated values for dog status and leaving reasons
- Constructor-based dependency injection
- MapStruct for entity-DTO mapping
- Comprehensive test coverage

## Technologies

- **Java 21**: Latest LTS version of Java
- **Micronaut 4.94**: Modern JVM-based framework
- **Gradle Kotlin DSL**: Build automation
- **JUnit 5**: Testing framework
- **MapStruct 1.5.5**: Java bean mappings
- **Micronaut Data JDBC**: Database access
- **H2 Database**: In-memory database for development
- **Flyway**: Database migration tool

## Architecture

### Package Structure

```
com.task.dogs/
├── controller/           # REST controllers
│   ├── DogController.java
│   ├── DogStatusController.java
│   └── LeavingReasonController.java
├── service/             # Business logic layer
│   ├── DogService.java
│   ├── DogStatusService.java
│   └── LeavingReasonService.java
├── repository/          # Data access layer
│   ├── DogRepository.java
│   ├── DogStatusRepository.java
│   └── LeavingReasonRepository.java
├── domain/
│   ├── entity/         # JPA entities
│   │   ├── Dog.java
│   │   ├── DogStatus.java
│   │   └── LeavingReason.java
│   ├── dto/           # Data Transfer Objects
│   │   └── DogDTO.java
│   └── mapper/        # MapStruct mappers
│       └── DogMapper.java
├── exception/         # Custom exceptions and handlers
│   ├── ResourceNotFoundException.java
│   └── GlobalExceptionHandler.java
└── Application.java   # Main application class
```

### Design Patterns

- **Constructor Injection**: All dependencies are injected via constructors for better testability and immutability
- **Repository Pattern**: Data access is abstracted through repository interfaces
- **DTO Pattern**: Separation of internal entities from API contracts
- **Service Layer**: Business logic is encapsulated in service classes
- **Soft Delete**: Records are marked as deleted rather than physically removed

## Getting Started

### Prerequisites

- Java 21 or higher
- Gradle 8.x (wrapper included)

### Installation

1. Clone the repository:
```bash
git clone <repository-url>
cd dogs-api
```

2. Build the project:
```bash
./gradlew build
```

3. Run the application:
```bash
./gradlew run
```

The API will start on `http://localhost:8080` with context path `/api/dogs`.

### Running Tests

Execute all tests:
```bash
./gradlew test
```

Run tests with coverage:
```bash
./gradlew test jacocoTestReport
```

## API Endpoints

All endpoints are prefixed with `/api/dogs`.

### Dogs Resource

#### Create a Dog
```http
POST /api/dogs/dogs
Content-Type: application/json

{
  "name": "Rex",
  "breed": "German Shepherd",
  "supplier": "K9 Kennels Ltd",
  "badgeId": "K9-001",
  "gender": "Male",
  "birthDate": "2020-05-15",
  "dateAcquired": "2021-01-10",
  "statusId": 1,
  "kennellingCharacteristic": "Friendly, high energy, responds well to commands"
}
```

**Response**: `201 Created`
```json
{
  "id": 1,
  "name": "Rex",
  "breed": "German Shepherd",
  "supplier": "K9 Kennels Ltd",
  "badgeId": "K9-001",
  "gender": "Male",
  "birthDate": "2020-05-15",
  "dateAcquired": "2021-01-10",
  "statusId": 1,
  "statusName": "In Training",
  "leavingDate": null,
  "leavingReasonId": null,
  "leavingReasonName": null,
  "kennellingCharacteristic": "Friendly, high energy, responds well to commands"
}
```

#### List All Dogs
```http
GET /api/dogs/dogs?page=0&size=20
```

**Response**: `200 OK`
```json
{
  "content": [
    {
      "id": 1,
      "name": "Rex",
      "breed": "German Shepherd",
      ...
    }
  ],
  "pageable": {
    "number": 0,
    "size": 20
  },
  "totalPages": 1,
  "totalSize": 5
}
```

#### Search Dogs with Filters
```http
GET /api/dogs/dogs?name=Rex&breed=Shepherd&supplier=K9&page=0&size=10
```

Supported filter parameters:
- `name`: Partial match on dog name (case-insensitive)
- `breed`: Partial match on breed (case-insensitive)
- `supplier`: Partial match on supplier (case-insensitive)

**Response**: `200 OK` (same structure as List All Dogs)

#### Get Single Dog
```http
GET /api/dogs/dogs/1
```

**Response**: `200 OK`
```json
{
  "id": 1,
  "name": "Rex",
  "breed": "German Shepherd",
  ...
}
```

**Error Response**: `404 Not Found`
```json
{
  "error": "Not Found",
  "message": "Dog not found with id: 999",
  "status": 404
}
```

#### Update a Dog
```http
PUT /api/dogs/dogs/1
Content-Type: application/json

{
  "name": "Rex",
  "breed": "German Shepherd",
  "supplier": "K9 Kennels Ltd",
  "badgeId": "K9-001",
  "gender": "Male",
  "birthDate": "2020-05-15",
  "dateAcquired": "2021-01-10",
  "statusId": 2,
  "kennellingCharacteristic": "Excellent tracking skills"
}
```

**Response**: `200 OK` (returns updated dog)

#### Delete a Dog (Soft Delete)
```http
DELETE /api/dogs/dogs/1
```

**Response**: `204 No Content`

**Note**: Dogs are soft-deleted and remain in the database for audit purposes but won't appear in list/get operations.

### Dog Statuses Resource

#### Get All Statuses
```http
GET /api/dogs/statuses
```

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "statusName": "In Training"
  },
  {
    "id": 2,
    "statusName": "In Service"
  },
  {
    "id": 3,
    "statusName": "Retired"
  },
  {
    "id": 4,
    "statusName": "Left"
  }
]
```

### Leaving Reasons Resource

#### Get All Leaving Reasons
```http
GET /api/dogs/leaving-reasons
```

**Response**: `200 OK`
```json
[
  {
    "id": 1,
    "reasonName": "Transferred"
  },
  {
    "id": 2,
    "reasonName": "Retired (Put Down)"
  },
  {
    "id": 3,
    "reasonName": "KIA"
  },
  {
    "id": 4,
    "reasonName": "Rejected"
  },
  {
    "id": 5,
    "reasonName": "Retired (Re-housed)"
  },
  {
    "id": 6,
    "reasonName": "Died"
  }
]
```

## Database Schema

### Tables

#### `dog`
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier |
| name | VARCHAR(255) | NOT NULL | Dog's name |
| breed | VARCHAR(255) | NOT NULL | Dog's breed |
| supplier | VARCHAR(255) | NOT NULL | Breeder or kennels |
| badge_id | VARCHAR(100) | | Unique badge identifier |
| gender | VARCHAR(10) | NOT NULL | Dog's gender |
| birth_date | DATE | NOT NULL | Date of birth |
| date_acquired | DATE | NOT NULL | Date acquired by force |
| status_id | BIGINT | NOT NULL, FK | Current status |
| leaving_date | DATE | | Date dog left service |
| leaving_reason_id | BIGINT | FK | Reason for leaving |
| kennelling_characteristic | TEXT | | Important kennel notes |
| deleted | BOOLEAN | DEFAULT FALSE | Soft delete flag |
| created_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Record creation time |
| updated_at | TIMESTAMP | DEFAULT CURRENT_TIMESTAMP | Last update time |

#### `dog_status`
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier |
| status_name | VARCHAR(50) | NOT NULL, UNIQUE | Status name |

**Pre-populated values**: In Training, In Service, Retired, Left

#### `leaving_reason`
| Column | Type | Constraints | Description |
|--------|------|-------------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | Unique identifier |
| reason_name | VARCHAR(50) | NOT NULL, UNIQUE | Reason name |

**Pre-populated values**: Transferred, Retired (Put Down), KIA, Rejected, Retired (Re-housed), Died

### Relationships

- `dog.status_id` → `dog_status.id` (MANY-TO-ONE)
- `dog.leaving_reason_id` → `leaving_reason.id` (MANY-TO-ONE, nullable)

## Testing

### Test Coverage

The project includes comprehensive tests covering:

#### Controller Tests (`DogControllerTest`)
- Create dog with valid data
- Create dog with invalid data (validation)
- Get all dogs with pagination
- Get dog by ID (success and not found)
- Update dog
- Delete dog (soft delete verification)
- Search dogs by name, breed, and supplier
- Pagination functionality

#### Service Tests (`DogServiceTest`)
- CRUD operations
- Exception handling
- Soft delete behavior
- Filter operations
- Business logic validation

### Test Structure

Tests follow the Arrange-Act-Assert pattern:
```java
@Test
void testCreateDogSuccessfully() {
    // Arrange
    DogDTO dogDTO = createTestDogDTO();
    
    // Act
    DogDTO result = dogService.createDog(dogDTO);

    // Assert
    assertNotNull(result.getId());
    assertEquals("Rex", result.getName());
}
```

### Running Specific Tests

Run controller tests only:
```bash
./gradlew test --tests "com.task.dogs.controller.*"
```

Run service tests only:
```bash
./gradlew test --tests "com.task.dogs.service.*"
```

## Project Structure

### Key Components

**Controllers**: Handle HTTP requests and responses. Use constructor injection for dependencies.

**Services**: Contain business logic. Services interact with repositories and mappers to perform operations.

**Repositories**: Extend Micronaut Data repositories for database access. Custom queries are defined using `@Query` annotations.

**Entities**: JPA entities representing database tables. Use Micronaut Data annotations.

**DTOs**: Data Transfer Objects for API contracts. Include validation annotations.

**Mappers**: MapStruct interfaces for converting between entities and DTOs, eliminating boilerplate code.

### Configuration Files

- `application.yml`: Application configuration (server, database, Jackson)
- `build.gradle.kts`: Build configuration and dependencies
- `V1__create_dogs_tables.sql`: Flyway migration for initial schema

## Example Usage

### Complete Workflow Example

1. **Get available statuses**:
```bash
curl http://localhost:8080/api/dogs/statuses
```

2. **Create a new dog**:
```bash
curl -X POST http://localhost:8080/api/dogs/dogs \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Max",
    "breed": "Belgian Malinois",
    "supplier": "Elite K9 Training",
    "badgeId": "K9-042",
    "gender": "Male",
    "birthDate": "2021-03-20",
    "dateAcquired": "2022-01-15",
    "statusId": 1,
    "kennellingCharacteristic": "Excellent tracking and detection skills"
  }'
```

3. **Search for dogs by breed**:
```bash
curl "http://localhost:8080/api/dogs/dogs?breed=Malinois&page=0&size=10"
```

4. **Update dog status to In Service**:
```bash
curl -X PUT http://localhost:8080/api/dogs/dogs/1 \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Max",
    "breed": "Belgian Malinois",
    "supplier": "Elite K9 Training",
    "badgeId": "K9-042",
    "gender": "Male",
    "birthDate": "2021-03-20",
    "dateAcquired": "2022-01-15",
    "statusId": 2,
    "kennellingCharacteristic": "Now actively serving"
  }'
```

5. **Soft delete a dog**:
```bash
curl -X DELETE http://localhost:8080/api/dogs/dogs/1
```

## Development Notes

### Soft Delete Implementation

Dogs are never physically deleted from the database. The `deleted` column is set to `true`, and subsequent queries filter out deleted records. This maintains a complete audit trail.

### Validation

Input validation is handled through Jakarta Validation annotations on DTOs:
- `@NotBlank`: Ensures required string fields are not empty
- `@NotNull`: Ensures required fields are present
- `@JsonFormat`: Ensures correct date format

### MapStruct Benefits

MapStruct generates type-safe, performant mapping code at compile time, reducing boilerplate and potential runtime errors.

## Troubleshooting

### Common Issues

**Port already in use**: Change the port in `application.yml`:
```yaml
micronaut:
  server:
    port: 8081
```

**Database migration fails**: Ensure Flyway migrations are in `src/main/resources/db/migration` and follow the naming convention `V{version}__{description}.sql`.

**Tests fail**: Ensure H2 database dependency is in the classpath and test resources are properly configured.

## License

This project is created for educational and evaluation purposes.