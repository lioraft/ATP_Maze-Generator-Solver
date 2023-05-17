package algorithms.maze3D;

/**
 * This interface represents the methods that should be implemented for 3D maze generators.
 */
public interface IMazeGenerator3D {

    // generate a 3D maze
    public Maze3D generate(int depth, int row, int column);

    // measure how much time it takes to generate it
    public long measureAlgorithmTimeMillis(int depth, int row, int column);
}
