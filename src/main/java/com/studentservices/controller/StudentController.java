package com.studentservices.controller;

import com.studentservices.exception.DuplicateStudentException;
import com.studentservices.exception.ValidationException;
import com.studentservices.model.Student;
import com.studentservices.repository.StudentRepositoryImpl;
import com.studentservices.service.StudentService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

/**
 * Controller layer responsible for binding UI inputs, delegating to the StudentService,
 * catching business exceptions, and presenting friendly feedback to the user.
 * 
 * Isaac Miti | 202509238 | ICT261 | Mr. E. Nyirenda
 */
public class StudentController {

    @FXML
    private TextField studentIdField;

    @FXML
    private TextField fullNameField;

    @FXML
    private TextField emailField;

    @FXML
    private ComboBox<String> programmeComboBox;

    @FXML
    private TableView<Student> studentTableView;

    @FXML
    private TableColumn<Student, String> colStudentId;

    @FXML
    private TableColumn<Student, String> colFullName;

    @FXML
    private TableColumn<Student, String> colEmail;

    @FXML
    private TableColumn<Student, String> colProgramme;

    @FXML
    private TableColumn<Student, String> colRegistrationDate;

    @FXML
    private Label announcementLabel;

    @FXML
    private Label totalStudentsBadge;

    @FXML
    private Label statusLabel;

    // Service dependency (Strict layered architecture: Controller -> Service)
    private final StudentService studentService;

    // Observable list backing the JavaFX TableView
    private final ObservableList<Student> studentObservableList = FXCollections.observableArrayList();

    public StudentController() {
        // Instantiate service with repository implementation
        this.studentService = new StudentService(new StudentRepositoryImpl());
    }

    // Constructor injection for testability or custom instantiation
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    /**
     * Initializes UI components, table cell value factories, and loads existing student records.
     */
    @FXML
    public void initialize() {
        setupTableColumns();
        setupProgrammeDropdown();
        loadAnnouncement();
        refreshStudentTable();
    }

    /**
     * Configures the TableView columns to extract values from Student properties.
     */
    private void setupTableColumns() {
        colStudentId.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudentId()));
        colFullName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFullName()));
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));
        colProgramme.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getProgramme()));
        colRegistrationDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedRegistrationDate()));

        studentTableView.setItems(studentObservableList);
    }

    /**
     * Populates available Mulungushi University academic programmes.
     */
    private void setupProgrammeDropdown() {
        programmeComboBox.getItems().addAll(
                "BSc Software Engineering",
                "BSc Computer Science",
                "BSc Information Technology",
                "BSc Electrical & Electronic Engineering",
                "BSc Mechanical Engineering",
                "BSc Civil Engineering"
        );
    }

    /**
     * Loads live campus announcements via the service layer.
     */
    private void loadAnnouncement() {
        try {
            String announcement = studentService.getLatestAnnouncement();
            announcementLabel.setText(announcement);
        } catch (Exception e) {
            announcementLabel.setText("📢 Welcome to Mulungushi University Student Services");
        }
    }

    /**
     * Handles the "Register Student" button click.
     * Demonstrates defensive error handling:
     * - Catches ValidationException
     * - Catches DuplicateStudentException
     * - Preserves user input upon failure so it can be corrected easily
     */
    @FXML
    public void handleRegisterStudent(ActionEvent event) {
        String studentId = studentIdField.getText();
        String fullName = fullNameField.getText();
        String email = emailField.getText();
        String programme = programmeComboBox.getValue();

        try {
            // Forward request to Service layer
            Student registeredStudent = studentService.registerStudent(studentId, fullName, email, programme);

            // On success: refresh table, clear form, update badges
            refreshStudentTable();
            handleClearForm();

            statusLabel.setText("Status: Student " + registeredStudent.getStudentId() + " registered successfully!");
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful",
                    "Success", "Student " + registeredStudent.getFullName() +
                            " (" + registeredStudent.getStudentId() + ") was successfully registered.");

        } catch (ValidationException ve) {
            // Form validation failure - preserve user input!
            statusLabel.setText("Validation Warning: " + ve.getMessage());
            showAlert(Alert.AlertType.WARNING, "Validation Error",
                    "Input Validation Failed", ve.getMessage());

        } catch (DuplicateStudentException de) {
            // Duplicate Student ID error - preserve user input!
            statusLabel.setText("Duplicate Error: " + de.getMessage());
            showAlert(Alert.AlertType.ERROR, "Duplicate Student ID",
                    "Registration Conflict", de.getMessage() +
                            "\nPlease verify the ID or contact the records office if this is an error.");

        } catch (Exception ex) {
            // Unexpected / Database error
            System.err.println("[StudentController] Unexpected error: " + ex.getMessage());
            statusLabel.setText("Error: A system error occurred.");
            showAlert(Alert.AlertType.ERROR, "System Error",
                    "Database / Internal Error",
                    "An unexpected error occurred while processing the registration. Please check the logs.");
        }
    }

    /**
     * Clears all input fields on the registration form.
     */
    @FXML
    public void handleClearForm() {
        studentIdField.clear();
        fullNameField.clear();
        emailField.clear();
        programmeComboBox.getSelectionModel().clearSelection();
        studentIdField.requestFocus();
    }

    /**
     * Queries the service layer to reload all students and update the TableView and counter.
     */
    private void refreshStudentTable() {
        try {
            List<Student> allStudents = studentService.getAllStudents();
            studentObservableList.setAll(allStudents);
            totalStudentsBadge.setText("Total: " + allStudents.size() + " Student" + (allStudents.size() == 1 ? "" : "s"));
        } catch (Exception e) {
            System.err.println("[StudentController] Failed to refresh table: " + e.getMessage());
            statusLabel.setText("Error: Could not refresh students list.");
        }
    }

    /**
     * Displays a customized JavaFX Alert modal dialog.
     */
    private void showAlert(Alert.AlertType alertType, String title, String header, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
