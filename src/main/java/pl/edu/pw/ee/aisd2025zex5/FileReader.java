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

    public void printFile() {

        char c;

        for(int i = 0; i < 256; i++) {
            if(counters[i] != 0) {
                c = (char)i;
                System.out.println(c + ": " + counters[i]);
            }
        }
    }

    public static void main(String [] args) {
        FileReader fr = new FileReader();

        fr.countCodes("C:\\Users\\kilux\\OneDrive\\Studia\\Semestr_3\\AiSD\\Laboratoria\\2025Z_AISD_2025Z_GR3_gr24\\CW_5\\AiSD2025ZEx5\\src\\main\\java\\pl\\edu\\pw\\ee\\aisd2025zex5\\test.txt");
        fr.printFile();

        int [] counters = fr.getCounters();
        PriorityQueue pq = new PriorityQueue();

        for(int i = 0; i < counters.length; i++) {
            if(counters[i] != 0) {
                pq.add(new Node(counters[i], (byte)i));
            }
        }

        Dictionary dictionary = new Dictionary();

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

            pq.add(dictionary.createTree(left, right));
        }

        if(!pq.isEmpty()) {
            dictionary.setRoot(pq.pull());
        }

        dictionary.printDictionary();

        ByteWriter bw = new ByteWriter();
        dictionary.compressDictionary(bw);

        try {
            BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream("C:\\Users\\kilux\\OneDrive\\Studia\\Semestr_3\\AiSD\\Laboratoria\\2025Z_AISD_2025Z_GR3_gr24\\CW_5\\AiSD2025ZEx5\\src\\main\\java\\pl\\edu\\pw\\ee\\aisd2025zex5\\output.txt"));
            bw.writeOutput(outputStream);
            outputStream.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

        ByteReader br = new ByteReader("C:\\Users\\kilux\\OneDrive\\Studia\\Semestr_3\\AiSD\\Laboratoria\\2025Z_AISD_2025Z_GR3_gr24\\CW_5\\AiSD2025ZEx5\\src\\main\\java\\pl\\edu\\pw\\ee\\aisd2025zex5\\output.txt");
        br.readNumberOfZeros();
        System.out.println("nrOfZeros: " + br.numberOfZeros);

        Dictionary decompressed = new Dictionary();
        decompressed.decompressDictionary(br);
        decompressed.printDictionary();
    }
}
