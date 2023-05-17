package componenten;

import java.io.File;

class FileSearchTask implements Runnable {

    private final File rootDirectory;
    private final String searchString;
    private final String fileType;
    private boolean done;
    private int count;

    public FileSearchTask(File rootDirectory, String searchString, String fileType) {
        this.rootDirectory = rootDirectory;
        this.searchString = searchString;
        this.fileType = fileType;
    }
    @Override
    public void run() {
        search(rootDirectory, searchString, fileType);
        done = true;
        //publish(count);
    }
    private void search(File rootDirectory, String searchString, String fileType) {
        File[] files = rootDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    search(file, searchString, fileType);
                } else if (file.getName().endsWith(fileType) && file.getName().contains(searchString)) {
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

}