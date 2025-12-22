package pl.edu.pw.ee.aisd2025zex5;

import java.io.*;
import java.util.Arrays;

public class FileWriter extends FileParser
{
    protected final ByteWriter byte_writer;
    private final byte [] input_buffer;

    public FileWriter(String file_path, int max_length) {
        FileOutputStream  output_stream;
        this.max_length = max_length;
        try {
            output_stream = new FileOutputStream(file_path);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Nie można utworzyć pliku");
        }
        byte_writer = new ByteWriter(output_stream);
        input_buffer = new byte[64*1024];

        for(int i = 0; i < 3; i++) {
            byte_writer.addBit(0);
        }
    }

    private int processBuffer(HashMap hash_map, byte [] buffer, int buffer_elem, int start_pointer)
    {
        int pointer = start_pointer;
        Code huffman_code;

        while(pointer + max_length - 1 < buffer_elem) {
            boolean phrase_found = false;
            for (int i = max_length - 1 + pointer; i >= pointer; i--) {
                if ((huffman_code = hash_map.getCode(Arrays.copyOfRange(buffer, pointer, i + 1))) != null) {
                    byte_writer.addHuffmanCode(huffman_code);
                    phrase_found = true;
                    pointer = i+1;
                    break;
                }
            }
            if (!phrase_found) {
                if (pointer < buffer_elem) {
                    huffman_code = hash_map.getCode(Arrays.copyOfRange(buffer, pointer, pointer + 1));
                    if (huffman_code != null) {
                        byte_writer.addHuffmanCode(huffman_code);
                    }
                    pointer += 1;
                } else {
                    break;
                }
            }
        }

        return pointer;
    }

    private int processMergedBuffer(HashMap hash_map, byte[] mergedBuffer)
    {
        int pointer = 0;
        Code huffman_code;

        while(pointer + max_length - 1 < mergedBuffer.length) {
            for(int i = max_length - 1 + pointer; i >= pointer; i--) {
                if((huffman_code = hash_map.getCode(Arrays.copyOfRange(mergedBuffer, pointer, i+1))) != null ) {
                   byte_writer.addHuffmanCode(huffman_code);
                   pointer = i + 1;
                   break;
                }
            }
        }

        return pointer;
    }

    public void writeFile(String file_path, HashMap hash_map)
    {
        BufferedInputStream input_stream;
        byte [] leftOvers = new byte[0];
        byte [] mergedBuffer;

        try {
            input_stream = new BufferedInputStream(new FileInputStream(file_path));
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Blad otwarcia pliku");
        }

        int buffer_elem;

        try {
            while((buffer_elem = input_stream.read(input_buffer, 0, input_buffer.length)) != -1)
            {
                int pointer = 0;

                if(leftOvers.length > 0 && max_length >= 2) {
                    mergedBuffer = createMergedBuffer(leftOvers, input_buffer, buffer_elem);
                    int mergedPointer = processMergedBuffer(hash_map, mergedBuffer);

                    int bytesConsumedFromInput = mergedPointer - leftOvers.length;

                    if (bytesConsumedFromInput < 0) {
                        leftOvers = Arrays.copyOfRange(mergedBuffer, mergedPointer, mergedBuffer.length);

                        continue;
                    } else {
                        pointer = bytesConsumedFromInput;
                    }
                }

                pointer = processBuffer(hash_map, input_buffer, buffer_elem, pointer);
                leftOvers = Arrays.copyOfRange(input_buffer, pointer, buffer_elem);
            }

            if (leftOvers.length > 0 && max_length >= 2) {
                processFinalBlock(hash_map, leftOvers);
            }

            byte_writer.flush();
            input_stream.close();

        } catch (IOException e) {
            throw new RuntimeException("Blad zapisu pliku");
        }
    }

    private void processFinalBlock(HashMap hash_map, byte[] finalBuffer) {
        int pointer = 0;
        Code huffman_code;

        while (pointer < finalBuffer.length) {

            boolean found = false;

            int remainingBytes = finalBuffer.length - pointer;
            int maxSearch = Math.min(max_length, remainingBytes);

            for (int len = maxSearch; len >= 1; len--) {
                byte[] subSequence = Arrays.copyOfRange(finalBuffer, pointer, pointer + len);

                if ((huffman_code = hash_map.getCode(subSequence)) != null) {
                    byte_writer.addHuffmanCode(huffman_code);
                    pointer += len;
                    found = true;
                    break;
                }
            }

            if (!found) {
                pointer++;
            }
        }
    }

}
