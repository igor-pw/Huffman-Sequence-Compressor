package pl.edu.pw.ee.aisd2025zex5;

public class PriorityQueue
{

    private final Node [] elems;
    private int size;


    public PriorityQueue(HashMap hash_map, byte [][] keys)
    {
        elems = new Node[hash_map.getNumEntries()];
        size = hash_map.getNumEntries();
        createPriorityQueue(hash_map, keys);
    }

    private void createPriorityQueue(HashMap hash_map, byte [][] keys)
    {
        for(int i = 0; i < size; i++) {
            elems[i] = new Node(hash_map.getCounter(keys[i]), keys[i]);
        }

        buildHeap();
    }

    public void buildHeap() {
        for(int i = size/2-1; i >= 0; i--) {
            heapDown(i);
        }
    }

    public void heapDown(int index) {
        int smallest = index;
        int left = 2 * index + 1;
        int right = 2 * index + 2;

        if(left < size && elems[left].compareTo(elems[smallest]) > 0) {
            smallest = left;
        }

        if(right < size && elems[right].compareTo(elems[smallest]) > 0) {
            smallest = right;
        }

        if(smallest != index) {
            swap(index, smallest);
            heapDown(smallest);
        }
    }

    public void heapUp(int index) {
        while (index > 0) {
            int parent = (index - 1) / 2;

            if (elems[index].compareTo(elems[parent]) > 0) {
                swap(index, parent);
                index = parent;
            } else {
                break;
            }
        }
    }

    public void add(Node node)
    {
        elems[size++] = node;
        int current = size - 1;

        heapUp(current);
    }

    public Node pull()
    {
        if(size == 0) {
            return null;
        }

        Node root = elems[0];
        elems[0] = elems[size - 1];
        size--;
        heapDown(0);

        return root;
    }

    public void swap(int i, int j) {
        Node temp = elems[i];
        elems[i] = elems[j];
        elems[j] = temp;
    }

    public boolean isEmpty() {
        return size == 0;
    }

}
