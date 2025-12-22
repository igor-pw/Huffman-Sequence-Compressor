package pl.edu.pw.ee.aisd2025zex5;

import javax.swing.plaf.basic.BasicTextAreaUI;
import java.io.File;
import java.util.ConcurrentModificationException;

public class Dictionary
{
    private Node root;

    public Dictionary(PriorityQueue pq) {
        createDictionary(pq);
    }
    public Dictionary() {}

    public Node getRoot() {
        return root;
    }

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

    public void compressDictionary(ByteWriter byte_writer, int max_length) {
        if(root != null) {
             compressDictionary(byte_writer, root, max_length);
        }
    }

    private void compressDictionary(ByteWriter byte_writer, Node node, int max_length)
    {
        int b = node.isLeaf() ? 1 : 0;

        byte_writer.addBit(b);

        if(b == 1) {
            if(max_length > 1) {
                byte_writer.addLength(node.getCode().length, max_length);
            }
            for(int i = 0; i < node.getCode().length; i++) {
                byte_writer.addByte(node.getCode()[i]);
            }
        }

        if(node.getLeft() != null) {
            compressDictionary(byte_writer, node.getLeft(), max_length);
        }

        if(node.getRight() != null) {
            compressDictionary(byte_writer, node.getRight(), max_length);
        }
    }

    public void decompressFile(ByteReader br, ByteWriter byte_writer, int max_length) {

        root = buildDictionary(br, max_length);

        while(!br.isEnd()) {
            byte [] code = searchDictionary(br);
            if(code != null) {
                byte_writer.addCode(code);
            }
        }
    }

    private Node buildDictionary(ByteReader br, int max_length) {

        Node newNode = new Node();
        int b = br.nextBit();

        if(b == 1) {
            int length;
            if (max_length > 1) {
                length = br.readLength(max_length);
            }
            else {
                length = 1;
            }
            newNode.setCode(br.readCode(length));
        }

        else {
            newNode.setLeft(buildDictionary(br, max_length));
            newNode.setRight(buildDictionary(br, max_length));
        }

        return newNode;
    }

    public byte [] searchDictionary(ByteReader byte_reader)
    {
        Node current = root;
        int bit;

        while(true) {
            if(current.isLeaf()) {
                return current.getCode();
            }

            if(byte_reader.isEnd()) {
                return null;
            }

            bit = byte_reader.nextBit();

            if(bit == 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }

        }
    }
}
