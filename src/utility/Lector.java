package utility;

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
    private Estados estado;
    private File cuento;
    private File direccionDeMemoria;
    private BufferedReader cargar;
    private BufferedReader bandera;
    private BufferedWriter cambioEstado;
    private Runnable contarUnCuneto;

    public Lector() throws IOException {
        super("Escritor");
        setContentPane(panel1);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setResizable(false);
        setLocationRelativeTo(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        pos = new PipedOutputStream();
        cuento = new File(System.getProperty("user.dir") + "/src/items/cuento.txt");
        direccionDeMemoria = new File(System.getProperty("user.dir") + "/src/items/5000.mem");
        bandera = new BufferedReader(new FileReader(direccionDeMemoria));
        cambioEstado = new BufferedWriter(new FileWriter(direccionDeMemoria));
        cargar = new BufferedReader(new FileReader(cuento));
        String s;
        while ((s = cargar.readLine()) != null) {
            elCuento += s + "\n";
        }
        contarUnCuneto = () -> contarElCuento(pos);
    }

    public void contarElCuento(PipedOutputStream pos) {
        try {
            int i = 0;
            do {
                switch (estado) {
                    case ESPERA:
                        manejoDeSeccionCritica();
                        break;
                    case ESCRITURA:
                        caracter = elCuento.charAt(i);
                        i++;
                        Texto.append(Character.toString(caracter));
                        Thread.sleep(10);
                        pos.write(((byte) caracter));
                        cambioEstado.write(1);
                        cambioEstado.close();
                        estado = Estados.ESPERA;
                        break;
                }
            }while (i < elCuento.length());
            System.out.println("Buenas noches");
            pos.write(((byte) ';'));
            pos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void manejoDeSeccionCritica() {
        try {
            String s = bandera.readLine();
            bandera.close();
            if (s.equals("0")){
                estado = Estados.ESCRITURA;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        this.setVisible(true);
        estado = Estados.ESPERA;
        new Thread(this.contarUnCuneto).start();
    }

    public PipedOutputStream getPos() {
        return pos;
    }
}
