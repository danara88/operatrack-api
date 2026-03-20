# OperaTrack API

A RESTful API service that allows users to **record and track buy/sell operations** in the stock market.

---

## Project Overview

OperaTrack API is a SaaS backend service designed to give stock market investors a clear and organized history of their trading operations. Users can register the stocks they want to monitor, define the tax rates applied by their brokerage firms, and log each buy/sell transaction. The system then calculates capital gains, taxes, and net earnings automatically—giving investors a complete picture of their portfolio performance.

---

## Tech Stack

| Layer                 | Technology                               |
| --------------------- | ---------------------------------------- |
| Language              | Java 21                                  |
| Framework             | Spring Boot 4.0.x                        |
| Build Tool            | Maven                                    |
| Database              | H2 (in-memory)                           |
| ORM                   | Spring Data JPA / Hibernate              |
| Validation            | Spring Boot Validation (Bean Validation) |
| Boilerplate Reduction | Lombok                                   |
| Protocol              | HTTP (REST)                              |

---

## Software Architecture

The project follows the **MVC (Model-View-Controller)** pattern adapted for a RESTful API. The "View" layer is replaced by a set of controllers that serialize and deserialize JSON payloads through DTOs.

### Package Structure

```
com.operatrack.operatrack_api
├── controllers/         # REST endpoints — thin layer, delegates logic to services
│   ├── dtos/            # Request and response Data Transfer Objects
│   ├── exceptions/      # Controller-level exceptions (e.g., ResourceNotFoundException)
│   ├── handlers/        # Global exception handler (@RestControllerAdvice)
│   └── responses/       # Standardized response wrappers (ApiResponse, PageResponse, ErrorInfo)
├── database/            # Data access layer
│   ├── entities/        # JPA entities (database table mappings)
│   ├── mappers/         # Mappers between domain models and JPA entities
│   └── repositories/    # Repository interfaces (domain-facing data access contracts)
├── model/               # Business domain entities (Operation, Stock, BrokerageFirm)
│   └── exceptions/      # Domain invariant exceptions
└── services/            # Business logic — keeps controllers thin
```

### Key Design Decisions

- **API Versioning** — Routes are namespaced under `/api/v1/[resource]/` to allow non-breaking evolution of the API.
- **DTOs** — All request and response payloads use dedicated DTO records (e.g., `CreateStockRequestDTO`, `GetBrokerageFirmResponseDTO`) to decouple internal domain models from the API contract.
- **Layered Data Access** — JPA entities and mappers are isolated in the `database/` package, keeping the domain model free of persistence concerns. Services work with domain objects; the database layer handles the translation.
- **Pagination** — List endpoints return paginated results wrapped in `PageResponse<T>`, supporting `page` and `size` query parameters.
- **Constructor Injection** — Dependencies are injected through constructors rather than fields, keeping components easy to test and reason about.
- **Thin Controllers** — Controllers validate input and delegate all business logic to service classes, following a clean separation of concerns.

---

## REST API

All endpoints consume and produce `application/json`. Successful responses are wrapped in `ApiResponse<T>`; errors are wrapped in `ErrorInfo`.

### Stocks — `/api/v1/stocks`

| Method   | Path                        | Description                                             | Success Status   |
| -------- | --------------------------- | ------------------------------------------------------- | ---------------- |
| `GET`    | `/api/v1/stocks`            | List all stocks (paginated). Supports `?page=0&size=10` | `200 OK`         |
| `POST`   | `/api/v1/stocks`            | Register a new stock                                    | `201 Created`    |
| `PATCH`  | `/api/v1/stocks/{id}/price` | Update the current price of a stock                     | `200 OK`         |
| `DELETE` | `/api/v1/stocks/{id}`       | Delete a stock by ID                                    | `204 No Content` |

### Brokerage Firms — `/api/v1/brokerage-firms`

| Method   | Path                           | Description                                                      | Success Status   |
| -------- | ------------------------------ | ---------------------------------------------------------------- | ---------------- |
| `GET`    | `/api/v1/brokerage-firms`      | List all brokerage firms (paginated). Supports `?page=0&size=10` | `200 OK`         |
| `POST`   | `/api/v1/brokerage-firms`      | Register a new brokerage firm                                    | `201 Created`    |
| `PUT`    | `/api/v1/brokerage-firms/{id}` | Update a brokerage firm's name and tax rate                      | `200 OK`         |
| `DELETE` | `/api/v1/brokerage-firms/{id}` | Delete a brokerage firm by ID                                    | `204 No Content` |

### Response Schemas

**`ApiResponse<T>`** — wraps all successful responses:

```json
{
  "data": { ... },
  "status": 200,
  "success": true
}
```

**`PageResponse<T>`** — returned as `data` for paginated list endpoints:

```json
{
  "content": [ ... ],
  "page": 0,
  "size": 10,
  "totalElements": 42,
  "totalPages": 5,
  "hasNext": true,
  "hasPrevious": false
}
```

**`ErrorInfo`** — wraps all error responses:

```json
{
  "message": "Stock with ticker symbol 'AAPL' already exists",
  "exception": "DuplicatedResourceException",
  "path": "/api/v1/stocks",
  "status": 409,
  "success": false,
  "errors": {}
}
```

---

## Domain Entities

The three core entities model the Ubiquitous Language of the platform.

### Operation

The heart of the system. An `Operation` represents a complete stock trade—from purchase to sale—and captures every financial detail needed to evaluate its outcome.

| Field             | Type      | Description                                                    |
| ----------------- | --------- | -------------------------------------------------------------- |
| `id`              | `String`  | Unique identifier (auto-generated UUID)                        |
| `shareQuantity`   | `Integer` | Number of shares involved                                      |
| `purchasePrice`   | `Double`  | Price per share at the time of purchase                        |
| `totalValue`      | `Double`  | Total monetary value (shares × price)                          |
| `capitalGain`     | `Double`  | Sale proceeds minus purchase cost                              |
| `purchaseTax`     | `Double`  | Tax applied at the time of purchase                            |
| `saleTax`         | `Double`  | Tax applied at the time of sale                                |
| `totalTax`        | `Double`  | Sum of purchase tax and sale tax                               |
| `netEarnings`     | `Double`  | Capital gain minus total taxes                                 |
| `purchaseDate`    | `Instant` | Timestamp of the purchase                                      |
| `saleDate`        | `Instant` | Timestamp of the sale (`null` if still open)                   |
| `stockId`         | `String`  | The identifier of the stock traded in this operation           |
| `brokerageFirmId` | `String`  | The identifier of the brokerage firm applied to this operation |

---

### Stock

A `Stock` represents a company that the user wants to monitor. It stores the ticker symbol and current market price, which are the foundation for calculating gains, losses, and position values.

| Field          | Type     | Description                                                  |
| -------------- | -------- | ------------------------------------------------------------ |
| `id`           | `String` | Unique identifier (auto-generated UUID)                      |
| `name`         | `String` | Full company name (4–20 characters)                          |
| `tickerSymbol` | `String` | Exchange ticker symbol, e.g. `AAPL`, `MSFT` (1–5 characters) |
| `currentPrice` | `Double` | Current market price per share (≥ 0)                         |

---

### BrokerageFirm

A `BrokerageFirm` models the commission structure of a specific financial institution. Because different brokers apply different rates, separating `BrokerageFirm` as its own entity lets users define a rate once and reuse it across as many operations as they like.

| Field             | Type     | Description                                                         |
| ----------------- | -------- | ------------------------------------------------------------------- |
| `id`              | `String` | Unique identifier (auto-generated UUID)                             |
| `institutionName` | `String` | Name of the brokerage or financial institution (min. 4 characters)  |
| `taxRate`         | `Double` | Decimal tax rate, e.g. `0.0035` for 0.35% (must be between 0 and 1) |

#### Tax Calculation Formulas

Taxes include a **16% VAT** applied on top of the broker commission.

```
Purchase Tax  = [(shareQuantity × purchasePrice) × taxRate] × 1.16
Sale Tax      = [(shareQuantity × currentPrice)  × taxRate] × 1.16
Total Tax     = purchaseTax + saleTax
Net Earnings  = capitalGain - totalTax
```

---

## Business Invariants

The following table lists all custom exceptions enforced by the domain model. Each exception is thrown when a business rule is violated during entity construction or mutation.

| Exception Name                      | Domain Entity | Explanation                                                                                                                                  |
| ----------------------------------- | ------------- | -------------------------------------------------------------------------------------------------------------------------------------------- |
| `InvalidShareQuantityException`     | Operation     | Share quantity must be zero or a positive number. Thrown when the number of shares is `null` or negative.                                    |
| `InvalidPurchasePriceException`     | Operation     | Purchase price must be zero or a positive number. Thrown when the price per share at purchase time is `null` or negative.                    |
| `InvalidTotalValueException`        | Operation     | Total value must be zero or a positive number. Thrown when the total monetary value of the operation (shares × price) is `null` or negative. |
| `InvalidTickerSymbolException`      | Stock         | Ticker symbol must be between 1 and 5 characters. Thrown when the ticker symbol is `null`, empty, or exceeds 5 characters.                   |
| `InvalidStockNameException`         | Stock         | Stock name must be between 4 and 20 characters. Thrown when the company name is `null` or outside the 4–20 character range.                  |
| `InvalidCurrentPriceException`      | Stock         | Current price must be zero or a positive number. Thrown when the current market price per share is `null` or negative.                       |
| `InvalidInstitutionNameException`   | BrokerageFirm | Institution name must be at least 4 characters long. Thrown when the brokerage or institution name is `null` or fewer than 4 characters.     |
| `InvalidBrokerageFirmRateException` | BrokerageFirm | Tax rate must be between 0 and 1. Thrown when the tax rate is `null` or outside the valid range (e.g., `-0.5` or `1.5` are invalid).         |
