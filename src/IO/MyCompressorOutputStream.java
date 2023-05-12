package IO;

import java.io.OutputStream;

public class MyCompressorOutputStream extends OutputStream {
    OutputStream out;

    public MyCompressorOutputStream(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(int b) {
        // TODO Auto-generated method stub

    }

    @Override
    public void write(byte[] b) {
        // TODO Auto-generated method stub

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
