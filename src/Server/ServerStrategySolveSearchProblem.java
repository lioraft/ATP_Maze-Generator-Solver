package Server;

import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        try {
            // path to the directory where mazes and solutions will be saved
            String tempDirectoryPath = System.getProperty("java.io.tmpdir");

            // create input and output stream of objects
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            // get the maze from client
            Maze maze = (Maze) fromClient.readObject();
            // get search algorithm from properties
            String searcher = Configurations.getInstance().getMazeSearchingAlgorithm();
            // create maze searcher according to the algorithm
            ISearchingAlgorithm mazeSearcher;
            if (searcher.equals("BestFirstSearch")) {
                mazeSearcher = new BestFirstSearch();
            }
            else if (searcher.equals("BreadthFirstSearch")) {
                mazeSearcher = new BreadthFirstSearch();
            }
            else if (searcher.equals("DepthFirstSearch")) {
                mazeSearcher = new DepthFirstSearch();
            }
            else {
                throw new RuntimeException("Invalid searcher name");
            }
            // get solution of maze
            Solution solution = mazeSearcher.solve(new SearchableMaze(maze));
            // write solution to client
            toClient.writeObject(solution);
            // flush & close
            toClient.flush();
            toClient.close();
            fromClient.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
