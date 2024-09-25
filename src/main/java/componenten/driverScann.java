package componenten;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

class driverScann extends Thread {
    private static int finalTotal = 0;

    public static class Finder extends SimpleFileVisitor<Path> {
        private final PathMatcher matcher;
        private int numMatches = 0;

        Finder(String pattern) {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        }

        void find(Path file) {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                numMatches++;
                System.out.println(file);
            }
        }

        void done() {
            System.out.println("Matched: " + numMatches);
            finalTotal += numMatches;
        }

        @Override
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
            find(file);
            return CONTINUE;
        }

        @Override
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            find(dir);
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            return CONTINUE;
        }
    }

    public static void startSearch(String filePattern) {
        File[] paths = File.listRoots();

        for (File path : paths) {
            Path startingDir = Paths.get(path.toString());

            Finder finderTXT = new Finder(filePattern + "*.txt");
            Finder finderPDF = new Finder(filePattern + "*.pdf");

            try {
                System.out.println("Start searching in: " + startingDir);
                Files.walkFileTree(startingDir, finderTXT);
                Files.walkFileTree(startingDir, finderPDF);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            finderTXT.done();
            finderPDF.done();
        }

        System.out.println("Total matched number of files: " + finalTotal);
    }

    @Override
    public void run() {
        // Implement thread logic here if needed
    }
}
