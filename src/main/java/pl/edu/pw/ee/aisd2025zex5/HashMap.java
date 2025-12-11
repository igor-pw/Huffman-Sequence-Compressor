package pl.edu.pw.ee.aisd2025zex5;

import java.security.Key;
import java.util.Random;

public class HashMap
{
    int size;
    private MapEntry [] elem;
    private final int [] consts_f1;
    private final int [] consts_f2;

    public HashMap(int length)
    {
        consts_f1 = new int[length];
        consts_f2 = new int[length];

        Random rand = new Random(337609);

        for(int i = 0; i < length; i++) {
            consts_f1[i] = rand.nextInt() | 1;
            consts_f2[i] = rand.nextInt() | 1;
        }
    }


    public void putCode(byte [] key, byte [] huffman_code)
    {
        int i = 0;
        int index = hashFunc(key, i);

        while(elem[index] != null) {

            if(elem[index].key == key) {
                elem[index].huffman_code = huffman_code;
                return;
            }

            index = hashFunc(key, ++i);
        }
    }

    public void incrementCounter(byte [] key)
    {
        int i = 0;
        int index = hashFunc(key, i);

        while(elem[index] != null) {
            if(elem[index].key == key) {
                elem[index].counter++;
                return;
            }

            index = hashFunc(key, ++i);
        }

        elem[index] = new MapEntry(key);
        elem[index].counter++;
    }

    public int getCounter(byte [] key)
    {
        int i = 0;
        int index = hashFunc(key, i);

        while(elem[index] != null) {
            if(elem[index].key == key) {
               return elem[index].counter;
            }

            index = hashFunc(key, ++i);
        }

        return 0;
    }

    public byte [] getHuffmanCode(byte [] key)
    {
        int i = 0;
        int index = hashFunc(key, i);

        while(elem[index] != null) {
            if(elem[index].key == key) {
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

    private class MapEntry
    {
        public byte [] key;
        public byte [] huffman_code;
        public int counter;

        public MapEntry(byte [] key) {
            this.key = key;
        }

    }
}
