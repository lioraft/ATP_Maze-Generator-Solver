package IO;

import java.io.IOException;
import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {
    OutputStream out;

    public MyCompressorOutputStream(OutputStream out) {
        this.out = out;
    }


    // function that takes an integer and converts it to a byte array
    @Override
    public void write(int b) {
        // TODO Auto-generated method stub

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
            String hex = "";
            for (int j = 0; j < 4; j++) {
                hex += b[i + j];
            }
            // convert the hexadecimal to a byte
            byte byteToWrite = (byte) Integer.parseInt(hex, 2);
            // write the byte
            out.write(byteToWrite);
        }
    }

    @Override
    public void write(byte[] b, int off, int len) {
        // TODO Auto-generated method stub

    }

    @Override
    public void flush() {
        // TODO Auto-generated method stub

    }

    @Override
    public void close() {
        // TODO Auto-generated method stub

    }
}
