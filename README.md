# Parallel Word Counter Client-Server

This project is a Java application that demonstrates a multi-threaded system. A client connects to two separate servers in parallel to download large text files, process them as a stream, and count the most frequent words in both texts combined.

The project is created to illustrate fundamental concepts in network programming and multithreading in Java.

This project is built on the strict design principle that the servers act purely as data providers. Their sole responsibility is to stream the requested text file and terminate the connection.

Crucially, the client is responsible for all subsequent logic.

## Architecture

The system consists of three main components:

1. **`Main.java` (The Client):**
    * Orchestrates the entire process.
    * Uses an `ExecutorService` with a thread pool to start two `WordCounter` tasks that run in parallel.
    * Collects the word count from both threads into a thread-safe `ConcurrentHashMap`.
    * Sorts and presents the 5 most frequent words when both threads are complete.

2. **`FirstServer.java` & `SecondServer.java` (The Servers):**
    * Two simple TCP servers listening on separate ports (8000 and 8001).
    * When a client connects, the server streams a predefined text file (`dracula.txt` or `frankenstein.txt`) to the client and then closes the connection.
    * **Note:** To clearly visualize an architecture with two separate server processes, `FirstServer` and `SecondServer` are intentionally identical in their codebase. In a real-world application, one would instead create a single, configurable server class to avoid code duplication.

3. **`WordCounter.java` (The Worker Thread):**
    * A `Runnable` class that performs the logic to connect to a server, read the text stream line by line using a `BufferedReader`, and update the shared `ConcurrentHashMap`.

## How to Run the Project

### Prerequisites
* Java Development Kit (JDK) installed.
* The text files `dracula.txt` and `frankenstein.txt` placed in the `src/main/data/` directory.

### Step-by-Step

1. **Start the first server:**
    Start `FirstServer` in its own terminal. It will listen on port 8000.
    ```bash
    java FirstServer
    ```

2. **Start the second server:**
    Open a *new* terminal and start `SecondServer`. It will listen on port 8001.
    ```bash
    java SecondServer
    ```

3. **Run the client:**
    Open the Main.java file in the editor.

- Click the Run button (▶) located at the top-right corner of the editor, or use the Run and Debug view (Ctrl+Shift+D / Cmd+Shift+D) and click "Run".

## Key Design Concepts

* **Parallel Processing:** `ExecutorService` is used to efficiently manage threads and run network operations in parallel, significantly reducing the total execution time.
* **Thread Safety:** `ConcurrentHashMap` is used to safely allow multiple threads to write to the same data structure without causing conflicts or data corruption.
* **Efficient Memory Management:** The client reads data as a stream using `BufferedReader` and processes it line by line. This ensures that the entire file never needs to be loaded into memory, allowing the system to handle extremely large files.
* **Robust Resource Management:** `try-with-resources` is used for all `Socket` and file streams to guarantee that resources are closed properly, even if an error occurs.

## License

This project is licensed under the **MIT License**.
