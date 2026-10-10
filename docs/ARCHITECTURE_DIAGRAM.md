# Layered Architecture Diagram

**Project:** Student Services Desktop App  
**Student:** Isaac Miti (ID: 202509238)  
**Institution:** Mulungushi University, School of Engineering and Technology  
**Course:** ICT261 Advanced Java Programming (Unit 1)  
**Lecturer:** Mr. E. Nyirenda  

---

## 1. Architectural Overview

The application adheres to a **Strict One-Way Layered Architecture**. Each component only interacts with the layer directly below it. The presentation layer never queries the repository or database directly, ensuring low coupling, high maintainability, and clean testability.

### Strict Dependency Direction:
```
JavaFX UI (FXML / Views)
          │
          ▼
Controller (StudentController)
          │
          ▼
Service (StudentService) ──────► (Optional Client: AnnouncementClient)
          │
          ▼
Repository (StudentRepository - JDBC)
          │
          ▼
Database (Embedded H2 / SQLite)
```

---

## 2. Architecture Diagram (Mermaid)

```mermaid
flowchart TD
    %% Styling
    classDef uiLayer fill:#e1f5fe,stroke:#0288d1,stroke-width:2px,color:#01579b;
    classDef controllerLayer fill:#e8f5e9,stroke:#388e3c,stroke-width:2px,color:#1b5e20;
    classDef serviceLayer fill:#fff8e1,stroke:#fbc02d,stroke-width:2px,color:#f57f17;
    classDef repoLayer fill:#f3e5f5,stroke:#7b1fa2,stroke-width:2px,color:#4a148c;
    classDef dbLayer fill:#ede7f6,stroke:#512da8,stroke-width:2px,color:#311b92;
    classDef errorLayer fill:#ffebee,stroke:#d32f2f,stroke-width:2px,color:#b71c1c;

    subgraph UI_Layer ["1. UI Layer (com.studentservices.ui / resources)"]
        RegisterView["RegisterView.fxml & styles.css<br/>(Student ID, Name, Email, Programme fields, TableView)"]:::uiLayer
    end

    subgraph Controller_Layer ["2. Controller Layer (com.studentservices.controller)"]
        StudentController["StudentController<br/>• Reads inputs from Form<br/>• Invokes StudentService<br/>• Updates TableView<br/>• Catches exceptions & displays Alerts"]:::controllerLayer
        AlertBox["JavaFX Alert Dialog<br/>(Keeps form data on error; friendly warning)"]:::errorLayer
    end

    subgraph Service_Layer ["3. Business / Service Layer (com.studentservices.service)"]
        StudentService["StudentService<br/>• Validates non-empty fields<br/>• Validates 9-digit Student ID<br/>• Validates RFC Email pattern<br/>• Checks duplicate ID"]:::serviceLayer
        ValidationEx["ValidationException<br/>DuplicateStudentException"]:::errorLayer
        AnnouncementClient["AnnouncementClient (HTTP)<br/>(Fetches campus announcements)"]:::serviceLayer
    end

    subgraph Repository_Layer ["4. Data Access Layer (com.studentservices.repository)"]
        StudentRepo["StudentRepository (Interface)<br/>StudentRepositoryImpl (JDBC)<br/>• Uses PreparedStatement<br/>• save(student)<br/>• findById(id)<br/>• findAll()"]:::repoLayer
    end

    subgraph Database_Layer ["5. Persistence Layer (com.studentservices.util)"]
        DatabaseManager["DatabaseManager (H2 JDBC)<br/>• Embedded H2 Database engine<br/>• Auto-creates STUDENTS table at startup"]:::dbLayer
        H2DB[("studentsdb (H2 Database)")]:::dbLayer
    end

    %% Flow Arrows
    RegisterView -->|"User submits form"| StudentController
    StudentController -->|"Calls registerStudent(student)"| StudentService
    StudentService -.->|"Optional HTTP request"| AnnouncementClient
    StudentService -->|"Calls repository.save(...) / findById(...)"| StudentRepo
    StudentRepo -->|"Executes SQL via PreparedStatement"| DatabaseManager
    DatabaseManager -->|"Reads / Writes records"| H2DB

    %% Error Handling Boundary
    StudentService -.->|"Throws ValidationException /<br/>DuplicateStudentException"| ValidationEx
    ValidationEx -.->|"Propagated to Controller"| StudentController
    StudentController -->|"Displays friendly message without data loss"| AlertBox

```

---

## 3. Error Handling Boundary & Data Flow

1. **Input Submission:** The user fills the student form and clicks **Register Student**.
2. **Controller Delegation:** The `StudentController` extracts strings and passes them to `StudentService.register(...)`.
3. **Validation & Duplicate Checking:**
   - If fields are empty, or the ID is not 9 digits, or the email is invalid, `StudentService` throws `ValidationException`.
   - If `StudentRepository.findById(id)` returns an existing student, `StudentService` throws `DuplicateStudentException`.
4. **Boundary Interception:** The `StudentController` catches these custom exceptions inside a `try-catch` block.
5. **Safe Response:** The controller triggers a JavaFX `Alert` with an informative message (e.g., *"Student ID 202509238 is already registered"*). **Crucially, the form fields remain populated**, allowing the student or administrator to correct the error without starting over. The app never crashes.
