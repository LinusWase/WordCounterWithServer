import java.io.*;
import java.net.*;
import java.nio.file.*;

public class SecondServer {
    public static void main(String[] args) throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(8001)) {

            while (true) { // Continue when connected
                try (Socket clientSocket = serverSocket.accept();) {

                    try {
                        //Get file
                        File file = new File("src/main/data" + File.separator + "frankenstein.txt");
                        if (!file.exists()) {
                            System.out.println("File not found: " + file.getAbsolutePath());
                            clientSocket.close();
                            return;
                        }
                        //Returns file to client
                        //For really large files this would not be optimal
                        try (OutputStream out = clientSocket.getOutputStream()) {
                            Files.copy(Path.of("src", "main", "data", "frankenstein.txt"), out);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}