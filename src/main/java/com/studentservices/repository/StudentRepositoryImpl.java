package com.studentservices.repository;

import com.studentservices.exception.DatabaseOperationException;
import com.studentservices.model.Student;
import com.studentservices.util.DatabaseManager;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * JDBC implementation of StudentRepository.
 * Uses parameterized PreparedStatements to guarantee protection against SQL injection.
 * 
 * Isaac Miti | 202509238 | ICT261 | Mr. E. Nyirenda
 */
public class StudentRepositoryImpl implements StudentRepository {

    @Override
    public void save(Student student) {
        String sql = "INSERT INTO students (student_id, full_name, email, programme, registration_date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, student.getStudentId());
            pstmt.setString(2, student.getFullName());
            pstmt.setString(3, student.getEmail());
            pstmt.setString(4, student.getProgramme());
            pstmt.setTimestamp(5, Timestamp.valueOf(
                    student.getRegistrationDate() != null ? student.getRegistrationDate() : LocalDateTime.now()
            ));

            pstmt.executeUpdate();

        } catch (SQLException e) {
            System.err.println("[StudentRepositoryImpl] Error saving student: " + e.getMessage());
            throw new DatabaseOperationException("Failed to save student record to database", e);
        }
    }

    @Override
    public Optional<Student> findById(String studentId) {
        String sql = "SELECT student_id, full_name, email, programme, registration_date FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Student student = mapRowToStudent(rs);
                    return Optional.of(student);
                }
            }

        } catch (SQLException e) {
            System.err.println("[StudentRepositoryImpl] Error finding student by ID: " + e.getMessage());
            throw new DatabaseOperationException("Failed to query student by ID", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Student> findAll() {
        String sql = "SELECT student_id, full_name, email, programme, registration_date FROM students ORDER BY registration_date DESC";
        List<Student> students = new ArrayList<>();

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                students.add(mapRowToStudent(rs));
            }

        } catch (SQLException e) {
            System.err.println("[StudentRepositoryImpl] Error finding all students: " + e.getMessage());
            throw new DatabaseOperationException("Failed to retrieve student records from database", e);
        }

        return students;
    }

    @Override
    public boolean existsById(String studentId) {
        String sql = "SELECT 1 FROM students WHERE student_id = ?";

        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentId);

            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.err.println("[StudentRepositoryImpl] Error checking student existence: " + e.getMessage());
            throw new DatabaseOperationException("Failed to check if student exists", e);
        }
    }

    /**
     * Helper method to map a single JDBC ResultSet row into a Student domain object.
     */
    private Student mapRowToStudent(ResultSet rs) throws SQLException {
        String id = rs.getString("student_id");
        String name = rs.getString("full_name");
        String email = rs.getString("email");
        String prog = rs.getString("programme");
        Timestamp ts = rs.getTimestamp("registration_date");
        LocalDateTime regDate = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();

        return new Student(id, name, email, prog, regDate);
    }
}
