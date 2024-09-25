package componenten;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.RecursiveTask;

public class FileSearcherTask extends RecursiveTask<Void> {
    private final Path dir;
    private final String searchPattern;
    private final MainUI ui;

    public FileSearcherTask(Path dir, String searchPattern, MainUI ui) {
        this.dir = dir;
        this.searchPattern = searchPattern;
        this.ui = ui;
    }

    @Override
    protected Void compute() {
        try {
            Files.walkFileTree(dir, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    // Dateien, die dem Suchmuster entsprechen, sofort verarbeiten
                    if (file.getFileName().toString().contains(searchPattern)) {
                        // In die Konsole schreiben
                        System.out.println("Gefunden: " + file.toString());

                        // In den JTable eintragen
                        SwingUtilities.invokeLater(() -> ui.addFileToTable(file.toFile()));
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) {
                    // Fehler ignorieren und trotzdem weiter suchen
                    System.err.println("Zugriff verweigert: " + file + " (" + exc.getMessage() + ")");
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
