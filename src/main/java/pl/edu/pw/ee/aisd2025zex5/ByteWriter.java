package pl.edu.pw.ee.aisd2025zex5;

import java.io.*;
import java.util.Arrays;

public class ByteWriter
{
    private FileOutputStream output_stream;
    protected final byte[] output_buffer;
    protected int elem = 0;
    protected byte buffer = 0;
    protected int counter = 0;

    public ByteWriter() {
        output_buffer = new byte[64 * 1024];
    }

    public ByteWriter(FileOutputStream output_stream) {

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

    public void addCode(byte [] code) {
        for(int i = 0; i < code.length; i++) {
            output_buffer[elem++] = code[i];
            if(elem >= output_buffer.length) {
                write(output_stream);
            }
        }
    }

    public void addLength(int length, int max_length) {
        int numOfBits = 32 - Integer.numberOfLeadingZeros(max_length);

        for(int i = numOfBits-1; i >= 0; i--) {
            addBit(length >> i & 1);
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

    private void write(FileOutputStream output_stream) {

        try {
            output_stream.write(output_buffer, 0, elem);
            elem = 0;
        } catch (IOException e) {
            throw new RuntimeException("Blad zapisu pliku");
        }
    }

    public void flush () {
        if(counter > 0) {
            buffer = (byte) (buffer << (8 - counter));
            output_buffer[elem++] = buffer;
        }

        try {
            output_stream.write(output_buffer, 0, elem);
            output_stream.close();
        } catch (IOException e) {
            throw new RuntimeException("Blad zapisu pliku");
        }
    }

    public void setNumberOfZeros(String file_path) {

        try (RandomAccessFile random_access_file = new RandomAccessFile(file_path, "rw")) {
            random_access_file.seek(0);

            int firstByte = random_access_file.read();

            int newFirstByte = ((8 - counter)%8 << 5) | (firstByte & 0xFF);

            random_access_file.seek(0);
            random_access_file.write(newFirstByte);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Nie znaleziono pliku");
        } catch (IOException e) {
            throw new RuntimeException("Blad zapisu pliku");
        }
    }
}