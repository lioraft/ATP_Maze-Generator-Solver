package algorithms.mazeGenerators;

public abstract class AMazeGenerator implements IMazeGenerator {
    //generate stay abstract

    public long measureAlgorithmTimeMillis(int numRows, int numColumns) {
        long startTime = System.currentTimeMillis();
        generate(numRows, numColumns);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }

}