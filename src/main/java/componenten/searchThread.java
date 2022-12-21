package componenten;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class searchThread extends Thread{
        public searchThread(String name, File file) {
        File[] list = file.listFiles();
        if(list!=null)
            for (File fil : list)
            {
                if (fil.isDirectory())
                {
                    new searchThread(name,fil);
                }
                else if (name.equalsIgnoreCase(fil.getName()))
                {
                    System.out.println(fil.getParentFile());
                }
            }
    }

    @Override
    public void run() {
        for (int i=0; i<1;i++) {
            try {
                Collection<File> all = new ArrayList<File>();
                addTree(new File("C:\\Users\\"), all);

                System.out.println(all);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    static File addTree(File file, Collection<File> all) {
        File[] children = file.listFiles();
        if (children != null) {     //scan des Ordners (anzahl Dateien, Typ, name, etc..)
            for (File child : children) {
                all.add(child);
                addTree(child, all);
                //textArea.append(child + System.lineSeparator());    // mit getName() nur FileNamen anzeigen? (momentan path)
                System.out.println(child + System.lineSeparator());
                return child;
            }
        }
        return file;
    }
}
