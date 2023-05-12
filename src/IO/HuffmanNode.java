package IO;

import java.io.Serializable;

// a serializable class that represents a huffman node
public class HuffmanNode implements Serializable {
    byte freqCounter; // the frequency in
    byte flag; // represents if it's a sequence of 0 and 1
    byte length; // the length of the sequence
    HuffmanNode left;
    HuffmanNode right;

    public HuffmanNode(int freqCounter, byte flag, byte length) {
        this.freqCounter = (byte)freqCounter;
        this.flag = flag;
        this.length = length;
        this.left = null;
        this.right = null;
    }

    // getter of flag
    public byte getFlag() {
        return flag;
    }

    // getter of length
    public byte getLength() {
        return length;
    }

    // getter of frequency counter
    public byte getFreqCounter() {
        return freqCounter;
    }

    // setter of frequency counter
    public void setFreqCounter(byte freqCounter) {
        this.freqCounter = freqCounter;
    }

    // setter of flag
    public void setFlag(byte flag) {
        this.flag = flag;
    }

    // setter of length
    public void setLength(byte length) {
        this.length = length;
    }

    // getter of left node
    public HuffmanNode getLeft() {
        return left;
    }

    // getter of right node
    public HuffmanNode getRight() {
        return right;
    }

    // setter of left node
    public void setLeft(HuffmanNode left) {
        this.left = left;
    }

    // setter of right node
    public void setRight(HuffmanNode right) {
        this.right = right;
    }
}
