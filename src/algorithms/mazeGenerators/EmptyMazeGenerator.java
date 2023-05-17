package algorithms.mazeGenerators;


/**
 * This abstract class implements the IMazeGenerator interface.
 * This class is responsible for generating an empty maze, which is filled with 0's only.
 */
public class EmptyMazeGenerator extends AMazeGenerator {
    @Override
    public Maze generate(int numRows, int numColumns) {
        int[][] matrix = new int[numRows][numColumns];  // Create a new matrix

        // Fill the maze with empty cells
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                matrix[row][col] = 0; // Set the cell to be 0 (only passages, no walls)
            }
        }

        return new Maze(new Position(0, 0), new Position(numRows - 1, numColumns - 1), matrix); // Return the new maze
    }
}
