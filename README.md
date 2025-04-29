# Customer Rewards API

This is a Spring Boot-based RESTful API that calculates customer reward points based on their purchase transactions. It's a simple proof-of-concept project that uses an in-memory H2 database.

---

## What It Does

- Stores customer transaction data (amount + date)
- Calculates reward points based on a simple rule:
  - No points for ₹50 or less
  - 1 point per ₹ over ₹50
  - 2 points per ₹ over ₹100
- Shows monthly and total reward points per customer
- Built-in exception handling and circuit breaker (Resilience4J)

---

## Project Structure

```
rewards/
├── controller/
│   └── RewardController.java             --> REST endpoints for transactions and rewards
├── service/
│   ├── RewardService.java                --> Interface for reward point calculations
│   └── RewardServiceImpl.java            --> Implements business logic for reward calculations
├── aspect/
│   └── RewardFallbackAspect.java         --> AOP-based fallback for reward calculation failures
├── repository/
│   └── TransactionRepository.java        --> JPA Repository for accessing transaction data
├── model/
│   └── Transaction.java                  --> Entity class mapped to transactions table
├── dto/
│   └── RewardResponse.java               --> DTO to represent API response with reward points
├── exception/
│   ├── CustomerNotFoundException.java    --> Custom exception when customer is not found
│   └── GlobalExceptionHandler.java       --> Handles exceptions and returns appropriate HTTP responses
├── RewardsApplication.java               --> Main Spring Boot class (entry point)
├── application.properties                --> H2 DB config, port settings, and JPA settings
└── test/
    ├── controller/
    │   └── RewardControllerTest.java     --> Unit tests for controller endpoints
    └── service/
        └── RewardServiceTests.java       --> Unit tests for reward calculation logic

```

## Method Descriptions (Key Files)

### `RewardController.java`
- `@PostMapping("/api/transactions")`
  - `addTransaction()` → Adds a new transaction to the database
- `@GetMapping("/api/transactions")`
  - `getAllTransactions()` → Fetches all transactions
- `@GetMapping("/api/rewards/{customerId}")`
  - `getCustomerRewards()` → Calculates and returns reward points per month & total

### `RewardServiceImpl.java`
- `calculateRewards(Long customerId)` →
  - Looks up customer transactions
  - Calculates rewards per transaction and groups them by month
- `calculatePoints(Double amount)` →
  - Points logic based on tiers above ₹50 and ₹100

### `GlobalExceptionHandler.java`
- Catches and returns meaningful error messages
- Covers exceptions like missing customers or invalid input

### `Transaction.java`
- Entity mapped to `transactions` table
- Fields: `id`, `customerId`, `amount`, `date`

### `RewardServiceTests.java`
- Unit test for rewards logic & exception flow
- Checks valid data and custom error responses

---

## How to Run

1. **Clone the repo:**
   ```bash
   git clone https://github.com/Starlord2197/CustomerRewardApp.git
   cd CustomerRewardApp
   ```

2. **Run using Maven:**
   ```bash
   ./mvnw spring-boot:run
   ```

3. **Access H2 Console:**  
   [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
   - JDBC URL: `jdbc:h2:mem:rewardsdb`
   - Username: `sa`  
   - Password: *(leave blank)*

---

## API Endpoints

| Method | Endpoint                          | Description                         |
|--------|-----------------------------------|-------------------------------------|
| POST   | `/api/transactions`              | Add a customer transaction          |
| GET    | `/api/transactions`              | Get all transactions                |
| GET    | `/api/rewards/{customerId}`      | Get reward summary for a customer   |

---

## Running Tests

Run all unit tests with:

```bash
mvn test
```

Includes:
- Valid reward logic
- Missing customer fallback
- Controller response tests
- Negative scenarios

---

## Tech Stack

- Java 17
- Spring Boot 3.4.4
- Spring Web, Spring Data JPA
- H2 Database
- JUnit 5, Mockito
- AOP (Spring) for fallback
- Maven

---

## Sample JSON Payload

```json
POST /api/transactions

{
  "customerId": 1,
  "amount": 120.0,
  "date": "2024-01-15"
}
```

---

## Example Reward Output

```json
GET /api/rewards/1

{
  "customerId": 1,
  "monthlyPoints": {
    "2024-01": 90,
    "2024-02": 30
  },
  "totalPoints": 120
}
```

---

## Reference URLs (Port: 8080)

- **Add Transaction (POST):** `http://localhost:8080/api/transactions`
- **View All Transactions (GET):** `http://localhost:8080/api/transactions`
- **Get Rewards by Customer (GET):** `http://localhost:8080/api/rewards/{customerId}`
- **H2 Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)

---

## 👨‍💻 Author

- GitHub: [Starlord2197](https://github.com/Starlord2197)
- Repo: [CustomerRewardApp](https://github.com/Starlord2197/CustomerRewardApp)