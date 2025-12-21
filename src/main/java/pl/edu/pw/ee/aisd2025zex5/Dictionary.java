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

    public void decompressDictionary(ByteReader br, ByteWriter byte_writer, int max_length) {

        root = buildDictionary(br, max_length);
        //printDictionary();

        while(!br.isEnd()) {
            byte [] code = searchDictionary(br);
            byte_writer.addCode(code);
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

    /*private Node buildDictionary(ByteReader br, int max_length) {
        int firstBit = br.nextBit();
        if (firstBit == 1) {
            return createLeafNode(br, max_length);
        }

        Node root = new Node();
        Node[] nodeArray = new Node[10000000]; // 100k to i tak aż nadto
        int top = 0;
        nodeArray[top] = root;

        while (top >= 0) {
            if (top >= nodeArray.length - 1) {
                throw new RuntimeException("Drzewo za głębokie! Prawdopodobnie błąd odczytu bitów (nieskończone zera).");
            }

            Node current = nodeArray[top];

            // 1. Najpierw budujemy lewe poddrzewo
            if (current.getLeft() == null) {
                int b = br.nextBit();
                if (b == 1) {
                    current.setLeft(createLeafNode(br, max_length));
                } else {
                    Node child = new Node();
                    current.setLeft(child);
                    nodeArray[++top] = child;
                    continue; // Idź budować lewe dziecko nowego węzła
                }
            }

            // 2. Potem budujemy prawe poddrzewo
            if (current.getRight() == null) {
                int b = br.nextBit();
                if (b == 1) {
                    current.setRight(createLeafNode(br, max_length));
                    top--; // Ten węzeł jest pełny, wracamy do rodzica
                } else {
                    Node child = new Node();
                    current.setRight(child);
                    nodeArray[++top] = child;
                    continue; // Idź budować lewe dziecko nowego prawego węzła
                }
            } else {
                // Jeśli jakimś cudem tu trafiliśmy, a węzeł jest pełny
                top--;
            }
        }
        return root;
    }*/

    private Node createLeafNode(ByteReader br, int max_length) {
        Node leaf = new Node();
        int length = (max_length > 1) ? br.readLength(max_length) : 1;
        leaf.setCode(br.readCode(length));
        return leaf;
    }

    public void printDictionary() {
        if(root != null) {
            printDictionary(root);
        }
    }

    public byte [] searchDictionary(ByteReader byte_reader)
    {
        Node current = root;
        int bit;

        while(true) {
            if(current.isLeaf()) {
                return current.getCode();
            }

            bit = byte_reader.nextBit();

            if(bit == 0) {
                current = current.getLeft();
            } else {
                current = current.getRight();
            }

        }
    }

    private void printDictionary(Node node) {
        if(node.getLeft() != null) {
            printDictionary(node.getLeft());
        }
        if(node.getRight() != null) {
            printDictionary(node.getRight());
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
