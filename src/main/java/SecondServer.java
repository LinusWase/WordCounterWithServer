import java.io.*;
import java.net.*;
import java.nio.file.*;

public class SecondServer {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(8001)) {

            while (true) { // Continue when connected
                Socket clientSocket = serverSocket.accept();

                try {
                    File file = new File("src/main/data" + File.separator + "frankenstein.txt");
                    if (!file.exists()) {
                        System.out.println("File not found: " + file.getAbsolutePath());
                        clientSocket.close();
                        return;
                    }
                    try (OutputStream out = clientSocket.getOutputStream()) {
                        Files.copy(Path.of("src/main/data/frankenstein.txt"), out);
                    }

                    clientSocket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}