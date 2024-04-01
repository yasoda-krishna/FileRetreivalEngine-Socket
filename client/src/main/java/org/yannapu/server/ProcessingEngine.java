package server;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ProcessingEngine {
    private IndexStore indexStore;
    private ExecutorService executorService;

    public ProcessingEngine() {
        this.indexStore = new IndexStore();
        this.executorService = Executors.newFixedThreadPool(8);
    }

    public String indexFiles(String directoryPath) {
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("start");
            Files.walk(Paths.get(directoryPath))
                    .filter(Files::isRegularFile)
                    .filter(filePath -> !filePath.getFileName().toString().equals(".DS_Store"))
                    .filter(filePath -> filePath.toString().endsWith(".txt"))
                    .forEach(filePath -> executorService.submit(new IndexingTask(filePath, indexStore)));
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Indexing completed in " + duration + " milliseconds.");
            System.out.println("end");
            return "Indexing request for directory " + directoryPath + " has been processed.";

        } catch (IOException e) {
            System.err.println("Failed to walk through directory: " + e.getMessage());

        }
        return "Indexing request for directory " + directoryPath + " has been Failed.";
    }


    public String searchFiles(String query) {
        long startTime = System.currentTimeMillis();
        System.out.println("Searching for: " + query);
        List<String> results = indexStore.search(query.toLowerCase());
        //System.out.println(results.size());
        if (results.isEmpty()) {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Indexing completed in " + duration + " milliseconds.");
            System.out.println("Indexing completed in " + duration + " milliseconds.");
            return "No results found.";
        } else {
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            System.out.println("Indexing completed in " + duration + " milliseconds.");
            System.out.println("Indexing completed in " + duration + " milliseconds.");
            return String.join("-", results);
        }

    }

    public void shutdown() {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executorService.shutdownNow();
        }
    }

    private static class IndexingTask implements Runnable {
        private final Path filePath;
        private final IndexStore indexStore;

        IndexingTask(Path filePath, IndexStore indexStore) {
            this.filePath = filePath;
            this.indexStore = indexStore;
        }

        @Override
        public void run() {

            Map<String, Integer> localIndex = new HashMap<>();
            try {
                Files.lines(filePath)
                        .flatMap(line -> Arrays.stream(line.split("\\W+")))
                        .filter(word -> !word.isEmpty())
                        .forEach(word -> localIndex.merge(word.toLowerCase(), 1, Integer::sum));

                indexStore.updateIndex(filePath.toString(), localIndex);
            } catch (IOException e) {
                System.err.println("Error processing file " + filePath + ": " + e.getMessage());
            }



        }
    }
}