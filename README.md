# Fleet Fuel Usage & Performance Monitoring System

A production-grade backend REST API built with **Java Spring Boot** and **MySQL** to monitor real-time fuel consumption and performance metrics across a fleet of vehicles.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Language | Java 17 |
| Framework | Spring Boot 3.2 |
| ORM | JPA / Hibernate |
| Database | MySQL 8 |
| Build Tool | Maven |
| API Testing | Postman |

---

## Features

- **Vehicle Management** — Register, update, soft-delete vehicles with status tracking (ACTIVE / INACTIVE / MAINTENANCE)
- **Fuel Log Entry** — Record fuel fill-ups with auto-calculated total cost and fuel efficiency (km/litre)
- **Performance Reporting** — Per-vehicle efficiency summary with total litres consumed and cost spent
- **Fleet Reports** — Date-range fleet-wide cost breakdown across all vehicles
- **Input Validation** — Jakarta Validation on all request fields with meaningful error messages
- **Exception Handling** — Global exception handler with structured JSON error responses

---

## Project Structure

```
src/
├── main/java/com/fleet/
│   ├── FleetFuelApplication.java       # Entry point
│   ├── controller/
│   │   ├── VehicleController.java      # Vehicle REST endpoints
│   │   └── FuelLogController.java      # Fuel log & reporting endpoints
│   ├── service/
│   │   ├── VehicleService.java         # Business logic for vehicles
│   │   └── FuelLogService.java         # Business logic for fuel logs
│   ├── repository/
│   │   ├── VehicleRepository.java      # JPA queries for vehicles
│   │   └── FuelLogRepository.java      # JPA & native SQL queries
│   ├── model/
│   │   ├── Vehicle.java                # Vehicle entity
│   │   └── FuelLog.java                # FuelLog entity
│   └── exception/
│       ├── ResourceNotFoundException.java
│       └── GlobalExceptionHandler.java
└── resources/
    └── application.properties
```

---

## API Endpoints

### Vehicles

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/vehicles` | Register a new vehicle |
| GET | `/api/vehicles` | Get all vehicles |
| GET | `/api/vehicles/{id}` | Get vehicle by ID |
| GET | `/api/vehicles/reg/{regNumber}` | Get vehicle by registration number |
| GET | `/api/vehicles/status/{status}` | Filter vehicles by status |
| PUT | `/api/vehicles/{id}` | Update vehicle details |
| DELETE | `/api/vehicles/{id}` | Soft delete (mark INACTIVE) |
| GET | `/api/vehicles/summary` | Fleet status count summary |

### Fuel Logs & Reports

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/vehicles/{id}/fuel-logs` | Add a fuel log entry |
| GET | `/api/vehicles/{id}/fuel-logs` | Get all logs for a vehicle |
| GET | `/api/fuel-logs/{id}` | Get a specific fuel log |
| GET | `/api/vehicles/{id}/performance` | Vehicle performance summary |
| GET | `/api/reports/fleet?from=&to=` | Fleet-wide cost report by date range |
| DELETE | `/api/fuel-logs/{id}` | Delete a fuel log |

---

## Setup & Run

### Prerequisites
- Java 17+
- MySQL 8+
- Maven 3.8+

### Steps

```bash
# 1. Clone the repository
git clone https://github.com/Mafthiyar/Fleet-Fuel-Management.git
cd Fleet-Fuel-Management

# 2. Set up the database
mysql -u root -p < schema.sql

# 3. Update DB credentials in application.properties
spring.datasource.username=your_username
spring.datasource.password=your_password

# 4. Build and run
mvn spring-boot:run
```

The server starts at `http://localhost:8080`

---

## Sample API Requests

**Register a vehicle:**
```json
POST /api/vehicles
{
  "registrationNumber": "AP31AB1234",
  "vehicleType": "TRUCK",
  "driverName": "Ravi Kumar",
  "tankCapacityLitres": 200.0,
  "status": "ACTIVE"
}
```

**Add a fuel log:**
```json
POST /api/vehicles/1/fuel-logs
{
  "fuelDate": "2025-03-01",
  "litresFilled": 150.0,
  "costPerLitre": 96.50,
  "odometerReading": 15000.0,
  "fuelStation": "HP Petrol Pump, Vijayawada"
}
```

**Get fleet report:**
```
GET /api/reports/fleet?from=2025-01-01&to=2025-03-31
```

---

## Architecture

Follows a strict **3-layer architecture**:

```
Controller (REST layer)
    ↓
Service (Business logic)
    ↓
Repository (Database / JPA)
```

This separation ensures testability, maintainability, and scalability.

---

## Author

**Shaik Mofthiyar**
[LinkedIn](https://www.linkedin.com/in/mofthiyar-shaik-44869b28b) | [GitHub](https://github.com/Mafthiyar)
