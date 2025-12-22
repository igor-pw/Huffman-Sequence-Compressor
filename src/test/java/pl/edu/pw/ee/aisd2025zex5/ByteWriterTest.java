package pl.edu.pw.ee.aisd2025zex5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

public class ByteWriterTest {

    @TempDir
    Path tempDir;

    @Test
    void testByteWriterLogic() throws IOException {
        Path path = tempDir.resolve("writer.bin");
        try (FileOutputStream fos = new FileOutputStream(path.toFile())) {
            ByteWriter bw = new ByteWriter(fos);

            bw.addCode(new byte[]{1, 2});
            bw.addLength(5, 15);

            byte[] rawCode = new byte[]{ (byte)0xFF };
            Code code = new Code(3, rawCode);
            bw.addHuffmanCode(code);

            bw.removeLSB();
            bw.flush();
        }

        byte[] result = Files.readAllBytes(path);
        assertTrue(result.length > 0);

        ByteWriter zerosWriter = new ByteWriter();
        zerosWriter.addBit(1);
        zerosWriter.setNumberOfZeros(path.toString());
    }
}

/*package pl.edu.pw.ee.aisd2025zex5;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ByteWriterTest {

    @Test
    public void should_correctly_collect_bits_into_byte() {
        ByteWriter writer = new ByteWriter(10);
        for(int i = 0; i < 8; i++) {
            writer.addBit(1);
        }
        byte[] result = writer.getWrittenBytes();
        assertEquals((byte)0xFF, result[0]);
    }

    @Test
    public void should_increment_counter_correctly() {
        ByteWriter writer = new ByteWriter(10);
        writer.addBit(1);
        writer.addBit(0);

        java.lang.reflect.Field counterField;
        try {
            counterField = ByteWriter.class.getDeclaredField("counter");
            counterField.setAccessible(true);
            assertEquals(2, counterField.get(writer));
        } catch (Exception e) {}
    }

    @Test
    public void should_add_full_byte_correctly() {
        ByteWriter writer = new ByteWriter(10);
        writer.addByte((byte)0xAA); // 10101010
        byte[] result = writer.getWrittenBytes();
        assertEquals((byte)0xAA, result[0]);
    }
}*/