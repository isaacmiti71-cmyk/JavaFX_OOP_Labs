# Architectural Design Explanation

**Student Name:** Isaac Miti (ID: 202509238)  
**Institution:** Mulungushi University, School of Engineering and Technology  
**Course:** ICT261 Advanced Java Programming (Unit 1)  
**Lecturer:** Mr. E. Nyirenda  
**Project:** Student Services Desktop App (Feature: Register Student)  

---

## 1. Why We Use a Layered Architecture

In modern software development, putting all logic directly into the user interface (UI) creates code that is messy, hard to test, and difficult to maintain. To avoid this, our **Student Services Desktop App** uses a **Strict One-Way Layered Architecture**:

1. **User Interface Layer (`ui` & FXML):** Handles what the user sees (the form and the table) and captures user clicks. It does not contain any database code or business rules.
2. **Controller Layer (`controller`):** Acts as the mediator. It reads user input from the form, asks the Service layer to process it, and updates the table once the action succeeds.
3. **Service Layer (`service`):** Contains the "brains" of the application. It validates that the student ID is exactly 9 digits, checks that the email format is correct, and verifies that the student is not already registered.
4. **Repository Layer (`repository`):** Handles all database communications using standard JDBC SQL queries (`PreparedStatement`). It isolates database queries from business rules.
5. **Database Layer (`util` & Embedded H2):** The embedded H2 database engine stores records locally in a file. At application startup, the database manager automatically creates the required tables if they do not exist.

By enforcing this strict downward flow (`UI -> Controller -> Service -> Repository -> Database`), each layer only depends on the one directly underneath it. If we ever decide to switch our database from H2 to PostgreSQL or MySQL, only the Repository layer needs to change—the UI and Service layers remain untouched.

---

## 2. How Error Handling Works

Errors are handled defensively and communicated gracefully without crashing the desktop application:

1. **Custom Exceptions:** Instead of generic error messages, we define domain-specific exceptions such as `ValidationException` and `DuplicateStudentException`.
2. **Graceful Recovery at the Controller:** When a user enters an existing Student ID (for example, `202509238`), the Service layer checks the repository, discovers the duplicate record, and throws a `DuplicateStudentException`.
3. **User-Friendly Feedback:** The `StudentController` catches this exception, prevents the database insertion, and immediately pops up a JavaFX `Alert` dialog explaining: *"Student ID 202509238 is already registered."*
4. **Data Preservation:** The form inputs remain in the text fields so the user can easily edit the typo instead of having to type everything again.
5. **Database Protection:** Lower-level SQL errors are caught and logged, while safe generic messages are presented to the end user.
