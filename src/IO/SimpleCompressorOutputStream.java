package IO;

import java.io.IOException;
import java.io.OutputStream;

public class SimpleCompressorOutputStream extends OutputStream {
    private OutputStream out; // the underlying output stream
    private int currentByte; // the current byte being processed
    private int count; // the number of occurrences of the current byte



    /**
     * @param out
     * The SimpleCompressorOutputStream class extends the OutputStream class.
     * It is used to compress a maze into a byte array.
     * The compression is done by counting the number of occurrences of each byte value in a row.
     * The compressed maze is written to the underlying output stream.
     */

    public SimpleCompressorOutputStream(OutputStream out) {
        this.out = out; // the underlying output stream
        this.currentByte = 0; // the current byte being processed
        this.count = 0; // the number of occurrences of the current byte
    }

    /**
     *
     * The write method is overridden from the OutputStream class. It writes a single byte to the output stream.
     * If the byte b is different from the current byte, it means a new byte is encountered.
     * In that case, it writes the count of the previous byte occurrences to the output stream, sets the current byte to b, and resets the count to 1.
     * If the byte b is the same as the current byte, it increments the count. If the count reaches 255 (the maximum value for a byte),
     * it writes the count to the output stream and resets it to 0.
     */
    public void write(int b) throws IOException {
        try {
            if (b != currentByte) { // if the byte value is different from the current byte
                out.write((byte) count); // write the number of occurrences of the current byte
                currentByte = b; // update the current byte
                count = 1; // reset the number of occurrences of the current byte
            } else {
                count++; // increment the number of occurrences of the current byte
                if (count == 255) { // if the number of occurrences of the current byte is 255
                    out.write((byte) count); // write the number of occurrences of the current byte
                    count = 0; // reset the number of occurrences of the current byte
                }
            }
        } catch (IOException e) {}
    }

    public void write(byte [] b) { // write the compressed maze to the underlying output stream
        try {
            for (int i = 0; i < 10; i++) { // write the first 10 bytes of the maze
                out.write(b[i]); // write the byte
            }
            for(int i = 10; i < b.length; i++){ // write the rest of the maze
                write(b[i]); // write the byte
            }
            if(count !=0){ // if the number of occurrences of the current byte is not 0
                out.write(count); // write the number of occurrences of the current byte
            }
        } catch (IOException e) {}
    }
}

/**
 * By processing one byte at a time, the method (write(int b)) can keep track of the current byte being processed and count the number of occurrences of that byte in a row.
 * This allows for efficient compression by storing the count of consecutive occurrences instead of repeating the same byte multiple times.
 * The write(byte[] b) method is responsible for writing the compressed maze to the output stream. It iterates over the byte array representing the maze and calls the
 * write(int b) method for each byte. This ensures that the compression logic is applied to each byte in the maze individually, resulting in an optimized compressed representation.
 */