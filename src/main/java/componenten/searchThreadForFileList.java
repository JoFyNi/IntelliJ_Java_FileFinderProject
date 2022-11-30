package componenten;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;

import static java.nio.file.FileVisitResult.CONTINUE;

public class searchThreadForFileList extends Thread {

    private static int finalTotal = 0;

    public searchThreadForFileList(File file, Collection<File> fileList) {
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();

        paths = File.listRoots();

        File[] children = file.listFiles();
        if (children != null) {     //scan des Ordners (anzahl Dateien, Typ, name, etc..)
            for (File child : children) {
                fileList.add(child);
                new searchThreadForFileList(child, fileList);

                for (File path : paths) {
                    String str = path.toString();
                    String slash = "\\";

                    String s = new StringBuilder(str).append(slash).toString();

                    Path startingDir = Paths.get(s);

                    String pattern = child +".txt";

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
        }



    }

    public static class Finder
            extends SimpleFileVisitor<Path> {

        private final PathMatcher matcher;
        private int numMatches = 0;

        Finder(String pattern) {
            matcher = FileSystems.getDefault()
                    .getPathMatcher("glob:" + pattern);
        }

        // Compares the glob pattern against
        // the file or directory name.
        void find(Path file) {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                numMatches++;
                System.out.println(file);
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
            find(file);
            return CONTINUE;
        }

        // Invoke the pattern matching
        // method on each directory.
        @Override
        public FileVisitResult preVisitDirectory(Path dir,
                                                 BasicFileAttributes attrs) {
            find(dir);
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