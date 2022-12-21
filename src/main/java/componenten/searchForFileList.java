package componenten;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class searchForFileList extends JFrame {
    JPanel panel;
    JTextArea textArea;
    JScrollPane scrollPane;
    JButton button;
    JOptionPane optionPane;
    public searchForFileList() {
        panel = new JPanel();
        optionPane = new JOptionPane();
        textArea = new JTextArea();
        scrollPane = new JScrollPane(textArea);
        button = new JButton("search");
        this.setContentPane(panel);
        panel.setLayout(null);

        panel.add(textArea);
        scrollPane.add(textArea);
        panel.setBounds(5,5,390,290);
        textArea.setBounds(5,5,390,290);
        button.setBounds(300,0,100,25);

        this.add(optionPane);
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

                List<String> list = new ArrayList<String>();
                list.add(textArea.getText().toString());

                for (int i=0; i< list.size(); i++) {
                    System.out.println(list.get(i));
                    SwingWorker(list.get(i));
                }
            }
        });
    }
    public void SwingWorker(final String list) {
        SwingWorker<Boolean, Integer> worker = new SwingWorker<Boolean, Integer>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                System.out.println("swing");
                // new Table data
                File file = new File(list);
                new searchThread(file.getName(), file);
                return null;
            }
        };
        worker.execute();
    }
}
