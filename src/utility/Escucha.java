package utility;

import javax.swing.*;
import java.io.*;

public class Escucha extends JFrame{
    private JPanel panel;
    private JLabel gif;
    private PipedInputStream pis;
    private Estados estado;
    private File miLibro;
    private File direccionDeMemoria;
    private BufferedReader bandera;
    private BufferedWriter cambioEstado;
    private BufferedWriter escritor;
    private String entrada;
    private Runnable escucharUnCuento;

    public Escucha(PipedOutputStream pos) throws IOException {
        super("Escucha");
        setLocationRelativeTo(this);
        setContentPane(panel);
        setSize(320, 234);
        setResizable(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        entrada = "";
        pis = new PipedInputStream(pos);
        direccionDeMemoria = new File(System.getProperty("user.dir") + "/src/items/5000.mem");
        bandera = new BufferedReader(new FileReader(direccionDeMemoria));
        cambioEstado = new BufferedWriter(new FileWriter(direccionDeMemoria));
        miLibro = new File(System.getProperty("user.dir") + "/src/out/miLibro.txt");
        escucharUnCuento = () -> escucharElCuento(pis);
    }

    public void escucharElCuento(PipedInputStream pis) {
        try {
            int verso = -1;
            this.escritor = new BufferedWriter(new FileWriter(this.miLibro));
            while ((verso = pis.read()) != -1) {
                switch (estado){
                    case ESPERA:
                        manejoDeSeccionCritica();
                        break;
                    case LECTURA:
                        this.entrada = Character.toString(((char) verso));
                        this.escritor.write(entrada);
                        this.cambioEstado.write(0);
                        this.cambioEstado.close();
                        this.estado = Estados.ESPERA;
                        break;
                }
            }
            this.setVisible(true);
            this.escritor.close();
            pis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void manejoDeSeccionCritica() {
        try {
            String s = bandera.readLine();
            this.bandera.close();
            if (s.equals("1")){
                this.estado = Estados.LECTURA;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        this.estado = Estados.ESPERA;
        new Thread(this.escucharUnCuento).start();
    }

}
