package Server;

import IO.MyCompressorOutputStream;
import algorithms.mazeGenerators.*;

import java.io.*;


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
        // generate a maze with the given dimensions and algorithm retrieved from properties
        String generator = Configurations.getInstance().getMazeGeneratingAlgorithm();
        // generate the maze
        try {
            // try to get the class of the generator
            Class<?> generatingClass = Class.forName(generator);
            // Create an instance of the class
            Object instanceOfGenerator = generatingClass.newInstance();
            // cast the instance to AMazeGenerator subclass
            AMazeGenerator mazeGenerator;
            if (generator.equals("EmptyMazeGenerator")) {
                mazeGenerator = (EmptyMazeGenerator) instanceOfGenerator;
            }
            else if (generator.equals("SimpleMazeGenerator")) {
                mazeGenerator = (SimpleMazeGenerator) instanceOfGenerator;
            }
            else if (generator.equals("MyMazeGenerator")) {
                mazeGenerator = (MyMazeGenerator) instanceOfGenerator;
            }
            else {
                throw new RuntimeException("Invalid generator name");
            }
            // generate the maze
            Maze maze = mazeGenerator.generate(intArray[0], intArray[1]);
            // compress maze
            String mazeFileName = "savedMaze.maze";
            outToClient = new MyCompressorOutputStream(new FileOutputStream(mazeFileName));
            // write the compressed maze to the output stream
            outToClient.write(maze.toByteArray());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
