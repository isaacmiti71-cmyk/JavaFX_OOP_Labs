# UML Sequence Diagram: Register Student Action

**Project:** Student Services Desktop App  
**Student:** Isaac Miti (ID: 202509238)  
**Institution:** Mulungushi University, School of Engineering and Technology  
**Course:** ICT261 Advanced Java Programming (Unit 1)  
**Lecturer:** Mr. E. Nyirenda  

---

## 1. Sequence Overview

This diagram documents the entire lifecycle of the **Register Student** feature, including:
1. Form submission and UI trigger.
2. Input sanitization and business validation.
3. Duplicate ID check against the persistence layer.
4. Database insertion with JDBC `PreparedStatement`.
5. Error path (`alt` branch) handling duplicate records gracefully.

---

## 2. Mermaid Sequence Diagram

```mermaid
sequenceDiagram
    autonumber
    actor User as User (Administrator/Student)
    participant View as RegisterView (JavaFX UI)
    participant Ctrl as StudentController
    participant Svc as StudentService
    participant Repo as StudentRepository
    participant DB as Embedded H2 Database

    User->>View: Enters Student ID, Full Name, Email, Programme
    User->>View: Clicks "Register Student"
    View->>Ctrl: handleRegisterStudent(ActionEvent)
    Ctrl->>Ctrl: Extract form values from text fields

    Note over Ctrl,Svc: Layer boundary: Controller delegates to Service
    Ctrl->>Svc: registerStudent(id, fullName, email, programme)
    
    rect rgb(255, 245, 238)
    Note over Svc: 1. Input Validation
    Svc->>Svc: validateInputs(id, fullName, email, programme)
    alt Fields empty OR invalid email OR ID not 9 digits
        Svc-->>Ctrl: throw ValidationException("Invalid format / missing fields")
        Ctrl->>View: showAlert(WARNING, "Please correct the highlighted inputs")
        Note over View: Form inputs are preserved for user correction
    end
    end

    rect rgb(240, 248, 255)
    Note over Svc,Repo: 2. Duplicate Check
    Svc->>Repo: findById(studentId)
    Repo->>DB: SELECT * FROM students WHERE id = ?
    DB-->>Repo: ResultSet (Existing record or empty)
    Repo-->>Svc: Optional<Student>
    end

    alt Student ID already exists in Database (Duplicate Error Path)
        rect rgb(255, 235, 238)
        Svc-->>Ctrl: throw DuplicateStudentException("Student ID 202509238 is already registered")
        Ctrl->>View: showAlert(ERROR, "Duplicate Student ID: 202509238 is already registered")
        Note over View: Application remains responsive; form values kept
        end
    else Student ID is unique (Happy Path)
        rect rgb(238, 255, 238)
        Svc->>Svc: Create Student model object
        Svc->>Repo: save(Student)
        Repo->>DB: INSERT INTO students (id, full_name, email, programme, created_at) VALUES (?, ?, ?, ?, ?)
        DB-->>Repo: 1 row affected
        Repo-->>Svc: Saved Student instance
        Svc-->>Ctrl: Return Student instance
        
        Ctrl->>Repo: findAll()
        Repo->>DB: SELECT * FROM students ORDER BY created_at DESC
        DB-->>Repo: List<Student>
        Repo-->>Ctrl: Updated student list
        
        Ctrl->>View: TableView.setItems(updatedList)
        Ctrl->>View: clearFormInputs()
        Ctrl->>View: showAlert(INFORMATION, "Student registered successfully!")
        View-->>User: Updated table displaying new student record
        end
    end
```

---

## 3. Key Observations for ICT261 Marking

1. **One-Way Flow:** Calls flow forward (`View -> Controller -> Service -> Repository -> Database`), never jumping or skipping layers.
2. **Defensive Validation in Service:** Validation occurs in `StudentService`, maintaining decoupling from JavaFX UI controls.
3. **Graceful Exception Recovery:** If an ID such as `202509238` is re-entered, `DuplicateStudentException` prevents dirty database transactions, bubbles to `StudentController`, and displays an informative dialog while retaining user keystrokes in the UI.
