package IO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class SimpleDecompressorInputStream extends InputStream {
    private InputStream in; // the underlying input stream
    int currerntBit; // the current bit
    int count; // the current count

    public SimpleDecompressorInputStream(InputStream in) {
        this.in = in; // the underlying input stream
        currerntBit = 0;
        count = 0;
    }

    /**
     * The read() method is overridden from the InputStream class.
     * Inside the method, it tries to read a single byte from the underlying input stream in using the read() method.
     * If an IOException occurs during the read operation, it catches the exception and returns 0.
     * Otherwise, it returns the byte value read from the input stream.
     */
    @Override
    public int read() throws IOException {
        try {
            return in.read();
        } catch (IOException e) {}
        return 0;
    }

    /**
     The read() method reads bytes from the input stream and decompresses them into the provided byte array b.
     It reads pairs of bytes representing the count and value, replicating the value byte count
     times in the array b. Finally, it alternates the bit value for each pair of bytes to achieve the decompression. The function returns the number of bytes read
     and stored in the array b
     */

    public int read(byte[] b) throws IOException {
        // read input stream into byte array
        int bytes = in.read(b);
        // create temporary bytes list that will be used to convert the maze from hexadecimal to binary
        ArrayList<Byte> tempBytesList = new ArrayList<>();
        // get number of bytes for rows
        int bytesForRows = b[0] + 1;
        // write the rows information
        for (int i = 0; i < bytesForRows; i++) {
            tempBytesList.add(b[i]);
        }
        // get number of columns bytes
        int bytesForColumns = b[bytesForRows] + 1;
        // write the columns information
        for (int i = bytesForRows; i < bytesForRows + bytesForColumns; i++) {
            tempBytesList.add(b[i]);
        }
        // get the start col bytes
        int bytesForStartCol = b[bytesForColumns] + 1;
        // write the start col information
        for (int i = bytesForRows + bytesForColumns; i < bytesForRows + bytesForColumns + bytesForStartCol; i++) {
            tempBytesList.add(b[i]);
        }
        // get the end col bytes
        int bytesForEndCol = b[bytesForStartCol] + 1;
        // write the end col information
        for (int i = bytesForRows + bytesForColumns + bytesForStartCol; i < bytesForRows + bytesForColumns + bytesForStartCol + bytesForEndCol; i++) {
            tempBytesList.add(b[i]);
        }
        // finally, get the maze index
        int mazeStartIndex = bytesForEndCol + bytesForStartCol + bytesForColumns + bytesForRows;


        while (mazeStartIndex < b.length) { // as long as there are elements in the maze
            while (count > 0) { // while the count is positive
                tempBytesList.add((byte) currerntBit); // write the current bit to the array b
                count--; // decrement the count
            }
            mazeStartIndex++; // increment the maze index
            currerntBit = (currerntBit == 0) ? 1 : 0; // switch the current bit between 0 and 1
        }
        // convert the array list to bytes array
        byte[] newBytes = new byte[tempBytesList.size()];
        for (int i = 0; i < tempBytesList.size(); i++) {
            newBytes[i] = tempBytesList.get(i);
        }
        // set b as the new bytes array
        System.arraycopy(newBytes, 0, b, 0, Math.min(newBytes.length, b.length)); // copy bytes back into b
        return bytes;
    }

}
