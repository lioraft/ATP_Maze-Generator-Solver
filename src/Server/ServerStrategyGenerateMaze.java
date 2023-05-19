package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;

import java.io.*;


/**
 * The IServerStrategy interface is responsible for the server's strategy.
 * It receives an input stream and an output stream.
 * The strategy will be applied on the input stream and the output stream.
 * It gets an array of integers from client, representing maze dimensions,
 * and returns a compressed maze as output stream.
 */
public class ServerStrategyGenerateMaze implements IServerStrategy {

    // applyStrategy method
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            // create input and output stream of objects
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            // get the array of integers from client (for maze dimensions)
            int[] dim = (int[]) fromClient.readObject();
            // retrieve algorithm from properties
            String generator = Configurations.getInstance().getMazeGeneratingAlgorithm();
            // create maze generator according to the algorithm
            AMazeGenerator mazeGenerator;
            if (generator.equals("EmptyMazeGenerator")) {
                mazeGenerator = new EmptyMazeGenerator();
            }
            else if (generator.equals("SimpleMazeGenerator")) {
                mazeGenerator = new SimpleMazeGenerator();
            }
            else if (generator.equals("MyMazeGenerator")) {
                mazeGenerator = new MyMazeGenerator();
            }
            else {
                throw new RuntimeException("Invalid generator name");
            }
            // generate the maze
            Maze maze = mazeGenerator.generate(dim[0], dim[1]);
            // write to the client the compressed maze
            OutputStream compressor = new MyCompressorOutputStream(outToClient);
            byte[] mazeBytes = maze.toByteArray();
            compressor.write(mazeBytes);
            toClient.flush();
            fromClient.close();
            toClient.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
