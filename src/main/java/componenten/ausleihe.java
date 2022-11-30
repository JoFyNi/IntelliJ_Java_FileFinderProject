package componenten;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;


public class ausleihe{
    static String ServiceTag;
    static String WindowsVersion;
    static String Prozessor;
    static String Grafikkarte;
    static String Ram;
    static String nutzen;
    static boolean verfuegbarkeit = true;

    public static void main(String[] args) throws IOException {
        Frame();
        /*
        // hiermit wird ein Laptop dem Programm hinzugefügt
        Scanner scanner = new Scanner(System.in);
        System.out.printf("ServiceTag: ");
        ServiceTag = scanner.nextLine();
        System.out.printf("WindowsVersion: ");
        WindowsVersion = scanner.nextLine();
        System.out.printf("Prozessor: ");
        Prozessor = scanner.nextLine();
        System.out.printf("Grafikkarte: ");
        Grafikkarte = scanner.nextLine();
        System.out.printf("Ram art+menge: ");
        Ram = scanner.nextLine();
        System.out.printf("Wofür wird das Gerät genutzt: ");
        nutzen = scanner.nextLine();

        File objectFile = new File("C:\\Users\\j.nievelstein\\Java\\Ausleihe\\src\\main\\java\\componenten\\geraete\\" + ServiceTag + ".txt");
        BufferedWriter ausleiheWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(objectFile, true)));
        ausleiheWriter.write("Service Tag: " + ServiceTag + "\nWindows Version: " + WindowsVersion + "\nProzessor: " + Prozessor + "\nGrafikkarte: " + Grafikkarte + "\nRam: " + Ram + "\nWird als: " + nutzen + " genutzt" );
        ausleiheWriter.newLine();
        ausleiheWriter.close();
        System.out.printf("erfolg");
        */
    }

    public static void addNew() throws IOException {
        ServiceTag = JOptionPane.showInputDialog("ServiceTag");
        WindowsVersion = JOptionPane.showInputDialog("WindowsVersion");
        Prozessor = JOptionPane.showInputDialog("Prozessor");
        Grafikkarte = JOptionPane.showInputDialog("Grafikkarte");
        Ram = JOptionPane.showInputDialog("Ram art+menge");
        nutzen = JOptionPane.showInputDialog("Wofür wird das Gerät genutzt");

        File objectFile = new File("C:\\Users\\j.nievelstein\\Java\\Ausleihe\\src\\main\\java\\componenten\\geraete\\" + ServiceTag + ".txt");
        BufferedWriter ausleiheWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(objectFile, true)));
        ausleiheWriter.write("Verfügbarkeit: "+ verfuegbarkeit + "\nService Tag: " + ServiceTag + "\nWindows Version: " + WindowsVersion + "\nProzessor: " + Prozessor + "\nGrafikkarte: " + Grafikkarte + "\nRam: " + Ram + "\nWird als: " + nutzen + " genutzt" );
        ausleiheWriter.newLine();
        ausleiheWriter.close();
        System.out.printf("erfolg");
    }

    public static void Frame() {
        JFrame frame = new JFrame("Ausleihe");
        frame.setSize(380,600);
        frame.setDefaultLookAndFeelDecorated(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton addBtn = new JButton("add");
        addBtn.setBounds(10,10,80,25);
        addBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    addNew();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        String[] columns = {"Service-Tag","Verfügbarkeit", "Datum"};
        String[][] data = {
                {"G4S72K","true", "none"},{"SM25S5","true", "none"},{"K82S24","true", "none"}
        };
        JTable table = new JTable(data,columns);
        table.setBounds(25,60,330,500);
        table.setLayout(new FlowLayout());
        table.setPreferredScrollableViewportSize(new Dimension(500,50));
        table.setFillsViewportHeight(true);
        table.setEnabled(false);

        JScrollPane scrollPane = new JScrollPane(table);
        //scrollPane.setViewportView(table);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setBounds(25,60,330,500);
        //frame.getContentPane().add(scrollPane);

        //frame.add(new ScrollPane(table));
        panel.add(scrollPane);
        panel.add(table);
        panel.add(addBtn);

        frame.getContentPane().add(panel);
        //frame.add(panel);
        frame.pack();
        frame.setResizable(false);
        //frame.setLayout(null);      // ohne benötige eigenes Designe mit panels etc.
        frame.setVisible(true);
    }
}
