package pl.edu.pw.ee.aisd2025zex5;

public class Code
{
    byte [] huffman_code;
    int elem = 0;
    int counter;

    public Code() {}

    public Code(int path_length, byte [] code)
    {
        huffman_code = code;
        counter = path_length%8;
        elem = counter == 0 ? path_length/8 : path_length/8+1;

        moveLastByte();
    }

    public int getElem() { return elem; }
    public int getCounter() { return counter; }
    public byte [] getHuffmanCode() { return huffman_code; }

    private void moveLastByte() {
        if(counter > 0) {
            huffman_code[elem - 1] = (byte) (huffman_code[elem - 1] << (8 - counter));
        }
    }

}
