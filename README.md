# TransactAPI

A simple banking REST API built with Spring Boot.
## Tech Stack

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- H2 In-Memory Database
- Lombok
- Maven

## Running the Project

```bash
mvn spring-boot:run
```

Server runs on `http://localhost:8080`

## Endpoints

### Create Customer
POST `/api/v1/customers`

```json
{
  "name": "Jane Mwangi",
  "email": "jane@gmail.com",
  "phone": "0712345678",
  "initialBalance": 5000.00
}
```

### Fund Transfer
POST `/api/v1/transactions/transfer`

```json
{
  "senderAccountNumber": "COOP123456",
  "receiverAccountNumber": "COOP789012",
  "amount": 1000.00,
  "description": "Rent payment",
  "transferReference": "TXN-001"
}
```

### Get Account Balance
GET `/api/v1/customers/{id}/balance`

## Design Notes

- BigDecimal used for all monetary values — floating point precision is unacceptable in banking
- Transfers use pessimistic locking to prevent balance corruption under concurrent requests
- Idempotency keys on transfers — the same reference is rejected if submitted twice
- All transfers are wrapped in a single transaction — debit and credit are atomic
- All API responses follow a consistent structure with success, message, and data fields

## H2 Console

Available at `http://localhost:8080/h2-console` during development.


Written By:
Darlene Wendy