import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class WordCounter implements Runnable{ //make it executable with threads
    private final String serverAddress;
    private final Map<String, Integer> wordCounts; //Key: String, Value: Integer

    public WordCounter(String serverAddress, Map<String, Integer> wordCounts) {
        this.serverAddress = serverAddress;
        this.wordCounts = wordCounts;
    }

    @Override
    public void run() {
        //Connecting to server
        try {
            String[] parts = serverAddress.split(":");
            String host = parts[0];
            int port = Integer.parseInt(parts[1]);

            Socket socket = new Socket(host, port);
            System.out.println("Connected to: " + this.serverAddress);

            // Using bufferedreader because it's better in large files
            try (BufferedReader br = new BufferedReader( new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] words = line.toLowerCase().split("\\W+"); //there's probably a more effective regex
                    for (String word : words) {
                        if (!word.isEmpty()) {
                            this.wordCounts.merge(word, 1, Integer::sum);
                        }
                    }
                }
            }

            socket.close();
            System.out.println("Done with: " + this.serverAddress);
        } catch (IOException e) {
            System.err.println("Error when reading " + this.serverAddress + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}