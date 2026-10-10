package com.example.hellofx;

// JavaFX imports
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HelloJavaFX extends Application {

    // List that will store all customer objects
    private ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        // Label for the customer name field
        Label nameLabel = new Label("Customer Name");

        // Text field where the user enters the customer's name
        TextField nameField = new TextField();
        // Create a drop-down list for selecting a province
        ComboBox<String> provinceBox = new ComboBox<>();

// Add provinces to the drop-down list
        provinceBox.getItems().addAll(
                "Central",
                "Copperbelt",
                "Eastern",
                "Luapula",
                "Lusaka",
                "Muchinga",
                "Northern",
                "North-Western",
                "Southern",
                "Western"
        );

// Display a prompt before a province is selected
        provinceBox.setPromptText("Select Province");
        Button addButton = new Button("Add Customer");
        // Create a table to display customers
        TableView<Customer> customerTable = new TableView<>();

// Create a column for customer names
        TableColumn<Customer, String> nameColumn = new TableColumn<>("Customer Name");

// Tell the column to get the name from Customer's getName() method
        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

// Create a column for provinces
        TableColumn<Customer, String> provinceColumn = new TableColumn<>("Province");

// Tell the column to get the province from Customer's getProvince() method
        provinceColumn.setCellValueFactory(
                new PropertyValueFactory<>("province")
        );

// Add the columns to the table
        customerTable.getColumns().addAll(nameColumn, provinceColumn);

// Connect the table to our customer list
        customerTable.setItems(customers);

        // Create the Delete Customer button
        Button deleteButton = new Button("Delete Customer");

// Run this code when the Delete Customer button is clicked
        deleteButton.setOnAction(e -> {

            // Get the customer selected in the table
            Customer selectedCustomer =
                    customerTable.getSelectionModel().getSelectedItem();

            // Check if a customer was selected
            if (selectedCustomer != null) {

                // Create a confirmation message
                Alert confirmation = new Alert(
                        Alert.AlertType.CONFIRMATION
                );

                // Set the title of the confirmation window
                confirmation.setTitle("Delete Customer");

                // Set the header text
                confirmation.setHeaderText("Delete selected customer?");

                // Set the message
                confirmation.setContentText(
                        "Are you sure you want to delete "
                                + selectedCustomer.getName() + "?"
                );

                // Display the confirmation box and wait for the user's choice
                confirmation.showAndWait().ifPresent(response -> {

                    // Delete the customer only if OK is selected
                    if (response == ButtonType.OK) {
                        customers.remove(selectedCustomer);
                    }
                });
            }
        });

// Run this code when the Add Customer button is clicked
        addButton.setOnAction(e -> {

            // Get the name entered in the text field
            String name = nameField.getText().trim();

            // Get the province selected from the drop-down list
            String province = provinceBox.getValue();

            // Check if the name or province is missing
            if (name.isEmpty() || province == null) {

                // Create a warning message
                Alert warning = new Alert(Alert.AlertType.WARNING);

                // Set the title of the warning window
                warning.setTitle("Invalid Input");

                // Set the header text
                warning.setHeaderText("Missing Information");

                // Tell the user what needs to be entered
                warning.setContentText(
                        "Please enter a customer name and select a province."
                );

                // Display the warning
                warning.showAndWait();

                // Stop here so no customer is added
                return;
            }

            // Create a new Customer object
            Customer customer = new Customer(name, province);

            // Add the customer to the customer list
            customers.add(customer);

            // Clear the name field after adding the customer
            nameField.clear();

            // Clear the selected province
            provinceBox.setValue(null);
        });

        // Allow the Enter key to activate the Add Customer button
        nameField.setOnAction(e -> addButton.fire());

        // Create the main vertical layout
        VBox layout = new VBox(20);

        // Center all controls in the layout
        layout.setAlignment(Pos.CENTER);
// Add the customer name and province controls to the layout
        layout.getChildren().addAll(
                nameLabel,
                nameField,
                provinceBox,
                addButton,
                deleteButton,
                customerTable
        );
        // Create the application scene
        Scene scene = new Scene(layout, 600, 500);

        // Set the window title
        stage.setTitle("Customer Manager");

        // Put the scene inside the window
        stage.setScene(scene);

        // Set the starting keyboard focus to the name field
        nameField.requestFocus();

        // Display the window
        stage.show();
    }

    public static void main(String[] args) {
        // Start the JavaFX application
        launch(args);
    }
}