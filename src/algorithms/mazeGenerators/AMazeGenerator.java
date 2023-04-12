package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator {

    // this method takes in the number of rows and number of columns of the maze, and returns the time it
    // takes to generate the maze
    public long measureAlgorithmTimeMillis(int numRows, int numColumns) {
        long startTime = System.currentTimeMillis();
        generate(numRows, numColumns);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

}