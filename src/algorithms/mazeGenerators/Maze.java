package algorithms.mazeGenerators;

import java.util.Map;

public class Maze { //the maze class
    private Position startPosition; //the start position
    private Position goalPosition; //the goal position
    private int[][] theMaze; //a matrix


    public Maze(Position startPosition, Position goalPosition, int[][] maze) { //the constructor of the maze
        this.startPosition = startPosition; //set the start position
        this.goalPosition = goalPosition; //set the goal position
        this.theMaze = maze; //set the matrix
    }


    public Position getStartPosition() {
        return this.startPosition;
    } //get the start position
    public Position getGoalPosition() {
        return this.goalPosition;
    } //get the goal position
    public int[][] getMazeMatrix() {
        return this.theMaze;
    } //get the matrix

    public void print () {  //print the maze
        for (int i = 0; i < theMaze.length; i++) {  //for each row in the matrix print the row and the column and the value of the cell in the matrix.
            System.out.print("{ ");
            for (int j = 0; j < theMaze[0].length; j++) {
                if (i == startPosition.getRowIndex() && j == startPosition.getColumnIndex())
                    System.out.print('S' + " ");
                else if (i == goalPosition.getRowIndex() && j == goalPosition.getColumnIndex())
                    System.out.print('E'+" ");
                else
                    System.out.print(theMaze[i][j]+" ");
            }
            System.out.println("}");
        }
    }

    public void setCell(int row, int col, int state) {
        theMaze[row][col] = state;
    }

    // function that returns the state of specified cell
    public int getCell(int row, int col) {
        return theMaze[row][col];
    }

    public boolean isWall(int row, int col) {
        if(theMaze[row][col] == 1)
            return true;
        return false;
    }

}