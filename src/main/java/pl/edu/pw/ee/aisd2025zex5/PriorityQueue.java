package pl.edu.pw.ee.aisd2025zex5;

public class PriorityQueue
{
    private QueueElement head;

    public void add(Node elem)
    {
        if(isEmpty()) {
            head = new QueueElement(elem);
            return;
        }

        head = add(head, elem);
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

    public Node pull() {

        if(!isEmpty()) {
            QueueElement elem = head;
            head = elem.next;
            return elem.node;
        }

        return null;

        //throw new NullPointerException("Kolejka jest pusta");
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
