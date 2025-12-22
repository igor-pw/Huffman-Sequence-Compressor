package pl.edu.pw.ee.aisd2025zex5;

import java.awt.*;
import java.math.BigInteger;
import java.io.*;
import static pl.edu.pw.ee.aisd2025zex5.MODE.COMP;

public class Program
{
    public static void main(String [] args)
    {
        FlagReader flag_reader = new FlagReader(args);

        FileReader file_reader = new FileReader(flag_reader.getInputPath(), flag_reader.getSequenceLength());

        if(flag_reader.getMode() == COMP)
        {
            HashMap hash_map = file_reader.countCodes();

            if (flag_reader.getSequenceLength() > 1) {
                int filter_counter = hash_map.filterCounter(flag_reader.getSequenceLength());

                if (filter_counter < hash_map.getNumEntries()) {
                    filter_counter /= 0.75;
                    filter_counter = BigInteger.valueOf(filter_counter).nextProbablePrime().intValue();

                    hash_map = hash_map.createFilteredHashMap(filter_counter, flag_reader.getSequenceLength());
                }

                int[] correction_counter = new int[256];

                int check_value = hash_map.filterCounter(correction_counter, flag_reader.getSequenceLength());

                hash_map.correctCounters(correction_counter);
            }

            byte[][] keys = hash_map.getKeys();

            PriorityQueue pq = new PriorityQueue(hash_map, keys);
            Dictionary dictionary = new Dictionary(pq);
            pq = null;

            FileWriter file_writer = new FileWriter(flag_reader.getOutputPath(), flag_reader.getSequenceLength());
            dictionary.compressDictionary(file_writer.byte_writer, flag_reader.getSequenceLength());

            FileOutputStream output_stream;

            ByteWriter code_writer = new ByteWriter(8);

            dictionary.createHuffmanCode(hash_map, code_writer);

            file_writer.writeFile(flag_reader.getInputPath(), hash_map);
            file_writer.byte_writer.setNumberOfZeros(flag_reader.getOutputPath());
        }

        else {
            ByteReader byte_reader = new ByteReader(flag_reader.getInputPath());
            ByteWriter byte_writer;
            try {
                byte_writer = new ByteWriter(new FileOutputStream(flag_reader.getOutputPath()));
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            Dictionary dictionary = new Dictionary();
            dictionary.decompressFile(byte_reader, byte_writer, flag_reader.getSequenceLength());
            byte_writer.flush();
        }
    }
}
