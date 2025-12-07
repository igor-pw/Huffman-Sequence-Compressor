package pl.edu.pw.ee.aisd2025zex5;

import com.sun.jdi.VMCannotBeModifiedException;

public class Dictionary
{
    private Node root;

    public void setRoot(Node root) { this.root = root; }

    public Node createTree(Node left, Node right)
    {
        int counter = left.getCounter() + right.getCounter();
        Node newNode = new Node(counter, (byte)0);
        newNode.setLeft(left);
        newNode.setRight(right);
        return newNode;
    }

    public void compressDictionary(ByteWriter bw) {
        if(root != null) {
            compressDictionary(bw, root);
        }
    }

    private void compressDictionary(ByteWriter bw, Node node) {

        int b = node.isLeaf() ? 1 : 0;

        bw.addBit(b);

        if(b == 1) {
            bw.addByte(node.getCode());
        }

        if(node.getLeft() != null) {
            compressDictionary(bw, node.getLeft());
        }

        if(node.getRight() != null) {
            compressDictionary(bw, node.getRight());
        }
    }

    public void decompressDictionary(ByteReader br) {
        root = buildDictionary(br);
        System.out.println(Integer.toBinaryString(br.getBuffer()));
        System.out.println(Integer.toBinaryString(br.nextByte()));
    }

    private Node buildDictionary(ByteReader br) {

        Node newNode = new Node();
        int b = br.nextBit();

        if(b == 1) {
           newNode.setCode(br.nextByte());
        }

        else {
            newNode.setLeft(buildDictionary(br));
            newNode.setRight(buildDictionary(br));
        }

        return newNode;
    }

    public void printDictionary() {
       printDictionary(root);
    }

    private void printDictionary(Node node) {
        if(node.getRight() != null) {
            printDictionary(node.getRight());
        }
        if(node.getLeft() != null) {
            printDictionary(node.getLeft());
        }

        System.out.println("counter: " + node.getCounter() + ", code: " + Integer.toBinaryString(node.getCode()));


    }
}
