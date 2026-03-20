# GitHib Copilot Instructions for `opentrack-api`

## Project Overview

- SaaS platform where users can **record and track buy/sell operations** in the stock market.
- **Java 21** workspace with **Spring Boot 4.0.x** and **Maven** build system.
- **MVC architecture** adapted for a RESTful API.
- **JPA** for data persistence with **PostgreSQL** as the database.

## Package Structure

Follow project standard hierarchy:

```
opentrack-api
├── src/
│   └── main/
│       └── java/com/operatrack/operatrack_api/
│           ├── controllers/        # REST endpoints (v1/, v2/ versioning)
│           │   └── exceptions/     # Custom exceptions for controller layer
│           │   └── dtos/           # Data Transfer Objects for API requests and responses
│           │   └── handlers/       # Exception handlers for API errors
│           │   └── responses/      # Response objects to standardize API endpoints responses
│           ├── database/           # Data access layer (e.g., JPA repositories)
│           │   └── entities/       # Database entities (Database tables)
│           │   └── mappers/        # Database entity mappers (from domain entity to database entity and vice versa)
│           │   └── repositories/   # Database repositories (Data access layer)
│           ├── model/              # Business domain entities (Domain layer)
│           │   └── exceptions/     # Custom exceptions for domain logic
│           ├── services/           # Business logic services
```

## Domain Language

Follow the Ubiquitous Language approach to define the core entities of the system:

- **Operation:** It is the core of the system. The main goal of the SaaS is to build a stock market operation history. Without it, there is no product.
- **Stock:** The user needs to register the companies they want to monitor. Stock encapsulates the Ticker Symbol and the Current Price, which is the foundation for calculating gains, losses, and the total value of the position.
- **BrokerageFirm:** Each brokerage firm or financial institution may apply a different tax rate. By separating BrokerageFirm as an independent entity, the user can define a specific tax percentage per brokerage firm and reuse it across multiple operations. This makes the system flexible and reusable. (e.g., Scotiabank has a tax rate of 0.35%, while Banorte has a tax rate of 0.25%)

## Controllers

- Support API versioning in separate package (v2/, v3)
- API routes follow REST conventions: /api/v1/[resource]/
- The protocol for API communication is HTTP.
- Use DTOs for request and response payloads to decouple internal data models from API contracts.
- DTOs naming should follow the convention: [Action][Resource]RequestDTO (e.g., CreateOperationRequestDTO, UpdateStockRequestDTO) for requests and [Action][Resource]ResponseDTO (e.g., CreateOperationResponseDTO, GetStockResponseDTO) for responses.
- Include validation annotations in DTOs to ensure data integrity (e.g., `@NotNull`, `@Size`, `@Positive`, `@Valid`).
- Use `record` for simple DTOs to reduce boilerplate code and improve readability.
- For api endpoints success response always use the class `ApiResponse<T>` and for error response use `ErrorInfo` to maintain consistency across the API.

## Coding Conventions

- Use Lombok for Java to reduce boilerplate code in models and services.
- Use streams and lambda expressions for collection processing in Java.
- Git branch name conventions: `feature/[feature-name]`, `fix/[bug-description]`, `refactor/[refactor-description]`
- If branch is created by Copilot, use the following format: `copilot/feature/[feature-name]`, `copilot/fix/[bug-description]`, `copilot/refactor/[refactor-description]`
- Git commit message conventions: `[type]: [short description]` (e.g., feat: add user authentication, fix: resolve null pointer exception)
- Use DTOs for requests and responses in the backend to decouple the internal data models from the API contracts.

## Java Best Practices

- Constructor injection over field injection
- Use meaningful variable and method names over comments.

## Coding Best Practices

- Keep controllers thin, logic in service layers.

## What to Avoid

- Avoid exposing secret keys or sensitive information in the codebase. Use environment variables or secure vaults for such data.
- Modify Maven dependencies without explicit request.
- Modify build scripts or CI/CD configurations.

When generating suggestions, prioritize opentrack-api specific patterns and maintain consistency across the workspace.
