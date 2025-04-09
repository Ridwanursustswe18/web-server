import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientHandler implements Runnable {
    private static final Logger LOGGER = Logger.getLogger(ClientHandler.class.getName());
    private final Socket clientSocket;
    public RequestHandler requestHandler = new RequestHandler();
    public ClientHandler(Socket socket) {
        this.clientSocket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader input = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter output = new PrintWriter(clientSocket.getOutputStream(), true);
                clientSocket
        ) {
            LOGGER.info("Client connected from " + clientSocket.getInetAddress());

            String inputLine;
            StringBuilder requestBuilder = new StringBuilder();
            while ((inputLine = input.readLine()) != null && !inputLine.isEmpty()) {
                requestBuilder.append(inputLine).append("\n");
            }
            String request = requestBuilder.toString();
            LOGGER.info("Received request:\n" + request);
            String[] parts = request.split(" ");
            String path = parts[1];
            String response = requestHandler.readFileContentAsText(path);
            output.println("HTTP/1.1 200 OK");
            output.println("Content-Type: text/plain");
            output.println();
            output.println(response);

        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error handling client connection", e);
        }
    }

    private String processRequest(String request) {
        return "Server received your request. Request details:\n" + request;
    }
}