package pl.edu.pw.ee.aisd2025zex5;

import java.util.Arrays;
import java.util.Random;

public class HashMap
{
    private final double max_margin = 79.0;
    private final double min_margin = 15.0;
    private final int size;
    private int numEntries;
    private final MapEntry [] elem;
    private final int [] consts_f1;
    private final int [] consts_f2;

    public byte[][] getKeys() {
        byte [][] keys = new byte[numEntries][];

        int counter = 0;

        for(int i = 0; i < size; i++) {
            if(elem[i] != null) {
                keys[counter] = new byte[elem[i].key.length];
                System.arraycopy(elem[i].key, 0, keys[counter++], 0, elem[i].key.length);
            }
        }

        return keys;
    }

    public HashMap(int size, int max_length)
    {
        consts_f1 = new int[max_length];
        consts_f2 = new int[max_length];
        this.size = size;

        Random rand = new Random(337609);

        for(int i = 0; i < max_length; i++) {
            consts_f1[i] = rand.nextInt() | 1;
            consts_f2[i] = rand.nextInt() | 1;
        }

        elem = new MapEntry[size];
    }

    public int getNumEntries() { return numEntries; }

    public void putCode(byte [] key, Code huffman_code)
    {
        int i = 0;
        int hash_key = hashFunc(key);
        int f1 = hashFunc1(hash_key);
        int f2 = hushFunc2(hash_key);
        int index = hashFunc(f1, f2, i);

        while(elem[index] != null) {

            if(Arrays.equals(elem[index].key, key)) {
                elem[index].huffman_code = huffman_code;
                return;
            }

            index = hashFunc(f1, f2, ++i);
        }
    }

    public boolean incrementCounter(byte [] key)
    {
        int i = 0;
        int hash_key = hashFunc(key);
        int f1 = hashFunc1(hash_key);
        int f2 = hushFunc2(hash_key);
        int index = hashFunc(f1, f2, i);

        while(elem[index] != null) {
            if(Arrays.equals(elem[index].key, key)) {
                elem[index].counter++;
                return false;
            }

            index = hashFunc(f1, f2, ++i);
        }

        elem[index] = new MapEntry(key);
        numEntries++;
        elem[index].counter++;
        return true;
    }

    private void putCorrectedCounter(byte [] key, int corrected_counter)
    {
        int i = 0;
        int hash_key = hashFunc(key);
        int f1 = hashFunc1(hash_key);
        int f2 = hushFunc2(hash_key);
        int index = hashFunc(f1, f2, i);

        while(elem[index] != null) {
            if(Arrays.equals(elem[index].key, key)) {
                elem[index].counter -= corrected_counter;
                elem[index].counter = Math.max(elem[index].counter, 1000);
                return;
            }

            index = hashFunc(f1, f2, ++i);
        }
    }

    private void putMapEntry(byte [] key, MapEntry map_entry)
    {
        int i = 0;
        int hash_key = hashFunc(key);
        int f1 = hashFunc1(hash_key);
        int f2 = hushFunc2(hash_key);
        int index = hashFunc(f1, f2, i);

        while(elem[index] != null) {
            index = hashFunc(f1, f2, ++i);
        }

        elem[index] = map_entry;
    }

    public HashMap createFilteredHashMap(int new_size, int max_length)
    {
        HashMap new_hash_map = new HashMap(new_size, consts_f1.length);
        int filteredNumEntries = 0;
        double start_header_cost = (Math.ceil(Math.log(max_length)/Math.log(2) + Math.log(numEntries)/Math.log(2)))/8.0;

        for(int i = 0; i < size; i++) {
            if(elem[i] != null) {
                if(elem[i].key.length >= 2) {
                    int gain = elem[i].counter * (elem[i].key.length - 1);
                    int thresh_hold = calculateThreshHold(elem[i].key.length, max_length, start_header_cost);
                    if (gain >= thresh_hold) {
                        new_hash_map.putMapEntry(elem[i].key, elem[i]);
                        filteredNumEntries++;
                    }
                }
                else {
                    new_hash_map.putMapEntry(elem[i].key, elem[i]);
                    filteredNumEntries++;
                }
            }
        }

        new_hash_map.numEntries = filteredNumEntries;

        return new_hash_map;
    }

    public int getCounter(byte [] key)
    {
        int i = 0;
        int hash_key = hashFunc(key);
        int f1 = hashFunc1(hash_key);
        int f2 = hushFunc2(hash_key);
        int index = hashFunc(f1, f2, i);

        while(elem[index] != null) {
            if(Arrays.equals(elem[index].key, key)) {
               return elem[index].counter;
            }

            index = hashFunc(f1, f2, ++i);
        }

        return 0;
    }

    public Code getCode(byte [] key)
    {
        int i = 0;
        int hash_key = hashFunc(key);
        int f1 = hashFunc1(hash_key);
        int f2 = hushFunc2(hash_key);
        int index = hashFunc(f1, f2, i);

        while(elem[index] != null) {
            if(Arrays.equals(elem[index].key, key)) {
                return elem[index].huffman_code;
            }

            index = hashFunc(f1, f2, ++i);
        }

        return null;
    }

    private int hashFunc(byte [] key)
    {
        long f = 0;
        int fkey = 0;

        for(int j = 0; j < key.length; j++) {
            fkey = key[j] & 0xFF;
            f += ((long) consts_f1[j]*fkey);
        }

        return (int)f & Integer.MAX_VALUE;
    }

    private int hashFunc1(int hashed_key) {
        return hashed_key % size;
    }

    private int hushFunc2(int hashed_key) {
        return (1 + hashed_key % (size - 1));
    }

    private int hashFunc(int f1, int f2, int i) {
        return ((f1%size + ( (1 + f2%(size-1)) * i))%size);
    }

    public int filterCounter(int max_length)
    {
        double start_header_cost = (Math.ceil(Math.log(max_length)/Math.log(2) + Math.log(numEntries)/Math.log(2)))/8.0;
        int filterCounter = 0;

        for(int i = 0; i < size; i++) {
            if(elem[i] != null) {
                if(elem[i].key.length >= 2) {
                    int gain = elem[i].counter * (elem[i].key.length - 1);
                    int thresh_hold = calculateThreshHold(elem[i].key.length, max_length, start_header_cost);
                    if (gain < thresh_hold) {
                        filterCounter++;
                    }
                }
            }
        }
        return numEntries - filterCounter;
    }

    public int filterCounter(int [] correction_counters, int max_length)
    {
        int filterCounter = 0;
        double start_header_cost = (Math.ceil(Math.log(max_length)/Math.log(2) + Math.log(numEntries)/Math.log(2)))/8.0;

        for(int i = 0; i < size; i++) {
            if(elem[i] != null) {
                if(elem[i].key.length >= 2) {
                    int gain = elem[i].counter * (elem[i].key.length - 1);
                    int thresh_hold = calculateThreshHold(elem[i].key.length, max_length, start_header_cost);
                    if (gain < thresh_hold) {
                        filterCounter++;
                    }
                    else {
                        for(int j = 0; j < elem[i].key.length; j++) {
                            correction_counters[elem[i].key[j] & 0xFF] += elem[i].counter;
                        }
                    }
                }
            }
        }

        return filterCounter;
    }

    public void correctCounters(int [] correction_counters)
    {
        for(int i = 0; i < correction_counters.length; i++) {
            byte [] key = new byte[1];
            key[0] = (byte)i;

            putCorrectedCounter(key, correction_counters[i]);
        }
    }

    private int calculateThreshHold(int length, int max_length, double start_header_cost)
    {
        double margin = max_margin - (max_margin - min_margin) * ((double) (length - 2) / (double) (max_length - 2));

        if(length <= 3) {
            margin = 15.0;
        }

        return (int) ((start_header_cost + length) * margin);
    }


    private static class MapEntry
    {
        public byte [] key;
        public Code huffman_code;
        public int counter;

        public MapEntry(byte [] key) {
            this.key = key;
        }
    }
}
