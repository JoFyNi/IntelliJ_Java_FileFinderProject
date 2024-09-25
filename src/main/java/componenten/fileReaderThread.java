package componenten;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class fileReaderThread {

    public fileReaderThread(final String Pfad, final String fileName, final String typ) throws IOException {
        JFrame frame = new JFrame("readUsingFiles");
        frame.setSize(400, 600);
        frame.setLocation(1200, 150);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        JFrame.setDefaultLookAndFeelDecorated(true);

        final JTextArea textArea = new JTextArea("Inhalt");
        textArea.setBounds(0, 0, 400, 560);
        textArea.setEditable(true);
        textArea.setLineWrap(true);

        JScrollPane scroll = new JScrollPane(textArea);
        scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBounds(5, 5, 380, 560);
        frame.add(scroll);
        frame.setLayout(null);
        frame.setVisible(true);

        Path path = Paths.get(Pfad + fileName + typ);
        byte[] bytes = Files.readAllBytes(path);
        textArea.setText(new String(bytes));

        JMenuBar menuBar = new JMenuBar();
        frame.add(menuBar);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(Pfad + fileName + typ));
                    writer.write(textArea.getText());
                    writer.close();
                } catch (IOException eee) {
                    eee.printStackTrace();
                }
            }
        });
        menuBar.add(saveBtn);
        frame.setJMenuBar(menuBar);
    }
}
