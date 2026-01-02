# SmartPantry: Domestic Inventory Intelligence

<div align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring_Boot-4.0.1-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Angular-19-DD0031?style=for-the-badge&logo=angular&logoColor=white" alt="Angular 19" />
  <img src="https://img.shields.io/badge/PostgreSQL-17-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/Everforest-Dark-A7C080?style=for-the-badge&logo=visual-studio-code&logoColor=white" alt="Theme" />
</div>

---

## Overview

I developed **SmartPantry** as a professional-grade domestic inventory management system engineered to eliminate food waste through automated intelligence. I built this high-performance monorepo using a robust **Spring Boot 4** backend and a reactive **Angular 19** frontend, harmonized by a sophisticated **Everforest Dark** aesthetic.

In this project, I implemented advanced full-stack patterns, including automated background status processing, a fortified security architecture, and a 100% adaptive mobile-first user experience.

---

## Key Features

### Automated Inventory Intelligence
*   **Precision Tracking**: I implemented real-time management of product quantities, categories, and expiration milestones.
*   **Unified Expiry Logic**: I standardized a 7-day threshold calculation used across both API responses and background processing.
*   **Automated Maintenance**: I created a daily background task (Cron) that recalibrates every product's status (Fresh, Alert, Expired) at midnight.

### Premium Responsive UX
*   **Everforest Glass UI**: I designed a professional dark theme utilizing glassmorphism, refined typography, and custom high-contrast SVG branding.
*   **100% Adaptive Architecture**: I ensured a seamless experience across small phones, tablets, and desktops with a dedicated mobile navigation system.
*   **SaaS-Grade Interactions**: I added optimistic UI updates, staggered animations, and robust global error handling with user-friendly feedback.

### Fortified Security Architecture
*   **Advanced Encryption**: I integrated secure **BCrypt** password hashing to ensure the cryptographic integrity of user credentials.
*   **Seamless Persistence**: I developed an automated **JWT Refresh Cycle** to keep sessions active without requiring re-authentication.
*   **Robust Security Chain**: I configured a centralized **Spring SecurityFilterChain** with rigorous CORS protection and stateless session management.

---

## Technical Architecture

### Backend (Java 21 / Spring Boot 4.0.1)
- **Manual DTO Mapping**: I chose a high-performance mapping pattern using the Builder pattern for maximum reliability and performance.
- **Scheduled Services**: I utilized Spring `@Scheduled` tasks for automated inventory health checks.
- **Security Context**: I implemented a thread-local UserContext for secure, isolated request handling.
- **Distroless Deployment**: I use Google Distroless images for a minimal, hardened production environment.

### Frontend (Angular 19 / TypeScript)
- **Signals-based State**: I employed cutting-edge reactive state management for predictable data flow and peak performance.
- **Smart/Dumb Component Pattern**: I followed a clean architectural separation to ensure high reusability and maintainability.
- **Pure CSS Transitions**: I optimized the UI with hardware-accelerated animations and zero runtime overhead.
- **Global Interceptors**: I centralized the handling of JWT injection, token refresh, and typed API errors.

---

## Quick Start

### Run with Docker (Recommended)
You can deploy the full enterprise stack (Frontend, Backend, and Database) with a single command:

```bash
docker compose up --build
```

| Service | Access URL |
| :--- | :--- |
| **Frontend UI** | [http://localhost:80](http://localhost:80) |
| **REST API** | [http://localhost:8080/api/v1](http://localhost:8080/api/v1) |
| **Swagger Docs** | [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html) |

---

## Development Workflow

I use Docker Compose to manage the entire development environment, ensuring seamless integration between the frontend, backend, and database.

### Core Commands
```bash
# Build and start all services in the background
docker compose up --build -d

# Stop all services and remove containers
docker compose down

# View live logs for all services
docker compose logs -f
```

### Database Management
The PostgreSQL database is persisted using a Docker volume. To reset the database state:
```bash
docker compose down -v
docker compose up --build -d
```

---

## Author
**Nacho Osella**  
*Full Stack Developer focused on high-performance, clean-code solutions.*

---

## AI Authorship Note
**Disclaimer**: This project showcases advanced AI-human collaboration. While I architected the system architecture, business logic, and security protocols, **100% of the CSS styles and a significant portion of the HTML templates were generated and optimized using AI** to achieve this professional aesthetic.

---
*For internal agent instructions and coding standards, see [AGENTS.md](./AGENTS.md).*
