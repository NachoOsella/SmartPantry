# SmartPantry

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4479A1?style=for-the-badge&logo=postgresql&logoColor=white)](https://www.postgresql.org/)
[![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)](https://angular.io/)

SmartPantry is a robust enterprise-grade solution designed for domestic inventory management and food waste reduction. The system provides automated tracking of product life cycles through a specialized expiration status engine.

## System Architecture

The application is built on a decoupled architecture to ensure scalability and maintainability:

*   **Backend:** Java-based RESTful API leveraging the Spring Boot ecosystem.
*   **Frontend:** Single Page Application (SPA) developed with Angular.
*   **Persistence:** Relational data modeling with JPA/Hibernate and PostgreSQL.
*   **Authentication:** Stateless security context (JWT ready).

## Key Functionalities

*   **Inventory Lifecycle Management:** Complete oversight of pantry products and stock levels.
*   **Expiration Engine:** Automated categorization of products into Red, Yellow, and Green status based on proximity to expiration.
*   **Categorization System:** Hierarchical organization of inventory items.
*   **User Isolation:** Multi-tenant data structure ensuring resource ownership and security at the service layer.

## Technical Specifications

### Prerequisites
*   Java Development Kit (JDK) 21
*   Apache Maven 3.9+
*   PostgreSQL (Production) / H2 (Development)

### Build and Execution
Compile the project and install dependencies:
```bash
./mvnw clean install
```

Run the application in development mode:
```bash
./mvnw spring-boot:run
```

## API Documentation

The REST API exposes the following primary endpoints:

| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/api/auth/register` | `POST` | User account creation. |
| `/api/auth/login` | `POST` | Identity verification and session initialization. |
| `/api/products` | `GET` | Retrieval of authenticated user's inventory. |
| `/api/products` | `POST` | Registry of new inventory items. |
| `/api/products/status/{status}` | `GET` | Filtered retrieval by expiration risk level. |
| `/api/categories` | `GET` | Listing of available inventory categories. |

## Project Structure

```text
src/main/java/SmartPantry/demo/
├── configs/      # Application and bean configurations.
├── controllers/  # REST API controllers and request mapping.
├── dtos/         # Data transfer objects for API contracts.
├── entities/     # Domain models and JPA entities.
├── exceptions/   # Centralized error handling and advice.
├── repositories/ # Data access interfaces.
└── services/     # Business logic and service abstractions.
```
