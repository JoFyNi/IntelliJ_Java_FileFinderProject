package componenten;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

public class searchForFileList extends JFrame {

    JPanel panel;
    JTextArea textArea;
    JScrollPane scrollPane;
    JButton button;
    public searchForFileList() {
        panel = new JPanel();
        textArea = new JTextArea();
        scrollPane = new JScrollPane();
        button = new JButton("search");
        this.setContentPane(panel);
        panel.setLayout(null);

        panel.add(textArea);
        scrollPane.add(textArea);
        panel.setBounds(5,5,390,290);
        textArea.setBounds(5,5,390,290);
        button.setBounds(30,100,100,25);

        this.add(button);
        this.add(scrollPane);
        this.add(textArea);
        this.setBounds(0,200,400,300);
        this.setVisible(true);

        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // read list and give it to searchThread
                System.out.println("try");
                Collection<File> files = new ArrayList<File>();
                File file = new File(textArea.getText());
                //Collection<File> all = new ArrayList<File>();
                //addTree(new File(textArea.getText()), all);
                searchThreadForFileList finder = new searchThreadForFileList(file, files);
                finder.start();
            }
        });
    }

    public static void main(String[] args) {
        searchForFileList obj = new searchForFileList();
    }
}
