import utility.Escucha;
import utility.Lector;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Lector le = new Lector();
        Escucha es = new Escucha(le.getPos());
        le.start();
        es.start();
    }
}
