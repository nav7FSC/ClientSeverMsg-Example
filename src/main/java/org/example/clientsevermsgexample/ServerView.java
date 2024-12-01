package org.example.clientsevermsgexample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Represents the server-side view in a client-server messaging application.
 * This class is responsible for managing the UI components used to send and display messages.
 */
public class ServerView {

    /** The main container that holds all UI components. */
    @FXML
    private AnchorPane ap_main;

    /** The button used to send messages. */
    @FXML
    private Button button_send;

    /** The scrollable pane that displays the list of messages. */
    @FXML
    private ScrollPane sp_main;

    /** The text field where the user types a message to send. */
    @FXML
    private TextField tf_message;

    /** The container that holds the individual message nodes. */
    @FXML
    private VBox vbox_messages;
}
