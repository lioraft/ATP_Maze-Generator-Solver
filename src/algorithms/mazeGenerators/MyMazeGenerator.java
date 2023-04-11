package algorithms.mazeGenerators;
import java.util.*;
public class MyMazeGenerator extends AMazeGenerator {

   /* The binary tree algorithm works by iterating over each cell in the maze and flipping a coin to decide whether to remove
    the left or upper wall of the current cell. The upper-left corner of the maze is always part of the maze, and its wall is
    removed to create an entrance. All other cells have either the left or upper wall removed, but not both, which creates a
    maze with only horizontal and vertical paths. Finally, the start and goal positions are set to the upper-left and
    lower-right corners of the maze, respectively.*/
    @Override
    public Maze generate(int rows, int columns) {
        int[][] maze = new int[rows][columns]; //create a new matrix

        // Initialize maze with all walls
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                maze[i][j] = 1; //initialize all the cells to 1
            }
        }
        Random rand = new Random(); //create a new random object
        // Remove walls to create the maze
        for (int i = 0; i < rows; i++) { //for each row
            for (int j = 0; j < columns; j++) { //for each column
                // Upper-left corner is always part of the maze
                if (i == 0 && j == 0) { //if we are in the upper-left corner
                    maze[i][j] = 0; //set the cell to 0
                    continue;
                }

                // If on top row, remove left wall
                if (i == 0) { //if we are in the top row
                    maze[i][j-1] = 0; //set the cell to 0
                    continue;
                }

                // If on leftmost column, remove upper wall
                if (j == 0) { //if we are in the leftmost column
                    maze[i-1][j] = 0;
                    continue;
                }

                // Flip a coin to choose whether to remove the left or upper wall
                if (rand.nextBoolean()) { //if the random number is true
                    maze[i][j-1] = 0;
                } else {
                    maze[i-1][j] = 0;
                }
            }
        }

        Position start = new Position(0, 0); //create a new start position that is always in the upper-left corner
        Position goal = new Position(rows - 1, columns - 1); //create a new goal position that is always in the lower-right corner

        return new Maze(start, goal, maze); //return a new maze
    }
}