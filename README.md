# SmartPantry Monorepo

[![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.1-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white)](https://angular.io/)

SmartPantry is a robust solution designed for domestic inventory management and food waste reduction. This repository is organized as a monorepo containing both the backend and frontend applications.

## Project Structure

- `backend/`: Java-based RESTful API leveraging the Spring Boot ecosystem.
- `frontend/`: Single Page Application (SPA) developed with Angular.

## Getting Started

### Prerequisites
- Java Development Kit (JDK) 21
- Node.js & npm (LTS recommended)
- Apache Maven 3.9+ (or use the wrapper in `backend/`)

### Backend Execution
Navigate to the `backend` directory:
```bash
cd backend
./mvnw spring-boot:run
```

### Frontend Execution
Navigate to the `frontend` directory:
```bash
cd frontend
npm install
npm start
```

## Documentation
For more details on coding standards and instructions for agents, please refer to [AGENTS.md](./AGENTS.md).
Specific documentation for the API and architecture can be found within the `backend/` and `frontend/` directories.
