package com.studentservices.exception;

/**
 * Exception thrown when student input validation fails (e.g. empty fields, bad email format, invalid ID length).
 * 
 * Isaac Miti | 202509238 | ICT261 | Mr. E. Nyirenda
 */
public class ValidationException extends RuntimeException {

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
