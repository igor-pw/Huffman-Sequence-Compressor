package pl.edu.pw.ee.aisd2025zex5;

import java.io.*;
import java.util.Arrays;

public class ByteWriter
{
    private BufferedOutputStream output_stream;
    protected final byte[] output_buffer;
    protected int elem = 0;
    protected byte buffer = 0;
    protected int counter = 3;

    public ByteWriter() {
        output_buffer = new byte[64 * 1024];
    }

    public ByteWriter(BufferedOutputStream output_stream) {

        this.output_stream = output_stream;
        output_buffer = new byte[64 * 1024];
    }

    public ByteWriter(int size) {
        output_buffer = new byte[size];
    }

    public int getByte() {
        return buffer;
    }

    public byte[] getWrittenBytes() {
        int size;

        if (counter == 0) {
            size = elem;
            return Arrays.copyOfRange(output_buffer, 0, size);
        }

        byte[] result = new byte[elem + 1];
        result[elem] = buffer;

        System.arraycopy(output_buffer, 0, result, 0, elem);

        return result;
    }

    public void addBit(int bit) {
        buffer = (byte) ((buffer << 1) | bit);
        counter++;

        if (counter >= 8) {
            output_buffer[elem++] = buffer;
            counter = 0;
            buffer = 0;
        }

        if(elem >= output_buffer.length) {
            write(output_stream);
        }
    }

    public void removeLSB() {
        if (counter == 0) {
            elem--;
            counter = 8;
            buffer = output_buffer[elem];
        }

        buffer = (byte) ((buffer & 0xFF) >>> 1);
        counter--;
    }

    public void addByte(byte b) {
        for (int i = 7; i >= 0; i--) {
            addBit((b >> i) & 1);
        }
    }

    public void addHuffmanCode(Code huffman_code) {
        byte[] code = huffman_code.getHuffmanCode();
        int huffman_elem = huffman_code.getElem();

        for (int i = 0; i < huffman_elem-1; i++) {
            addByte(code[i]);
        }

        if(huffman_code.getCounter() == 0) {
            addByte(code[huffman_elem-1]);
            return;
        }

        for(int i = 7; i >= 8 - huffman_code.getCounter(); i--) {
            addBit((code[huffman_elem-1] >> i) & 1);
        }
    }

    private void write(BufferedOutputStream output_stream) {
        //System.out.println("Writing " + elem + " bytes to output stream.");
        try {
            output_stream.write(output_buffer, 0, elem);
            elem = 0;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void flush () {
        if(counter > 0) {
            buffer = (byte) (buffer << (8 - counter));
            output_buffer[elem++] = buffer;
        }

        try {
            //System.out.println("Writing " + elem + " bytes to output stream.");
            output_stream.write(output_buffer, 0, elem);
            output_stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}