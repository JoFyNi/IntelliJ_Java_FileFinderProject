package componenten;

import java.awt.*;
import java.io.File;
import java.io.IOException;

public class fileOpenerThread extends Thread{
    public fileOpenerThread(File file) {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File("\\"+file);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
    }

    public static void main(String[] args) {
        //new fileOpenerThread();
    }
}
