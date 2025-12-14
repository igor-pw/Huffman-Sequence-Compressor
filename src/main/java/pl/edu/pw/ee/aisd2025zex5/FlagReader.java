package pl.edu.pw.ee.aisd2025zex5;

import pl.edu.pw.ee.aisd2025zex5.exception.InvalidTypeOfFlagException;
import pl.edu.pw.ee.aisd2025zex5.exception.InvalidValueOfFlagException;

import static pl.edu.pw.ee.aisd2025zex5.MODE.*;

public class FlagReader
{
    private final String [] args;
    private MODE mode = COMP;
    private String input_path = null;
    private String output_path = null;
    private int sequence_length = 1;

    public FlagReader(String [] args) {
        this.args = args;
        readFlags();
    }

    public MODE getMode() { return mode; }
    public String getInputPath() { return input_path; }
    public String getOutputPath() { return output_path; }
    public int getSequenceLength() { return sequence_length; }

    public void readFlags()
    {
        for(int i = 0; i < args.length; i++) {
            if(args[i].startsWith("-") && i < args.length-1) {
                if(!args[i+1].startsWith("-")) {
                    readFlag(args[i].toCharArray()[1], args[++i]);
                    System.out.println(args[i]);
                }
            }
        }
    }

    private void readFlag(char typeOfFlag, String valueOfFlag)
    {
            switch(typeOfFlag) {
                case 'm' -> setMode(typeOfFlag, valueOfFlag);
                case 's' -> input_path = valueOfFlag;
                case 'd' -> output_path = valueOfFlag;
                case 'l' -> {
                    try {
                        sequence_length = Integer.parseInt(valueOfFlag);
                    } catch (NumberFormatException e) {
                        throw new InvalidValueOfFlagException(typeOfFlag, valueOfFlag);
                    }

                }
                default -> throw new InvalidTypeOfFlagException(typeOfFlag);
            }
    }

    private void setMode(char typeOfFlag, String valueOfFlag) {
        if(valueOfFlag.equals("decomp")) {
            mode = DECOMP;
            return;
        }
        if(!valueOfFlag.equals("comp")) {
            throw new InvalidValueOfFlagException(typeOfFlag, valueOfFlag);
        }

    }
}
