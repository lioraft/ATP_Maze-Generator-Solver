package IO;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.ArrayList;

public class MyDecompressorInputStream extends InputStream {
    InputStream in; // input stream object

    // constructor
    public MyDecompressorInputStream(InputStream in) {
        this.in = in;
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        // read input stream into byte array
        int bytes = in.read(b);
        // create temporary bytes list that will be used to convert the maze from hexadecimal to binary
        ArrayList<Byte> tempBytesList = new ArrayList<>();
        // get number of bytes for rows
        int bytesForRows = b[0] + 1;
        // add the rows information
        for (int i = 0; i < bytesForRows; i++) {
            tempBytesList.add(b[i]);
        }
        // get number of columns bytes
        int bytesForColumns = b[bytesForRows] + 1;
        // add the columns information
        for (int i = bytesForRows; i < bytesForRows + bytesForColumns; i++) {
            tempBytesList.add(b[i]);
        }
        // get the start col bytes
        int bytesForStartCol = b[bytesForColumns+bytesForRows] + 1;
        // add the start col information
        for (int i = bytesForRows + bytesForColumns; i < bytesForRows + bytesForColumns + bytesForStartCol; i++) {
            tempBytesList.add(b[i]);
        }
        // add the end col bytes
        int bytesForEndCol = b[bytesForStartCol+bytesForColumns+bytesForRows] + 1;
        // write the end col information
        for (int i = bytesForRows + bytesForColumns + bytesForStartCol; i < bytesForRows + bytesForColumns + bytesForStartCol + bytesForEndCol; i++) {
            tempBytesList.add(b[i]);
        }
        // finally, get the maze index
        int mazeStartIndex = bytesForEndCol + bytesForStartCol + bytesForColumns + bytesForRows;
        // from this index on, this is the maze. therefore, we are going to convert every hexadecimal to 4 binary bits
        for (int i = mazeStartIndex; i < b.length; i++) {
            if (b[i] >= 48 && b[i] <= 102) {
                // convert the byte to a string
                String hexa = "" + (char) b[i];
                int decimal = Integer.parseInt(hexa, 16);
                String binary = Integer.toBinaryString(decimal);
                // if the string is not 4 bits long, add 0's to the beginning of the string
                while (binary.length() < 4) {
                    binary = "0" + binary;
                }
                for (int k = 0; k < binary.length(); k++) {
                    if (binary.charAt(k) == '0') {
                        tempBytesList.add((byte) 0);
                    } else if (binary.charAt(k) == '1'){
                        tempBytesList.add((byte) 1);
                    }
                }
            }
        }
        byte[] newBytes = new byte[tempBytesList.size()];
        for (int i = 0; i < tempBytesList.size(); i++) {
            newBytes[i] = tempBytesList.get(i);
        }
        // set b as the new bytes array
        System.arraycopy(newBytes, 0, b, 0, Math.min(newBytes.length, b.length)); // copy bytes back into b
        return bytes;
    }
}
