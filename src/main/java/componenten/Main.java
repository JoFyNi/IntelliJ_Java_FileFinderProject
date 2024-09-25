package componenten;

import javax.swing.*;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                createGUI();
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    // Erstelle die GUI für das Programm
    private static void createGUI() throws IOException, InterruptedException {
        MainUI ui = new MainUI();
        JPanel root = ui.getRootPanel();
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setContentPane(root);
        frame.pack();
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}