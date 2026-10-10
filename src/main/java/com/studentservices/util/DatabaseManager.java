package com.studentservices.util;

import com.studentservices.exception.DatabaseOperationException;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Utility class responsible for managing the embedded H2 database connection
 * and initializing the required database schema automatically at application startup.
 * 
 * Isaac Miti | 202509238 | ICT261 | Mr. E. Nyirenda
 */
public class DatabaseManager {

    // Default file-based database stored locally in the ./data directory
    private static final String DEFAULT_DB_URL = "jdbc:h2:./data/studentsdb;DB_CLOSE_DELAY=-1";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private static String activeDbUrl = DEFAULT_DB_URL;

    // Load H2 driver explicitly
    static {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new DatabaseOperationException("H2 Database driver not found on classpath", e);
        }
    }

    /**
     * Override database URL (useful for in-memory unit tests).
     */
    public static void setDatabaseUrl(String dbUrl) {
        activeDbUrl = dbUrl;
    }

    /**
     * Resets database URL to default file-based storage.
     */
    public static void resetDatabaseUrl() {
        activeDbUrl = DEFAULT_DB_URL;
    }

    /**
     * Obtains a new JDBC Connection to the embedded H2 database.
     */
    public static Connection getConnection() throws SQLException {
        // Ensure data directory exists if using file storage
        if (activeDbUrl.contains("./data")) {
            File dataDir = new File("./data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
        }
        return DriverManager.getConnection(activeDbUrl, DB_USER, DB_PASSWORD);
    }

    /**
     * Automatically creates the STUDENTS table if it does not already exist.
     * Called once at application startup.
     */
    public static void initializeDatabase() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS students (
                student_id VARCHAR(20) PRIMARY KEY,
                full_name VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL,
                programme VARCHAR(100) NOT NULL,
                registration_date TIMESTAMP NOT NULL
            );
        """;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
            System.out.println("[DatabaseManager] Database initialized successfully. Table 'students' is ready.");
        } catch (SQLException e) {
            System.err.println("[DatabaseManager] Database initialization failed: " + e.getMessage());
            throw new DatabaseOperationException("Failed to initialize database tables", e);
        }
    }
}
