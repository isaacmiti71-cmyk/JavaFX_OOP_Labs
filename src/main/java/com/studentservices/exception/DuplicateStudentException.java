package com.studentservices.exception;

/**
 * Exception thrown when an attempt is made to register a student with an ID that already exists.
 * 
 * Isaac Miti | 202509238 | ICT261 | Mr. E. Nyirenda
 */
public class DuplicateStudentException extends RuntimeException {

    public DuplicateStudentException(String message) {
        super(message);
    }

    public DuplicateStudentException(String message, Throwable cause) {
        super(message, cause);
    }
}
