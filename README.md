# 🍃 SmartPantry: Domestic Inventory Intelligence

<div align="center">
  <img src="https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 21" />
  <img src="https://img.shields.io/badge/Spring_Boot-4.0.1-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white" alt="Spring Boot" />
  <img src="https://img.shields.io/badge/Angular-19-DD0031?style=for-the-badge&logo=angular&logoColor=white" alt="Angular 19" />
  <img src="https://img.shields.io/badge/PostgreSQL-17-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL" />
  <img src="https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white" alt="Docker" />
  <img src="https://img.shields.io/badge/Everforest-Dark-A7C080?style=for-the-badge&logo=visual-studio-code&logoColor=white" alt="Theme" />
</div>

---

## 📖 Overview

**SmartPantry** is a full-stack, enterprise-grade domestic inventory management system designed to eliminate food waste and optimize household efficiency. Developed as a high-performance monorepo, it combines a robust **Spring Boot** backend with a cutting-edge **Angular 19** frontend, all wrapped in a sophisticated **Everforest Dark** aesthetic.

This project showcases professional software engineering practices, including secure containerization, reactive state management, and refined UX design.

## ✨ Key Features

### 🛒 Precision Inventory Tracking
*   **Real-time Stock Management**: Add, update, and track products with precision.
*   **Intelligent Expiry Alerts**: Visual indicators (Fresh, Alert, Expired) based on dynamic date calculations.
*   **Category Organization**: Custom category management with inline creation for seamless workflows.

### 🎨 Premium User Experience
*   **Everforest Glass UI**: A professional dark theme featuring glassmorphism, refined typography, and Material Icons.
*   **SaaS-Grade Interactions**: Staggered row animations, real-time search filtering, and floating center modals.
*   **Responsive Architecture**: Designed for clarity and usability across all desktop environments.

### 🛡️ Secure & Scalable
*   **JWT Authentication**: Secure login and registration flow with automated token management.
*   **Protected Routing**: Guarded dashboard access and backend ownership verification.
*   **Persistence**: PostgreSQL 17 integration with Docker volumes for durable data management.

## 🛠️ Technical Architecture

### Backend (Java 21 / Spring Boot 4)
- **Manual DTO Mapping**: High-performance mapping without the overhead of heavy reflection libraries.
- **Global Error Handling**: Centralized exception management for consistent API responses.
- **Distroless Containerization**: Utilizing Google Distroless images to reduce the attack surface and image size.

### Frontend (Angular 19 / TypeScript)
- **Signals-based State**: Reactive state management for high performance and clean data flow.
- **Smart/Dumb Component Pattern**: Separation of concerns for maximum maintainability.
- **Pure CSS Layouts**: Optimized styling with zero-runtime CSS overhead.

---

## 🚀 Quick Start

### 🐳 Run with Docker (Recommended)
Deploy the entire stack including the database with a single command:

```bash
docker compose up --build
```

| Service | Access URL |
| :--- | :--- |
| **Frontend UI** | [http://localhost:80](http://localhost:80) |
| **REST API** | [http://localhost:8080](http://localhost:8080) |
| **Database** | `localhost:5432` |

---

## 💻 Development Workflow

### Backend Setup
```bash
cd backend
./mvnw clean compile
./mvnw spring-boot:run
```

### Frontend Setup
```bash
cd frontend
npm install
npm start
```

---

## 👨‍💻 Author
**Nacho Osella**  
*Full Stack Developer focused on high-performance, clean-code solutions.*

---
*For internal agent instructions and coding standards, see [AGENTS.md](./AGENTS.md).*
