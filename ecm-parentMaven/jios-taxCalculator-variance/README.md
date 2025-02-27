# JIOS Tax Calculator Variance Microservices

## Introduction

The **JIOS Tax Calculator Variance Microservices** project is designed to perform variance analysis on tax returns. This service compares filed tax return values against recalculated values to identify discrepancies, ensuring that tax returns are accurate. The project leverages a microservices architecture built with Spring Boot and Spring Cloud, providing a robust and scalable solution for tax variance analysis.

## Swagger

The project includes integrated API documentation with Swagger. To view the Swagger UI and explore the available endpoints:

- **URL**: `http://<your-domain>:<port>/swagger-ui.html`

## Getting Started

### Installation Process

To get the project up and running on your local system, follow these steps:

1. **Clone the repository**:
    ```bash
    git clone git@bitbucket.org:vibrantech/jios-taxcalculator-variance.git
    cd jios-taxCalculator-variance
    ```

2. **Build the project** using Maven:
    ```bash
    mvn clean install
    ```

3. **Run the application**:
    ```bash
    mvn spring-boot:run
    ```

### Software Dependencies

Ensure that you have the following installed:

- **Java 17**: Required for running the application.
- **Maven 3.6+**: Used for building and managing dependencies.
- **Git**: For version control and cloning the repository.

### API References

- API documentation is available via Swagger. Refer to the [Swagger section](#swagger) for accessing it.

## Build and Test

### Building the Project

- To build the project, run:
    ```bash
    mvn clean install
    ```

- This will compile the code, run the tests, and package the application.

### Running Tests

- **Unit Tests**: Execute unit tests with:
    ```bash
    mvn test
    ```

- **Integration Tests**: Run integration tests using:
    ```bash
    mvn verify
    ```

- **Test Coverage**: Make sure tests cover key functionalities, such as:
  - JSON payload validation
  - Variance calculation logic
  - Integration with external services
  - Circuit breaker functionality using Resilience4j

## Contribute

### How to Contribute

We welcome contributions to improve the project! Here’s how you can get started:

1. **Fork the repository**: Click the "Fork" button at the top of this repository to create a copy in your GitHub account.
2. **Create a new branch**: 
    ```bash
    git checkout -b feature/your-feature-name
    ```
3. **Make your changes**: Implement your feature or bugfix with clear and concise code.
4. **Commit your changes**: 
    ```bash
    git commit -m "Add detailed description of your changes"
    ```
5. **Push to your branch**:
    ```bash
    git push origin feature/your-feature-name
    ```
6. **Submit a Pull Request**: Go to the original repository and submit a pull request for your changes.

### Code Style and Guidelines

- Follow the existing code style and conventions used in the project.
- Write unit and integration tests for any new features or fixes.
- Ensure all tests pass before submitting your pull request.
