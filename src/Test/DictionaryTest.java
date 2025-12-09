import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.pw.ee.aisd2025zex5.PriorityQueue;
import pl.edu.pw.ee.aisd2025zex5.Node;
import pl.edu.pw.ee.aisd2025zex5.Dictionary;
import pl.edu.pw.ee.aisd2025zex5.ByteWriter;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DictionaryTest
{
    private PriorityQueue pq;
    private Dictionary dc;

    @BeforeEach
    public void setUp()
    {
        dc = new Dictionary();
        pq = new PriorityQueue();
        pq.add(new Node(5, (byte)10));
        pq.add(new Node(10, (byte)15));
        pq.add(new Node(7, (byte)20));
        pq.add(new Node(15, (byte)30));
        pq.add(new Node(10, (byte)40));
    }

    @Test
    public void createTreeTest()
    {
        Node parent;
        parent = dc.createTree(pq.pull(), pq.pull());

        assertEquals(12, parent.getCounter());
        assertEquals((byte)10, parent.getLeft().getCode());
        assertEquals((byte)20, parent.getRight().getCode());
    }

    @Test
    public void createDictionaryTest()
    {
        dc.createDictionary(pq);
        Node result = dc.getRoot();

        Node left = result.getLeft();
        Node right = result.getRight();

        assertEquals(47, result.getCounter());

        assertEquals(27, right.getCounter());
        assertEquals(20, left.getCounter());

        assertEquals(10, left.getLeft().getCounter());
        assertEquals((byte)15, left.getLeft().getCode());

        assertEquals(10, left.getRight().getCounter());
        assertEquals((byte)40, left.getRight().getCode());

        assertEquals(15, right.getRight().getCounter());
        assertEquals((byte)30, right.getRight().getCode());

        assertNull(right.getRight().getRight());
        assertNull(right.getRight().getLeft());

        assertNotNull(right.getLeft().getRight());
        assertNotNull(right.getLeft().getLeft());
    }

    @Test
    public void compressDictionaryTest()
    {
        dc.createDictionary(pq);
        ByteWriter bw = new ByteWriter();
        dc.compressDictionary(bw);


    }
}
