import java.io.*;
import java.net.*;
import java.nio.file.*;

public class FirstServer {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {

            while (true) { // Continue when connected
                Socket clientSocket = serverSocket.accept();

                try {
                    File file = new File("src/main/data" + File.separator + "dracula.txt");
                    if (!file.exists()) {
                        System.out.println("File not found: " + file.getAbsolutePath());
                        clientSocket.close();
                        return;
                    }
                    try (OutputStream out = clientSocket.getOutputStream()) {
                        Files.copy(Path.of("src/main/data/dracula.txt"), out);
                    }

                    clientSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}