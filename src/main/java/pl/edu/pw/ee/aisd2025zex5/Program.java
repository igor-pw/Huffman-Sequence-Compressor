package pl.edu.pw.ee.aisd2025zex5;

public class Program
{
    public static void main(String [] args) {

        FlagReader fg = new FlagReader(args);
        FileReader fr = new FileReader();

        fr.countCodes("src/main/java/pl/edu/pw/ee/aisd2025zex5/test.txt");
        fr.printFile();

        int [] counters = fr.getCounters();

        PriorityQueue pq = new PriorityQueue(counters);
        Dictionary dictionary = new Dictionary(pq);

        dictionary.printDictionary();
    }

}
