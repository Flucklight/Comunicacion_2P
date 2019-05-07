import javax.swing.*;
import java.awt.*;
import java.io.*;

public class Writer extends JFrame {
    private JTextArea Texto;
    private JPanel panel1;
    private javax.swing.JLabel JLabel;
    private PipedOutputStream pos;
    private String elCuento = "";
    private char caracter = ' ';
    private File cuento;
    private BufferedReader cargar;
    private Runnable contarUnCuneto;

    public Writer() throws HeadlessException, IOException {
        super("Escritor");
        setLocationRelativeTo(this);
        setContentPane(panel1);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        cuento = new File(System.getProperty("user.dir") + "/src/cuento.txt");
        cargar = new BufferedReader(new FileReader(cuento));
        String s;
        while ((s = cargar.readLine()) != null) {
            elCuento += s + "\n";
        }
        contarUnCuneto = () -> contarElCuento(pos);
    }

    public static void main(String[] args) {
        try {
            Writer las = new Writer();
            las.setVisible(true);
            las.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void contarElCuento(PipedOutputStream pos) {
        try {
            for (int i = 0; i < elCuento.length(); i++) {
                caracter = elCuento.charAt(i);
                this.Texto.append(Character.toString(caracter));
                Thread.sleep(10);
                /*pos.write(((byte) caracter));
                if (caracter == '\n') {
                    pos.flush();
                }*/
            }
            System.out.println("Buenas noches");
            /*pos.write("Buenas noches".getBytes());
            pos.close();*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        new Thread(this.contarUnCuneto).start();
    }

}
