package pl.edu.pw.ee.aisd2025zex5;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

class HashMapTest {

    private HashMap hashMap;
    private final int MAP_SIZE = 20;
    private final int MAX_KEY_LENGTH = 10;

    @BeforeEach
    void setUp() {
        hashMap = new HashMap(MAP_SIZE, MAX_KEY_LENGTH);
    }

    @Test
    void testGetKeys() {
        byte[] key1 = {1, 2, 3};
        byte[] key2 = {4, 5};
        hashMap.incrementCounter(key1);
        hashMap.incrementCounter(key2);

        byte[][] keys = hashMap.getKeys();

        assertEquals(2, keys.length);

        boolean foundKey1 = false;
        boolean foundKey2 = false;

        for (byte[] k : keys) {
            if (Arrays.equals(k, key1)) foundKey1 = true;
            if (Arrays.equals(k, key2)) foundKey2 = true;
        }

        assertTrue(foundKey1);
        assertTrue(foundKey2);
    }

    @Test
    void testPutAndGetCode() {
        byte[] key = {10, 20};
        Code mockCode = new Code();

        hashMap.incrementCounter(key);

        hashMap.putCode(key, mockCode);
        Code retrievedCode = hashMap.getCode(key);

        assertNotNull(retrievedCode);
        assertEquals(mockCode, retrievedCode);
    }

    @Test
    void testGetCodeNonExistent() {
        Code code = hashMap.getCode(new byte[]{99, 99});

        assertNull(code);
    }

    @Test
    void testCorrectCounters() {
        int[] corrections = new int[256];
        int targetIndex = 5;
        int initialCount = 1500;
        int correctionValue = 200;

        byte[] key = new byte[]{(byte) targetIndex};

        for(int i=0; i<initialCount; i++) {
            hashMap.incrementCounter(key);
        }

        corrections[targetIndex] = correctionValue;

        hashMap.correctCounters(corrections);

        int currentCount = hashMap.getCounter(key);
        assertEquals(1300, currentCount);
    }

    @Test
    void testCorrectCountersClampingAt1000() {
        int targetIndex = 10;
        byte[] key = new byte[]{(byte) targetIndex};
        int[] corrections = new int[256];

        for(int i=0; i<1050; i++) {
            hashMap.incrementCounter(key);
        }

        corrections[targetIndex] = 500;

        hashMap.correctCounters(corrections);

        int currentCount = hashMap.getCounter(key);
        assertEquals(1000, currentCount);
    }

    @Test
    void testFilterCounterSimple() {
        byte[] longRareKey = {1, 2, 3, 4, 5};
        hashMap.incrementCounter(longRareKey);

        byte[] shortKey = {1};
        hashMap.incrementCounter(shortKey);

        int remainingEntries = hashMap.filterCounter(MAX_KEY_LENGTH);

        assertEquals(1, remainingEntries);
    }

    @Test
    void testFilterCounterWithCorrectionArray() {
        int[] correctionCounters = new int[256];

        byte[] weakKey = {2, 3, 4};
        hashMap.incrementCounter(weakKey);

        byte[] strongKey = {5, 6, 7};
        for(int i=0; i<100; i++) hashMap.incrementCounter(strongKey);

        int filteredCount = hashMap.filterCounter(correctionCounters, MAX_KEY_LENGTH);

        assertEquals(1, filteredCount);

        assertEquals(100, correctionCounters[5]);
        assertEquals(100, correctionCounters[6]);
        assertEquals(100, correctionCounters[7]);
    }

    @Test
    void testGetCounterNonExistent() {
        int count = hashMap.getCounter(new byte[]{12, 34});
        assertEquals(0, count);
    }

    @Test
    void testCollisionHandling() {

        for (int i = 0; i < 15; i++) {
            hashMap.incrementCounter(new byte[]{(byte) i, (byte) (i * 2)});
        }

        assertEquals(15, hashMap.getNumEntries());

        byte[] keyToCheck = new byte[]{0, 0};
        assertTrue(hashMap.getCounter(keyToCheck) > 0);
    }
}