package algorithms.mazeGenerators;

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

    /// get the matrix of the maze
    public int[][] getMaze() {
        return this.theMaze;
    }

    public void print () {  //print the maze
        for (int i = 0; i < theMaze.length; i++) {  //for each row in the matrix print the row and the column and the value of the cell in the matrix.
            System.out.print("{ ");
            for (int j = 0; j < theMaze[0].length; j++) {
                // if start position, print S
                if (i == startPosition.getRowIndex() && j == startPosition.getColumnIndex())
                    System.out.print('S' + " ");
                // if end position, print E
                else if (i == goalPosition.getRowIndex() && j == goalPosition.getColumnIndex())
                    System.out.print('E'+" ");
                else
                    System.out.print(theMaze[i][j]+" "); // if regular position, print the value of the cell
            }
            System.out.println("}");
        }
    }

    // function that takes in index of position: row & column, and a state, and set the input state as the new state of the cell
    // of the position received
    public void setCell(int row, int col, int state) {
        theMaze[row][col] = state;
    }

    // function that returns the state of specified cell
    public int getCell(int row, int col) {
        return theMaze[row][col];
    }

    // function that takes in index of position, and returns true if it's a valid move (inside the maze and a passage), or
    // false if it's not (outside of maze boundaries or a wall)
    public boolean isValidPassage(int row, int col) {
        // if out of boundaries
        if (row >= theMaze.length || row < 0 || col >= theMaze[0].length || col <= 0)
            return false;
        // if wall returns false, if passage returns true
        return theMaze[row][col] != 1;
    }

}