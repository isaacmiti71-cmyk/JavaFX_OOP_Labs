package com.studentservices.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * Domain model representing a university student.
 * 
 * Isaac Miti | 202509238 | ICT261 | Mr. E. Nyirenda
 */
public class Student {

    private String studentId;
    private String fullName;
    private String email;
    private String programme;
    private LocalDateTime registrationDate;

    // Default constructor
    public Student() {
        this.registrationDate = LocalDateTime.now();
    }

    // Parameterized constructor
    public Student(String studentId, String fullName, String email, String programme) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.programme = programme;
        this.registrationDate = LocalDateTime.now();
    }

    public Student(String studentId, String fullName, String email, String programme, LocalDateTime registrationDate) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.email = email;
        this.programme = programme;
        this.registrationDate = registrationDate;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProgramme() {
        return programme;
    }

    public void setProgramme(String programme) {
        this.programme = programme;
    }

    public LocalDateTime getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(LocalDateTime registrationDate) {
        this.registrationDate = registrationDate;
    }

    // Formatted date string for JavaFX TableView display
    public String getFormattedRegistrationDate() {
        if (registrationDate == null) {
            return "";
        }
        return registrationDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return Objects.equals(studentId, student.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(studentId);
    }

    @Override
    public String toString() {
        return "Student{" +
                "studentId='" + studentId + '\'' +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", programme='" + programme + '\'' +
                ", registrationDate=" + registrationDate +
                '}';
    }
}
