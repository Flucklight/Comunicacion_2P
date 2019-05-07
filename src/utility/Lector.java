package java;

import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Lector extends JFrame {
    private JTextArea Texto;
    private JPanel panel1;
    private javax.swing.JLabel JLabel;
    private PipedOutputStream pos;
    private String elCuento = "";
    private char caracter = ' ';
    private File cuento;
    private BufferedReader cargar;
    private Runnable contarUnCuneto;

    public Lector() throws IOException {
        super("Escritor");
        setLocationRelativeTo(this);
        setContentPane(panel1);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pos = new PipedOutputStream();
        cuento = new File(System.getProperty("user.dir") + "/src/items/cuento.txt");
        cargar = new BufferedReader(new FileReader(cuento));
        String s;
        while ((s = cargar.readLine()) != null) {
            elCuento += s + "\n";
        }
        contarUnCuneto = () -> contarElCuento(pos);
    }

    public void contarElCuento(PipedOutputStream pos) {
        try {
            for (int i = 0; i < elCuento.length(); i++) {
                caracter = elCuento.charAt(i);
                this.Texto.append(Character.toString(caracter));
                Thread.sleep(100);
                if (caracter == '\n') {
                    pos.write(((byte) caracter));
                }
            }
            pos.flush();
            System.out.println("Buenas noches");
            pos.write("Buenas noches".getBytes());
            pos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        this.setVisible(true);
        new Thread(this.contarUnCuneto).start();
    }

    public PipedOutputStream getPos() {
        return pos;
    }
}
