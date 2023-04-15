package algorithms.maze3D;

public abstract class AMaze3DGenerator implements IMazeGenerator3D {
    // this abstract class implements the 3D maze generator interface

    // this method takes in the length of dimensions of the 3D maze, and returns the time it
    // takes to generate it
    public long measureAlgorithmTimeMillis(int rows, int columns, int depth) {
        long startTime = System.currentTimeMillis();
        generate(rows, columns, depth);
        long endTime = System.currentTimeMillis();
        return endTime - startTime;
    }
}
