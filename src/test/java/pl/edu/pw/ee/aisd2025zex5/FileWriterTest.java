package pl.edu.pw.ee.aisd2025zex5;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FileWriterTest {

    @Test
    public void should_correctly_reserve_three_bits_for_header() {
        FileWriter writer = new FileWriter("test_data/out.comp", 32);
        try {
            java.lang.reflect.Field writerField = FileWriter.class.getDeclaredField("byte_writer");
            writerField.setAccessible(true);
            ByteWriter bw = (ByteWriter) writerField.get(writer);

            java.lang.reflect.Field counterField = ByteWriter.class.getDeclaredField("counter");
            counterField.setAccessible(true);
            assertEquals(3, counterField.get(bw));
        } catch (Exception e) {}
    }
}