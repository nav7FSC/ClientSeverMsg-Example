package org.example.clientsevermsgexample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Set;

/**
 * The {@code ClientHandler} class handles communication between a server and a connected client.
 * It implements the {@code Runnable} interface to support multithreaded operations, allowing
 * each client to be handled in a separate thread.
 */
class ClientHandler implements Runnable {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Set<ClientHandler> clients;

    /**
     * Constructs a new {@code ClientHandler} instance for a given client socket and set of connected clients.
     *
     * @param clientSocket the socket associated with the connected client
     * @param clients      the set of all connected {@code ClientHandler} instances
     */
    public ClientHandler(Socket clientSocket, Set<ClientHandler> clients) {
        this.socket = clientSocket;
        this.clients = clients;
        try {
            this.out = new PrintWriter(socket.getOutputStream(), true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The entry point of the {@code ClientHandler} thread. Listens for messages from the client,
     * processes them, and broadcasts responses to other clients. Terminates on receiving an "exit" message.
     */
    @Override
    public void run() {
        String message;
        try {
            while ((message = in.readLine()) != null) {
                System.out.println("Received from client: " + message);
                if (message.equalsIgnoreCase("exit")) {
                    break;
                }
                broadcastMessage("Response: " + message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cleanup();
        }
    }

    /**
     * Broadcasts a message to all connected clients except the sender.
     * If there is only one client, it echoes the message back.
     *
     * @param message the message to be broadcast to all clients
     */
    private void broadcastMessage(String message) {
        System.out.println("Broadcasting msg: " + message);
        for (ClientHandler client : clients) {
            if (client != this || clients.size() == 1) {
                client.out.println(message);
            }
        }
    }

    /**
     * Closes the input/output streams and the socket associated with this client.
     * This method is called when the client disconnects or the thread finishes execution.
     */
    private void cleanup() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
