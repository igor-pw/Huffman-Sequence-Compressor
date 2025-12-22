package pl.edu.pw.ee.aisd2025zex5;


public class Node implements Comparable<Node>{

    byte [] code;
    private int counter = 0;
    private Node left;
    private Node right;

    public Node() {
        left = null;
        right = null;
    }

    public Node(int counter, byte [] code) {
        this.code = code;
        this.counter = counter;
        left = null;
        right = null;
    }

    public void setLeft(Node left) { this.left = left; }

    public void setRight(Node right) { this.right = right; }

    public Node getRight() { return right; }

    public Node getLeft() { return left; }

    public byte [] getCode() { return code; }

    public int getCounter() { return counter; }

    public void setCode(byte [] code) { this.code = code; }

    public boolean isLeaf() {
        return left == null && right == null;
    }

    @Override
    public int compareTo(Node node) {
        return -(counter - node.getCounter());
    }
}
