import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.pw.ee.aisd2025zex5.*;
import pl.edu.pw.ee.aisd2025zex5.FileReader;
import pl.edu.pw.ee.aisd2025zex5.FileWriter;

import java.io.*;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class CompressionTest
{
    private FlagReader flag_reader;

    @BeforeEach
    public void setUp() {
        String [] args = {"-m", "comp", "-s", "data/test.txt", "-d", "output/test.comp", "-l", "1"};
        flag_reader = new FlagReader(args);
    }

    @Test
    public void dictionaryCompressionTest()
    {
        FileReader file_reader = new FileReader(flag_reader.getInputPath(), flag_reader.getSequenceLength());

    }
}
