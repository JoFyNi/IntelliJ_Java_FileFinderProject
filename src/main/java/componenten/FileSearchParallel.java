package componenten;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FileSearchParallel {
    private static final int THREAD_COUNT = Runtime.getRuntime().availableProcessors();

    public static void main(String[] args) throws Exception {
        String searchPattern = ".txt";  // Beispiel-Suchmuster
        Path startPath = Paths.get("C:\\");  // Beispiel-Verzeichnis

        List<File> resultFiles = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(startPath)) {
            for (Path entry : stream) {
                executor.submit(() -> {
                    if (entry.toString().endsWith(searchPattern)) {
                        synchronized (resultFiles) {
                            resultFiles.add(entry.toFile());
                        }
                        System.out.println("Found: " + entry);
                    }
                });
            }
        }

        executor.shutdown();
        executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

        // Ausgabe der gefundenen Dateien
        System.out.println("Gefundene Dateien:");
        for (File file : resultFiles) {
            System.out.println(file.getAbsolutePath());
        }
    }
}