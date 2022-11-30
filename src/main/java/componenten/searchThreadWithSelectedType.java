package componenten;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static componenten.MainUI.getResultToListOnTable;
import static java.nio.file.FileVisitResult.CONTINUE;

public class searchThreadWithSelectedType extends Thread {

    // fileSystem -> https://www.straub.as/java/history/file-files-paths-path-fileSystems-fileSystem-fileStore.html
    private static int finalTotal = 0;

    public searchThreadWithSelectedType(String file, String typ) {
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();

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
        void find(Path file) throws InterruptedException {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                numMatches++;
                System.out.println(file +" (void find) " + name);
                PathResult = file;
                getResultToListOnTable(PathResult);
            }
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