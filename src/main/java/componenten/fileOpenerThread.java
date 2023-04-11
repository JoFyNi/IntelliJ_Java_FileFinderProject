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
                if (file.isDirectory()) {
                    Desktop.getDesktop().open(file.getAbsoluteFile());
                } else {
                    File myFile = new File("\\"+file);
                    Desktop.getDesktop().open(myFile);
                }
            } catch (IOException ex) {
                // no application registered for PDFs
            }
        }
    }
}
