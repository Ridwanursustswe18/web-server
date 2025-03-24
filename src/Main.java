
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
public class Main {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(80), 0);
        server.createContext("/", new ServerHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server is running on port 80");
    }

}