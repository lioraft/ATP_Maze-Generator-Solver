package Server;

import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.search.*;

import java.io.*;
import java.util.HashMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * The IServerStrategy interface is responsible for the server's strategy.
 * It receives an input stream and an output stream.
 * The strategy will be applied on the input stream and the output stream.
 * It gets a maze from client, and returns a solution to the maze as output stream.
 * If the maze was already solved, it will return the solution from the directory.
 */
public class ServerStrategySolveSearchProblem implements IServerStrategy {
    private static AtomicInteger currentSol = new AtomicInteger(0); // serial number of current solution, represented by atomic integer
    private static String solutionsDirectory = System.getProperty("java.io.tmpdir"); // path to the directory where solutions will be saved
    private static HashMap<Maze, Integer> prevMazes = new HashMap<>(); // map of previous mazes and their serial number. whenever solve new maze, adds it to map.
    // if already solved, gets the solution from directory and returns it to client
    @Override
    public void applyStrategy(InputStream inFromClient, OutputStream outToClient) {
        try {
            // create input and output stream of objects
            ObjectInputStream fromClient = new ObjectInputStream(inFromClient);
            ObjectOutputStream toClient = new ObjectOutputStream(outToClient);
            // get the maze from client
            Maze maze = (Maze) fromClient.readObject();
            // check if maze was already solved
            if (prevMazes.containsKey(maze)) {
                // get solution from directory
                Solution solution = getSolutionFromDirectory(maze);
                if (solution == null) {
                    throw new RuntimeException("Solution not found in directory");
                }
                // write solution to client
                toClient.writeObject(solution);
                // flush & close
                toClient.flush();
                toClient.close();
                fromClient.close();
                return;
            }
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
            // save solution to directory
            saveSolutionToDirectory(solution);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // method that gets a maze and returns its solution from the directory
    private Solution getSolutionFromDirectory(Maze maze) {
        // get the solution from the directory
        Solution solution = null;
        String solutionPath = solutionsDirectory + "\\" + prevMazes.get(maze);
        try {
            // create input stream of objects
            ObjectInputStream fromFile = new ObjectInputStream(new FileInputStream(solutionPath));
            // read the solution from the file
            solution = (Solution) fromFile.readObject();
            // close the input stream
            fromFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return solution;
    }

    // method that saves a solution to the directory
    private void saveSolutionToDirectory(Solution solution) {
        // create new file
        File file = new File(solutionsDirectory + "\\" + currentSol);
        // update current solution number
        currentSol = new AtomicInteger(currentSol.get() + 1);
        try {
            // create output stream of objects
            ObjectOutputStream toFile = new ObjectOutputStream(new FileOutputStream(file));
            // write the solution to the file
            toFile.writeObject(solution);
            // flush & close the output stream
            toFile.flush();
            toFile.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
