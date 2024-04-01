package client;

import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        String hostName = "localhost";
        int portNumber = 8080;

        try (Socket socket = new Socket(hostName, portNumber);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Connected to the server.");

            String userInput;
            System.out.println("File Retrieval Engine");
            System.out.println("Available commands:");
            System.out.println("index <path>  - Enter relative path with the index");
            System.out.println("search <query> - Enter the valid query");
            System.out.println("quit  - Exit the program");
            System.out.print("> ");
            while ((userInput = stdIn.readLine()) != null && !"quit".equalsIgnoreCase(userInput)) {
                out.println(userInput); // Send user input to server
                System.out.println("Server response: \n"); // Read response from server
                String[] s = (in.readLine()).split("-");
                for(int i=0;i<s.length;i++)
                {
                    System.out.println(s[i]);
                }
                System.out.println("File Retrieval Engine");
                System.out.println("Available commands:");
                System.out.println("index <path>  - Enter relative path with the index");
                System.out.println("search <query> - Enter the valid query");
                System.out.println("quit  - Exit the program");
                System.out.print("> ");
            }
            if ("quit".equalsIgnoreCase(userInput)) {
                out.println(userInput); // Inform server about quitting
                System.out.println("Server response: \n" + in.readLine()); // Read server's goodbye message
            }
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host " + hostName);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to " + hostName);
            System.exit(1);
        }
    }
}