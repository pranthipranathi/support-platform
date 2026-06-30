# AI-Powered Multi-Tenant Customer Support SaaS Platform

An enterprise-grade backend platform for customer support, built with Spring Boot 3, PostgreSQL, JWT authentication, and Docker.

## Tech Stack

- **Language**: Java 21
- **Framework**: Spring Boot 3.5.9, Spring Security, Spring Data JPA
- **Database**: PostgreSQL 16
- **Migrations**: Flyway
- **Caching**: Redis
- **Authentication**: JWT (Access + Refresh tokens)
- **Documentation**: Swagger / OpenAPI 3.1
- **Build Tool**: Maven
- **Containerization**: Docker, Docker Compose

## Features Implemented

- JWT-based authentication with access and refresh tokens
- BCrypt password encryption
- Role-based security with Spring Security
- Full CRUD REST API for Support Tickets
- Customer management API
- Global exception handling with consistent API responses
- Database migrations via Flyway (auto-applied on startup)
- Live API documentation via Swagger UI
- Clean layered architecture (Controller, Service, Repository, Entity)
- DTO pattern (entities never exposed directly via API)

## Architecture

Controller Layer handles REST endpoints and request/response DTOs.
Service Layer contains business logic and transactions.
Repository Layer uses Spring Data JPA repositories.
Entity Layer maps JPA entities to PostgreSQL tables.

## Getting Started

### Prerequisites
- Java 21+
- Maven 3.9+
- Docker Desktop

### Setup

1. Start PostgreSQL and Redis:

docker compose up -d postgres redis

2. Run the application:

mvn spring-boot:run

3. Access Swagger UI:

http://localhost:8080/swagger-ui/index.html

## API Endpoints

### Authentication
- POST /api/v1/auth/login - Login with email and password
- POST /api/v1/auth/refresh-token - Refresh access token

### Tickets
- GET /api/v1/tickets - Get all tickets
- GET /api/v1/tickets/{id} - Get ticket by ID
- POST /api/v1/tickets - Create a new ticket
- PUT /api/v1/tickets/{id} - Update a ticket
- DELETE /api/v1/tickets/{id} - Delete a ticket
- GET /api/v1/tickets/customer/{customerId} - Get tickets by customer
- GET /api/v1/tickets/agent/{agentId} - Get tickets by agent

### Customers
- GET /api/v1/customers - Get all customers
- GET /api/v1/customers/{id} - Get customer by ID
- POST /api/v1/customers - Create a new customer
- DELETE /api/v1/customers/{id} - Delete a customer

## Authentication Flow

1. Call POST /api/v1/auth/login with email and password
2. Receive accessToken and refreshToken
3. Include Authorization: Bearer accessToken header on subsequent requests
4. When access token expires, call POST /api/v1/auth/refresh-token with the refresh token

## Project Structure

src/main/java/com/saas/support/
- auth - Authentication (login, JWT)
- customer - Customer management
- ticket - Ticket management
- user - User entity and service
- tenant - Multi-tenancy foundation
- config - Security, Swagger configuration
- common - Exceptions, API response wrapper
- util - JWT utility

## Roadmap

- Multi-tenant schema-per-tenant switching
- Support Agents module
- Comments on tickets
- File attachments
- Kafka event-driven notifications
- Redis caching layer
- AI-powered ticket summarization (OpenAI integration)
- Reporting and analytics dashboard
- Unit and integration tests