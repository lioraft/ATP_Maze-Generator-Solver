package IO;

import java.io.IOException;
import java.io.OutputStream;

public class SimpleCompressorOutputStream extends OutputStream {
    private OutputStream out; // the underlying output stream
    private int currentByte; // the current byte being processed
    private int count; // the number of occurrences of the current byte


    /**
     * @param out The SimpleCompressorOutputStream class extends the OutputStream class.
     *            It is used to compress a maze into a byte array.
     *            The compression is done by counting the number of occurrences of each byte value in a row.
     *            The compressed maze is written to the underlying output stream.
     */

    public SimpleCompressorOutputStream(OutputStream out) {
        this.out = out; // the underlying output stream
        this.currentByte = 0; // the current byte being processed
        this.count = 0; // the number of occurrences of the current byte
    }

    // method that writes a single byte to the underlying output stream
    public void write(int b) throws IOException {
        out.write(b);
    }

    public void write(byte[] b) throws IOException { // write the compressed maze to the underlying output stream
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
        int bytesForStartCol = b[bytesForColumns+bytesForRows] + 1;
        // write the start col information
        for (int i = bytesForRows + bytesForColumns; i < bytesForRows + bytesForColumns + bytesForStartCol; i++) {
            out.write(b[i]);
        }
        // get the end col bytes
        int bytesForEndCol = b[bytesForStartCol+bytesForColumns+bytesForRows] + 1;
        // write the end col information
        for (int i = bytesForRows + bytesForColumns + bytesForStartCol; i < bytesForRows + bytesForColumns + bytesForStartCol + bytesForEndCol; i++) {
            out.write(b[i]);
        }
        // finally, get the maze index
        int mazeStartIndex = bytesForEndCol + bytesForStartCol + bytesForColumns + bytesForRows;
        // iterate the maze, count frequencies and write to the output stream
        for (int i = mazeStartIndex; i < b.length; i++) {
            if (b[i] == 0) {
                // if looking for zero, increment the counter
                if (currentByte == 0) {
                    if (count == 255) { // if reached 255, we need to split
                        if (i == b.length - 1) { // if reached the end of the array, add the count of 0 to the arraylist
                            out.write(-128);
                        } else {
                            // if not reached the end of the array, check next digit. if it's another 0, we need to mark 0 1s between them
                            if (b[i + 1] == 0) {
                                if (count > 127) {
                                    out.write(127-count);
                                }
                                else {
                                    out.write(count);
                                }
                                out.write(0);
                                count = 0;
                            } else { // if it's 1, just reset counter
                                if (count > 127) {
                                    out.write(127-count);
                                }
                                else {
                                    out.write(count);
                                }
                                count = 0;
                            }
                        }
                    }
                    else { // if normal count, just increase counter
                        count++;
                    }
                } else {
                    // if found 0 and was looking for 1, write out counter of 1, reset counter for 0 and change indicator
                    if (count > 127) {
                        out.write(127-count);
                    }
                    else {
                        out.write(count);
                    }
                    currentByte = 0;
                    count = 1;
                }
            } else {
                // if looking for 1, increment the counter
                if (currentByte == 1) {
                    if (count == 255) { // if reached 255, we need to split
                        if (i == b.length - 1) { // if reached the end of the array, add the count of 0 to the arraylist
                            out.write(-128);
                        } else {
                            // if not reached the end of the array, check next digit. if it's another 1, we need to mark 0 1s between them
                            if (b[i + 1] == 1) {
                                if (count > 127) {
                                    out.write(127-count);
                                }
                                else {
                                    out.write(count);
                                }
                                out.write(0);
                                count = 0;
                            } else { // if it's 0, just reset counter
                                if (count > 127) {
                                    out.write(127-count);
                                }
                                else {
                                    out.write(count);
                                }
                                count = 0;
                            }
                        }
                    }
                    else { // if normal count, just increase counter
                        count++;
                    }
                } else {
                    // if found 1 and was looking for 0, write out counter of 0, reset counter for 1 and change indicator
                    if (count > 127) {
                        out.write(127-count);
                    }
                    else {
                        out.write(count);
                    }
                    currentByte = 1;
                    count = 1;
                }
            }
        }
        }
}