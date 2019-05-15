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
        miLibro = new File(System.getProperty("user.dir") + "/src/out/miLibro.txt");
        escucharUnCuento = () -> escucharElCuento(pis);
    }

    public void escucharElCuento(PipedInputStream pis) {
        try {
            int verso;
            boolean ejecucion = true;
            this.escritor = new BufferedWriter(new FileWriter(this.miLibro));
            while (ejecucion) {
                switch (estado){
                    case ESPERA:
                        manejoDeSeccionCritica();
                        break;
                    case LECTURA:
                        verso = pis.read();
                        this.entrada = Character.toString(((char) verso));
                        if (this.entrada.equals(";")){
                            this.estado = Estados.TERMINAR;
                            manejoDeSeccionCritica();
                        } else {
                            this.escritor.write(entrada);
                            manejoDeSeccionCritica();
                            break;
                        }
                    case TERMINAR:
                        this.setVisible(true);
                        this.escritor.close();
                        ejecucion = false;
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void manejoDeSeccionCritica() throws IOException {
        switch (this.estado) {
            case LECTURA:
                BufferedWriter cambioEstado1 = new BufferedWriter(new FileWriter(this.direccionDeMemoria));
                cambioEstado1.write("0");
                cambioEstado1.close();
                estado = Estados.ESPERA;
                break;
            case ESPERA:
                BufferedReader bandera = new BufferedReader(new FileReader(this.direccionDeMemoria));
                String s = bandera.readLine();
                bandera.close();
                if (s.equals("1")){
                    this.estado = Estados.LECTURA;
                } else {
                    try {
                        Thread.sleep(110);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case TERMINAR:
                pis.close();
                break;

        }
    }

    public void start() {
        this.estado = Estados.ESPERA;
        new Thread(this.escucharUnCuento).start();
    }

}
