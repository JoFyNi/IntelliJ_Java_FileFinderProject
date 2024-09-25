package componenten;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class fileOpenerThread extends Thread {
    public fileOpenerThread(File file) {
        if (Desktop.isDesktopSupported()) {
            try {
                if (file.isDirectory()) {
                    Desktop.getDesktop().open(file.getAbsoluteFile());
                } else {
                    File myFile = new File("\\" + file);
                    Desktop.getDesktop().open(myFile);
                }
            } catch (IOException ex) {
                System.err.println("No application registered for this file type.");
            }
        }
    }
}
