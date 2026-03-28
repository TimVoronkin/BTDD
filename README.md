# Cinema Booking System (TDD Project) 🎬🍿

A modern, robust cinema booking system backend built strictly with **Test-Driven Development (TDD)** as part of a university semester project. The project demonstrates clean architecture, business logic encapsulation, and high test coverage.

## 🚀 Features & Business Rules
The core of this application is driven by 6 strict business rules developed using the **Red-Green-Refactor** cycle:

1. **Price Validation:** Ticket price cannot be negative or zero.
2. **Seat Availability:** A booking cannot be made if there are not enough available seats in the screening.
3. **Morning Discount:** Screenings before 12:00 PM automatically apply a 20% discount to the ticket price.
4. **Ticket Limit:** A single user can book a maximum of 6 tickets per transaction.
5. **Age Rating Check:** The system strictly checks if the customer's age is greater than or equal to the movie's age rating (e.g., 16+).
6. **Cancellation Window:** Bookings can only be cancelled if there is more than 1 hour remaining until the screening starts.

## 🛠️ Technology Stack
- **Java 21**
- **Spring Boot 3.x**
- **Spring Data JPA** (Hibernate)
- **H2 Database** (In-memory, for easy testing)
- **Thymeleaf** (Modern dark-themed UI with Lime Green accents)
- **JUnit 5 / Mockito** (For TDD)
- **JaCoCo** (Test coverage reporting)
- **GitHub Actions** (CI pipeline for automated testing)

## 💻 How to Run Locally

You can run the project either via your favorite IDE (IntelliJ IDEA, VS Code) or using Maven.

### Using IDE (Recommended):
1. Open the project folder in your IDE.
2. Run the `CinemaBookingApplication` main class.
3. Open `http://localhost:8080` in your browser.

### Using Command Line (Maven):
Since the project uses a standard Maven setup:
```bash
mvn spring-boot:run
```

**Note:** The application uses an `H2` in-memory database. All test data (Movies and Screenings) is automatically generated on startup via the `DataInitializer` class.

## 🧪 Testing (TDD Process)

This project strictly followed the TDD workflow. You can view the commit history (`git log`) to see the step-by-step implementation of unit tests before the actual business logic was written.

To run tests and see the coverage report:
```bash
mvn clean test jacoco:report
```
*The JaCoCo test coverage report will be generated in `target/site/jacoco/index.html`.*

## 🎨 UI Design
The UI is fully responsive and uses a premium Dark Theme (`#0a0a0a`) with vibrant Lime Green (`#b8f500`) accents, providing a sleek, modern user experience. All prices are displayed in **CZK**.
