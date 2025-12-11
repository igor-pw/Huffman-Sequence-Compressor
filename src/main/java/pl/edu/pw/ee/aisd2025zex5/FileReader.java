package pl.edu.pw.ee.aisd2025zex5;

import java.io.*;
import java.security.cert.TrustAnchor;
import java.util.Arrays;

public class FileReader
{
    private final int[] counters;

    public FileReader() {
        counters = new int[256];
    }

    public int[] getCounters() {
        return Arrays.copyOf(counters, counters.length);
    }

    public void countCodes(String file_path)
    {
        try {
            InputStream input = new BufferedInputStream(new FileInputStream(file_path));
            int index = input.read();
            counters[index & 0xFF]++;

            while((index = input.read()) != -1) {
                counters[index & 0xFF]++;
            }

            input.close();

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public void readFile(String file_path, ByteWriter bw)
    {
        int b;
        InputStream input;
        try {
            input = new BufferedInputStream(new FileInputStream(file_path));
            while ((b = input.read()) != -1) {
                //bw.compressByte();
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    public void printFile() {

        char c;

        for(int i = 0; i < 256; i++) {
            if(counters[i] != 0) {
                c = (char)i;
                System.out.println(c + ": " + counters[i]);
            }
        }
    }

    /*public static void main(String [] args)
    {
        ByteWriter bw = new ByteWriter();
        dictionary.compressDictionary(bw);

        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("src/main/java/pl/edu/pw/ee/aisd2025zex5/output.txt"));
            bw.writeTree(outputStream);

            //outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        ByteReader br = new ByteReader("src/main/java/pl/edu/pw/ee/aisd2025zex5/output.txt");

        //Dictionary decompressed = new Dictionary();
        //decompressed.decompressDictionary(br);
        //decompressed.printDictionary();
    }*/
}
