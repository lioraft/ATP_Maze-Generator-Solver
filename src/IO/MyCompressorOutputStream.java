package IO;

import algorithms.mazeGenerators.Maze;
import javafx.util.Pair;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class MyCompressorOutputStream extends OutputStream {
    OutputStream out;

    public MyCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    // function that takes in a maze represented in bytes, and converts it to arraylist filled with arraylists of frequencies of zeros and ones
    public HashMap<Integer, ArrayList<Integer>> frequencyArray(byte[] mazeInBytes) {
        // indicator of looking for 0 or 1
        boolean isLookingForZero = true;
        // variable for counting the number of 0s or 1s
        int curCount = 0;
        // arraylist of array lists of frequencies of 0s and 1s
        HashMap<Integer, ArrayList<Integer>> freqArrays = new HashMap<Integer, ArrayList<Integer>>();
        freqArrays.put(0, new ArrayList<Integer>());
        freqArrays.put(1, new ArrayList<Integer>());
        // calculate maze start index
        int bytesForRows = mazeInBytes[0] + 1;
        int bytesForColumns = mazeInBytes[bytesForRows] + 1;
        int bytesForStartCol = mazeInBytes[bytesForColumns] + 1;
        int bytesForEndCol = mazeInBytes[bytesForStartCol] + 1;
        int mazeStartIndex = bytesForEndCol + 1;
        for (int i = mazeStartIndex; i < mazeInBytes.length; i++) {
                if (mazeInBytes[i] == 0) {
                    // if looking for zero, increment the counter
                    if (isLookingForZero) {
                        curCount++;
                    } else {
                        // if found 0 and was looking for 1, add the count of 1 to the arraylist, reset counter for 0 and change indicator
                        freqArrays.get(1).add(curCount);
                        isLookingForZero = true;
                        curCount = 1;
                    }
                } else {
                    // if found 1 and was looking for 0, add the count of 0 to the arraylist, reset counter for 1 and change indicator
                    if (isLookingForZero) {
                        freqArrays.get(0).add(curCount);
                        isLookingForZero = false;
                        curCount = 1;
                    } else {
                        // if looking for 1, increment the counter
                        curCount++;
                    }
                }
        }
        return freqArrays;
    }


    // function that takes an integer and converts it to a byte array
    @Override
    public void write(int b) {
        // TODO Auto-generated method stub

    }

    @Override
    public void write(byte[] b) {
        // TODO Auto-generated method stub

    }

    @Override
    public void write(byte[] b, int off, int len) {
        // TODO Auto-generated method stub

    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }
}
