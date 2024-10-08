package componenten;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

public class searchThreadWithSelectedType extends Thread {

    // fileSystem -> https://www.straub.as/java/history/file-files-paths-path-fileSystems-fileSystem-fileStore.html
    private static int finalTotal = 0;

    /**
     * call this Method with a file name and a filetype like docs or pdf.
     * If you want to search a file with all kind of filetypes you can use **
     * This method is checking every driver step by step and print the results inside the console
     */
    public searchThreadWithSelectedType(String file, String typ) {
        File[] paths;

        paths = File.listRoots();

        for (File path : paths) {
            String str = path.toString();
            String slash = "\\";

            String s = new StringBuilder(str).append(slash).toString();

            Path startingDir = Paths.get(s);

            String pattern = file +"."+ typ;

            searchThreadWithSelectedType.Finder finder = new searchThreadWithSelectedType.Finder(pattern);
            try {
                System.out.println("searching for "+ pattern);
                Files.walkFileTree(startingDir, finder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            finder.done();
        }
        System.out.println("Total Matched Number of Files : " + finalTotal);
    }

    public static class Finder
            extends SimpleFileVisitor<Path> {

        private final PathMatcher matcher;
        private int numMatches = 0;
        public static Path PathResult;

        Finder(String pattern) {
            matcher = FileSystems.getDefault()
                    .getPathMatcher("glob:" + pattern);
        }

        // Compares the glob pattern against
        // the file or directory name.
        PathMatcher find(Path file) throws InterruptedException {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                numMatches++;
                System.out.println(file);
                PathResult = file;
                return this.matcher;
            }
            return null;
        }

        // Prints the total number of
        // matches to standard out.
        void done() {
            System.out.println("Matched: "
                    + numMatches);
            finalTotal = finalTotal + numMatches;
        }

        // Invoke the pattern matching
        // method on each file.
        @Override
        public FileVisitResult visitFile(Path file,
                                         BasicFileAttributes attrs) {
            try {
                find(file);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return CONTINUE;
        }

        // Invoke the pattern matching
        // method on each directory.
        @Override
        public FileVisitResult preVisitDirectory(Path dir,
                                                 BasicFileAttributes attrs) {
            try {
                find(dir);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(Path file,
                                               IOException exc) {
            //            System.err.println(exc);
            return CONTINUE;
        }
    }

    @Override
    public void run() {

    }
}