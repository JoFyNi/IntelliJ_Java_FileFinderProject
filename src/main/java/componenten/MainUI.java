package componenten;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.event.*;
import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

import static java.nio.file.FileVisitResult.CONTINUE;

public class MainUI {
    // JFrame components
    private JPanel rootPanel;
    private JTable listTable;
    private JComboBox driverSelector;
    private JComboBox dataType;
    private JTextField pathInput;
    private JTextField fileInput;
    private JButton addObject;
    private JButton openObject;
    private JButton updateBtn;
    private JButton scannBtn;
    private JButton searchListBtn;
    private JButton helpBtn;
    private JButton clearBtn;
    private JButton fileInputBtn;
    private JButton pathInputBtn;
    // export parameter
    private String typ = "**";
    private String driver;
    private Object[] row;

    // Tutorials/ help
    // editable JTable https://www.codejava.net/java-se/swing/editable-jtable-example
    // https://www.youtube.com/watch?v=xk4_1vDrzzo&list=TLPQMjMxMTIwMjJsbEKGZ80Atg&index=6
    public MainUI() throws FileNotFoundException, InterruptedException {
        pathInput.setText("C:\\Users\\j.nievelstein\\Java");
        fileInput.setText("_Briefvorlagegrbv");
        buttons();
        selectDrivers();
        selectDataType();
        createObject();
        //exportList();
        //rerere();
    }
    private void rerere() {
    ArrayList<File>list = listDirectories(new File("C:\\Users\\j.nievelstein\\Java\\Ausleihe\\src\\main\\java\\componenten\\geraete"), "", null, false, false);
    for (int i = 0; i<list.size();i++) {
        System.out.println("HIER!=!==!"+list.get(i).toString());

    }
}
    private ArrayList<File> listDirectories(File root, String dirName, ArrayList<File> list, boolean caseSensitivity, boolean printEmpty) {
        if (root == null || dirName.isEmpty()) {
            System.err.println("ausgangsverzeichnis fehler");
            return null;
        } if (list == null) {
            list = new ArrayList<File>();
            File[] files = root.listFiles();
            if (files.length != 0) {

                for (File file: files) {
                    String fileStr = file.toString();
                    if (!caseSensitivity) {
                        fileStr = fileStr.toLowerCase();
                        dirName = dirName.toLowerCase();
                        System.out.println("fileStr " + fileStr);
                        System.out.println("dirName " + dirName);
                    }
                    if (fileStr.lastIndexOf(dirName) == fileStr.length() - dirName.length() && file.isDirectory() && file.getName().equalsIgnoreCase(dirName)) {
                        list.add(file);
                        System.out.println("list " + list);
                    }
                    if (file.isDirectory()) {
                        listDirectories(file, dirName, list, caseSensitivity, printEmpty);
                        System.out.println("listDirectories " + listDirectories(file, dirName, list, caseSensitivity, printEmpty));
                    }
                }
            } else if (printEmpty) {
                System.out.println(root + " ist leer");
            }
        } return list;
    }

    public JPanel getRootPanel() {
        return rootPanel;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void buttons() {
        // searching with fileInput value
        fileInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // clear the Table
                DefaultTableModel dm = (DefaultTableModel)listTable.getModel();
                dm.getDataVector().removeAllElements();
                dm.fireTableDataChanged();
                // new Table data
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_ENTER)
                {
                    //listTable.clearSelection();
                    searchThreadWithSelectedType(fileInput.getText(), typ);
                    //searchThreadWithSelectedType finder = new searchThreadWithSelectedType(fileInput.getText(), typ);
                    //finder.start();
                } else if (fileInput.getText() == "") {
                    // get All files
                    Collection<File> all = new ArrayList<File>();
                    searchAll(new File(pathInput.getText()), all);
                }
                super.keyPressed(e);
            }
        });
        fileInputBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // clear the Table
                DefaultTableModel dm = (DefaultTableModel)listTable.getModel();
                dm.getDataVector().removeAllElements();
                dm.fireTableDataChanged();
                // new Table data
                searchThreadWithSelectedType(fileInput.getText(), typ);

                 if (fileInput.getText() == "") {
                    // get All files
                    Collection<File> all = new ArrayList<File>();
                    searchAll(new File(pathInput.getText()), all);
                }
            }
        });
        // searching with pathInput value
        pathInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                // clear the Table
                DefaultTableModel dm = (DefaultTableModel)listTable.getModel();
                dm.getDataVector().removeAllElements();
                dm.fireTableDataChanged();
                // new Table data
                int key = e.getKeyCode();
                String fileName = fileInput.getText();
                String PATH = "C:\\";
                String directoryName = PATH.concat(String.valueOf(this.getClass()));
                File directory = new File(directoryName);
                if (key == KeyEvent.VK_ENTER) {
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
                }
                super.keyPressed(e);
            }
        });
        pathInputBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
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

            }
        });
        listTable.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int column = 0;
                int row = listTable.getSelectedRow();
                String value = listTable.getModel().getValueAt(row, column).toString();
                fileOpenerThread fileOpenerThread = new fileOpenerThread(new File(value));
                fileOpenerThread.start();
                //fileReaderThread.fileReaderThread(pathInput.getText(),fileInput.getText(),typ);
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        openObject.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fileReaderThread.fileReaderThread(pathInput.getText(),fileInput.getText(),typ);
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
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
        scannBtn.addActionListener(new ActionListener(){
            //scann for existing drivers
            @Override
            public void actionPerformed(ActionEvent e){
                driverScann driverScannThread = new driverScann();
                driverScannThread.start();
                //searchThreadWithSelectedType finder = new searchThreadWithSelectedType(fileInput.getText(), typ);
                //finder.start();
            }
        });
        searchListBtn.addActionListener(new ActionListener() {
            // opens a JFrame with TextArea -> list into searchThread
            // allows to search for more than 1 file at the time
            @Override
            public void actionPerformed(ActionEvent e) {
                new searchForFileList();
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
                // clear the Table
                DefaultTableModel dm = (DefaultTableModel)listTable.getModel();
                dm.getDataVector().removeAllElements();
                dm.fireTableDataChanged();
            }
        });
        /**
         * file.getName();
         * file.getPath();
         * file.getEdit();
         * file.getTime();
         * file.getFileIcon
         * Object[][]data={{fileName,filePath,fileEdit,fileTime}.../=}
         * foreach {
         * listTable.setModel(new DefaultTableModel(data, headings));
         * }
         * File für jede Komponente
         *      file = file[]
         *      pfad = path[]
         *      auto = autor[]
         *      datum -> getDate(tag an welchem hinzugefügt)
         *
         * editor erstellen
         * -> öffnen von Dateien (Format anpassungen)
         * -> Import/Export
         * -> schreiben
         */
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // searching file from fileInput (name)
    private static int finalTotal = 0;
    private void searchThreadWithSelectedType(String file, String typ) {
        File[] paths;
        FileSystemView fsv = FileSystemView.getFileSystemView();
        paths = File.listRoots();
        for (File path : paths) {
            String str = path.toString();
            String slash = "\\";
            String s = new StringBuilder(str).append(slash).toString();
            Path startingDir = Paths.get(s);
            String pattern = file +"."+ typ;
            Finder finder = new Finder(pattern);
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
            int a = 1;
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
                for (int i = 0; i <a ; i++) {
                    row[0] = filePath;
                    row[1] = filePath.getFileName();
                    model.addRow(row);
                }
                a++;
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
        public FileVisitResult preVisitDirectory(Path dir,
                                                 BasicFileAttributes attrs) {
            try {
                find(dir);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            return CONTINUE;
        }
        @Override
        public FileVisitResult visitFileFailed(Path file,
                                               IOException exc) {
            //            System.err.println(exc);
            return CONTINUE;
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Excel into JTable -> not working yet
    // when improvement for searching is finish -> finish exports/imports
    public void givenWorkbook_whenInsertRowBetween_thenRowCreated() throws IOException {
        String fileLocation = "U:\\TestMappe.xlsx";

        int startRow = 2;
        int rowNumber = 1;
        Workbook workbook = new XSSFWorkbook(fileLocation);
        Sheet sheet = workbook.getSheetAt(0);
        int lastRow = sheet.getLastRowNum();
        if (lastRow < startRow) {
            sheet.createRow(startRow);
        }
        sheet.shiftRows(startRow, lastRow, rowNumber, true, true);
        sheet.createRow(startRow);
        FileOutputStream outputStream = new FileOutputStream("fileListe.xlsx");
        workbook.write(outputStream);
        File file = new File("C:\\Users\\j.nievelstein\\Java\\Ausleihe\\src\\main\\java\\componenten\\geraete");
        final int expectedRowResult = 5;
        //Assertions.assertEquals(expectedRowResult, workbook.getSheetAt(0).getLastRowNum());
        outputStream.close();
        file.delete();
        workbook.close();

        //System.setProperty("log4j.configurationFile","./path_to_the_log4j2_config_file/log4j2.xml");
        //Logger log = LogManager.getLogger(LogExample.class.getName());

        //Object[][] data = {{"Laptop", "B4FG98E", "Heinz", 29.03}, {"Laptop", "OS26J6E", "Heinrich", 15.11}, {"Laptop", "OK29J2", "Ernst", 09.08}};
        //listTable.setModel(new DefaultTableModel(data, headings));  // data and Header
        /*
        TableColumnModel columns = listTable.getColumnModel();
        columns.getColumn(0).setMaxWidth(200);
        columns.getColumn(1).setMaxWidth(200);
        columns.getColumn(2).setMaxWidth(200);
        columns.getColumn(3).setMaxWidth(200);

        DefaultTableCellRenderer centerRender = new DefaultTableCellRenderer();
        centerRender.setHorizontalAlignment(JLabel.CENTER);
        columns.getColumn(0).setCellRenderer(centerRender);
        columns.getColumn(1).setCellRenderer(centerRender);
        columns.getColumn(2).setCellRenderer(centerRender);
        columns.getColumn(3).setCellRenderer(centerRender);
         */
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void addTree(File file, Collection<File> all) {
        File[] children = file.listFiles();
        if (children != null) {     //scan des Ordners (anzahl Dateien, Typ, name, etc..)
            for (File child : children) {
                all.add(child);
                addTree(child, all);
                /**
                 * insertRow = neue Zeile mit row[0]/row[1]/row[2]
                 * row[0] = FileName
                 * row[1] = filePath
                 * row[2] = TotalSpace soll später Autor oder Datum sein
                 */
                row = new Object[2];
                DefaultTableModel model = (DefaultTableModel) listTable.getModel();
                model.setColumnIdentifiers(new String[]{"Path","Files Names"});
                listTable.setModel(model);
                for (int i = 0;i<children.length; i++) {
                    row[0] = children[i];
                    row[1] = children[i].getName();
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
                //Object[][] data = {{child, all}};
                //listTable.setModel(new DefaultTableModel(data, headings));  // data and Header
                row = new Object[2];
                DefaultTableModel model = (DefaultTableModel) listTable.getModel();
                model.setColumnIdentifiers(new String[]{"Path","Files Names"});
                listTable.setModel(model);
                for (int i = 0;i<files.length; i++) {
                    row[0] = files[i];
                    row[1] = files[i].getName();
                    model.addRow(row);
                }
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    /** ComboBox for driver Selection -> select driver for small amount (search speed increase)
     * not working correctly -> need improvement
     */
    private void selectDrivers() {   // comboBox
        driverSelector.setModel(new DefaultComboBoxModel(new String[]{"C", "Q", "W"}));
        driverSelector.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource()==driverSelector) {
                    if (driverSelector.getSelectedItem()=="C") {
                        driver = "C:\\";
                    } else if (driverSelector.getSelectedItem()=="Q"){
                        driver = "Q:\\";
                    } else if (driverSelector.getSelectedItem()=="W") {
                        driver = "W:\\";
                    } else {
                        System.out.println("error");
                    }
                    System.out.println("Selected driver: "+driver);
                    new driverGetter();
                }
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * ComboBox for fileType selection
     * typ = ** -> search all kind of fileTypes with the Name from fileInput
     * */
    private void selectDataType() {
        dataType.setModel(new DefaultComboBoxModel(new String[]{"typ","txt", "ods","pdf","ai","eps","psd","doc","docx","ppt","pptx","pps","ppsm","ppsx","xls","xlsx"}));
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
                        System.out.printf("error");
                    }
                    System.out.println("DataType: "+typ);
                    //listTable.setModel(new DefaultTableModel(available, headings));
                } else {
                    System.out.printf("retry");
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

    // x = columns, y = rows
    private String getCellVal(int x, int y) {
        return listTable.getValueAt(x, y).toString();
    }
    // not implemented yet (not working correctly)
    private void exportList() {

        TreeMap<String, Object[]> data = new TreeMap<>();
        data.put("0", new Object[]{listTable.getColumnName(0), listTable.getColumnName(1), listTable.getColumnName(2), listTable.getColumnName(3)});
        data.put("1", new Object[]{getCellVal(0, 0), getCellVal(0, 1), getCellVal(0, 2), getCellVal(0, 3)});
        data.put("2", new Object[]{getCellVal(1, 0), getCellVal(1, 1), getCellVal(1, 2), getCellVal(1, 3)});
        data.put("3", new Object[]{getCellVal(2, 0), getCellVal(2, 1), getCellVal(2, 2), getCellVal(2, 3)});
        data.put("4", new Object[]{getCellVal(3, 0), getCellVal(3, 1), getCellVal(3, 2), getCellVal(3, 3)});
        data.put("5", new Object[]{getCellVal(4, 0), getCellVal(4, 1), getCellVal(4, 2), getCellVal(4, 3)});

        Set<String> ids = data.keySet();
        int rowID = 0;
        for (String key : ids) {
            Object[] value = data.get(key);
        }
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File("U:\\Test.xlsx"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
// created with https://www.youtube.com/watch?v=3m1j3PiUeVI
