# Agent Guide - SmartPantry Monorepo

This document provides essential information for autonomous agents (like opencode) working on the SmartPantry project. Adhere strictly to these guidelines to maintain consistency and quality.

## 1. Project Overview
SmartPantry is a domestic inventory management system. It features a Spring Boot backend and an Angular 19 frontend, containerized with Docker.

### Backend
- **Location:** `/backend`
- **Stack:** Java 21, Spring Boot 4.0.1, Maven, PostgreSQL (Prod) / H2 (Dev/Test).
- **Architecture:** Controller-Service-Repository pattern.

### Frontend
- **Location:** `/frontend`
- **Stack:** TypeScript, Angular 19+, npm, Vitest.
- **UI:** Everforest Dark theme, Material Icons, Glassmorphism.
- **Pattern:** Smart/Dumb components, Signals-based state management.

## 2. Critical Commands

### Infrastructure
- **Full Stack (Docker):** `docker compose up --build`
- **Prune Volumes:** `docker volume prune` (Use with caution)

### Backend (from `/backend`)
- **Compile:** `./mvnw clean compile`
- **Run Tests:** `./mvnw test`
- **Run Single Test:** `./mvnw test -Dtest=ClassName` (e.g., `./mvnw test -Dtest=SmartPantryApplicationTests`)
- **Run Application:** `./mvnw spring-boot:run`
- **Linter (Checkstyle):** `./mvnw checkstyle:check` (if configured)

### Frontend (from `/frontend`)
- **Install:** `npm install`
- **Build:** `npm run build`
- **Dev Server:** `npm start`
- **Run Tests:** `npx vitest run` (since 'test' script may be missing)
- **Single Test File:** `npx vitest run path/to/file.spec.ts`

## 3. Project Structure
- `backend/src/main/java/SmartPantry/demo/`:
    - `configs/`: Security (JWT), CORS, and Global Filters.
    - `controllers/`: REST API endpoints.
    - `services/`: Business logic. Prefix interfaces with `I`.
    - `dtos/`: `requests/` for inputs, `responses/` for outputs.
    - `entities/`: JPA entities.
    - `exceptions/`: Custom exceptions and `GlobalExceptionHandler`.
- `frontend/src/app/`:
    - `core/`: Singletons (services, guards, interceptors, models).
    - `shared/`: Reusable dumb components (buttons, inputs, cards).
    - `features/`: Smart components organized by domain (auth, pantry).

## 4. Coding Style & Conventions

### Naming Conventions
- **Packages:** `SmartPantry.demo` (PascalCase root package name is intentional).
- **Classes:** PascalCase (e.g., `ProductService`).
- **Interfaces:** Prefix with `I` (e.g., `IProductService`).
- **Methods/Variables:** camelCase (e.g., `getExpiringProducts`).
- **Constants:** UPPER_SNAKE_CASE.
- **Enums:** PascalCase type, UPPER_SNAKE_CASE values.

### Formatting & Imports
- **Indentation:** 4 spaces for Java, 2 spaces for TS/HTML/CSS.
- **Braces:** K&R style (opening brace on the same line).
- **Imports:** Group by package. No wildcard imports (except common Spring annotations in controllers).
- **Styles:** Use Pure CSS with Everforest Dark variables defined in `styles.css`.

### Backend Implementation
- **Manual Mapping:** **NEVER** use ModelMapper or AutoMapper. Use manual Builder-based mapping in the Service layer for performance and reliability.
- **Lombok:** Use `@Data`, `@NoArgsConstructor`, `@AllArgsConstructor`, and `@Builder` for Entities and DTOs.
- **Constructor Injection:** Use `@RequiredArgsConstructor` for DI.
- **Validation:** Use `jakarta.validation.constraints` (e.g., `@NotBlank`, `@NotNull`, `@FutureOrPresent`) in Request DTOs.

### Frontend Implementation
- **Angular Signals:** Use Signals (`signal`, `computed`, `effect`) for all reactive state. Avoid RXJS `BehaviorSubject` where Signals are appropriate.
- **Smart/Dumb Pattern:**
    - **Dumb:** No logic, only `@Input` and `@Output`. Reusable and stateless.
    - **Smart:** Injects services, manages state via Signals, and handles events from dumb components.
- **3-File Structure:** Every component MUST have separate `.ts`, `.html`, and `.css` files. **NO INLINE TEMPLATES OR STYLES.**

## 5. REST API Design
- **Endpoints:** Lowercase kebab-case (e.g., `/api/v1/products`).
- **Versioning:** Always use `/api/v1` prefix.
- **Contract:** Match DTO field names between frontend and backend exactly (e.g., `expirationDate`).
- **Status Codes:**
    - `200 OK`: Successful GET/PUT.
    - `201 Created`: Successful POST.
    - `204 No Content`: Successful DELETE.
    - `400 Bad Request`: Validation or logic errors.
    - `401 Unauthorized`: Missing/Invalid JWT.
    - `404 Not Found`: Resource not found.

## 6. Error Handling
- **Backend:** Throw standard or custom exceptions. `GlobalExceptionHandler` will catch and return `ErrorResponse`.
- **Frontend:** Use `NotificationService` to display bottom-center toasts for errors and success messages.

## 7. Security & Authentication
- **JWT:** Tokens are stored in `localStorage` and injected via `JwtInterceptor`.
- **Guards:** Protect routes using `authGuard`.
- **Ownership:** Service layer **MUST** verify that the resource belongs to the currently authenticated user (`user_id` check).

## 8. Git & Workflow
- **Atomic Commits:** Make small, descriptive commits (e.g., `feat: ...`, `fix: ...`, `chore: ...`).
- **Tests:** Run `./mvnw test` before pushing backend changes.
- **Docker:** If modifying dependencies or configurations, verify with `docker compose up --build`.

## 9. Environment Specifics
- **Port 80:** Frontend (Nginx).
- **Port 8080:** Backend API.
- **PostgreSQL:** Port 5432.

---
*This guide is for automated agents. Maintain these standards to ensure project health.*
