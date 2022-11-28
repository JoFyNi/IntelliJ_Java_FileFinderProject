package componenten;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class fileReaderThread{

    public static void fileReaderThread(String Pfad, String fileName, String typ) throws IOException {
        JFrame frame = new JFrame("readUsingFiles");
        frame.setSize(400, 600);
        frame.setLocation(1200, 150);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setDefaultLookAndFeelDecorated(true);
        JTextArea TextArea = new JTextArea("Inhalt");
        TextArea.setBounds(0,0,400,560);
        TextArea.setEditable(true);
        TextArea.setLineWrap(true);
        JScrollPane scroll = new JScrollPane(TextArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(5, 5, 380, 560);                     // <-- THIS
        frame.add ( scroll );
        frame.setLayout(null);
        frame.setVisible(true);

        Path path = Paths.get(Pfad+fileName+typ);
        //read file to byte array
        byte[] bytes = Files.readAllBytes(path);
        System.out.println("Read text file using Files class");
        //read file to String list
        @SuppressWarnings("unused")
        List<String> allLines = Files.readAllLines(path, StandardCharsets.UTF_8);
        TextArea.setText(new String(bytes));
        JMenuBar menuBar = new JMenuBar();
        frame.add(menuBar);
        JButton saveBtn = new JButton("save");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    // soll neu geschriebenes in Datei speichern (alt + neu) und alt überschreiben
                    BufferedWriter ausleiheWriter = new BufferedWriter(new FileWriter(Pfad+fileName+typ));
                    ausleiheWriter.write(TextArea.getText());
                    //ausleiheWriter.newLine();
                    ausleiheWriter.close();
                    System.out.printf("saved");
                } catch (IOException eee) {
                    eee.printStackTrace();
                    System.out.printf("saving failed");
                }
            }
        });
        menuBar.add(saveBtn);
        frame.setJMenuBar(menuBar);
    }
}
