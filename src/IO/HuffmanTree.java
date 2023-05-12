package IO;

import java.io.Serializable;

// a serializable class that represents a huffman tree
public class HuffmanTree implements Serializable {
    HuffmanNode root; // the root of the tree
    byte[] huffmanCode; // the huffman code that should be looking for while traversing the tree

    public HuffmanTree(HuffmanNode root, byte[] huffmanCode) {
        this.root = root;
        this.huffmanCode = huffmanCode;
    }

    // getter of root
    public HuffmanNode getRoot() {
        return root;
    }

    // setter of root
    public void setRoot(HuffmanNode root) {
        this.root = root;
    }
}
