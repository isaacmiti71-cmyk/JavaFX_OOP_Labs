package com.studentservices.service;

import com.studentservices.client.AnnouncementClient;
import com.studentservices.exception.DuplicateStudentException;
import com.studentservices.exception.ValidationException;
import com.studentservices.model.Student;
import com.studentservices.repository.StudentRepository;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Service Layer responsible for business logic, defensive input validation,
 * and duplicate student ID prevention.
 * 
 * Isaac Miti | 202509238 | ICT261 | Mr. E. Nyirenda
 */
public class StudentService {

    // Regex pattern for exactly 9 numeric digits (e.g. 202509238)
    private static final Pattern STUDENT_ID_PATTERN = Pattern.compile("^\\d{9}$");

    // Standard email validation regex
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private final StudentRepository studentRepository;
    private final AnnouncementClient announcementClient;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        this.announcementClient = new AnnouncementClient();
    }

    public StudentService(StudentRepository studentRepository, AnnouncementClient announcementClient) {
        this.studentRepository = studentRepository;
        this.announcementClient = announcementClient;
    }

    /**
     * Registers a new student after strict validation and duplicate checking.
     *
     * @param studentId 9-digit unique student ID
     * @param fullName  Full name of the student
     * @param email     Valid email address
     * @param programme Academic programme (e.g., Computer Science)
     * @return Saved Student object
     * @throws ValidationException       if any field is blank or improperly formatted
     * @throws DuplicateStudentException if studentId already exists in repository
     */
    public Student registerStudent(String studentId, String fullName, String email, String programme) {
        // Step 1: Input Validation
        validateInput(studentId, fullName, email, programme);

        String trimmedId = studentId.trim();
        String trimmedName = fullName.trim();
        String trimmedEmail = email.trim();
        String trimmedProg = programme.trim();

        // Step 2: Duplicate Check
        if (studentRepository.existsById(trimmedId)) {
            throw new DuplicateStudentException("Student ID " + trimmedId + " is already registered.");
        }

        // Step 3: Create Model and Delegate to Repository
        Student student = new Student(trimmedId, trimmedName, trimmedEmail, trimmedProg);
        studentRepository.save(student);

        return student;
    }

    /**
     * Retrieves all registered students.
     */
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    /**
     * Fetches the latest announcement via the announcement client.
     */
    public String getLatestAnnouncement() {
        if (announcementClient != null) {
            return announcementClient.fetchLatestAnnouncement();
        }
        return "Welcome to Student Services.";
    }

    /**
     * Validates student registration inputs defensively.
     */
    private void validateInput(String studentId, String fullName, String email, String programme) {
        if (studentId == null || studentId.trim().isEmpty()) {
            throw new ValidationException("Student ID cannot be empty.");
        }

        if (fullName == null || fullName.trim().isEmpty()) {
            throw new ValidationException("Full Name cannot be empty.");
        }

        if (email == null || email.trim().isEmpty()) {
            throw new ValidationException("Email address cannot be empty.");
        }

        if (programme == null || programme.trim().isEmpty()) {
            throw new ValidationException("Please select an academic Programme.");
        }

        String trimmedId = studentId.trim();
        if (!STUDENT_ID_PATTERN.matcher(trimmedId).matches()) {
            throw new ValidationException("Student ID must be exactly 9 numeric digits (e.g., 202509238).");
        }

        String trimmedEmail = email.trim();
        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new ValidationException("Invalid email format (e.g., student@mu.ac.zm).");
        }

        if (fullName.trim().length() < 2) {
            throw new ValidationException("Full Name must be at least 2 characters long.");
        }
    }
}
