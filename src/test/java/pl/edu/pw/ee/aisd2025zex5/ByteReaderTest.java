package pl.edu.pw.ee.aisd2025zex5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.*;

class ByteReaderTest {
    private final String file_path = "test_data/test_reader.comp";
    private final int TOTAL_BITS = 16;
    private final int HEADER_BITS = 3;

    @BeforeEach
    void setUp() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(file_path)) {
            fos.write(0x1B);
            fos.write(0xFF);
        }
    }

    @AfterEach
    void tearDown() {
        File file = new File(file_path);
        if (file.exists()) file.delete();
    }

    @Test
    void should_correctly_detect_end_of_file() {
        ByteReader reader = new ByteReader(file_path);
        int bitsToRead = TOTAL_BITS - HEADER_BITS;

        for (int i = 0; i < bitsToRead; i++) {
            assertFalse(reader.isEnd());
            reader.nextBit();
        }
        assertTrue(reader.isEnd());
    }

    @Test
    void should_throw_exception_when_reading_past_end() {
        ByteReader reader = new ByteReader(file_path);
        int bitsToRead = TOTAL_BITS - HEADER_BITS;

        for (int i = 0; i < bitsToRead; i++) {
            reader.nextBit();
        }

        assertThrows(RuntimeException.class, reader::nextBit);
    }
}