# AIDE Backend Service

A Spring Boot RESTful API service for the AIDE application.

## Technologies

- Java 21
- Spring Boot 3.4.5
- PostgreSQL
- Docker
- Maven

## Prerequisites

- JDK 21
- Maven 3.9+
- Docker and Docker Compose
- PostgreSQL (for local development)

## Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── aide/
│   │           └── service/
│   │               ├── config/       # Configuration classes
│   │               ├── controller/   # REST controllers
│   │               ├── dto/          # Data Transfer Objects
│   │               ├── entity/       # JPA entities
│   │               ├── repository/   # Data repositories
│   │               ├── service/      # Business logic
│   │               └── exception/    # Custom exceptions
│   └── resources/
│       ├── application.yml          # Common configuration
│       ├── application-local.yml    # Local environment config
│       ├── application-dev.yml      # Development environment config
│       └── application-prod.yml     # Production environment config
└── test/
    └── java/                        # Test classes
```

## Getting Started

### Local Development

1. Clone the repository:
   ```bash
   git clone https://github.com/yourusername/aide-backend.git
   cd aide-backend
   ```

2. Start the database using Docker Compose:
   ```bash
   docker-compose up db
   ```

3. Run the application:
   ```bash
   ./mvnw spring-boot:run
   ```

### Using Docker

To run the entire application stack using Docker Compose:

```bash
docker-compose up
```

## Configuration

The application uses different configuration profiles:

- `local`: For local development
- `dev`: For development environment
- `prod`: For production environment

Environment variables required for production:
- `DB_HOST`: Database host
- `DB_PORT`: Database port
- `DB_NAME`: Database name
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password

## API Documentation

The API documentation is available at:
- Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`
- OpenAPI docs: `http://localhost:8080/api/v1/v3/api-docs`

## Testing

Run tests using Maven:

```bash
./mvnw test
```

## Deployment

The application uses GitHub Actions for CI/CD. The pipeline:
1. Builds the application
2. Runs tests
3. Creates a Docker image
4. Deploys to production (when merging to main)

## Monitoring

The application exposes the following actuator endpoints:
- Health check: `/actuator/health`
- Metrics: `/actuator/metrics`
- Prometheus: `/actuator/prometheus`

## Contributing

1. Create a feature branch
2. Commit your changes
3. Push to the branch
4. Create a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details. 