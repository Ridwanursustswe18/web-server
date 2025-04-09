import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final int PORT = 80;
    private static final int MAX_THREADS = 50;

    public static void main(String[] args) {
        ExecutorService threadPool = Executors.newFixedThreadPool(MAX_THREADS);

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            LOGGER.info("Server started on port " + PORT);
            LOGGER.info("Waiting for client connections...");

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    threadPool.submit(new ClientHandler(serverSocket.accept()));
                } catch (IOException e) {
                    LOGGER.log(Level.SEVERE, "Error accepting client connection", e);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Could not listen on port " + PORT, e);
        }
    }
}