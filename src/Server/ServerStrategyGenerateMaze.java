package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.MyMazeGenerator;

import java.io.InputStream;
import java.io.OutputStream;


/**
 * The IServerStrategy interface is responsible for the server's strategy.
 * It receives an input stream and an output stream.
 * The strategy will be applied on the input stream and the output stream.
 * It gets an array of integers from client, representing maze dimensions,
 * and returns a maze as output stream.
 */
public class ServerStrategyGenerateMaze implements IServerStrategy {
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        // try to read the maze dimensions from the client
        byte[] mazeDimensions = new byte[8]; // 8 bytes for 2 integers
        try {
            inFromClient.read(mazeDimensions);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // convert the maze dimensions from byte array to int array
        int[] intArray = new int[mazeDimensions.length];
        for (int i = 0; i < mazeDimensions.length; i++) {
            intArray[i] = mazeDimensions[i] & 0xFF;
        }
        // generate a maze with the given dimensions
        MyMazeGenerator myMazeGenerator = new MyMazeGenerator();
        Maze maze = myMazeGenerator.generate(intArray[0], intArray[1]);
    }
}
