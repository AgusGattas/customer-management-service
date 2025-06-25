# Customer Management Service - Backend Challenge

## Project Overview

This microservice provides a comprehensive REST API for customer management, developed as part of a **Backend Developer Technical Challenge**. It allows the registration, querying, and analysis of customer data, with a focus on scalability, security, and software development best practices.

---

## Features

### Core Functionality

* **Create Customers**: Endpoint for registering first name, last name, age, and birth date with validation.
* **Statistics**: Calculate average age, standard deviation, and total customers count.
* **Customer Listing**: Retrieve all customers with complete data and estimated retirement date.
* **Full CRUD Support**: Create, read, update, and delete operations.
* **Data Validation**: Age consistency validation with birth date and input sanitization.

### Technical Requirements

* **Layered Architecture**: Built using clean code and design patterns.
* **API Documentation**: Available via Swagger/OpenAPI.
* **Persistence**: JPA/Hibernate with H2 for development and PostgreSQL for production.
* **Security**: Configured with Spring Security.
* **Asynchronous Messaging**: Integrated with RabbitMQ with proper error handling.
* **Monitoring**: Spring Boot Actuator with Prometheus-compatible metrics.
* **Logging**: Structured and configurable logging.
* **Comprehensive Testing**: Unit, integration, and validation tests.
* **Containerization**: Docker and Docker Compose support.

---

## Technology Stack

* Java 17
* Spring Boot 3.2.3
* Spring Data JPA
* Spring Security
* Spring Boot Actuator
* RabbitMQ
* H2 Database (Development)
* PostgreSQL (Production)
* Maven
* Lombok
* Swagger / OpenAPI
* JUnit 5
* Docker & Docker Compose

---

## Getting Started

### Prerequisites

* Java 17+
* Maven 3.6+
* Docker & Docker Compose (optional, for full functionality)

### Quick Start with Docker

```bash
git clone <repository-url>
cd Gattas_Challenge_Backend
docker-compose up -d
```

### Manual Installation

```bash
git clone <repository-url>
cd Gattas_Challenge_Backend
mvn spring-boot:run
```

### Access Points

* API: `http://localhost:8080`
* Swagger UI: `http://localhost:8080/swagger-ui.html`
* H2 Console: `http://localhost:8080/h2-console`
* RabbitMQ Management: `http://localhost:15672` (guest/guest)

### Database Configuration

* **Development**: H2 in-memory database
  * URL: `jdbc:h2:mem:customer_management`
  * Username: `sa`
  * Password: *(empty)*
* **Production**: PostgreSQL
  * URL: `jdbc:postgresql://localhost:5432/customer_management`
  * Username: `postgres`
  * Password: `password`

---

## API Endpoints

### Customers

* `POST /api/customers` - Create a new customer
* `GET /api/customers` - List all customers
* `GET /api/customers/{id}` - Get customer by ID
* `PATCH /api/customers/{id}` - Update customer
* `DELETE /api/customers/{id}` - Delete customer

### Statistics

* `GET /api/customers/stats` - Retrieve all statistics (average age, standard deviation, total customers)
* `GET /api/customers/stats/average-age` - Average age
* `GET /api/customers/stats/age-standard-deviation` - Age standard deviation

### Monitoring

* `GET /actuator/health` - Health check
* `GET /actuator/metrics` - System metrics
* `GET /actuator/prometheus` - Prometheus metrics

---

## Example Usage

### Create a Customer

```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "John",
    "lastName": "Smith",
    "age": 30,
    "birthDate": "1994-01-01"
  }'
```

### Get All Statistics

```bash
curl http://localhost:8080/api/customers/stats
```

Response:
```json
{
  "averageAge": 30.0,
  "ageStandardDeviation": 5.0,
  "totalCustomers": 10
}
```

---

## Testing

### Run Tests

```bash
mvn test
```

### Test Coverage

* **Unit tests**: Service, Controller, Repository layers
* **Integration tests**: End-to-end API validation
* **Validation tests**: DTOs and Entities
* **Error handling tests**: Exception scenarios

### Test Profiles

* `test` profile for isolated testing without external dependencies
* Mocked RabbitMQ for unit tests
* In-memory H2 database for integration tests

---

## Architecture Decisions

### Layered Architecture

* **Controller**: Handles HTTP requests and input validation
* **Service**: Business logic and processing
* **Repository**: Database operations
* **Model**: Domain entities
* **DTO**: Data transfer between layers

### Design Patterns

* **Repository Pattern**: Data access abstraction
* **DTO Pattern**: Data transfer between layers
* **Factory Pattern**: Object creation
* **Builder Pattern**: Complex object construction
* **Strategy Pattern**: Algorithm selection

### Security

* Spring Security (development environment allows public access)
* Input validation via Jakarta Validation
* Global exception handling
* Input sanitization via regex patterns
* Age consistency validation

### Asynchronous Messaging

* RabbitMQ for event-driven architecture
* Event handling for customer create/update/delete
* Use cases: welcome emails, statistics updates, notifications
* **Error handling**: Graceful degradation when RabbitMQ is unavailable

### Monitoring and Observability

* Spring Boot Actuator for health and metrics
* Prometheus-compatible metrics
* Structured logging
* Custom business metrics

---

## Data Validation

### Input Validation

* **Names**: Only letters and spaces allowed (including Spanish characters)
* **Age**: Must be between 0 and 150
* **Birth Date**: Must be in the past
* **Age Consistency**: Age must match birth date (±1 year tolerance)

### Error Handling

* **400 Bad Request**: Validation errors
* **404 Not Found**: Customer not found
* **500 Internal Server Error**: Unexpected errors

---

## Scalability

### Design Considerations

* In-memory H2 for development, PostgreSQL for production
* Redis-ready cache architecture
* Stateless architecture for scaling
* RabbitMQ decouples async operations
* Prometheus integration for observability

### Production Readiness

* Environment variable support
* Profiles: dev, test, prod, docker
* Docker-ready with multi-stage builds
* Compatible with AWS, Azure, GCP
* Health checks and graceful shutdown

---

## Advanced Configuration

### Environment Variables

```bash
export SPRING_PROFILES_ACTIVE=prod
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/customers
export RABBITMQ_HOST=localhost
export RABBITMQ_PORT=5672
```

### Logging

Logs are saved at `logs/customer-management-service.log` with auto-rotation.

---

## Metrics and Monitoring

* Total customers
* Average age
* Standard deviation
* Endpoint response times
* System resource usage

**Health Checks:**

* Database connectivity
* RabbitMQ broker status
* Disk space
* Memory usage

---

## Deployment

### Docker

```bash
docker build -t customer-management-service .
docker run -p 8080:8080 customer-management-service
```

### Docker Compose (Recommended)

```bash
docker-compose up -d
```

### Kubernetes (Example)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: customer-management-service
spec:
  replicas: 3
  selector:
    matchLabels:
      app: customer-management-service
  template:
    metadata:
      labels:
        app: customer-management-service
    spec:
      containers:
      - name: customer-management-service
        image: customer-management-service:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: "prod"
        livenessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 60
          periodSeconds: 30
        readinessProbe:
          httpGet:
            path: /actuator/health
            port: 8080
          initialDelaySeconds: 30
          periodSeconds: 10
```

---

## Recent Improvements

### Version 2.1 Enhancements

1. **Fixed RabbitMQ Configuration**
   - Proper exchange and queue declaration
   - Graceful error handling when RabbitMQ is unavailable
   - JSON message converter configuration

2. **Enhanced Security Configuration**
   - Explicit permissions for Swagger UI and Actuator
   - H2 console access for development

3. **Production Readiness**
   - Docker multi-stage build
   - Docker Compose for local development
   - PostgreSQL support for production
   - Health checks and graceful shutdown

4. **Improved JPA Configuration**
   - Proper auditing configuration
   - Repository scanning configuration

5. **Better Error Handling**
   - RabbitMQ connection failures don't break the application
   - Comprehensive logging for debugging

---

## Performance Considerations

* **Database Indexing**: Automatic indexing on primary keys
* **Query Optimization**: Efficient JPA queries
* **Connection Pooling**: HikariCP for database connections
* **Caching**: Ready for Redis integration
* **Async Processing**: RabbitMQ for non-blocking operations

---

## Security Best Practices

* Input validation and sanitization
* SQL injection prevention via JPA
* XSS protection via content type headers
* CORS configuration for web clients
* Rate limiting ready (can be added with Spring Security)

---

## Future Enhancements

* **Caching Layer**: Redis integration for performance
* **Rate Limiting**: API throttling
* **OAuth2**: JWT-based authentication
* **API Versioning**: Version control for API changes
* **GraphQL**: Alternative to REST for complex queries
* **Event Sourcing**: Complete audit trail
* **Microservices**: Service decomposition

---

## Author

**Agustín Gattas** - [GitHub](https://github.com/AgusGattas)


