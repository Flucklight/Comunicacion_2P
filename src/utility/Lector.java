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
    private Runnable contarUnCuneto;

    public Lector() throws IOException {
        super("Escritor");
        setContentPane(panel1);
        setSize(Toolkit.getDefaultToolkit().getScreenSize());
        setResizable(false);
        setLocationRelativeTo(this);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        estado = Estados.CREACION;
        pos = new PipedOutputStream();
        direccionDeMemoria = new File(System.getProperty("user.dir") + "/src/items/5000.mem");
        cuento = new File(System.getProperty("user.dir") + "/src/items/cuento.txt");
        cargar = new BufferedReader(new FileReader(cuento));
        String s;
        while ((s = cargar.readLine()) != null) {
            elCuento += s + "\n";
        }
        contarUnCuneto = () -> contarElCuento(pos);
        manejoDeSeccionCritica();
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
                        pos.write(((byte) caracter));
                        manejoDeSeccionCritica();
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

    public void manejoDeSeccionCritica() throws IOException {
        switch (this.estado) {
            case CREACION:
                BufferedWriter cambioEstado0 = new BufferedWriter(new FileWriter(this.direccionDeMemoria));
                cambioEstado0.write("0");
                cambioEstado0.close();
                estado = Estados.ESPERA;
                break;
            case ESCRITURA:
                BufferedWriter cambioEstado1 = new BufferedWriter(new FileWriter(this.direccionDeMemoria));
                cambioEstado1.write("1");
                cambioEstado1.close();
                estado = Estados.ESPERA;
                break;
            case ESPERA:
                BufferedReader bandera = new BufferedReader(new FileReader(this.direccionDeMemoria));
                String s = bandera.readLine();
                bandera.close();
                if (s.equals("0")){
                    estado = Estados.ESCRITURA;
                } else {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
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
