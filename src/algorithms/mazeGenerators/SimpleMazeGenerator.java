package algorithms.mazeGenerators;
import java.util.ArrayList;
import java.util.Random;
import java.util.Stack;

public class SimpleMazeGenerator extends AMazeGenerator {


     /*  the generate method randomly chooses a start and goal position for the maze. It sets the matrix value at these positions to 0 to indicate they are part
       of the path in the maze. It sets all other matrix values to 1, indicating they are not part of the path.
       The generate method then creates a path between the start and goal positions by using a while loop. It sets matrix values to 0 along this path,
       and adds the positions to an ArrayList called "path". Finally, the generate method randomly assigns either a 0 or 1 to the matrix values of any
       positions that are not part of the path. This creates multiple possible solutions for the maze.
        Overall, the SimpleMazeGenerator class generates a simple maze with one path from the start to goal, but with multiple possible solutions.*/
    @Override
     public Maze generate(int numRows, int numColumns) {

        int[][] simpleMazeMatrix = new int[numRows][numColumns]; // create a new matrix
        // first, we randomly choose start and goal positions. we need to make
        // sure that the start and goal positions is in the range of the matrix.
        int randomColumnS = (int) (Math.random() * (numColumns - 1)); // random number between 0 and numColumns-1.
        Position start = new Position(0, randomColumnS); // create a new start position.

        int randomColumnF = (int) (Math.random() * (numColumns - 1)); // random number between 0 and numColumns-1.

        Position goal = new Position(numRows-1, randomColumnF-1); // create a new goal position
        simpleMazeMatrix[start.getRowIndex()][start.getColumnIndex()] = 0; // set the start position to 0 - that means that it is part of the path
        simpleMazeMatrix[goal.getRowIndex()][goal.getColumnIndex()] = 0; // set the goal position to 0 - that means that it is part of the path
        for (int i=0; i<numRows; i++) {
            for (int j=0; j<numColumns; j++) {
                simpleMazeMatrix[i][j] = 1;
            }
        }
        Maze maze = new Maze(start, goal, simpleMazeMatrix);

        // using while loop, we will create a path from the start Position to the goal
        int i = 0; // rows
        int j = 0; // columns
        ArrayList<Position> path = new ArrayList<>();
        while (i != maze.getGoalPosition().getRowIndex() || j != maze.getGoalPosition().getColumnIndex()) {
            if (i < maze.getGoalPosition().getRowIndex()) {
                i++;
                maze.getMazeMatrix()[i][j] = 0;
                path.add(new Position(i,j));
            }
            if (j < maze.getGoalPosition().getColumnIndex()) {
                j++;
                maze.getMazeMatrix()[i][j] = 0;
                path.add(new Position(i,j));
            }
        }

        // now we promised to have one path from the start to goal
        // we randomly give values to the cells who dont belong to the path - probably generating more solutions

        Random rand = new Random();
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numColumns; col++) {
                if (!(path.contains(new Position(row,col)))) {
                    maze.getMazeMatrix()[row][col] = (int)(Math.random()*2);
                }
            }
        }

        return maze;
    }
}
