package componenten;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * opens the selected file with the specific programm depends on the filetype
 * example: when opening a pdf file the programm will start adobe with selected file
  */
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
