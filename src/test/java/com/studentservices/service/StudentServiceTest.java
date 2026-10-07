package com.studentservices.service;

import com.studentservices.exception.DuplicateStudentException;
import com.studentservices.exception.ValidationException;
import com.studentservices.model.Student;
import com.studentservices.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for StudentService verifying:
 * 1. Successful student registration
 * 2. Duplicate student ID detection and exception throwing
 * 3. Input validation errors (invalid email, invalid 9-digit ID, blank fields)
 * 
 * Isaac Miti | 202509238 | ICT261 | Mr. E. Nyirenda
 */
public class StudentServiceTest {

    private StudentService studentService;
    private InMemoryStudentRepository testRepository;

    @BeforeEach
    void setUp() {
        testRepository = new InMemoryStudentRepository();
        studentService = new StudentService(testRepository);
    }

    @Test
    @DisplayName("Should successfully register student with valid data")
    void testSuccessfulStudentRegistration() {
        // Arrange
        String studentId = "202509238";
        String fullName = "Isaac Miti";
        String email = "imiti@mu.ac.zm";
        String programme = "BSc Software Engineering";

        // Act
        Student registered = studentService.registerStudent(studentId, fullName, email, programme);

        // Assert
        assertNotNull(registered, "Registered student should not be null");
        assertEquals("202509238", registered.getStudentId());
        assertEquals("Isaac Miti", registered.getFullName());
        assertEquals("imiti@mu.ac.zm", registered.getEmail());
        assertEquals("BSc Software Engineering", registered.getProgramme());
        assertTrue(testRepository.existsById("202509238"), "Student should be saved in repository");
    }

    @Test
    @DisplayName("Should throw DuplicateStudentException when student ID already exists")
    void testDuplicateStudentIdThrowsException() {
        // Arrange
        String studentId = "202509238";
        studentService.registerStudent(studentId, "Isaac Miti", "imiti@mu.ac.zm", "Software Engineering");

        // Act & Assert
        DuplicateStudentException exception = assertThrows(
                DuplicateStudentException.class,
                () -> studentService.registerStudent(studentId, "Another Student", "another@mu.ac.zm", "Computer Science"),
                "Registering with duplicate student ID must throw DuplicateStudentException"
        );

        assertTrue(exception.getMessage().contains("202509238"), "Exception message should mention the duplicate ID");
        assertEquals(1, testRepository.findAll().size(), "Repository should still only contain 1 student");
    }

    @Test
    @DisplayName("Should throw ValidationException when email format is invalid")
    void testInvalidEmailThrowsValidationException() {
        // Arrange & Act & Assert
        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> studentService.registerStudent("202509238", "Isaac Miti", "not-a-valid-email", "Information Technology"),
                "Invalid email format must throw ValidationException"
        );

        assertTrue(exception.getMessage().toLowerCase().contains("email"), "Error message should mention email");
    }

    @Test
    @DisplayName("Should throw ValidationException when Student ID is not 9 digits")
    void testInvalidStudentIdThrowsValidationException() {
        // Test with fewer than 9 digits
        assertThrows(
                ValidationException.class,
                () -> studentService.registerStudent("12345", "Isaac Miti", "imiti@mu.ac.zm", "Computer Science"),
                "Short Student ID must throw ValidationException"
        );

        // Test with letters
        assertThrows(
                ValidationException.class,
                () -> studentService.registerStudent("2025ABCD8", "Isaac Miti", "imiti@mu.ac.zm", "Computer Science"),
                "Alpha-numeric Student ID must throw ValidationException"
        );
    }

    @Test
    @DisplayName("Should throw ValidationException when fields are empty")
    void testEmptyFieldsThrowsValidationException() {
        // Empty student ID
        assertThrows(ValidationException.class, () ->
                studentService.registerStudent("", "Isaac Miti", "imiti@mu.ac.zm", "Computer Science"));

        // Empty Full Name
        assertThrows(ValidationException.class, () ->
                studentService.registerStudent("202509238", "", "imiti@mu.ac.zm", "Computer Science"));

        // Empty Programme
        assertThrows(ValidationException.class, () ->
                studentService.registerStudent("202509238", "Isaac Miti", "imiti@mu.ac.zm", ""));
    }

    /**
     * In-memory implementation of StudentRepository for lightning-fast, isolated unit testing.
     */
    private static class InMemoryStudentRepository implements StudentRepository {
        private final Map<String, Student> store = new HashMap<>();

        @Override
        public void save(Student student) {
            store.put(student.getStudentId(), student);
        }

        @Override
        public Optional<Student> findById(String studentId) {
            return Optional.ofNullable(store.get(studentId));
        }

        @Override
        public List<Student> findAll() {
            return new ArrayList<>(store.values());
        }

        @Override
        public boolean existsById(String studentId) {
            return store.containsKey(studentId);
        }
    }
}
