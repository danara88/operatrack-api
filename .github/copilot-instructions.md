# Project Context

## What is this?

SaaS platform where users can record and track buy/sell operations in the stock market.

## Tech Stack

- Frontend: Angular, TypeScript, HTML, CSS
- Backend: Java, Spring Boot, RESTful APIs
- Database: MongoDB

## Domain Language

- Operation: It is the core of the system. The main goal of the SaaS is to build a stock market operation history. Without it, there is no product.
- Stock: The user needs to register the companies they want to monitor. Stock encapsulates the Ticker Symbol and the Current Price, which is the foundation for calculating gains, losses, and the total value of the position.
- Tax: Each brokerage firm or financial institution may apply a different tax rate. By separating Tax as an independent entity, the user can define a specific tax percentage per brokerage firm and reuse it across multiple operations. This makes the system flexible and reusable. (e.g., Scotiabank has a tax rate of 0.35%, while Banorte has a tax rate of 0.25%)

## Architectural Patterns

- Use MVC (Model-View-Controller) adapted for a RESTful API with the following directories: model, services, contrrollers, repositories.
- Use dependency injection for services and repositories in the backend to promote loose coupling and easier testing.
- API routes follow REST conventions: /api/v1/[resource]/
- The protocol for API communication is HTTP.

## Coding Conventions

- Use Lombok for Java to reduce boilerplate code in models and services.
- Use streams and lambda expressions for collection processing in Java.
- Git branch name conventions: feature/[feature-name], fix/[bug-description], refactor/[refactor-description]
- Use DTOs for requests and responses in the backend to decouple the internal data models from the API contracts.

## What to Avoid

- Avoid exposing secret keys or sensitive information in the codebase. Use environment variables or secure vaults for such data.
