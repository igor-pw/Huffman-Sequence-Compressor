package pl.edu.pw.ee.aisd2025zex5;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import java.nio.file.Files;
import java.nio.file.Path;
import static org.junit.jupiter.api.Assertions.*;

class FileReaderTest {

    @TempDir
    Path tempDir;

    @Test
    void testCountCodesBasicFlow() throws Exception {
        Path inputPath = tempDir.resolve("input.txt");
        // Zapisujemy ciąg, który ma powtórzenia, żeby sprawdzić zliczanie
        String content = "ABCABCABC";
        Files.write(inputPath, content.getBytes());

        FileReader fileReader = new FileReader(inputPath.toString(), 3);

        HashMap result = fileReader.countCodes();

        assertNotNull(result);
        assertTrue(result.getNumEntries() > 0);

        byte[] keyA = {(byte) 'A'};
        assertTrue(result.getCounter(keyA) >= 3);
    }

    @Test
    void testFileNotFound() {
        assertThrows(RuntimeException.class, () -> {
            new FileReader("nieistniejacy_plik_xyz.txt", 3);
        });
    }
}
