package org.example.clientsevermsgexample;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * The {@code ClientView} class handles the client-side UI and network communication with the server.
 * It provides functionality for sending and receiving messages through a GUI built with JavaFX.
 */
public class ClientView {
    @FXML
    private TextArea chatArea;

    @FXML
    private AnchorPane ap_main;

    @FXML
    private Button button_send;

    @FXML
    private ScrollPane sp_main;

    @FXML
    private TextField tf_message;

    @FXML
    private VBox vbox_messages;

    private PrintWriter out;
    private BufferedReader in;

    /**
     * Initializes the client view, sets up the connection to the server, and starts a thread to listen for incoming messages.
     * This method is called automatically by the JavaFX framework when the scene is loaded.
     */
    public void initialize() {
        try {
            Socket socket = new Socket("localhost", 6666);
            out = new PrintWriter(socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Starts a new thread to listen for server responses.
            new Thread(() -> {
                String response;
                try {
                    while ((response = in.readLine()) != null) {
                        updateChatArea(response);
                    }
                } catch (Exception e) {
                    updateChatArea("Error: " + e.getMessage());
                }
            }).start();

        } catch (Exception e) {
            updateChatArea("Error: " + e.getMessage());
        }
    }

    /**
     * Sends a message to the server when the send button is clicked.
     * The message is taken from the text field and then cleared after sending.
     *
     * @param event the action event triggered by clicking the send button
     */
    @FXML
    private void sendMessage(ActionEvent event) {
        String message = tf_message.getText();
        if (message != null && !message.isEmpty()) {
            out.println(message);
            updateChatArea("You: " + message);
            tf_message.clear();
        }
    }

    /**
     * Updates the chat area with a new message. This method ensures that UI updates
     * are performed on the JavaFX application thread using {@code Platform.runLater}.
     *
     * @param message the message to display in the chat area
     */
    private void updateChatArea(String message) {
        Platform.runLater(() -> chatArea.appendText(message + "\n"));
    }
}
