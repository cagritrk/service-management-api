# Service Management System

A full-stack service management application with Spring Boot backend and Angular frontend that performs CRUD operations on Service, Resource and Owner entities.

## Architecture Layers

1. **Controller Layer**: Handles HTTP requests/responses and API documentation
    - `ServiceController`: Manages service-related operations
    - Uses Spring's `@RestController` and Swagger annotations

2. **Service Layer**: Contains business logic
    - `ServiceService`: Implements core business operations
    - Thread-safe implementation with proper synchronization

3. **Repository Layer**: Handles data persistence
    - `ServiceRepository`: MongoDB operations
    - Uses Spring Data MongoDB

4. **DTO Layer**: Data Transfer Objects for API contracts
    - `ServiceDto`, `ResourceDto`, `OwnerDto`

5. **Model Layer**: Entity classes
    - `Service`, `Resource`, `Owner`

## API Documentation (Swagger)

Swagger UI is available at: `http://localhost:8080/swagger-ui.html`

Documented endpoints include:
- `POST /services` - Create new service
- `GET /services` - Get all service summaries
- `GET /services/{id}` - Get service by ID
- `PUT /services/{id}` - Update existing service
- `DELETE /services/{id}` - Delete service

## Spring Profiles

1. **dev**: Development profile with relaxed security and debug logging
2. **prod**: Production profile with optimized settings

## Cache Implementation

- Uses Spring's `@Cacheable`, `@CachePut`, `@CacheEvict` annotations
- Service methods are cached in memory
- Cache is checked before database access
- Automatically updated on create/update operations

## Thread Safety

- Update operations are synchronized
- Uses proper locking mechanisms
- Ensures data consistency in concurrent scenarios

## Dependencies

- Spring Boot 3.4.4
- Spring Data MongoDB
- Spring Cache
- Spring Web
- Springdoc OpenAPI (Swagger)
- Maven 3.9.6

## Requirements

- Java 24
- Maven 3.9.6
- MongoDB 6.0+
- Node.js 18+ (for frontend)

## How to Run

1. Start MongoDB
2. Build and run the backend:
```bash
./mvnw spring-boot:run
```
3. For production:
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```
4. Access Swagger UI at: `http://localhost:8080/swagger-ui.html`

## Quick Start with Bash Script (Local)

1. Make the script executable:
   ```bash
   chmod +x run-dev.sh
   ```
2. Run the script:
   ```bash
   ./run-dev.sh
   ```
   Example bash output
   ```bash
   ...
   ...
   2025-04-14T16:00:18.956+03:00  INFO 16108 --- [service-manager] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed initialization in 3 ms
   2025-04-14T16:00:18.967+03:00 DEBUG 16108 --- [service-manager] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : HEAD "/swagger-ui/index.html", parameters={}
   2025-04-14T16:00:18.975+03:00 DEBUG 16108 --- [service-manager] [nio-8080-exec-1] o.s.w.s.handler.SimpleUrlHandlerMapping  : Mapped to ResourceHttpRequestHandler [classpath [META-INF/resources/webjars/]]
   2025-04-14T16:00:19.000+03:00 DEBUG 16108 --- [service-manager] [nio-8080-exec-1] o.s.web.servlet.DispatcherServlet        : Completed 200 OK  
   ✅ Application successfully running at:
      - Local URL:    http://localhost:8080
      - Swagger UI:   http://localhost:8080/swagger-ui.html

   Press Ctrl+C to stop the application
   ```

## Testing

Service layer includes comprehensive unit tests covering:
- CRUD operations
- Cache behavior
- Error cases

Run tests with:
```bash
./mvnw test
```

