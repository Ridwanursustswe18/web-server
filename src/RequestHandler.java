import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler {
    String baseDirectory = "C:\\web-server\\web-server\\src\\www\\";
    public static String determineContentType(String path) {
        Map<String, String> contentTypes = new HashMap<>();
        contentTypes.put(".html", "text/html");
        contentTypes.put(".htm", "text/html");
        contentTypes.put(".txt", "text/plain");
        contentTypes.put(".css", "text/css");
        contentTypes.put(".js", "application/javascript");
        contentTypes.put(".jpg", "image/jpeg");
        contentTypes.put(".jpeg", "image/jpeg");
        contentTypes.put(".png", "image/png");
        contentTypes.put(".gif", "image/gif");
        contentTypes.put(".pdf", "application/pdf");
        int lastDotIndex = path.lastIndexOf('.');
        if (lastDotIndex > 0) {
            String extension = path.substring(lastDotIndex);
            return contentTypes.getOrDefault(extension, "application/octet-stream");
        }
        return "application/octet-stream";
    }
    public String readFileContentAsText(String requestPath) throws IOException {
        Path sanitizedPath = new File(baseDirectory, requestPath).toPath().normalize();
        if (!sanitizedPath.startsWith(baseDirectory)) {
            throw new SecurityException("Access to file outside base directory is not allowed");
        }
        byte[] bytes = Files.readAllBytes(sanitizedPath);
        return new String(bytes, StandardCharsets.UTF_8);
    }}