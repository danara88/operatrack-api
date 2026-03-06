# OperaTrack API

A RESTful API service that allows users to **record and track buy/sell operations** in the stock market.

---

## Project Overview

OperaTrack API is a SaaS backend service designed to give stock market investors a clear and organized history of their trading operations. Users can register the stocks they want to monitor, define the tax rates applied by their brokerage firms, and log each buy/sell transaction. The system then calculates capital gains, taxes, and net earnings automatically—giving investors a complete picture of their portfolio performance.

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 4.0.x |
| Build Tool | Maven |
| Validation | Spring Boot Validation (Bean Validation) |
| Boilerplate Reduction | Lombok |
| Protocol | HTTP (REST) |

---

## Software Architecture

The project follows the **MVC (Model-View-Controller)** pattern adapted for a RESTful API. The "View" layer is replaced by a set of controllers that serialize and deserialize JSON payloads through DTOs.

### Package Structure

```
com.operatrack.operatrack_api
├── controllers/         # REST endpoints — thin layer, delegates logic to services
│   ├── v1/              # API version 1 controllers
│   ├── dtos/            # Request and response Data Transfer Objects
│   └── handlers/        # Global exception handlers for API errors
├── model/               # Business domain entities (Operation, Stock, Tax)
├── services/            # Business logic — keeps controllers thin
│   └── exceptions/      # Custom exceptions thrown by the service layer
└── repositories/        # Data access layer (JPA repositories)
```

### Key Design Decisions

- **API Versioning** — Routes are namespaced under `/api/v1/[resource]/` and controllers are organized into version packages (`v1/`, `v2/`, …) to allow non-breaking evolution of the API.
- **DTOs** — All request and response payloads use dedicated DTO records (e.g., `CreateOperationRequestDTO`, `GetStockResponseDTO`) to decouple internal domain models from the API contract.
- **Constructor Injection** — Dependencies are injected through constructors rather than fields, keeping components easy to test and reason about.
- **Thin Controllers** — Controllers validate input and delegate all business logic to service classes, following a clean separation of concerns.

---

## Domain Entities

The three core entities model the Ubiquitous Language of the platform.

### Operation

The heart of the system. An `Operation` represents a complete stock trade—from purchase to sale—and captures every financial detail needed to evaluate its outcome.

| Field | Type | Description |
|---|---|---|
| `id` | `String` | Unique identifier (auto-generated UUID) |
| `shareQuantity` | `Integer` | Number of shares involved |
| `purchasePrice` | `Double` | Price per share at the time of purchase |
| `totalValue` | `Double` | Total monetary value (shares × price) |
| `capitalGain` | `Double` | Sale proceeds minus purchase cost |
| `purchaseTax` | `Double` | Tax applied at the time of purchase |
| `saleTax` | `Double` | Tax applied at the time of sale |
| `totalTax` | `Double` | Sum of purchase tax and sale tax |
| `netEarnings` | `Double` | Capital gain minus total taxes |
| `purchaseDate` | `Instant` | Timestamp of the purchase |
| `saleDate` | `Instant` | Timestamp of the sale (`null` if still open) |
| `stock` | `Stock` | The stock traded in this operation |
| `tax` | `Tax` | The tax entity applied to this operation |

---

### Stock

A `Stock` represents a company that the user wants to monitor. It stores the ticker symbol and current market price, which are the foundation for calculating gains, losses, and position values.

| Field | Type | Description |
|---|---|---|
| `id` | `String` | Unique identifier (auto-generated UUID) |
| `name` | `String` | Full company name |
| `tickerSymbol` | `String` | Exchange ticker symbol (e.g., `AAPL`, `MSFT`) |
| `currentPrice` | `Double` | Current market price per share |
| `operations` | `List<Operation>` | All operations associated with this stock |

---

### Tax

A `Tax` entity models the commission or fee structure of a specific brokerage firm or financial institution. Because different brokers apply different rates, separating `Tax` as its own entity lets users define a rate once and reuse it across as many operations as they like.

| Field | Type | Description |
|---|---|---|
| `id` | `String` | Unique identifier (auto-generated UUID) |
| `institutionName` | `String` | Name of the brokerage or financial institution (min. 4 characters) |
| `taxRate` | `Double` | Decimal tax rate, e.g. `0.0035` for 0.35% (must be between 0 and 1) |
| `operations` | `List<Operation>` | All operations that use this tax |

#### Tax Calculation Formulas

Taxes include a **16% VAT** applied on top of the broker commission.

```
Purchase Tax  = -[(shareQuantity × purchasePrice) × taxRate] × 1.16
Sale Tax      = -[(shareQuantity × currentPrice)  × taxRate] × 1.16
Total Tax     = purchaseTax + saleTax
Net Earnings  = capitalGain + totalTax
```

> Taxes are expressed as **negative values** because they represent a cost deducted from earnings.
