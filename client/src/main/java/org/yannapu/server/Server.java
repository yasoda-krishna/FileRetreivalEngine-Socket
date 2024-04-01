package server;

import java.io.*;
import java.net.*;

public class Server {
    public static void main(String[] args) {
        int port = 8080;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server started. Listening on port " + port);

            // Accept client connections
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connected.");

            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);

            String inputLine, response = null;
            ProcessingEngine processingEngine = new ProcessingEngine();
            // Continuously read messages from the client and respond
            while ((inputLine = in.readLine()) != null) {
                if (inputLine.startsWith("index ")) {
                    String path = inputLine.substring(6);
                    response = processingEngine.indexFiles(path);

                } else if (inputLine.startsWith("search ")) {
                    String query = inputLine.substring(7);
                    response = processingEngine.searchFiles(query);
                }
                else if ("quit".equalsIgnoreCase(inputLine)) {

                    out.println("Goodbye!");
                    break;
                }
                out.println(response); // Echo the received message back to the client
            }
        } catch (IOException e) {
            System.out.println("Exception caught when trying to listen on port " + port + " or listening for a connection");
            System.out.println(e.getMessage());
        }
    }
}