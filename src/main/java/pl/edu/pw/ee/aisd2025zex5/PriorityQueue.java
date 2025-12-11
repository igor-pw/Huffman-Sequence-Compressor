package pl.edu.pw.ee.aisd2025zex5;

public class PriorityQueue
{
    private QueueElement head;

    public PriorityQueue(int [] counters) {
        createPriorityQueue(counters);
    }

    public void add(Node elem)
    {
        if(isEmpty()) {
            head = new QueueElement(elem);
            return;
        }

        head = add(head, elem);
    }

    public Node pull() {

        if(!isEmpty()) {
            QueueElement elem = head;
            head = elem.next;
            return elem.node;
        }

        return null;
    }

    public QueueElement add(QueueElement elem, Node node)
    {
        if(elem == null) {
            return new QueueElement(node);

        }
        if(node.compareTo(elem.node) > 0) {
            QueueElement newElem = new QueueElement(node);
            newElem.next = elem;
            return newElem;
        }


        elem.next = add(elem.next, node);
        return elem;
    }

    private void createPriorityQueue(int [] counters)
    {
        for(int i = 0; i < counters.length; i++) {
            if(counters[i] != 0) {
                add(new Node(counters[i], (byte)i));
            }
        }
    }

    public boolean isEmpty() {
        return head == null;
    }

    private static class QueueElement
    {
        private final Node node;
        private QueueElement next;

        public QueueElement(Node node) {
            this.node = node;
            next = null;
        }
    }
}
