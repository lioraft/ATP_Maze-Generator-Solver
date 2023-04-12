package algorithms.mazeGenerators;
import java.util.Random;

public class SimpleMazeGenerator extends AMazeGenerator {

    @Override
    public Maze generate(int numRows, int numColumns) {

        int[][] simpleMazeMatrix = new int[numRows][numColumns]; // create a new matrix
        Random rand = new Random(); // instance of random object
        int startColumn = rand.nextInt(numColumns); // selecting start column randomly
        // if start column is even number, initialize all even rows to 0, and all odd rows to 1.
        // if start column is odd number, initialize all odd rows to 0, and all even rows to 1.
        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numColumns; j++) {
                if (startColumn % 2 == 0) {
                    if (j % 2 == 0) {
                        simpleMazeMatrix[i][j] = 0;
                    }
                    else {
                        simpleMazeMatrix[i][j] = 1;
                    }
                }
                else {
                    if (j % 2 == 0) {
                        simpleMazeMatrix[i][j] = 1;
                    }
                    else {
                        simpleMazeMatrix[i][j] = 0;
                    }
                }
            }
        }
        // create start position
        Position start = new Position(0, startColumn);
        // select end column
        int endColumn;
        // if maze big enough
        if (numColumns > 2) {
            endColumn = rand.nextInt(numColumns-2); // selecting start column randomly
            // if start column is even, we want end column to be even too. symmetric case for odd start column
            // we need it in order to be certain we have path
            if (startColumn % 2 == 0) {
                if (endColumn % 2 == 1) {
                    endColumn = endColumn + 1;
                }
            }
            else {
                if (endColumn % 2 == 0)
                    endColumn = endColumn + 1;
            }
        }
        // if maze too small
        else {
            endColumn = startColumn;
        }
        // create end position
        Position end = new Position(numRows-1, endColumn);
        // iterate all columns of 1, and "break" one random cell in each column in order to create a path
        for (int j = (startColumn+1) % 2; j < numColumns; j=j+2) {
            // choose random cell in that row and break it
            int randomRow = rand.nextInt(numRows);
            simpleMazeMatrix[randomRow][j] = 0;
        }
        // now that we have a secured path, iterate all the columns of 1 again and break walls randomly
        for (int i = 0; i < numRows; i++) {
            for (int j = (startColumn+1) % 2; j < numColumns; j=j+2) {
                simpleMazeMatrix[i][j] = rand.nextInt(2);
            }
        }
        // create a maze and return it
        Maze maze = new Maze(start, end, simpleMazeMatrix);
        return maze;
    }
}
