package org.example.clientsevermsgexample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * The {@code NetworkApplication} class is the main entry point for the JavaFX application.
 * It loads the user interface from an FXML file and sets up the primary stage.
 */
public class NetworkApplication extends Application {

    /**
     * Starts the JavaFX application by loading the FXML layout and initializing the stage.
     *
     * @param stage the primary stage for this application.
     * @throws IOException if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(NetworkApplication.class.getResource("main_form.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 580, 375);
        stage.setTitle("Test Server!");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * The main method serves as the entry point of the application.
     * It launches the JavaFX runtime.
     *
     * @param args the command-line arguments.
     */
    public static void main(String[] args) {
        launch();
    }
}
