# SmartPantry 🍎🥫

SmartPantry is a modern Spring Boot application designed to help users efficiently manage their home pantry. It tracks product expiration dates, categorizes items, and provides a foundation for a proactive notification system to reduce food waste.

## 🚀 Features

- **Pantry Management:** Full CRUD operations for products in your pantry.
- **Smart Expiration Tracking:** Automatic status calculation (RED, YELLOW, GREEN) based on expiration dates.
- **Category System:** Organize products by types (Dairy, Grains, Canned Goods, etc.).
- **Secure Authentication:** User registration and login system.
- **Resource Ownership:** Strict security logic ensuring users only access their own data.

## 🛠️ Tech Stack

### Backend
- **Language:** Java 21
- **Framework:** Spring Boot 4.0.1
- **Build System:** Maven
- **Persistence:** Spring Data JPA
- **Database:** 
  - **Dev:** H2 (In-memory)
  - **Prod:** PostgreSQL
- **Mapping:** ModelMapper 3.2.0
- **Boilerplate:** Project Lombok

### Frontend (Upcoming)
- **Framework:** Angular 19+
- **Styling:** Tailwind CSS / Angular Material
- **State Management:** RxJS / Signals

## 📁 Project Structure

The backend follows a standard layered architecture:

```text
src/main/java/SmartPantry/demo/
├── configs/      # Bean configurations (ModelMapper, etc.)
├── controllers/  # REST API Endpoints
├── dtos/         # Data Transfer Objects (Requests/Responses)
├── entities/     # JPA Entities and Enums
├── exceptions/   # Global Error Handling
├── repositories/ # Data Access Layer
└── services/     # Business Logic (Interface-based)
```

## ⚙️ Installation & Setup

### Prerequisites
- JDK 21
- Maven 3.9+

### Commands
1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd SmartPantry
   ```

2. **Compile and build:**
   ```bash
   ./mvnw clean install
   ```

3. **Run the application:**
   ```bash
   ./mvnw spring-boot:run
   ```
   *The server will start at `http://localhost:8080`*

## 🛣️ API Roadmap

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/register` | Create a new user account |
| POST | `/api/auth/login` | Authenticate and get token |
| GET | `/api/products` | List all products for current user |
| POST | `/api/products` | Add a new product to pantry |
| GET | `/api/products/status/{status}` | Filter by expiry status (RED/YELLOW/GREEN) |
| GET | `/api/categories` | List all available categories |

## 🔮 Future Roadmap

- **Angular Frontend:** Implementation of a responsive SPA to manage the pantry from any device.
- **Push Notifications:** Alert users when products are about to expire.
- **Recipe Suggestions:** Integration with external APIs to suggest recipes based on available pantry items.
- **Barcode Scanner:** Mobile integration for quick product entry.

---
*Developed as part of a modern software engineering practice.*
