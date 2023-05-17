package algorithms.mazeGenerators;

/**
 * IMazeGenerator interface represents a maze generator.
 * The interface has two methods:
 * generate - that generates a maze
 * measureAlgorithmTimeMillis - that measures the time it takes to generate a maze
 * both methods need to be implemented by classes that implement the interface.
 */
public interface IMazeGenerator {
    public Maze generate(int numRows, int numColumns); //takes two integer inputs: the number of rows and columns for the maze, and returns an instance of the "Maze" class.
    public long measureAlgorithmTimeMillis(int numRows, int numColumns); // a method that measures the time it takes to generate a maze

}
