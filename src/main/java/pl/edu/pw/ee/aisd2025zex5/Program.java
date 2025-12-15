package pl.edu.pw.ee.aisd2025zex5;

import java.math.BigInteger;

import java.io.*;

public class Program
{
    public static void main(String [] args)
    {
        long startTime = System.nanoTime();
        FlagReader flag_reader = new FlagReader(args);
        FileReader file_reader = new FileReader(flag_reader.getInputPath(), flag_reader.getSequenceLength());

        HashMap hash_map = file_reader.countCodes();

        if(flag_reader.getSequenceLength() > 1) {
            int filter_counter = hash_map.filterCounter(flag_reader.getSequenceLength());

            System.out.println("Elementy do wstawienia: " + filter_counter);
            if (filter_counter < hash_map.getNumEntries()) {
                System.out.println("Przefiltrowano pomyslnie");
                filter_counter /= 0.75;
                filter_counter = BigInteger.valueOf(filter_counter).nextProbablePrime().intValue();

                hash_map = hash_map.createFilteredHashMap(filter_counter, flag_reader.getSequenceLength());
            }

            int[] correction_counter = new int[256];

            int check_value = hash_map.filterCounter(correction_counter, flag_reader.getSequenceLength());

            if (check_value != 0) {
                System.out.println("Blad filtrowania");
            }

            hash_map.correctCounters(correction_counter);
        }
        /*for(int i = 0; i < correction_counter.length; i++) {
            System.out.println("Poprawka dla " + (char)i + ": " + correction_counter[i]);
        }*/

        byte [][] keys = hash_map.getKeys();

        PriorityQueue pq = new PriorityQueue(hash_map, keys);
        Dictionary dictionary = new Dictionary(pq);
        pq = null;
        //dictionary.printDictionary();

        FileWriter file_writer = new FileWriter(flag_reader.getOutputPath(), flag_reader.getSequenceLength());
        dictionary.compressDictionary(file_writer.byte_writer);
        file_writer.writeTree();

        BufferedOutputStream output_stream;

        try {
            output_stream = new BufferedOutputStream(new FileOutputStream(flag_reader.getOutputPath()));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage());
        }

        try {
            output_stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }

        ByteWriter code_writer = new ByteWriter(8);

        dictionary.createHuffmanCode(hash_map, code_writer);

        /*for(int i = 0; i < keys.length; i++) {
            System.out.print("huffman_code: ");
            System.out.println(8 - hash_map.getCode(keys[i]).getCounter());
            for (int j = 0; j < hash_map.getCode(keys[i]).getElem(); j++) {
                    System.out.print(Integer.toBinaryString(hash_map.getCode(keys[i]).getHuffmanCode()[j] & 0xFF));
            }
            System.out.println();
        }*/

        file_writer.writeFile(flag_reader.getInputPath(), hash_map);

        long endTime = System.nanoTime();
        long durationNs = endTime - startTime;
        double durationSeconds = durationNs / 1_000_000_000.0;

        System.out.println("Czas kompresji: " + durationSeconds + " s");
    }
}
