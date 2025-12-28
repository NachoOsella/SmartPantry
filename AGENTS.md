# Agent Guide - SmartPantry Monorepo

This document provides essential information for autonomous agents working on the SmartPantry project.

## 1. Project Overview
SmartPantry is a monorepo containing a Spring Boot backend and an Angular frontend.

### Backend
- **Location:** `/backend`
- **Language:** Java 21
- **Framework:** Spring Boot 4.0.1
- **Build System:** Maven

### Frontend
- **Location:** `/frontend`
- **Language:** TypeScript
- **Framework:** Angular 19+
- **Build System:** npm

## 2. Critical Commands

### Backend (from /backend)
- **Compile:** `./mvnw clean compile`
- **Test:** `./mvnw test`
- **Run:** `./mvnw spring-boot:run`

### Frontend (from /frontend)
- **Install:** `npm install`
- **Build:** `npm run build`
- **Test:** `npm test`
- **Start:** `npm start`

## 3. Project Structure
- `backend/src/main/java/SmartPantry/demo/`: Java source code.
- `frontend/src/`: Angular source code.
    - `configs/`: Configuration classes (e.g., ModelMapper).
    - `controllers/`: REST API endpoints.
    - `services/`: Business logic.
        - `interfaces/`: Service interfaces (prefixed with `I`).
    - `repositories/`: Data access layer (Spring Data JPA).
    - `entities/`: JPA entities and Enums.
    - `dtos/`: Data Transfer Objects.
        - `requests/`: Input validation DTOs.
        - `responses/`: Output DTOs.
    - `exceptions/`: Custom exceptions and global error handling.

## 4. Coding Style & Conventions

### Naming Conventions
- **Packages:** Use `SmartPantry.demo` as the base package (Note the PascalCase in the root package).
- **Classes:** PascalCase (e.g., `ProductService`).
- **Interfaces:** Prefix with `I` (e.g., `IProductService`).
- **Methods/Variables:** camelCase (e.g., `getAllForCurrentUser`).
- **Constants:** UPPER_SNAKE_CASE.
- **Enums:** PascalCase for the type, UPPER_SNAKE_CASE for values.

### Formatting
- **Indentation:** Use 4 spaces for most classes (Services, Entities, etc.). Note: Some existing Controllers may use 2 spaces; prioritize consistency within the file you are editing.
- **Braces:** Use K&R style (braces on the same line as the statement).
- **Imports:** Group imports by package. Avoid wildcard imports where possible, except for common Spring annotations in controllers if already present.

### Dependencies & Boilerplate
- **Lombok:** Use `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, and `@Builder` for Entities and DTOs.
- **Constructor Injection:** Use `@RequiredArgsConstructor` for dependency injection in Controllers and Services.
- **Validation:** Use `jakarta.validation.constraints` annotations (e.g., `@NotBlank`, `@NotNull`, `@FutureOrPresent`) in DTOs.

### REST API Design
- **Endpoints:** Use lowercase kebab-case for URLs (e.g., `/api/products`).
- **Methods:** Follow standard HTTP verbs (GET, POST, PUT, DELETE).
- **Responses:** Always return `ResponseEntity<T>`.
- **Status Codes:**
    - `200 OK`: Successful GET/PUT.
    - `201 Created`: Successful POST.
    - `204 No Content`: Successful DELETE.
    - `400 Bad Request`: Validation errors.
    - `404 Not Found`: Resource not found.

## 5. Testing Best Practices
- **Unit Tests:** Use Mockito to mock dependencies (Repositories/Services).
- **Integration Tests:** Use `@SpringBootTest` and `@ActiveProfiles("dev")` to test with H2.
- **Controller Tests:** Use `MockMvc` for testing API endpoints without starting the full server.
- **Naming:** Test methods should be descriptive (e.g., `shouldReturnProduct_WhenIdExists`).
- **Assertions:** Use AssertJ (`assertThat`) for fluent assertions.

## 6. Error Handling
- Use `GlobalExceptionHandler` to catch and format exceptions.
- Return `ErrorResponse` DTO for all errors.
- Prefer throwing standard Spring/Persistence exceptions (e.g., `EntityNotFoundException`) or specific `IllegalArgumentException` which are already handled globally.

## 7. Service Layer Pattern
- Always define an interface in `services.interfaces`.
- Implement the interface in `services`.
- Inject the interface, not the implementation.
- Use `ModelMapper` for converting between Entities and DTOs.

## 8. Database & Persistence
- **Entities:** Annotate with `@Entity` and `@Table`. Use `@Id @GeneratedValue(strategy = GenerationType.IDENTITY)` for primary keys.
- **Relationships:** Use `FetchType.LAZY` for `@ManyToOne` and `@OneToMany` to avoid performance issues.
- **Repositories:** Extend `JpaRepository<Entity, IdType>`.
- **Queries:** Prefer Spring Data query methods; use `@Query` for complex logic.

## 9. Security & Authentication
- Authentication is handled via `AuthService` and `AuthController`.
- Always check resource ownership in the service layer by comparing the current user's ID with the resource's `user_id`.
- Use `IUserService.getCurrentUser()` to retrieve the authenticated user context.

## 10. Git & Workflow
- Ensure all tests pass before completing a task.
- Follow existing patterns for commit messages (if a repo exists).
- Do not modify `pom.xml` unless adding a necessary dependency.

## 11. Model Specific Rules
- When implementing a service method, check the "TO DO" comments in existing service implementations for specific logic requirements.
- Always verify if the resource being accessed belongs to the currently authenticated user (logic for this should be in the service layer).

---
*This file is generated for automated agents. Maintain the structure and conventions described herein.*
