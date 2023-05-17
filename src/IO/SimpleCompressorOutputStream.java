package IO;

import java.io.IOException;
import java.io.OutputStream;

/**
 * SimpleCompressorOutputStream class extends the OutputStream class.
 *  It is used to compress a maze into a byte array.
 *  The compression is done by counting the number of occurrences of each byte value in a row.
 *  The compressed maze is written to the underlying output stream.
 */

public class SimpleCompressorOutputStream extends OutputStream {
    private OutputStream out; // the underlying output stream
    private int currentByte; // the current byte being processed
    private int count; // the number of occurrences of the current byte


    public SimpleCompressorOutputStream(OutputStream out) {
        this.out = out; // the underlying output stream
        this.currentByte = 0; // the current byte being processed
        this.count = 0; // the number of occurrences of the current byte
    }

    // method that writes a single byte to the underlying output stream
    public void write(int b) throws IOException {
        out.write(b);
    }

    // function that write the compressed maze to the underlying output stream
    public void write(byte[] b) throws IOException {
        // get number of bytes for rows
        int bytesForRows = b[0] + 1;
        // write the rows information
        for (int i = 0; i < bytesForRows; i++) {
            out.write(b[i]);
        }
        // get number of columns bytes
        int bytesForColumns = b[bytesForRows] + 1;
        // write the columns information
        for (int i = bytesForRows; i < bytesForRows + bytesForColumns; i++) {
            out.write(b[i]);
        }
        // get the start col bytes
        int bytesForStartCol = b[bytesForColumns + bytesForRows] + 1;
        // write the start col information
        for (int i = bytesForRows + bytesForColumns; i < bytesForRows + bytesForColumns + bytesForStartCol; i++) {
            out.write(b[i]);
        }
        // get the end col bytes
        int bytesForEndCol = b[bytesForStartCol + bytesForColumns + bytesForRows] + 1;
        // write the end col information
        for (int i = bytesForRows + bytesForColumns + bytesForStartCol; i < bytesForRows + bytesForColumns + bytesForStartCol + bytesForEndCol; i++) {
            out.write(b[i]);
        }
        // finally, get the maze index
        int mazeStartIndex = bytesForEndCol + bytesForStartCol + bytesForColumns + bytesForRows;
        // iterate the maze, count frequencies and write to the output stream
        for (int i = mazeStartIndex; i < b.length; i++) {
            // if found zero
            if (b[i] == 0) {
                // if looking for zero, increment the counter
                if (currentByte == 0) {
                    count++;
                } else {
                    // if was not looking for zero and found zero
                    // check if count of 1s is above 255
                    while (count > 255) {
                        out.write((byte) -128); // write -128 to the output stream
                        count = count - 255;
                        if (count > 0)
                            out.write((byte) 0); // if count still bigger than 0, mark there is zero ones between appearances of 1
                    }
                    // write remainder
                    if (count > 127) { // if bigger than 127, convert to negative
                        out.write((byte) 127 - count);
                    } else
                        out.write((byte) count); // if smaller than 127, just print the count
                    count = 1; // reset the counter
                    currentByte = 0; // set the current byte to 0
                }
            }
            else { // if found 1
                // if looking for 1, increment the counter
                if (currentByte == 1) {
                    count++;
                } else {
                    // if was not looking for 1 and found 1
                    // check if count of 0s is above 255
                    while (count > 255) {
                        out.write((byte) -128); // write -128 to the output stream
                        count = count - 255;
                        if (count > 0)
                            out.write((byte) 0); // if count still bigger than 0, mark there is zero ones between appearances of 0
                    }
                    // write remainder
                    if (count > 127) { // if bigger than 127, convert to negative
                        out.write((byte) 127 - count);
                    } else
                        out.write((byte) count); // if smaller than 127, just print the count
                    count = 1; // reset the counter
                    currentByte = 1; // set the current byte to 1
                }
            }
        }
    }
}