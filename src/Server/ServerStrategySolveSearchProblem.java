package Server;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * The IServerStrategy interface is responsible for the server's strategy.
 * It receives an input stream and an output stream.
 * The strategy will be applied on the input stream and the output stream.
 * TO DO: complete this class
 */
public class ServerStrategySolveSearchProblem implements IServerStrategy {
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        // path to the directory where the temporary files will be saved
        String tempDirectoryPath = System.getProperty("java.io.tmpdir");
    }
}
