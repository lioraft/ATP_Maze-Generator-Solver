package IO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

// a serializable class that represents a huffman tree
public class HuffmanTreeMaze implements Serializable {
    HuffmanNode root; // the root of the tree
    byte[] huffmanCode; // the huffman code that should be looking for while traversing the tree

    byte[] numOfColumns; // number of columns of the maze

    byte[] numOfRows; // number of rows of the maze

    byte[] endCol; // end column of the maze

    byte[] startCol; // start column of the maze

    HashMap<byte[], Byte[]> treeRulesMap; // a map that matches each leaf to the corresponding huffman code

    // initialize a tree representing the maze
    public HuffmanTreeMaze(HuffmanNode root, byte[] numOfRows, byte[] numOfColumns, byte[] startCol, byte[] endCol) {
        this.root = root;
        this.huffmanCode = null;
        this.numOfColumns = numOfColumns;
        this.numOfRows = numOfRows;
        this.endCol = endCol;
        this.startCol = startCol;
        treeRulesMap = new HashMap<byte[], Byte[]>();
    }

    // getter of root
    public HuffmanNode getRoot() {
        return root;
    }

    // setter of root
    public void setRoot(HuffmanNode root) {
        this.root = root;
    }

    public void traverse(HuffmanNode root, ArrayList<Byte> path) {
        // if empty, return
        if (root == null) {
            return;
        }
        // if reached leaf, add to map
        if (root.isLeaf()) {
            byte[] currentNode = new byte[2];
            currentNode[0] = root.getFlag();
            currentNode[1] = root.getLength();
            Byte[] bytePath = path.toArray(new Byte[path.size()]);
            treeRulesMap.put(currentNode, bytePath);

        } else {
            path.add((byte)1);
            traverse(root.left, path);
            path.remove(path.size() - 1);
            path.add((byte)0);
            traverse(root.right, path);
        }
    }

    // builder of huffman code
    public void buildHuffmanCode(ArrayList<Integer> freqMaze) {
        ArrayList<Byte> huffCode = new ArrayList<Byte>();
        // get current node
        HuffmanNode currentNode = this.root;
        // make tree rules map
        traverse(currentNode, huffCode);
        // create the bytes arraylist
        for (Integer currentFreq : freqMaze) {
                byte length = (byte)(int)currentFreq;
                byte flag = currentNode.getFlag();
                Byte[] currentCode = treeRulesMap.get(new byte[]{flag, length});
                huffCode.addAll(Arrays.asList(currentCode));
            }

        // convert to byte[] and set in the field
        this.huffmanCode = new byte[huffCode.size()];
        for (int i = 0; i < huffCode.size(); i++) {
            this.huffmanCode[i] = huffCode.get(i);
        }
        }

}
