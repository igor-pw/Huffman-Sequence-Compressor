import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.edu.pw.ee.aisd2025zex5.PriorityQueue;
import pl.edu.pw.ee.aisd2025zex5.Node;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNull;

public class PriorityQueueTest
{
    private PriorityQueue pq;

    @BeforeEach
    public void setUp() {
        pq = new PriorityQueue();
    }

    @Test
    public void isEmptyTest()
    {
        assertTrue(pq.isEmpty());

        pq.add(new Node());
        assertFalse(pq.isEmpty());
    }

    @Test
    public void addAndPullWhenQueueIsEmptyTest()
    {
        pq.add(new Node(5, (byte)10));
        Node result = pq.pull();

        assertEquals(5, result.getCounter());
        assertEquals((byte)10, result.getCode());
    }

    @Test
    public void addAndPullMultipleElementsTest()
    {
        Node result;

        pq.add(new Node(5, (byte)10));
        pq.add(new Node(10, (byte)15));

        result = pq.pull();

        assertEquals(5, result.getCounter());
        assertEquals((byte)10, result.getCode());

        pq.add(new Node(7, (byte)20));
        pq.add(new Node(15, (byte)30));

        result = pq.pull();

        assertEquals(7, result.getCounter());
        assertEquals((byte)20, result.getCode());

        result = pq.pull();

        assertEquals(10, result.getCounter());
        assertEquals((byte)15, result.getCode());
    }

    @Test
    public void addElementsWithSamePriorityTest()
    {
        Node result;

        pq.add(new Node(20, (byte)10));
        pq.add(new Node(5, (byte)15));
        pq.add(new Node(10, (byte)10));
        pq.add(new Node(10, (byte)15));
        pq.add(new Node(10, (byte)20));

        pq.pull();

        result = pq.pull();
        assertEquals(10, result.getCounter());
        assertEquals((byte)10, result.getCode());

        result = pq.pull();
        assertEquals(10, result.getCounter());
        assertEquals((byte)15, result.getCode());

        result = pq.pull();
        assertEquals(10, result.getCounter());
        assertEquals((byte)20, result.getCode());
    }

    @Test
    public void shouldThrowExceptionWhenPullFromEmptyQueue()
    {
        Node result = pq.pull();
        assertNull(result);
    }
}
