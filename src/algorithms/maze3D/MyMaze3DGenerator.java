package algorithms.maze3D;

import java.util.Random;

public class MyMaze3DGenerator extends AMaze3DGenerator{

    // this algorithm modifies the algorithm of the generating algorithm of 2D maze by adding another
    // dimension to the maze. the extra axis is represented by adding one more dimension to the maze array
    public Maze3D generate(int rows, int columns, int depth) {
        int[][][] maze = new int[rows][columns][depth]; // create a new 3D matrix

        // Initialize maze with all walls
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                for (int k = 0; k < depth; k++) {
                    maze[i][j][k] = 1; // initialize all the cells to 1
                }
            }
        }
        Random rand = new Random(); // create a new random object
        // Remove walls to create the maze
        for (int i = 0; i < rows; i++) { // for each row
            for (int j = 0; j < columns; j++) { // for each column
                for (int k = 0; k < depth; k++) { // for each depth
                    // Upper-left corner is always part of the maze
                    if (i == 0 && j == 0 && k == 0) { // if we are in the upper-left corner
                        maze[i][j][k] = 0; // set the cell to 0
                        continue;
                    }

                    // If on top row, remove left wall
                    if (i == 0 && k == 0) { // if we are in the top row
                        maze[i][j-1][k] = 0; // set the cell to 0
                        continue;
                    }

                    // If on leftmost column, remove upper wall
                    if (i == 0 && j == 0) { // if we are in the leftmost column
                        maze[i][j][k-1] = 0;
                        continue;
                    }

                    // If on first level, remove inside wall
                    if (j == 0 & k == 0) { // if we are on the first depth
                        maze[i-1][j][k] = 0;
                        continue;
                    }

                    // Flip a coin to choose whether to remove the left, upper or top wall
                    int direction = rand.nextInt(3); // generate a random number between 0 and 2
                    if (direction == 0 && j > 0) {
                        maze[i][j-1][k] = 0; // remove left wall
                    } else if (direction == 1 && i > 0) {
                        maze[i-1][j][k] = 0; // remove upper wall
                    } else if (k > 0) {
                        maze[i][j][k-1] = 0; // remove inside wall
                    }
                }
            }
        }

        Position3D start = new Position3D(0, 0, 0); // create a new start position that is always in the upper-left corner
        Position3D goal = new Position3D(rows - 1, columns - 1, depth - 1); // create a new goal position that is always in the lower-right corner
        // set end to 0
        maze[rows - 1][columns - 1][depth - 1] = 0;
        return new Maze3D(start, goal, maze); // return a new 3D maze
    }
}
