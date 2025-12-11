package pl.edu.pw.ee.aisd2025zex5;

import com.sun.jdi.IntegerType;

import javax.lang.model.util.ElementScanner6;
import java.io.*;

public class ByteWriter
{
    //private final OutputStream outputStream;
    private final byte [] output;
    private int elem = 0;
    private byte buffer = 0;
    private int counter = 3;

    public ByteWriter() {
            output = new byte[512];
    }

    public int getByte() { return buffer; }
    public int getCounter() { return counter; }

    public void addBit(int bit)
    {
        buffer = (byte) ((buffer << 1) | bit);
        counter++;

        if(counter >= 8) {
            output[elem++] = buffer;
            counter = 0;
        }
    }

    public void addByte(byte b)
    {
        for(int i = 7; i >= 0; i--) {
            addBit((b >> i) & 1);
        }
    }

    public void writeTree(BufferedOutputStream outputStream)
    {
        byte numberOfZeros = (byte)counter;
        output[0] = (byte)((numberOfZeros << 5) | output[0]);

        System.out.println(Integer.toBinaryString(output[0]));

        if(counter > 0) {
            buffer = (byte) (buffer << counter);
            addByte(buffer);
        }

        try {
            for(int i = 0; i < elem; i++) {
                outputStream.write(output[i]);
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
