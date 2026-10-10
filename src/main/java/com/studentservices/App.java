package com.studentservices;

import com.studentservices.util.DatabaseManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

/**
 * Main JavaFX Application class for Student Services Desktop App.
 * 
 * Isaac Miti | 202509238 | ICT261 | Mr. E. Nyirenda
 */
public class App extends Application {

    @Override
    public void init() {
        // Automatically initialize embedded H2 database and schema at startup
        System.out.println("[App] Initializing database subsystem...");
        DatabaseManager.initializeDatabase();
    }

    @Override
    public void start(Stage primaryStage) throws IOException {
        URL fxmlLocation = getClass().getResource("/com/studentservices/fxml/RegisterView.fxml");
        if (fxmlLocation == null) {
            throw new IllegalStateException("FXML file not found: /com/studentservices/fxml/RegisterView.fxml");
        }

        FXMLLoader loader = new FXMLLoader(fxmlLocation);
        Parent root = loader.load();

        Scene scene = new Scene(root, 1020, 650);
        primaryStage.setTitle("Student Services Desktop App — Isaac Miti (202509238) | ICT261 | Mulungushi University");
        primaryStage.setMinWidth(920);
        primaryStage.setMinHeight(580);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
