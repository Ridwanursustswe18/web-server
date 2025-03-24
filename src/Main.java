import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.InetSocketAddress;
import java.util.Objects;

public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
        server.createContext("/", new ServerHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server is running on port 80");
    }

    static class ServerHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();
            if(Objects.equals(requestPath, "/")){
                requestPath = "index.html";
            }
            String baseDirectory = "C:\\web-server\\web-server\\src\\www\\";
            Path filePath = Paths.get(baseDirectory, requestPath);
            try {
                if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                    byte[] fileContent = Files.readAllBytes(filePath);
                    String contentType = determineContentType(filePath.toString());
                    exchange.getResponseHeaders().set("Content-Type", contentType);
                    exchange.sendResponseHeaders(200, fileContent.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(fileContent);
                    os.close();
                } else {
                    String response = "404 - File not found: " + requestPath;
                    exchange.sendResponseHeaders(404, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            } catch (IOException e) {
                String response = "500 - Server error: " + e.getMessage();
                exchange.sendResponseHeaders(500, response.length());
                OutputStream os = exchange.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }

        private String determineContentType(String path) {
            if (path.endsWith(".html") || path.endsWith(".htm")) {
                return "text/html";
            } else if (path.endsWith(".txt")) {
                return "text/plain";
            } else if (path.endsWith(".css")) {
                return "text/css";
            } else if (path.endsWith(".js")) {
                return "application/javascript";
            } else if (path.endsWith(".jpg") || path.endsWith(".jpeg")) {
                return "image/jpeg";
            } else if (path.endsWith(".png")) {
                return "image/png";
            } else if (path.endsWith(".gif")) {
                return "image/gif";
            } else if (path.endsWith(".pdf")) {
                return "application/pdf";
            } else {
                return "application/octet-stream";
            }
        }
    }
}