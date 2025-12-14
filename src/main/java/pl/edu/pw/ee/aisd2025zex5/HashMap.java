package pl.edu.pw.ee.aisd2025zex5;

import java.util.Arrays;
import java.util.Random;

public class HashMap
{
    private final int Z = 500;
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
        int index = hashFunc(key, i);

        while(elem[index] != null) {

            if(Arrays.equals(elem[index].key, key)) {
                elem[index].huffman_code = huffman_code;
                return;
            }

            index = hashFunc(key, ++i);
        }
    }

    public boolean incrementCounter(byte [] key)
    {
        int i = 0;
        int index = hashFunc(key, i);

        while(elem[index] != null) {
            if(Arrays.equals(elem[index].key, key)) {
                //System.out.println("Wystapila kolizja");
                elem[index].counter++;
                return false;
            }

            index = hashFunc(key, ++i);
        }

        elem[index] = new MapEntry(key);
        numEntries++;
        elem[index].counter++;
        return true;
    }

    private void putCorrectedCounter(byte [] key, int corrected_counter)
    {
        int i = 0;
        int index = hashFunc(key, i);

        while(elem[index] != null) {
            if(Arrays.equals(elem[index].key, key)) {
                elem[index].counter -= corrected_counter;
                //System.out.println("skorygowany licznik:" + elem[index].counter);
                elem[index].counter = Math.max(elem[index].counter, 1000);
                return;
            }

            index = hashFunc(key, ++i);
        }
    }

    private void putMapEntry(byte [] key, MapEntry map_entry)
    {
        int i = 0;
        int index = hashFunc(key, i);

        while(elem[index] != null) {
            index = hashFunc(key, ++i);
        }

        elem[index] = map_entry;
    }

    public HashMap createFilteredHashMap(int new_size)
    {
        HashMap new_hash_map = new HashMap(new_size, consts_f1.length);
        int filteredNumEntries = 0;

        for(int i = 0; i < size; i++) {
            if(elem[i] != null) {
                if(elem[i].key.length >= 2) {
                    int gain = elem[i].counter * (elem[i].key.length - 1);
                    if (gain >= Z) {
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
        int index = hashFunc(key, i);

        while(elem[index] != null) {
            if(Arrays.equals(elem[index].key, key)) {
               return elem[index].counter;
            }

            index = hashFunc(key, ++i);
        }

        return 0;
    }

    public Code getCode(byte [] key)
    {
        int i = 0;
        int index = hashFunc(key, i);

        while(elem[index] != null) {
            if(Arrays.equals(elem[index].key, key)) {
                return elem[index].huffman_code;
            }

            index = hashFunc(key, ++i);
        }

        return null;
    }

    private int hashFunc(byte [] key, int i)
    {
        long f1 = 0;
        long f2 = 0;
        int fkey = 0;

        for(int j = 0; j < key.length; j++) {
            fkey = key[j] & 0xFF;
            f1 += ((long) consts_f1[j]*fkey);
            f2 += ((long) consts_f2[j]*fkey);
        }

        f1 = f1 & Integer.MAX_VALUE;
        f2 = f2 & Integer.MAX_VALUE;

        return (int)( (f1%size + ( (1 + f2%(size-1)) * i))%size );
    }

    public int filterCounter()
    {
        int filterCounter = 0;
        for(int i = 0; i < size; i++) {
            if(elem[i] != null) {
                if(elem[i].key.length >= 2) {
                    int gain = elem[i].counter * (elem[i].key.length - 1);
                    if (gain < Z) {
                        filterCounter++;
                    }
                }
            }
        }
        System.out.println("Elementy w hashmap przed odfiltrowaniem: " + numEntries);
        return numEntries - filterCounter;
    }

    public int filterCounter(int [] correction_counters)
    {
        int filterCounter = 0;
        for(int i = 0; i < size; i++) {
            if(elem[i] != null) {
                if(elem[i].key.length >= 2) {
                    int gain = elem[i].counter * (elem[i].key.length - 1);
                    if (gain < Z) {
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
