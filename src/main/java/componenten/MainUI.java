package componenten;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;

import static java.nio.file.FileVisitResult.CONTINUE;

public class MainUI {
    // JFrame components
    private JPanel rootPanel;
    private JTable listTable;
    private JComboBox<Path> driverSelector;
    private JComboBox<String> dataType;
    private JTextField pathInput;
    private JTextField fileInput;
    private JButton addObject;
    private JButton openBtn;
    private JButton updateBtn;
    private JButton scanBtn;
    private JButton helpBtn;
    private JButton clearBtn;
    private JButton fileInputBtn;
    private JButton pathInputBtn;
    private JLabel pathLabel;
    private JLabel fileLabel;
    private JLabel countLabel;
    private JLabel licenseLab;
    private JLabel dirLabel;
    // export parameter
    private String typ = "**";
    private String driver;
    private Object[] row;
    private static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

    // Tutorials/ help
    // editable JTable https://www.codejava.net/java-se/swing/editable-jtable-example
    // https://www.youtube.com/watch?v=xk4_1vDrzzo&list=TLPQMjMxMTIwMjJsbEKGZ80Atg&index=6
    public MainUI() throws IOException, InterruptedException {
        pathInput.setText("C:\\Users\\Default\\Documents");
        fileInput.setText("file");
        buttons();
        selectDrivers();
        selectDataType();
        createObject();
        //exportList();
    }
    public JPanel getRootPanel() {
        return rootPanel;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    void buttons() {
        // searching with fileInput value
        fileInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    File file = new File(fileInput.getText());
                    if (file.isDirectory()) {
                        fileLabel.setText("enter a file name (no Path)");
                    } else if(fileInput.getText().equals("")) {
                        fileLabel.setText("enter a file name");
                    } else {
                        searchFilesWithSwingWorker(fileInput.getText());
                    }
                }
                super.keyPressed(e);
            }
        });
        fileInputBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // clear the Table
                searchFilesWithSwingWorker(fileInput.getText());
            }
        });
        // searching with pathInput value
        pathInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER) {
                    searchPathsWithSwingWorker();
                } else {
                    pathLabel.setText("enter a path name");
                }
                super.keyPressed(e);
            }
        });
        pathInputBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                searchPathsWithSwingWorker();
            }
        });
        listTable.addMouseListener(new MouseAdapter() {
            final JPopupMenu popupMenu = new JPopupMenu();
            final JMenuItem addItem = new JMenuItem("add");
            final JMenuItem openItem = new JMenuItem("open");
            final JMenuItem openInItem = new JMenuItem("open in");
            final JMenuItem copyPathItem = new JMenuItem("copy path");
            final JMenuItem copyNameItem = new JMenuItem("copy Name");
            String selectedValue = null;
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)){
                    int selectedRow = listTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Object selectedPath = listTable.getValueAt(selectedRow, 0);
                        Object selectedFile = listTable.getValueAt(selectedRow, 1);
                        fileLabel.setText(selectedFile.toString());
                        pathLabel.setText(selectedPath.toString());
                    }
                }
                if (SwingUtilities.isRightMouseButton(e)){
                    popupMenu.add(addItem);
                    popupMenu.add(openItem);
                    popupMenu.add(openInItem);
                    popupMenu.add(copyPathItem);
                    popupMenu.add(copyNameItem);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    addItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (e.getSource()==addItem) {
                                JFileChooser fileChooser = new JFileChooser();
                                int response = fileChooser.showSaveDialog(null);
                                if (response == JFileChooser.APPROVE_OPTION) {
                                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                                    System.out.println(file);
                                    row = new Object[2];
                                    DefaultTableModel model = (DefaultTableModel) listTable.getModel();
                                    model.setColumnIdentifiers(new String[]{"Path","Files Names"});
                                    listTable.setModel(model);
                                    row[0] = file;
                                    row[1] = file.getName();
                                    model.addRow(row);
                                }
                            }
                        }
                    });
                    openItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            if (selectedValue == null) {
                                int selectedColumn = 0;
                                int selectedRow = listTable.getSelectedRow();
                                selectedValue = listTable.getModel().getValueAt(selectedRow, selectedColumn).toString();
                                fileOpenerThread fileOpenerThread = new fileOpenerThread(new File(selectedValue));
                                fileOpenerThread.start();
                            } else {
                                selectedValue = null;
                            }
                        }
                    });
                    openInItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            // create option menu
                        }
                    });
                    copyPathItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int selectedColumn = 0;
                            int selectedRow = listTable.getSelectedRow();
                            selectedValue = listTable.getModel().getValueAt(selectedRow, selectedColumn).toString();
                            copyToClipboard(selectedValue);
                            pathLabel.setText(selectedValue);
                        }
                    });
                    copyNameItem.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            int selectedColumn = 1;
                            int selectedRow = listTable.getSelectedRow();
                            selectedValue = listTable.getModel().getValueAt(selectedRow, selectedColumn).toString();
                            copyToClipboard(selectedValue);
                            fileLabel.setText(selectedValue);
                        }
                    });
                }
                selectedValue = null;
            }
        });
        openBtn.addActionListener(new ActionListener() {
            String selectedValue = null;
            @Override
            public void actionPerformed(ActionEvent e) {
                if (selectedValue == null) {
                    int selectedColumn = 0;
                    int selectedRow = listTable.getSelectedRow();
                    selectedValue = listTable.getModel().getValueAt(selectedRow, selectedColumn).toString();
                    fileOpenerThread fileOpenerThread = new fileOpenerThread(new File(selectedValue));
                    fileOpenerThread.start();
                } else {
                    selectedValue = null;
                }
            }
        });
        updateBtn.addActionListener(new ActionListener() {
            // updates the JTable
            @Override
            public void actionPerformed(ActionEvent e) {
                Collection<File> all = new ArrayList<File>();
                addTree(new File(pathInput.getText()), all);
                System.out.println("updating");
            }
        });
        scanBtn.addActionListener(new ActionListener(){
            //scann for existing drivers
            @Override
            public void actionPerformed(ActionEvent e){
                int selectedColumn = 1;
                int selectedRow = listTable.getSelectedRow();
                if (selectedRow >= 0) {
                    String selectedValue = listTable.getModel().getValueAt(selectedRow, selectedColumn).toString();
                    String[] slit = selectedValue.split("\\.");
                    clearTable();
                    searchFilesWithSwingWorker(slit[0]);
                } else {
                    searchFilesWithSwingWorker(fileInput.getText());
                }
            }
        });
        helpBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "" +
                        "Input Field     = searching for Input on all Drivers\n" +
                        "Path Field      = listing all files in that location\n" +
                        "search Button   = multi search Option\n" +
                        "Scann Button    = Driver Scanner + all existing files\n" +
                        "Update Button   = gets all files with the path from the Path Input Field\n" +
                        "Add Button      = Adds a file (creating a file)\n" +
                        "Open Button     = opens selected file\n");
            }
        });
        clearBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clearTable();
            }
        });
    }
    private void clearTable() {
        // clear the Table
        DefaultTableModel dm = (DefaultTableModel)listTable.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();
        int value = 0;
        countLabel.setText("files: " + value);
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * SwingWorker that open, close or work on a process, deepens on the Parameter that activate it
     */
    private void StringWorkerWithParameters(final Boolean first, final Boolean second) {
        SwingWorker<Boolean, Integer> swingWorker = new SwingWorker<Boolean, Integer>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                if (first && !second) {
                    System.out.println("first true");
                    new searchForFileList();
                }
                if (second && !first) {
                    System.out.println("second true");
                }
                if (first && second) {
                    System.out.println("first and second true");
                }
                if (!first && !second) {
                    System.out.println("first and second false");
                }
                return first && second;
            }
            @Override
            protected void process(List<Integer> chunks) {
                int value = chunks.size();
                System.out.println(value);
            }
            @Override
            protected void done() {
                try {
                    Boolean status = get();
                    System.out.println(status);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        swingWorker.execute();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Swing Worker
     * while searching files, the GUI can't still be used
     * files get listed instand
     */
    private void searchFilesWithSwingWorker(final String inputValue) {
        SwingWorker<Boolean, File> fileWorker = new SwingWorker<Boolean, File>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // Get the selected driver
                Object selectedDriver = driverSelector.getSelectedItem();
                if (selectedDriver == null) {
                    // "All" option is selected
                    driver = "All";
                } else {
                    driver = selectedDriver.toString();
                }
                // Clear the table
                DefaultTableModel dm = (DefaultTableModel)listTable.getModel();
                dm.getDataVector().removeAllElements();
                dm.fireTableDataChanged();
                // Search for files with the selected type
                searchThreadWithSelectedType(inputValue, typ);
                // Create a list of FileSearchTask objects, one for each file type
                List<String> allTypes = new ArrayList<>();
                allTypes.add(".**");
                List<FileSearchTask> tasks = new ArrayList<>();
                // Check which driver is selected
                /*
                 * TODO adding selected driver to search algorithm to search only on selected driver
                 */
                if (driver.equals("All")) {
                    // "All" option is selected, so create a task for each driver
                    File[] roots = File.listRoots();
                    for (File root : roots) {
                        for (String fileType : allTypes) {
                            FileSearchTask task = new FileSearchTask(root, inputValue, fileType);
                            tasks.add(task);
                        }
                    }
                } else {
                    // A specific driver is selected, so create a task only for that driver
                    File selectedRoot = new File(driver);
                    for (String fileType : allTypes) {
                        FileSearchTask task = new FileSearchTask(selectedRoot, inputValue, fileType);
                        tasks.add(task);
                    }
                }
                // Execute the tasks using the ExecutorService
                for (FileSearchTask task : tasks) {
                    Executor executor = new Executor() {
                        @Override
                        public void execute(@NotNull Runnable command) {
                            command.run();
                        }
                    };
                    executor.execute(task);
                }
                // Wait for all tasks to complete
                for (FileSearchTask task : tasks) {
                    task.waitUntilDone();
                }
                return true;
            }
            @Override
            protected void process(List<File> chunks) {
                DefaultTableModel model = (DefaultTableModel) listTable.getModel();
                for (File file : chunks) {
                    model.addRow(new Object[] { file.getName(), file.getAbsolutePath() });
                    listTable.setModel(model);
                    model.setColumnCount(listTable.getColumnCount());
                }
            }
            @Override
            protected void done() {
                try {
                    Boolean staus = get();
                    fileLabel.setText("Completed with status: " + staus);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        fileWorker.execute();
    }
    /**
     * Swing Worker
     * while searching files, the GUI can't still be used
     * files get listed instand
     */
    private void searchPathsWithSwingWorker() {
        SwingWorker<Boolean, Integer> PathWorker = new SwingWorker<Boolean, Integer>() {
            @Override
            protected Boolean doInBackground() {
                // clear the Table
                DefaultTableModel dm = (DefaultTableModel)listTable.getModel();
                dm.getDataVector().removeAllElements();
                dm.fireTableDataChanged();
                // new Table data
                String fileName = fileInput.getText();
                String PATH = "C:\\";
                String directoryName = PATH.concat(String.valueOf(this.getClass()));
                File directory = new File(directoryName);
                if (directory.exists()) {
                    // get All files
                    Collection<File> all = new ArrayList<File>();
                    addTree(new File(pathInput.getText()), all);
                    System.out.println("Path exists");
                }
                if (!directory.exists()) {
                    directory.mkdir();
                    // If you require it to make the entire directory path including parents,
                    // use directory.mkdirs(); here instead.
                    System.out.println("Path doesn't exists");
                }
                File file = new File(directoryName + "/" + fileName);
                try {
                    FileWriter fw = new FileWriter(file.getAbsoluteFile());
                    BufferedWriter bw = new BufferedWriter(fw);
                    bw.write(pathInput.getText());
                    bw.close();
                } catch (IOException eeee) {
                    eeee.printStackTrace();
                    System.exit(-1);
                }
                return true;
            }
            @Override
            protected void process(List<Integer> chunks) {
                int value = 0;
                for (int count : chunks) {
                    value += count;
                }
                countLabel.setText("files: " + value);
            }
            @Override
            protected void done() {
                try {
                    Boolean staus = get();
                    pathLabel.setText("Completed with status: " + staus);
                } catch (InterruptedException | ExecutionException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        PathWorker.execute();
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /** searching file from fileInput (name)
     *  Multithreading, for each driver 1 thread
     *  for each folder another thread
     */
    private static int finalTotal = 0;
    private void searchThreadWithSelectedType(String file, String typ) {
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        paths = File.listRoots();
        for (File path : paths) {   // first path "C:\" <-- how to get directory's
            String str = path.toString();
            String slash = "\\";
            String s = str + slash;
            Path startingDirectory = Paths.get(s);      // for each directory new Thread!
            String pattern = file +"."+ typ;    // get mkdir (driver's)
            Finder finder = new Finder(pattern);

            try {
                System.out.println("searching for "+ pattern);
                Files.walkFileTree(startingDirectory, finder);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            finder.done();
        }
        System.out.println("Total Matched Number of Files : " + finalTotal);
        countLabel.setText("Files : " + finalTotal);
    }
    private class Finder extends SimpleFileVisitor<Path> {
        private final PathMatcher matcher;
        private int numMatches = 0;
        Finder(String pattern) {
            matcher = FileSystems.getDefault()
                    .getPathMatcher("glob:" + pattern);
        }
        // Compares the glob pattern against
        // the file or directory name.
        void find(Path filePath) throws InterruptedException {
            FileSystemView fsv = FileSystemView.getFileSystemView();
            Path name = filePath.getFileName();
            if (name != null && matcher.matches(name)) {
                numMatches++;
                System.out.println(filePath);
                row = new Object[2];
                DefaultTableModel model = (DefaultTableModel) listTable.getModel();
                model.setColumnIdentifiers(new String[]{"Path","Files Names"});
                listTable.setModel(model);
                /**
                 * a = 1, i = 0
                 * if i < a create new row with filePath
                 * than make a+1 so "a" is bigger than "i" again for the next round
                 */
                for (int i = 0; i <1 ; i++) {
                    row[0] = filePath;
                    row[1] = filePath.getFileName();
                    model.addRow(row);
                }
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
        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
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
        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
            try {
                find(dir);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return CONTINUE;
        }
        @Override
        public FileVisitResult visitFileFailed(Path file, IOException exc) {
            //            System.err.println(exc);
            return CONTINUE;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void addTree(File file, Collection<File> all) {
        File[] children = file.listFiles();
        if (children != null) {     //scan des Ordners (anzahl Dateien, Typ, name, etc..)
            for (File child : children) {
                all.add(child);
                addTree(child, all);
                /*
                 insertRow = neue Zeile mit row[0]/row[1]/row[2]
                 row[0] = FileName
                 row[1] = filePath
                 row[2] = TotalSpace soll später Autor oder Datum sein
                 */
                row = new Object[2];
                DefaultTableModel model = (DefaultTableModel) listTable.getModel();
                model.setColumnIdentifiers(new String[]{"Path","Files Names"});
                listTable.setModel(model);
                for (File value : children) {
                    row[0] = value;
                    row[1] = value.getName();
                    model.addRow(row);
                }
                //System.out.printf(child + System.lineSeparator());
            }
        }
    }
    private void searchAll(File file, Collection<File> all) {
        File[] files = file.listFiles();        // File [] files = list of all files
        if (files != null) {
            for (File child : files) {          // difference between the child(one file) and all files
                all.add(child);                 // add the selected file(child) to all files and repeat
                searchAll(child, all);
                row = new Object[2];
                DefaultTableModel model = (DefaultTableModel) listTable.getModel();
                model.setColumnIdentifiers(new String[]{"Path","Files Names"});
                listTable.setModel(model);
                for (File value : files) {
                    row[0] = value;
                    row[1] = value.getName();
                    model.addRow(row);
                }
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** ComboBox for driver Selection -> select driver for smaller amount (search speed increase)
     * not working correctly -> need improvement
     * scann from a to z
     */
    private void selectDrivers() {
        // Initialize the driverArray with all available drives
        File[] roots = File.listRoots();
        Path[] driverArray = new Path[roots.length];
        for (int i = 0; i < roots.length; i++) {
            driverArray[i] = roots[i].toPath();
        }

        // Add an action listener to the driverSelector combo box
        driverSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == driverSelector) {
                    // Get the selected driver
                    String SD = null;
                    Object selectedItem = driverSelector.getSelectedItem();
                    if (selectedItem == null) {
                        // "All" option is selected
                        driver = "All";
                        SD = driver;
                    } else if (selectedItem != null) {
                        driver = selectedItem.toString();
                        SD = driver;
                    }
                    System.out.println("Selected driver: " + driver);
                    new driverGetter();
                    dirLabel.setText(SD);
                }
            }
        });

        // Add all available drivers to the driverSelector JComboBox
        driverSelector.addItem(null); // Add "All" option
        for (Path path : driverArray) {
            driverSelector.addItem(path);
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void selectDataType() throws IOException {
        //typScann();
        dataType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                TableColumnModel columns = listTable.getColumnModel();

                if (e.getSource()==dataType) {
                    if (dataType.getSelectedItem()=="**") {
                        typ = "**";
                    } else if (dataType.getSelectedItem()=="txt"){
                        typ = "txt";
                    } else if (dataType.getSelectedItem()=="ods") {
                        typ = "ods";
                    } else if (dataType.getSelectedItem()=="pdf") {
                        typ = "pdf";
                    } else if (dataType.getSelectedItem()=="ai") {
                        typ = "ai";
                    } else if (dataType.getSelectedItem()=="eps") {
                        typ = "eps";
                    } else if (dataType.getSelectedItem()=="psd") {
                        typ = "psd";
                    } else if (dataType.getSelectedItem()=="doc") {
                        typ = "doc";
                    } else if (dataType.getSelectedItem()=="docx") {
                        typ = "docx";
                    } else if (dataType.getSelectedItem()=="ppt") {
                        typ = "ppt";
                    } else if (dataType.getSelectedItem()=="pptx") {
                        typ = "pptx";
                    } else if (dataType.getSelectedItem()=="pps") {
                        typ = "pps";
                    } else if (dataType.getSelectedItem()=="ppsm") {
                        typ = "ppsm";
                    } else if (dataType.getSelectedItem()=="ppsx") {
                        typ = "ppsx";
                    } else if (dataType.getSelectedItem()=="xls") {
                        typ = "xls";
                    } else if (dataType.getSelectedItem()=="xlsx") {
                        typ = "xlsx";
                    } else {
                        System.out.print("error");
                    }
                    System.out.println("DataType: "+typ);
                    //listTable.setModel(new DefaultTableModel(available, headings));
                } else {
                    System.out.print("retry");
                }
            }
        });
    }
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * createObject opens a search frame
     * select a file -> selected file get list in JTable
     */
    private void createObject() {
        addObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==addObject) {
                    JFileChooser fileChooser = new JFileChooser();
                    int response = fileChooser.showSaveDialog(null);
                    if (response == JFileChooser.APPROVE_OPTION) {
                        File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                        System.out.println(file);
                        row = new Object[2];
                        DefaultTableModel model = (DefaultTableModel) listTable.getModel();
                        model.setColumnIdentifiers(new String[]{"Path","Files Names"});
                        listTable.setModel(model);
                        row[0] = file;
                        row[1] = file.getName();
                        model.addRow(row);
                    }
                }
            }
        });
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void copyToClipboard(String content) {
        StringSelection stringSelection = new StringSelection(content);
        if (content.length() > 0) {
            stringSelection = new StringSelection(content);
        } else {
            return;
        }
        // verschiebe content in clipboard
        clipboard.setContents(stringSelection, stringSelection);
        // Ausgabe
        Transferable transferable = clipboard.getContents(null);
        if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
            try {
                String ausgabe = (String) transferable.getTransferData(DataFlavor.stringFlavor);
                System.out.println(ausgabe);
            } catch (UnsupportedFlavorException | IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static class FileSearchTask implements Runnable {

        private final File rootDirectory;
        private final String searchString;
        private final String fileType;
        private boolean done;
        private int count;

        private FileSearchTask(File rootDirectory, String searchString, String fileType) {
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
}