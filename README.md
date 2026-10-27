# Hotel Reservation Management API

A RESTful backend for managing hotels, rooms, guests and room reservations, built with **Spring Boot 4.1.0** and **Java 21**. It started as an in-class mini assignment for the LHIND TIA Java Internship and was extended with **Spring Security + JWT authentication**, **Log4j2 logging** and **Swagger / OpenAPI documentation**.

---

## Features

- Full CRUD for hotels, rooms, guests and guest profiles
- Room reservations with server-side business rules:
  - date validation (check-in must be before check-out)
  - room capacity check
  - room status check (only available rooms can be booked)
  - reservation overlap check for the same room
  - total price calculated on the server
- Stateless authentication with **JWT** (jjwt 0.12.6)
- Role-based access control with two roles: `ADMIN` and `USER`
- Request validation with Jakarta Validation
- Global exception handling with consistent error responses
- Interactive API docs with Swagger UI, including JWT bearer authorization
- Logging with Log4j2 (replaces the default Logback)

---

## Tech Stack

| Area | Technology |
| --- | --- |
| Language | Java 21 |
| Framework | Spring Boot 4.1.0 (Spring Web, Spring Data JPA, Spring Security) |
| Database | MySQL (`hotel_db`) |
| Authentication | JWT (jjwt 0.12.6) |
| Validation | Jakarta Validation |
| API docs | springdoc-openapi 3.0.2 (Swagger UI) |
| Logging | Log4j2 |
| Boilerplate | Lombok 1.18.42 |
| Build tool | Maven (Maven Wrapper included) |

---

## Domain Model

| Entity | Description |
| --- | --- |
| `Hotel` | A hotel that owns a set of rooms |
| `Room` | A bookable room with type, capacity, price and status |
| `Guest` | A person who can make reservations |
| `GuestProfile` | Additional details linked one-to-one to a guest |
| `Reservation` | A booking of a room by a guest for a date range |
| `User` | Application account used for authentication (separate from `Guest`) |

Enums: `RoomType`, `RoomStatus`, `ReservationStatus`, and the security roles `ADMIN` / `USER`.

---

## Architecture

The project follows a layered architecture with constructor injection:

```
src/main/java/de/lhind/internship/mini/project/
├── Application.java      # Main entry point
├── controller/           # REST controllers
├── service/              # Business logic
├── repository/           # Spring Data JPA repositories
├── entity/               # JPA entities and enums
├── dto/                  # Request / response objects
├── security/ & config/   # Spring Security, JWT, OpenAPI configuration
└── exception/            # Global exception handling
```

---

## Getting Started

### Prerequisites

- JDK 21 or newer
- MySQL 8+
- Maven (or use the included `mvnw` / `mvnw.cmd`)

### 1. Clone the repository

```bash
git clone https://github.com/islicara/Hotel-Reservation-Management-API.git
cd Hotel-Reservation-Management-API
```

### 2. Create the database

```sql
CREATE DATABASE hotel_db;
```

### 3. Configure the application

Edit `src/main/resources/application.properties` with your own values:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/hotel_db
spring.datasource.username=YOUR_DB_USER
spring.datasource.password=YOUR_DB_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# JWT
jwt.secret=YOUR_LONG_RANDOM_SECRET
jwt.expiration=3600000
```

> Never commit real passwords or secrets. Use environment variables or a local, git-ignored properties file.

### 4. Build and run

```bash
# Linux / macOS
./mvnw spring-boot:run

# Windows
mvnw.cmd spring-boot:run
```

The API starts on `http://localhost:8080`.

### 5. Run the tests

```bash
./mvnw test
```

---

## API Documentation

Swagger UI is available once the app is running:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

To call protected endpoints from Swagger UI, log in first, copy the returned token, click **Authorize** and paste the token.

---

## Authentication

1. Register a user and log in through the authentication endpoints to receive a JWT.
2. Send the token on every protected request:

```
Authorization: Bearer <your-token>
```

| Role | Access |
| --- | --- |
| `ADMIN` | Manage hotels, rooms, guests and all reservations |
| `USER` | Browse hotels and rooms, create and view own reservations |

---

## Main Endpoints

| Resource | Base path | Operations |
| --- | --- | --- |
| Auth | `/api/auth` | register, login |
| Hotels | `/api/hotels` | create, read, update, delete |
| Rooms | `/api/rooms` | create, read, update, delete |
| Guests | `/api/guests` | create, read, update, delete |
| Reservations | `/api/reservations` | create, read, update status, cancel |

See Swagger UI for the full list of endpoints, request bodies and responses.

---

## Business Rules

- Check-in date must be before check-out date.
- The number of guests cannot exceed the room capacity.
- Only rooms with an available status can be reserved.
- A room cannot have two overlapping reservations.
- The total price is calculated by the server from the room price and the number of nights, never taken from the client.

---

## Error Handling

All errors are handled centrally and returned in a consistent JSON format with an appropriate HTTP status (`400` validation errors, `401` / `403` authentication and authorization errors, `404` not found, `409` conflicts such as overlapping reservations).

---

## Author

**Isli** - Software Developer, Tirana, Albania
GitHub: [@islicara](https://github.com/islicara)
Linkedln: [Isli Cara](https://www.linkedin.com/in/isli-cara/)
