# SmartPantry Monorepo

SmartPantry is a premium, high-performance solution designed for domestic inventory management and food waste reduction. This repository is organized as a monorepo featuring a Java Spring Boot backend and an Angular 19 frontend with a sophisticated Everforest Dark aesthetic.

## 🚀 Quick Start with Docker

The easiest way to run the entire stack (Frontend, Backend, and Database) is using Docker Compose.

### Prerequisites
- [Docker](https://www.docker.com/get-started)
- [Docker Compose](https://docs.docker.com/compose/install/)

### Execution
From the root directory, run:
```bash
docker compose up --build
```

The services will be available at:
- **Frontend**: [http://localhost:80](http://localhost:80)
- **Backend API**: [http://localhost:8080](http://localhost:8080)
- **Database**: PostgreSQL on port `5432`

---

## 🛠 Project Structure

- `backend/`: Java 21 RESTful API using **Spring Boot 4.0.1**.
  - Optimized with Google Distroless for security and minimal image size.
- `frontend/`: Single Page Application (SPA) using **Angular 19+**.
  - Premium **Everforest Dark** UI with Glassmorphism and Material Icons.
  - Real-time search, staggered animations, and skeleton loading.

---

## 💻 Manual Development

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

---

## 🛡 Security & Design
- **Authentication**: JWT-based security with route protection.
- **Persistence**: PostgreSQL 17 with Docker volumes for data durability.
- **UI/UX**: Smart/Dumb component architecture, responsive layout, and custom notification system.

For more details on coding standards, please refer to [AGENTS.md](./AGENTS.md).
