package pl.edu.pw.ee.aisd2025zex5;

import java.util.Arrays;

public abstract class FileParser
{
    protected int max_length;

    protected byte [] createMergedBuffer(byte [] leftOvers, byte [] buffer, int buffer_elem)
    {
        int tail_size = leftOvers.length;
        int head_size = Math.min(max_length-1, buffer_elem);

        byte [] mergedBuffer = new byte[tail_size+head_size];

        System.arraycopy(leftOvers, 0, mergedBuffer, 0, tail_size);
        System.arraycopy(buffer, 0, mergedBuffer, tail_size, head_size);

        return mergedBuffer;
    }

}
