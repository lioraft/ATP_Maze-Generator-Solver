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

    // constructor that takes in a byte array and creates a maze
    public Maze(byte[] maze) {
        int rows = maze[0]; // get the number of rows
        int columns = maze[1]; // get the number of columns
        int startColumn = maze[2]; // get the start column
        int goalColumn = maze[3]; // get the end column
        this.startPosition = new Position(0, startColumn); // set the start position
        this.goalPosition = new Position(rows - 1, goalColumn); // set the goal position
        int[][] theMaze = new int[rows][columns]; // create a new matrix with the number of rows and columns
        int index = 4; //set the index to 4
        for (int i = 0; i < rows; i++) { // for each row
            for (int j = 0; j < columns; j++) { // for each column
                theMaze[i][j] = maze[index]; // set the value of the cell in the matrix to the value in the byte array
                index++; // increase the index
            }
        }
        this.theMaze = theMaze; // set the matrix
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
        if (row >= theMaze.length || row < 0 || col >= theMaze[0].length || col < 0)
            return false;
        // if wall returns false, if passage returns true
        return theMaze[row][col] != 1;
    }

    // function that takes in a maze represented in int (0 and 1) and convert it to flat byte array
    public byte[] toByteArray() {
        // get the number of rows and columns of the maze
        int rows = theMaze.length;
        int cols = theMaze[0].length;
        // in all mazes the start row is 0 and the end row is the last row, so we don't need to save them.
        // we only need to save the start and end column
        byte startColByte = (byte)startPosition.getColumnIndex();
        byte endColByte = (byte)goalPosition.getColumnIndex();
        // create a byte array representing the maze
        byte[] flatMaze = new byte[rows * cols + 4];
        // convert rows and cols to byte and add to array. also add start and end columns
        flatMaze[0] = (byte)rows;
        flatMaze[1] = (byte)cols;
        flatMaze[2] = startColByte;
        flatMaze[3] = endColByte;
        // iterate the 2d array and convert each cell to a byte and add to array of bytes
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (theMaze[i][j] == 0)
                    flatMaze[i * cols + j + 4] = (byte)0;
                else
                    flatMaze[i * cols + j + 4] = (byte)1;
            }
        }
        // return result
        return flatMaze;
    }

}