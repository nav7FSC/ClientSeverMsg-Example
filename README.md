# Client-Server Messenger Application

This project is a simple JavaFX-based client-server application that allows users to send messages to each other. The application consists of a server and a client interface, where the client can connect to the server, send messages, and receive responses. The server listens for incoming client connections and displays the received messages.

## Features

- **Server**: The server application listens on a specific port and waits for clients to connect. Once a connection is established, the server can receive messages and respond back to the client.
- **Client**: The client application allows users to send messages to the server. It displays the server's response and updates the UI accordingly.
- **Real-time Chat**: Both client and server can send and receive messages in real-time.
- **Port Testing**: The application allows the user to test whether a specific port on a server is open and ready to accept connections.

## Technologies Used

- **JavaFX**: A framework for building rich graphical user interfaces.
- **Java**: The primary programming language used to implement the logic.
- **Socket Programming**: The client-server communication is based on socket programming for real-time message exchange.
- **FXML**: JavaFX's XML-based markup language used for defining the user interface.

## Author

- **Nav Singh** 