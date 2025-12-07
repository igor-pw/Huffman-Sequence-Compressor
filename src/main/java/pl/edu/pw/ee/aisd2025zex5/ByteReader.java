package pl.edu.pw.ee.aisd2025zex5;

import java.io.*;

public class ByteReader
{
    private byte buffer;
    private BufferedInputStream input;
    private int counter = 7;
    public int numberOfZeros = 0;

    public ByteReader(String file_path) {
        try {
            input = new BufferedInputStream(new FileInputStream(file_path));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }

        buffer = readNextByte();
    }

    public byte getBuffer() { return buffer; }

    public int nextBit() {
        if(counter < 0) {
            buffer = readNextByte();
            counter = 7;
        }

        return ((buffer >> counter--) & 1);
    }

    public byte nextByte() {
        byte nextByte = 0;

        for(int i = 0; i < 8; i++) {
            nextByte = (byte)((nextByte << 1) | nextBit());
        }

        return nextByte;
    }

    private byte readNextByte() {
        try {
            return (byte)input.read();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        return (byte)0xFF;
    }

    public void readNumberOfZeros() {

        byte nrOfZeros = 0;

        for(int i = 0; i < 3; i++) {
            nrOfZeros = (byte)((nrOfZeros << 1 ) | nextBit());
        }

        numberOfZeros = nrOfZeros;
    }

}
