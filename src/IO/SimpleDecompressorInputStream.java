package IO;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        int i = 0; // the index of the array b
        while ((count = read()) != -1) { // read the count of the current byte
            while (count > 0) { // while the count is positive
                b[i] = (byte) currerntBit; // write the current bit to the array b
                count--; // decrement the count
                i++;
            }
            currerntBit = (currerntBit == 0) ? 1 : 0; // switch the current bit between 0 and 1
        }
        return i;
    }

}
