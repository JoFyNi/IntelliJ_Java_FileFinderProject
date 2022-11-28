package componenten;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.FileVisitResult.CONTINUE;

class driverScann extends Thread {
    String file;
    String typ;
    private static int finalTotal = 0;

    public static class Finder
            extends SimpleFileVisitor<Path> {

        private final PathMatcher matcher;
        private int numMatches = 0;

        Finder(String pattern) {
            matcher = FileSystems.getDefault().getPathMatcher("glob:" + pattern);
        }

        // Compares the glob pattern against
        // the file or directory name.
        void find(Path file) {
            Path name = file.getFileName();
            if (name != null && matcher.matches(name)) {
                numMatches++;
                System.out.println(file);

                //Collection<File> all = new ArrayList<File>();
                //MainUI.printSearchResult(new File(file, all);
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
    public static void startSearch(String file) {
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();

        paths = File.listRoots();

        for (File path : paths) {
            String str = path.toString();
            String slash = "\\";

            String s = new StringBuilder(str).append(slash).toString();
            Path startingDir = Paths.get(s);

            //Finder finder = new Finder(file+"."+typ);
            Finder finderTXT = new Finder(file+"*.txt");
            Finder finderODS = new Finder(file+"*.ods");
            // Adobe
            Finder finderPDF = new Finder(file+"*.pdf");
            Finder finderPDS = new Finder(file+"*.pds");
            Finder finderEPS = new Finder(file+"*.eps");
            Finder finderPSD = new Finder(file+"*.psd");
            // Microsoft
            Finder finderXLSX = new Finder(file+"*.xlsx");
            Finder finderDOC = new Finder(file+"*.doc");
            Finder finderDOCX = new Finder(file+"*.docx");
            Finder finderPPS = new Finder(file+"*.pps");
            Finder finderPPSM = new Finder(file+"*.ppsm");
            Finder finderPPSX = new Finder(file+"*.ppsx");
            Finder finderPPT = new Finder(file+"*.ppt");
            Finder finderPPTM = new Finder(file+"*.pptm");
            Finder finderPPTX = new Finder(file+"*.pptx");
            Finder finderXLS = new Finder(file+"*.xls");

            try {
                System.out.println("start searching");
                //Files.walkFileTree(startingDir, finder);
                Files.walkFileTree(startingDir, finderTXT);
                Files.walkFileTree(startingDir, finderODS);
                // Adobe
                Files.walkFileTree(startingDir, finderPDF);
                Files.walkFileTree(startingDir, finderPDS);
                Files.walkFileTree(startingDir, finderEPS);
                Files.walkFileTree(startingDir, finderPSD);
                // Microsoft
                Files.walkFileTree(startingDir, finderXLSX);
                Files.walkFileTree(startingDir, finderDOC);
                Files.walkFileTree(startingDir, finderDOCX);
                Files.walkFileTree(startingDir, finderPPS);
                Files.walkFileTree(startingDir, finderPPSM);
                Files.walkFileTree(startingDir, finderPPSX);
                Files.walkFileTree(startingDir, finderPPT);
                Files.walkFileTree(startingDir, finderPPTM);
                Files.walkFileTree(startingDir, finderPPTX);
                Files.walkFileTree(startingDir, finderXLS);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            //finder.done();
            finderTXT.done();
            finderODS.done();
            finderPDF.done();
            finderPDS.done();
            finderEPS.done();
            finderPSD.done();
            finderXLSX.done();
            finderDOC.done();
            finderDOCX.done();
            finderPPS.done();
            finderPPSM.done();
            finderPPSX.done();
            finderPPT.done();
            finderPPTM.done();
            finderPPTX.done();
            finderXLS.done();

        }

        System.out.println("Total Matched Number of Files : " + finalTotal);
    }
    @Override
    public void run() {
    }
}

