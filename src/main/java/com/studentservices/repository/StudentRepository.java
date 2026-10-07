package com.studentservices.repository;

import com.studentservices.model.Student;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface defining persistence operations for Student entities.
 * Decouples business logic from specific JDBC implementation details.
 * 
 * Isaac Miti | 202509238 | ICT261 | Mr. E. Nyirenda
 */
public interface StudentRepository {

    /**
     * Persists a new student record into the database.
     * @param student The student to save
     */
    void save(Student student);

    /**
     * Finds a student by their unique 9-digit Student ID.
     * @param studentId The student ID to search
     * @return Optional containing the student if found, or empty Optional
     */
    Optional<Student> findById(String studentId);

    /**
     * Retrieves all registered students ordered by registration date descending.
     * @return List of all students
     */
    List<Student> findAll();

    /**
     * Checks if a student with the given ID already exists.
     * @param studentId The student ID to check
     * @return true if exists, false otherwise
     */
    boolean existsById(String studentId);
}
