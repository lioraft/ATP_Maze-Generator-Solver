package IO;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.PriorityQueue;

public class MyCompressorOutputStream extends OutputStream {
    OutputStream out;

    public MyCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    // function that takes in a maze represented in bytes, and converts it to arraylist filled with arraylists of frequencies of zeros and ones
    public HashMap<Integer, ArrayList<Integer>> mazeInBytesToFrequency(byte[] mazeInBytes, int mazeStartIndex) {
        // indicator of looking for 0 or 1
        boolean isLookingForZero = true;
        // variable for counting the number of 0s or 1s
        int curCount = 0;
        // arraylist of array lists of frequencies of 0s and 1s
        HashMap<Integer, ArrayList<Integer>> freqArrays = new HashMap<Integer, ArrayList<Integer>>();
        freqArrays.put(0, new ArrayList<Integer>()); // 0 sequences
        freqArrays.put(1, new ArrayList<Integer>()); // 1 sequences
        freqArrays.put(2, new ArrayList<Integer>()); // combined arraylist - both 0, 1 sequences, alternating
        if (mazeInBytes[mazeStartIndex] == 1) { // if starts with 1, we want number of 0 to be 0
            freqArrays.get(2).add(0);
        }
        // calculate maze start index
        for (int i = mazeStartIndex; i < mazeInBytes.length; i++) {
                if (mazeInBytes[i] == 0) {
                    // if looking for zero, increment the counter
                    if (isLookingForZero) {
                        if (curCount == 255) { // if reached 255, we need to split
                            freqArrays.get(0).add(curCount);
                            curCount = 1;
                            // add to combined arraylist
                            freqArrays.get(2).add(curCount);
                            freqArrays.get(2).add(0);
                        }
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
                        if (curCount == 255) { // if reached 255, we need to split
                            freqArrays.get(1).add(curCount);
                            curCount = 1;
                            // add to combined arraylist
                            freqArrays.get(2).add(curCount);
                            freqArrays.get(2).add(0);
                        }
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
    public void write(byte[] b) throws IOException {
        // calculate maze start index
        int bytesForRows = b[0] + 1;
        // get number of rows
        byte[] rows = new byte[b[0]];
        for (int i = 1; i < bytesForRows; i++) {
            rows[i - 1] = b[i];
        }
        int bytesForColumns = b[bytesForRows] + 1;
        // get number of columns
        byte[] columns = new byte[b[bytesForRows]];
        for (int i = bytesForRows + 1; i < bytesForRows + bytesForColumns; i++) {
            columns[i - bytesForRows - 1] = b[i];
        }
        int bytesForStartCol = b[bytesForColumns] + 1;
        // get the start col index
        byte[] startCol = new byte[b[bytesForColumns]];
        for (int i = bytesForRows + bytesForColumns + 1; i < bytesForRows + bytesForColumns + bytesForStartCol; i++) {
            startCol[i - bytesForRows - bytesForColumns - 1] = b[i];
        }
        int bytesForEndCol = b[bytesForStartCol] + 1;
        // get the end index
        byte[] endCol = new byte[b[bytesForStartCol]];
        for (int i = bytesForRows + bytesForColumns + bytesForStartCol + 1; i < bytesForRows + bytesForColumns + bytesForStartCol + bytesForEndCol; i++) {
            endCol[i - bytesForRows - bytesForColumns - bytesForStartCol - 1] = b[i];
        }
        int mazeStartIndex = bytesForEndCol + bytesForStartCol + bytesForColumns + bytesForRows;
        // get frequency map of maze in bytes
        HashMap<Integer, ArrayList<Integer>> freqMap = mazeInBytesToFrequency(b, mazeStartIndex);
        // create a priority queue of huffman nodes
        PriorityQueue<HuffmanNode> huffmanNodes = new PriorityQueue<HuffmanNode>();
        // convert all 0 sequences to huffman nodes, and to priority queue
        while (!freqMap.get(0).isEmpty()) {
            // get next element in arraylist
            int zeroSequence = freqMap.get(0).get(0);
            // we know for sure that zero sequence is smaller than 255, so we can cast it to byte
            byte length = (byte)zeroSequence;
            // get frequency of zero sequence in arraylist
            int freq = Collections.frequency(freqMap.get(0), zeroSequence);
            // create a new huffman node
            HuffmanNode curNode = new HuffmanNode(freq, (byte)0, length);
            huffmanNodes.add(curNode);
            freqMap.get(0).removeAll(Collections.singleton(zeroSequence));
        }
        // convert all 1 sequences to huffman nodes, and to priority queue
        while (!freqMap.get(1).isEmpty()) {
            // get next element in arraylist
            int oneSequence = freqMap.get(1).get(0);
            // we know for sure that zero sequence is smaller than 255, so we can cast it to byte
            byte length = (byte)oneSequence;
            // get frequency of zero sequence in arraylist
            int freq = Collections.frequency(freqMap.get(1), oneSequence);
            // create a new huffman node
            HuffmanNode curNode = new HuffmanNode(freq, (byte)1, length);
            huffmanNodes.add(curNode);
            freqMap.get(1).removeAll(Collections.singleton(oneSequence));
        }

        // create a tree root from the priority queue
        while (huffmanNodes.size() > 1) {
            // get the two nodes with the smallest frequencies
            HuffmanNode left = huffmanNodes.poll();
            HuffmanNode right = huffmanNodes.poll();
            // create a new node with the sum of the frequencies of the two nodes
            HuffmanNode newNode = new HuffmanNode(left.getFreqCounter() + right.getFreqCounter(), (byte)'\0', (byte)0);
            // set the left and right children of the new node
            newNode.setLeft(left);
            newNode.setRight(right);
            // add the new node to the priority queue
            huffmanNodes.add(newNode);
        }

        // create a huffman tree from the root of the tree, with all the frequences
        HuffmanTreeMaze huffmanTree = new HuffmanTreeMaze(huffmanNodes.poll(), rows, columns, startCol, endCol);
        // build byte string that represents the maze
        huffmanTree.buildHuffmanCode(freqMap.get(2));
        // write object to output stream
        ObjectOutputStream outstreamObj = new ObjectOutputStream(out);
        outstreamObj.writeObject(huffmanTree);
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
