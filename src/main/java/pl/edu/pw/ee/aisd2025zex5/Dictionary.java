package pl.edu.pw.ee.aisd2025zex5;

import java.io.File;

public class Dictionary
{
    private Node root;

    public Dictionary(PriorityQueue pq) {
        createDictionary(pq);
    }

    public Node getRoot() { return root; }

    public Node createTree(Node left, Node right)
    {
        int counter = left.getCounter() + right.getCounter();
        Node newNode = new Node(counter, null);
        newNode.setLeft(left);
        newNode.setRight(right);
        return newNode;
    }

    public void createDictionary(PriorityQueue pq)
    {
        Node left;
        Node right;

        while(true) {

            if((left = pq.pull()) == null) {
                break;
            }

            if((right = pq.pull()) == null) {
                pq.add(left);
                break;
            }

            pq.add(createTree(left, right));
        }

        if(!pq.isEmpty()) {
            root = pq.pull();
        }
    }

    public void createHuffmanCode(HashMap hash_map, ByteWriter buffer) {
        createHuffmanCode(root, hash_map, buffer, 0);
    }

   private void createHuffmanCode(Node node, HashMap hash_map, ByteWriter buffer, int path_length)
    {
        if(node.isLeaf()) {
            Code huffman_code = new Code(path_length, buffer.getWrittenBytes());
            hash_map.putCode(node.getCode(), huffman_code);
            return;
        }

        buffer.addBit(0);
        createHuffmanCode(node.getLeft(), hash_map, buffer, path_length + 1);
        buffer.removeLSB();
        buffer.addBit(1);
        createHuffmanCode(node.getRight(), hash_map, buffer , path_length + 1);
        buffer.removeLSB();
    }

    public void compressDictionary(ByteWriter byte_writer) {
        if(root != null) {
             compressDictionary(byte_writer, root);
        }
    }

    private void compressDictionary(ByteWriter byte_writer, Node node)
    {
        int b = node.isLeaf() ? 1 : 0;

        byte_writer.addBit(b);

        if(b == 1) {
            for(int i = 0; i < node.getCode().length; i++) {
                byte_writer.addByte(node.getCode()[i]);
            }
        }

        if(node.getLeft() != null) {
            compressDictionary(byte_writer, node.getLeft());
        }

        if(node.getRight() != null) {
            compressDictionary(byte_writer, node.getRight());
        }
    }

    public void decompressDictionary(ByteReader br, int l) {
        //root = buildDictionary(br, l);
        //System.out.println(Integer.toBinaryString(br.getBuffer()));
        //System.out.println(Integer.toBinaryString(br.nextByte()));
    }

    private Node buildDictionary(ByteReader br, int l) {

        Node newNode = new Node();
        int b = br.nextBit();

        if(b == 1) {
           newNode.setCode(br.readCode(l));
        }

        else {
            newNode.setLeft(buildDictionary(br, l));
            newNode.setRight(buildDictionary(br, l));
        }

        return newNode;
    }

    public void printDictionary() {
        if(root != null) {
            printDictionary(root);
        }
    }

    private void printDictionary(Node node) {
        if(node.getRight() != null) {
            printDictionary(node.getRight());
        }
        if(node.getLeft() != null) {
            printDictionary(node.getLeft());
        }

        System.out.print("counter: " + node.getCounter() + ", code: ");

        if(node.getCode() != null) {
            for (int i = 0; i < node.getCode().length; i++) {
                System.out.print(Integer.toBinaryString(node.getCode()[i]));
            }
        }
        else {
            System.out.print("0");
        }

        System.out.println();

    }
}
