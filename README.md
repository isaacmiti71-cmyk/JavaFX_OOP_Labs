# Student Services Desktop App (Register Student)

**Student Name:** Isaac Miti  
**Student ID:** 202509238  
**University:** Mulungushi University  
**School:** School of Engineering and Technology  
**Department:** Department of Computer Science and IT  
**Course:** ICT261 Advanced Java Programming (Unit 1)  
**Lecturer:** Mr. E. Nyirenda  

---

## 1. Project Overview

The **Student Services Desktop App** is a professional JavaFX desktop application designed to streamline student registration. Built as part of the ICT261 practical assignment under the theme **"Design first, then explain your design."**

This project strictly implements a **One-Way Layered Architecture**, defensive input validation, custom exception handling, and embedded H2 persistence via JDBC.

---

## 2. Layered Architecture (Strict One-Way Dependencies)

Each tier only depends on the tier directly beneath it:

```
[JavaFX UI / FXML] ──► [Controller] ──► [Service] ──► [Repository (JDBC)] ──► [Embedded H2 Database]
                                           │
                                     (Optional Client)
                                           ▼
                                  [AnnouncementClient]
```

### Layer Details:
1. **UI Layer (`src/main/resources/com/studentservices/`):**
   - `RegisterView.fxml`: JavaFX declarative view with form fields (Student ID, Full Name, Email, Programme) and `TableView`.
   - `styles.css`: Clean, professional academic stylesheet.
2. **Controller Layer (`com.studentservices.controller`):**
   - `StudentController.java`: Intercepts UI events, converts input into domain calls, catches exceptions, displays JavaFX `Alert` dialogs, and preserves form inputs upon error.
3. **Service Layer (`com.studentservices.service`):**
   - `StudentService.java`: Enforces business rules:
     - All fields required.
     - Student ID must be exactly 9 numeric digits (`^\d{9}$`).
     - Email format validation.
     - Duplicate check via repository before saving.
     - Throws `ValidationException` or `DuplicateStudentException`.
4. **Repository Layer (`com.studentservices.repository`):**
   - `StudentRepository.java`: DAO interface (`save`, `findById`, `findAll`, `existsById`).
   - `StudentRepositoryImpl.java`: JDBC implementation using parameterized `PreparedStatement` to prevent SQL injection.
5. **Database Layer (`com.studentservices.util`):**
   - `DatabaseManager.java`: Manages connection to embedded H2 (`jdbc:h2:./data/studentsdb`).
   - Automatically executes `CREATE TABLE IF NOT EXISTS students (...)` at application launch.
6. **Domain Model & Exceptions:**
   - `Student.java`: Encapsulates student data.
   - `ValidationException.java`, `DuplicateStudentException.java`, `DatabaseOperationException.java`.

---

## 3. Design Deliverables (in `/docs`)

All design documentation requested for grading is located in the `docs/` folder:
- `docs/ARCHITECTURE_DIAGRAM.md`: Detailed architecture flowchart with Mermaid syntax.
- `docs/architecture_diagram.svg`: Standalone vector graphic of the layered architecture and error boundary.
- `docs/SEQUENCE_DIAGRAM.md`: UML Sequence diagram showing user action flow and alt/error paths.
- `docs/DESIGN_EXPLANATION.md`: Half-page clear explanation of separation of concerns and error handling.
- `docs/PRESENTATION_SCRIPT.md`: 3-minute presentation script (~400 words) with speaker split for 4 group members.
- `docs/view_diagrams.html`: Interactive web page rendering the diagrams directly in a browser.

---

## 4. How to Build, Test, and Run

### Prerequisites
- Java JDK 21 (or JDK 17+)
- Maven (or use the provided `mvn.cmd` wrapper)

### Step 1: Run Unit Tests
To execute the automated JUnit 5 test suite verifying business validation, duplicate ID detection, and registration:
```bash
.\mvn.cmd test
```

### Step 2: Compile the Project
```bash
.\mvn.cmd compile
```

### Step 3: Run the Application
You can run the JavaFX desktop application using either:
```bash
.\mvn.cmd javafx:run
```
OR by executing the included `run.bat` script:
```bash
.\run.bat
```

---

## 5. Live Demonstration Guide for Mr. Nyirenda

When presenting to your lecturer:
1. **Show Initial State:** Notice the announcement bar, clean empty form, and table showing existing records from `./data/studentsdb`.
2. **Happy Path Registration:**
   - Student ID: `202509238`
   - Full Name: `Isaac Miti`
   - Email: `imiti@mu.ac.zm`
   - Programme: `BSc Software Engineering`
   - Click **Register Student** $\rightarrow$ Success alert appears, table refreshes immediately, and form clears.
3. **Error Handling (Duplicate ID):**
   - Re-enter the same ID `202509238` with any name.
   - Click **Register Student** $\rightarrow$ `DuplicateStudentException` is caught!
   - Alert dialog pops up: *"Student ID 202509238 is already registered."*
   - **Point out to lecturer:** The application did NOT crash, and the form data is **preserved** so the user does not need to retype everything!
4. **Validation Handling:**
   - Enter an invalid email (e.g. `testuser`) or a short ID (e.g. `12345`).
   - Click **Register Student** $\rightarrow$ `ValidationException` is caught and alerts the user to fix the issue.

---

## 6. Summary of Project Files

| File | Layer | Purpose |
| :--- | :--- | :--- |
| `App.java` | Presentation | JavaFX Application entry, initializes H2 database and loads FXML. |
| `Launcher.java` | Bootstrap | Standard main method bypassing module-path runtime checks. |
| `RegisterView.fxml` | View | UI layout with input form, TableView, and status labels. |
| `styles.css` | Styling | Modern, clean theme for buttons, cards, and tables. |
| `StudentController.java` | Controller | Event handling, service invocation, and JavaFX alert error recovery. |
| `StudentService.java` | Service | Business logic, 9-digit ID regex validation, email validation, duplicate checks. |
| `AnnouncementClient.java` | External Client | Demonstrates optional HTTP API connectivity for announcements. |
| `StudentRepository.java` | Repository | DAO interface decoupling persistence from business rules. |
| `StudentRepositoryImpl.java`| Repository | JDBC implementation with SQL `PreparedStatement`. |
| `DatabaseManager.java` | Persistence | H2 database connection provider and auto-table initializer. |
| `Student.java` | Model | Domain entity for student data. |
| `ValidationException.java` | Exception | Thrown on blank or malformed input. |
| `DuplicateStudentException.java`| Exception | Thrown when student ID is already taken. |
| `StudentServiceTest.java` | Test | JUnit 5 unit tests for registration, duplicates, and validation. |
