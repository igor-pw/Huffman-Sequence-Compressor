package pl.edu.pw.ee.aisd2025zex5;

import java.io.*;
import java.util.Arrays;

public class FileReader extends FileParser
{
    private final int M = 1_000_000;
    private final int R = 10*1024*1024;
    private int M_counter = 0;
    private int R_counter = 0;
    private boolean stop_reading = false;
    private final byte [] buffer;
    private final BufferedInputStream inputStream;

    public FileReader(String file_path, int max_length) {
        buffer = new byte [65536];
        this.max_length = max_length;
        try {
            inputStream = new BufferedInputStream(new FileInputStream(file_path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Plik nie istnieje");
        }
    }

    private void processBuffer(HashMap hash_map, byte [] buffer, int buffer_elem)
    {
        for(int i = 0; i < buffer_elem; i++) {
            hash_map.incrementCounter(Arrays.copyOfRange(buffer, i, i + 1));

            if(M_counter < M - max_length && max_length >= 2) {
                for(int j = 2; j <= max_length; j++) {
                    if(i + j <= buffer_elem) {
                        if(hash_map.incrementCounter(Arrays.copyOfRange(buffer, i, i + j))) {
                            M_counter++;
                        }
                    }
                }
            }
            else if(max_length >= 2) {
                stop_reading = true;
                break;
            }
            R_counter++;

            if(R_counter >= R) {
                stop_reading = true;
                break;
            }
        }
    }

    private void processMergedBuffer(HashMap hash_map, byte[] mergedBuffer, int boundary)
    {
        for(int i = 0; i < boundary; i++) {

            if(M >= M_counter - max_length) {
                return;
            }

            for(int j = 2; j <= max_length; j++) {
                if(i + j <= mergedBuffer.length) {
                    if(i + j > boundary) {
                        if(hash_map.incrementCounter(Arrays.copyOfRange(mergedBuffer, i, i + j))) {
                            M_counter++;
                        }
                    }
                }
            }
        }
    }

    public HashMap countCodes()
    {
        HashMap hash_map = new HashMap(1333337, max_length); //65539

        int buffer_elem;
        byte [] leftOvers = null;
        byte [] mergedBuffer;

        try {
            while(!stop_reading && (buffer_elem = inputStream.read(buffer, 0, buffer.length)) != -1) {
                if(leftOvers != null && max_length >= 2) {
                    mergedBuffer = createMergedBuffer(leftOvers, buffer,  buffer_elem);
                    processMergedBuffer(hash_map, mergedBuffer, leftOvers.length);
                }

                processBuffer(hash_map, buffer, buffer_elem);

                if(buffer_elem > 0) {
                    int max_elem = Math.min(buffer_elem, max_length - 1);
                    leftOvers = Arrays.copyOfRange(buffer, buffer_elem - max_elem, buffer_elem);
                }
            }

            //dodac sztuczne dopelnienie jesli nie odczytamy calego plku

            inputStream.close();
        } catch (IOException e) {
            throw new RuntimeException("Bład odczytu");
        }

        return hash_map;
    }

}

