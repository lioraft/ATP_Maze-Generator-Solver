package algorithms.mazeGenerators;

public interface IMazeGenerator {
    public Maze generate(int numRows, int numColumns); //takes two integer inputs: the number of rows and columns for the maze, and returns an instance of the "Maze" class.
    public long measureAlgorithmTimeMillis(int numRows, int numColumns);

}
