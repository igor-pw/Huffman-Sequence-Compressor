import java.io.FileOutputStream;
import java.io.IOException;

public class TestGenerator {
    public static void main(String[] args) {
        try (FileOutputStream fos = new FileOutputStream("test_latwy.bin")) {
            for (int i = 0; i < 1_000_000; i++) {
                if (i % 10 == 0) {
                    fos.write(1);
                } else {
                    fos.write(0);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}