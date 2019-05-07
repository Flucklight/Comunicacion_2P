package utility;

import javax.swing.*;
import java.io.*;

public class Escucha extends JFrame{
    private JPanel panel;
    private JLabel gif;
    private PipedInputStream pis;
    private File miLibro;
    private BufferedWriter escritor;
    private String entrada;
    private Runnable escucharUnCuento;

    public Escucha(PipedOutputStream pos) throws IOException {
        super("Escucha");
        setLocationRelativeTo(this);
        setContentPane(panel);
        setSize(360, 240);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        entrada = "";
        pis = new PipedInputStream(pos);
        miLibro = new File(System.getProperty("user.dir") + "/src/out/miLibro.txt");
        escucharUnCuento = () -> escucharElCuento(pis);
    }

    public void escucharElCuento(PipedInputStream pis) {
        try {
            int verso = -1;
            this.escritor = new BufferedWriter(new FileWriter(this.miLibro));
            while ((verso = pis.read()) != -1) {
                this.entrada = Character.toString(((char) verso));
                this.escritor.write(entrada);
            }
            this.setVisible(true);
            this.escritor.close();
            pis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(this.escucharUnCuento).start();
    }

}
