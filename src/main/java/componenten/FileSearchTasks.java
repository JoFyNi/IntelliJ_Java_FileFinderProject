package componenten;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileSearchTasks implements Runnable {
    private final String fileType;
    private final List<File> results;
    private final List<File> foundFiles;
    private final File rootDirectory;

    private final String searchString;
    private boolean done;
    private int count;

    private FileSearchTasks(File rootDirectory, String searchString, String fileType) {
        this.rootDirectory = rootDirectory;
        this.searchString = searchString;
        this.fileType = fileType;
        this.foundFiles = new ArrayList<>();
        this.results = new ArrayList<>();
    }
    @Override
    public void run() {

        // Search for files
        search(rootDirectory, searchString, fileType);

        // Add the found files to the results field
        results.addAll(foundFiles);
    }
    private void search(File rootDirectory, String searchString, String fileType) {
        File[] files = rootDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    search(file, searchString, fileType);
                } else if (file.getName().endsWith(fileType) && file.getName().contains(searchString)) {
                    foundFiles.add(file);
                    count++;
                }
            }
        }
    }
    public synchronized void waitUntilDone() {
        while (!done) {
            try {
                wait();
            } catch (InterruptedException e) {
                // ignore
            }
        }
    }
    public List<File> getResults() {
        return results;
    }

}