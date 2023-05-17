package IO;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {
    OutputStream out; // output stream object

    // constructor
    public MyCompressorOutputStream(OutputStream out) {
        this.out = out;
    }


    // function that takes an integer and writes it to the output stream as a binary number
    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }

    // the compression method we chose is hexadecimal conversion - we convert 4 bits to a hexadecimal number,
    // which will result in less bytes than the original maze.
    @Override
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
        // from this index on, this is the maze. therefore, we are going to convert every 4 bits to hexadecimal
        for (int i = mazeStartIndex; i < b.length; i=i+4) {
            // convert 4 bits to binary string
            StringBuilder binary = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                if (i + j < b.length)
                    binary.append(b[i + j]);
                else
                    binary.append(0);
            }
            // convert to decimal
            int decimal = Integer.parseInt(binary.toString(), 2);
            // convert to hexadecimal
            String hex = Integer.toHexString(decimal);
            // write to output stream
            for (int j = 0; j < hex.length(); j++) {
                byte curByte = (byte) hex.charAt(j);
                out.write(curByte);
            }
        }
    }
}
