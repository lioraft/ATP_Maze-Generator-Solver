package algorithms.mazeGenerators;

public class EmptyMazeGenerator extends AMazeGenerator {
    @Override
    public Maze generate(int numRows, int numColumns) {
        int[][] matrix = new int[numRows][numColumns];  // Create a new matrix

        // Fill the maze with empty cells
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                matrix[row][col] = 0; // Set the cell to be empty
            }
        }

        return new Maze(new Position(0, 0), new Position(numRows - 1, numColumns - 1), matrix); // Return the new maze
    }
}
