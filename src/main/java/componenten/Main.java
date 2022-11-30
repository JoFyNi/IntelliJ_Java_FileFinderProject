package componenten;

import javax.swing.*;
import java.io.FileNotFoundException;

public class Main {

    // add something
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    createGUI();
                } catch (FileNotFoundException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    private static void createGUI() throws FileNotFoundException, InterruptedException {
        MainUI ui = new MainUI();
        JPanel root = ui.getRootPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(root);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
