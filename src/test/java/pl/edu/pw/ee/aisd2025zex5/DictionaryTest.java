package pl.edu.pw.ee.aisd2025zex5;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DictionaryTest {

    @Test
    public void should_correctly_build_tree_from_priority_queue() {
        PriorityQueue pq = new PriorityQueue();
        pq.add(new Node(10, new byte[]{1}));
        pq.add(new Node(20, new byte[]{2}));

        Dictionary dictionary = new Dictionary(pq);

        assertNotNull(dictionary.getRoot());
        assertEquals(30, dictionary.getRoot().getCounter());
    }

    @Test
    public void should_return_correct_code_during_search() {
        Node root = new Node(30, null);
        Node left = new Node(10, new byte[]{65});
        Node right = new Node(20, new byte[]{66});
        root.setLeft(left);
        root.setRight(right);

        Dictionary dictionary = new Dictionary();
        java.lang.reflect.Field field;
        try {
            field = Dictionary.class.getDeclaredField("root");
            field.setAccessible(true);
            field.set(dictionary, root);
        } catch (Exception e) {
        }

        ByteReader br = new ByteReader("data/test.txt") {
            @Override
            public int nextBit() {
                return 0;
            }

            @Override
            public boolean isEnd() {
                return false;
            }
        };

        assertArrayEquals(new byte[]{65}, dictionary.searchDictionary(br));
    }

    @Test
    public void should_handle_empty_priority_queue() {
        Dictionary dictionary = new Dictionary(new PriorityQueue());
        assertNull(dictionary.getRoot());
    }
}