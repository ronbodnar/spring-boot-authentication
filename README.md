# User Authentication Service

## Overview

This standalone authentication microservice is built with Java and Spring Boot, utilizing Spring Security for secure authorization and JWT (JSON Web Tokens) for stateless authentication. Designed to integrate easily into larger systems, it provides essential authentication and authorization features. Currently, only JWT is supported but additional support for OAuth2.0 and possibly others will follow.

## Features

- **User Management:** Basic user creation, retrieval, and role management.
- **Security:** JWT-based authentication, secure password storage, and role-based access control.
- **Configuration Management:** Environment-based configurations for database, CORS, JWT, and other settings.
- **Exception Handling:** Custom exceptions and responses for error cases, like user conflicts.
- **Spring Security Integration:** Leverages Spring Security’s powerful features for secure handling of user authentication.

## Technologies Used

- **Spring Boot** - 3.x
- **Spring Security**
- **H2 Database** - for in-memory database testing
- **JWT** - for stateless authentication
- **Java Persistence API (JPA)** - for ORM
- **JUnit 5** - for unit testing

## Project Structure

This project is organized around core modules for separation of concerns:

- `user` - Contains user management logic, including `UserController`, `UserService`, and `UserRepository`.
- `security` - Contains security and JWT configurations, along with filters and exception handlers.
- `exception` - Handles custom exception classes for cleaner error handling.

## Endpoints

- **POST /**: Adds a new user.
- **GET /**: Retrieves all users.
- **GET /{id}**: Retrieves a user by ID.

## Getting Started

1. **Clone the repository**:

   ```bash
   git clone https://github.com/your-repo/user-authentication-service.git
   cd user-authentication-service
   ```

2. **Install dependencies**:

   ```bash
   ./mvnw install
   ```

3. **Run the application**:
   ```bash
   ./mvnw spring-boot:run
   ```

## Environment Variables

- `JWT_SECRET`: Your secret key for JWT signing.
- `BEARER_TOKEN`: The token used for bearer authentication.

## Contributing

Contributions, bug reports, and feature requests are welcome! Please fork this repository and submit pull requests.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
