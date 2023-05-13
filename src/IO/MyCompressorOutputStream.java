package IO;

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
        // convert int to string
        String binary = Integer.toBinaryString(b);
        // write out the string
        for (int i = 0; i < binary.length(); i++) {
            out.write((byte) binary.charAt(i));
        }
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
        int bytesForStartCol = b[bytesForColumns] + 1;
        // write the start col information
        for (int i = bytesForRows + bytesForColumns; i < bytesForRows + bytesForColumns + bytesForStartCol; i++) {
            out.write(b[i]);
        }
        // get the end col bytes
        int bytesForEndCol = b[bytesForStartCol] + 1;
        // write the end col information
        for (int i = bytesForRows + bytesForColumns + bytesForStartCol; i < bytesForRows + bytesForColumns + bytesForStartCol + bytesForEndCol; i++) {
            out.write(b[i]);
        }
        // finally, get the maze index
        int mazeStartIndex = bytesForEndCol + bytesForStartCol + bytesForColumns + bytesForRows;
        // from this index on, this is the maze. therefore, we are going to convert every 4 bits to hexadecimal
        for (int i = mazeStartIndex; i < b.length; i=i+4) {
            // convert 4 bits to hexadecimal
            StringBuilder hex = new StringBuilder();
            for (int j = 0; j < 4; j++) {
                hex.append(b[i + j]);
            }
            // convert to decimal
            int decimal = Integer.parseInt(hex.toString(), 2);
            // convert decimal to hexadecimal
            String hexNumber = Integer.toHexString(decimal);
            // convert to byte array
            for (int j = 0; j < hexNumber.length(); j++) {
                // convert to byte
                byte byteToWrite = (byte) hexNumber.charAt(j);
                // write the byte
                out.write(byteToWrite);
            }
        }
    }
}
