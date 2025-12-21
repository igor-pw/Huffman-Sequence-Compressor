package pl.edu.pw.ee.aisd2025zex5;

import java.io.*;

public class ByteReader
{
    private final byte [] input_buffer;
    private BufferedInputStream input;
    private byte buffer;
    private int counter = 7;
    private int pointer = 0;
    private int elem = 0;
    public int numberOfZeros = 0;

    public ByteReader(String file_path) {
        try {
            input = new BufferedInputStream(new FileInputStream(file_path));
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        input_buffer = new byte[64*1024];
        loadNextBuffer();
        buffer = readNextByte();

    }

    public int getElem() { return elem; }

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

    public byte [] readCode(int l) {
        byte [] code = new byte[l];

        for(int i = 0; i < l; i++) {
            code[i] = nextByte();
        }

        return code;
    }

    private byte readNextByte()
    {
        if(pointer >= elem) {
            loadNextBuffer();
            pointer = 0;
        }

        return input_buffer[pointer++];
    }

    private void loadNextBuffer() {
        try {
            elem = input.read(input_buffer, 0, input_buffer.length);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void readNumberOfZeros() {

        byte nrOfZeros = 0;

        for(int i = 0; i < 3; i++) {
            nrOfZeros = (byte)((nrOfZeros << 1 ) | nextBit());
        }

        numberOfZeros = nrOfZeros;
    }

    public int readLength(int max_length) {
        int numOfBits = 32 - Integer.numberOfLeadingZeros(max_length);
        int length = 0;

        for(int i = 0; i < numOfBits; i++) {
            length = (length << 1) | nextBit();
        }

        return length;
    }

    public void skipZeros() {

        while(counter >= 0) {
        //for(int i = 0; i < numberOfZeros+1; i++) {
            nextBit();
        }

        System.out.println("Zera: " + numberOfZeros);
        System.out.println("Counter: " + counter);
    }

    public boolean isEnd() {
        return pointer >= elem && elem < input_buffer.length;
    }
}
