package IO;

import java.io.Serializable;

// a serializable class that represents a huffman node
public class HuffmanNode implements Serializable, Comparable<HuffmanNode> {
    int freqCounter; // the frequency counter of the node
    byte flag; // represents if it's a sequence of 0 and 1. if it's a combined node, will be marked as '\0'
    byte length; // the length of the sequence
    HuffmanNode left;
    HuffmanNode right;

    public HuffmanNode(int freqCounter, byte flag, byte length) {
        this.freqCounter = freqCounter;
        this.flag = flag;
        this.length = length;
        this.left = null;
        this.right = null;
    }

    // compare two nodes by their frequency counter.
    // if the frequency counter of this node is bigger than the other node, returns a positive number.
    // if the frequency counter of this node is smaller than the other node, returns a negative number.
    @Override
    public int compareTo(HuffmanNode other) {
        return this.freqCounter - other.freqCounter;
    }

    // function that returns if the node is leaf
    public boolean isLeaf() {
        if (this.left == null && this.right == null) {
            return true;
        }
        return false;
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
    public int getFreqCounter() {
        return freqCounter;
    }

    // setter of frequency counter
    public void setFreqCounter(int freqCounter) {
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
