package org.example.clientsevermsgexample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

/**
 * The {@code MainController} class handles client-server communication and manages
 * the user interface actions in a JavaFX application.
 */
public class MainController implements Initializable {

    private PrintWriter out;
    private BufferedReader in;
    private Set<ClientHandler> clients = new HashSet<>();

    @FXML
    private ComboBox<String> dropdownPort;

    @FXML
    private Button clearBtn;

    @FXML
    private TextArea resultArea;

    @FXML
    private Label server_lbl;

    @FXML
    private Button testBtn;

    @FXML
    private Label test_lbl;

    @FXML
    private TextField urlName;

    private Socket socket1;

    @FXML
    private Label lb122, lb12;

    private TextField msgText;

    /**
     * Initializes the controller by populating the dropdown with port numbers and starting the server.
     *
     * @param location  the location used to resolve relative paths for the root object
     * @param resources the resources used to localize the root object
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dropdownPort.getItems().addAll(
                "7",     // ping
                "13",    // daytime
                "21",    // ftp
                "23",    // telnet
                "71",    // finger
                "80",    // http
                "119",   // nntp (news)
                "161"    // snmp
        );

        // Starts the server in a new thread
        new Thread(this::runServer).start();
    }

    /**
     * Checks the connection to the specified host and port.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    void checkConnection(ActionEvent event) {
        String host = urlName.getText();
        int port = Integer.parseInt(dropdownPort.getValue().toString());

        try (Socket sock = new Socket(host, port)) {
            resultArea.appendText(host + " listening on port " + port + "\n");
        } catch (UnknownHostException e) {
            resultArea.setText(String.valueOf(e) + "\n");
        } catch (Exception e) {
            resultArea.appendText(host + " not listening on port " + port + "\n");
        }
    }

    /**
     * Clears the result area and the URL input field.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    void clearBtn(ActionEvent event) {
        resultArea.setText("");
        urlName.setText("");
    }

    /**
     * Starts a new server window and displays server information.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    void startServer(ActionEvent event) {
        Stage stage = new Stage();
        Group root = new Group();
        Label lb11 = new Label("Server");
        lb11.setLayoutX(100);
        lb11.setLayoutY(100);

        lb12 = new Label("info");
        lb12.setLayoutX(100);
        lb12.setLayoutY(200);
        root.getChildren().addAll(lb11, lb12);
        Scene scene = new Scene(root, 600, 350);
        stage.setScene(scene);
        lb12.setText("Server is running and waiting for a client...");

        stage.setTitle("Server");
        stage.show();
    }

    /**
     * Runs the server, accepts client connections, and handles communication.
     */
    private void runServer() {
        try (ServerSocket serverSocket = new ServerSocket(6666)) {
            updateServer("Server is running and waiting for a client...");
            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler clientHandler = new ClientHandler(clientSocket, clients);
                    clients.add(clientHandler);
                    new Thread(clientHandler).start();
                    updateServer("Client connected!");
                } catch (IOException e) {
                    updateServer("Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            updateServer("Error: " + e.getMessage());
        }
    }

    /**
     * Updates the server information label with the given message.
     *
     * @param message the message to display
     */
    private void updateServer(String message) {
        javafx.application.Platform.runLater(() -> lb12.setText(message + "\n"));
    }

    /**
     * Starts a new client window and sets up the user interface for client actions.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    void startClient(ActionEvent event) {
        Stage stage = new Stage();
        Group root = new Group();
        Button connectButton = new Button("Connect to server");
        connectButton.setLayoutX(100);
        connectButton.setLayoutY(300);
        connectButton.setOnAction(this::connectToServer);

        Button sendButton = new Button("Send");
        sendButton.setLayoutX(200);
        sendButton.setLayoutY(300);
        sendButton.setOnAction(this::sendMessage);

        Label lb11 = new Label("Client");
        lb11.setLayoutX(100);
        lb11.setLayoutY(100);
        msgText = new TextField("msg");
        msgText.setLayoutX(100);
        msgText.setLayoutY(150);

        lb122 = new Label("info");
        lb122.setLayoutX(100);
        lb122.setLayoutY(200);
        root.getChildren().addAll(lb11, lb122, connectButton, msgText);

        Scene scene = new Scene(root, 600, 350);
        stage.setScene(scene);
        stage.setTitle("Client");
        stage.show();
    }

    /**
     * Connects to the server and handles communication in a separate thread.
     *
     * @param event the action event triggered by the user
     */
    private void connectToServer(ActionEvent event) {
        try {
            socket1 = new Socket("localhost", 6666);
            out = new PrintWriter(socket1.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(socket1.getInputStream()));

            new Thread(() -> {
                String response;
                try {
                    while ((response = in.readLine()) != null) {
                        updateTextClient(response);
                    }
                } catch (IOException e) {
                    updateTextClient("Error: " + e.getMessage());
                }
            }).start();
            out.println(msgText.getText());
        } catch (Exception e) {
            updateTextClient("Error: " + e.getMessage() + "\n");
        }
    }

    /**
     * Sends a message to the server.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    void sendMessage(ActionEvent event) {
        if (out != null) {
            out.println(msgText.getText());
        }
    }

    /**
     * Updates the client information label with the given message.
     *
     * @param message the message to display
     */
    private void updateTextClient(String message) {
        javafx.application.Platform.runLater(() -> lb122.setText(message + "\n"));
    }

    /**
     * Opens a new window for user 1.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    void user1Msg(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/clientsevermsgexample/client-view.fxml"));
            Parent root = loader.load();
            ClientView controller = loader.getController();
            controller.initialize();
            Scene scene = new Scene(root, 500, 400);
            stage.setScene(scene);
            stage.setTitle("User 1");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a new window for user 2.
     *
     * @param event the action event triggered by the user
     */
    @FXML
    void user2Msg(ActionEvent event) {
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/clientsevermsgexample/client-view.fxml"));
            Parent root = loader.load();
            ClientView controller = loader.getController();
            controller.initialize();
            Scene scene = new Scene(root, 500, 400);
            stage.setScene(scene);
            stage.setTitle("User 2");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
