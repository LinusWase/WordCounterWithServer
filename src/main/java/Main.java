import java.io.IOException;
import java.util.concurrent.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {

        //ConcurrentHashMap allows multithreading without problems
        Map<String, Integer> globalWordCounts = new ConcurrentHashMap<>(); //Key: String, Value: Integers

        // Could be hidden if needed
        String server1 = "localhost:8000";
        String server2 = "localhost:8001";

        //Creating executor to create multithreading with 2 threads
        ExecutorService executor = Executors.newFixedThreadPool(2);

        executor.submit(new WordCounter(server1, globalWordCounts));
        executor.submit(new WordCounter(server2, globalWordCounts));

        executor.shutdown();
        System.out.println("Waiting for the threads to finish...");

        //If executor is not done after 1 minute
        if (!executor.awaitTermination(1, TimeUnit.MINUTES)) {
            System.err.println("Time limit exceeded. Forcing stop");
            executor.shutdownNow();
            return;
        }

        //List with Map.Entry<String, Integer> elements.
        List<Map.Entry<String, Integer>> topWords = globalWordCounts.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) //Sort with the largest first
                .limit(5) //5 largest
                .toList(); //Collecting in a list

        System.out.println("\nMost common words:");
        for (int i = 0; i < topWords.size(); i++){
            Map.Entry<String, Integer> entry = topWords.get(i);
            System.out.println(i + 1 + ". " + entry.getKey() + " (" + entry.getValue() + " occurrences)");
        }
    }
}