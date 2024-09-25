package componenten;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

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
    private JLabel LICENCE_LABEL;
    private JLabel pathLabel;
    private JLabel fileLabel;
    private JLabel countLabel;
    private JLabel dirLabel;

    // export parameter
    private String typ = "pdf";
    private String driver;
    private Object[] row;
    private static final Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
    private ExecutorService executorService;
    private int fileCount = 0;

    public MainUI() throws IOException, InterruptedException {
        pathInput.setText("C:\\Users\\Default\\Documents");
        fileInput.setText("file");
        buttons();
        selectDrivers();
        selectDataType();
        createObject();
        executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
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
                    searchFilesWithExecutor(fileInput.getText());
                }
                super.keyPressed(e);
            }
        });

        fileInputBtn.addActionListener(e -> searchFilesWithExecutor(fileInput.getText()));

        // searching with pathInput value
        pathInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    searchPathsWithExecutor();
                }
                super.keyPressed(e);
            }
        });

        pathInputBtn.addActionListener(e -> searchPathsWithExecutor());

        listTable.addMouseListener(new MouseAdapter() {
            final JPopupMenu popupMenu = new JPopupMenu();
            final JMenuItem addItem = new JMenuItem("add");
            final JMenuItem openItem = new JMenuItem("open");
            final JMenuItem copyPathItem = new JMenuItem("copy path");
            final JMenuItem copyNameItem = new JMenuItem("copy Name");
            String selectedValue = null;

            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    int selectedRow = listTable.getSelectedRow();
                    if (selectedRow != -1) {
                        Object selectedPath = listTable.getValueAt(selectedRow, 0);
                        Object selectedFile = listTable.getValueAt(selectedRow, 1);
                        fileLabel.setText(selectedFile.toString());
                        pathLabel.setText(selectedPath.toString());
                    }
                }
                if (SwingUtilities.isRightMouseButton(e)) {
                    popupMenu.add(addItem);
                    popupMenu.add(openItem);
                    popupMenu.add(copyPathItem);
                    popupMenu.add(copyNameItem);
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());

                    addItem.addActionListener(evt -> {
                        JFileChooser fileChooser = new JFileChooser();
                        int response = fileChooser.showSaveDialog(null);
                        if (response == JFileChooser.APPROVE_OPTION) {
                            File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                            addFileToTable(file);
                        }
                    });

                    openItem.addActionListener(evt -> {
                        int selectedRow = listTable.getSelectedRow();
                        if (selectedRow != -1) {
                            selectedValue = listTable.getValueAt(selectedRow, 0).toString();
                            new fileOpenerThread(new File(selectedValue)).start();
                        }
                    });

                    copyPathItem.addActionListener(evt -> {
                        int selectedRow = listTable.getSelectedRow();
                        if (selectedRow != -1) {
                            selectedValue = listTable.getValueAt(selectedRow, 0).toString();
                            copyToClipboard(selectedValue);
                            pathLabel.setText(selectedValue);
                        }
                    });

                    copyNameItem.addActionListener(evt -> {
                        int selectedRow = listTable.getSelectedRow();
                        if (selectedRow != -1) {
                            selectedValue = listTable.getValueAt(selectedRow, 1).toString();
                            copyToClipboard(selectedValue);
                            fileLabel.setText(selectedValue);
                        }
                    });
                }
            }
        });

        // Button zum Öffnen einer Datei
        openBtn.addActionListener(e -> {
            int selectedRow = listTable.getSelectedRow();
            if (selectedRow != -1) {
                String selectedPath = listTable.getValueAt(selectedRow, 0).toString();
                new fileOpenerThread(new File(selectedPath)).start();
            }
        });

        updateBtn.addActionListener(e -> {
            clearTable();
            searchPathsWithExecutor();
        });

        scanBtn.addActionListener(e -> searchFilesWithExecutor(fileInput.getText()));

        helpBtn.addActionListener(e -> JOptionPane.showMessageDialog(null, """
                Input Field     = Search input on all Drivers
                Path Field      = List files in the specified path
                Scan Button     = Scan Drivers for files
                Update Button   = Refresh file list with selected path
                Add Button      = Add new file to the list
                Open Button     = Open selected file"""));

        clearBtn.addActionListener(e -> clearTable());
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void clearTable() {
        DefaultTableModel dm = (DefaultTableModel) listTable.getModel();
        dm.getDataVector().removeAllElements();
        dm.fireTableDataChanged();
        fileCount = 0;
        countLabel.setText("files: 0");
    }

    // Suchfunktion mit parallelisierten Tasks (ForkJoinPool)
    private void searchFilesWithExecutor(final String inputValue) {
        clearTable();
        fileCount = 0;
        Path startDir = driver != null ? Paths.get(driver) : Paths.get(pathInput.getText());

        ForkJoinPool pool = new ForkJoinPool();
        FileSearcherTask task = new FileSearcherTask(startDir, inputValue, this);
        pool.invoke(task);
    }

    // Methode zur parallelen Pfadsuche mit ExecutorService
    private void searchPathsWithExecutor() {
        clearTable();
        fileCount = 0;
        Path startDir = Paths.get(pathInput.getText());

        executorService.submit(() -> {
            try {
                Files.walkFileTree(startDir, new SimpleFileVisitor<Path>() {
                    @Override
                    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                        addFileToTable(file.toFile());
                        return FileVisitResult.CONTINUE;
                    }

                    @Override
                    public FileVisitResult visitFileFailed(Path file, IOException exc) {
                        return FileVisitResult.CONTINUE;
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    void addFileToTable(File file) {
        SwingUtilities.invokeLater(() -> {
            if (file.getName().contains(fileLabel.getText())) {
                DefaultTableModel model = (DefaultTableModel) listTable.getModel();
                model.addRow(new Object[]{file.getAbsolutePath(), file.getName()});
                fileCount++;
                countLabel.setText("Files: " + fileCount);
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void addTree(File file, Collection<File> all) {
        File[] children = file.listFiles();
        if (children != null) {
            for (File child : children) {
                all.add(child);
                addTree(child, all);

                row = new Object[2];
                DefaultTableModel model = (DefaultTableModel) listTable.getModel();
                model.setColumnIdentifiers(new String[]{"Path", "Files Names"});
                listTable.setModel(model);
                for (File value : children) {
                    row[0] = value;
                    row[1] = value.getName();
                    model.addRow(row);
                }
            }
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void selectDrivers() {
        File[] roots = File.listRoots();
        Path[] driverArray = new Path[roots.length];
        for (int i = 0; i < roots.length; i++) {
            driverArray[i] = roots[i].toPath();
        }

        driverSelector.addActionListener(e -> {
            if (e.getSource() == driverSelector) {
                Object selectedItem = driverSelector.getSelectedItem();
                driver = selectedItem != null ? selectedItem.toString() : "All";
                System.out.println("Selected driver: " + driver);
                dirLabel.setText(driver);
            }
        });

        driverSelector.addItem(null);
        for (Path path : driverArray) {
            driverSelector.addItem(path);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void selectDataType() {
        dataType.addActionListener(e -> {
            if (e.getSource() == dataType) {
                typ = (String) dataType.getSelectedItem();
                System.out.println("DataType: " + typ);
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private void createObject() {
        addObject.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int response = fileChooser.showSaveDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                addFileToTable(file);
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private static void copyToClipboard(String content) {
        if (!content.isEmpty()) {
            StringSelection stringSelection = new StringSelection(content);
            clipboard.setContents(stringSelection, stringSelection);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private class fileOpenerThread extends Thread {
        private final File file;

        fileOpenerThread(File file) {
            this.file = file;
        }

        @Override
        public void run() {
            try {
                Desktop.getDesktop().open(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
