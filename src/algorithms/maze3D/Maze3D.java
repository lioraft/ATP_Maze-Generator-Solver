package algorithms.maze3D;

public class Maze3D {
    private Position3D startPosition; //the start position
    private Position3D goalPosition; //the goal position
    private int[][][] theMaze; //a matrix


    public Maze3D(Position3D startPosition, Position3D goalPosition, int[][][] maze) { //the constructor of the maze
        this.startPosition = startPosition; //set the start position
        this.goalPosition = goalPosition; //set the goal position
        this.theMaze = maze; //set the matrix
    }

    public Position3D getStartPosition() {
        return this.startPosition;
    } //get the start position
    public Position3D getGoalPosition() {
        return this.goalPosition;
    } //get the goal position

    /// get the matrix of the maze
    public int[][][] getMaze() {
        return this.theMaze;
    }

    // function that takes in index of position: row & column, and a state, and set the input state as the new state of the cell
    // of the position received
    public void setCell(int row, int col, int depth, int state) {
        theMaze[row][col][depth] = state;
    }

    // function that returns the state of specified cell
    public int getCell(int row, int col, int depth) {
        return theMaze[row][col][depth];
    }

    public void print() {
        for (int k = 0; k < theMaze.length; k++) { // for each depth in the matrix
            for (int i = 0; i < theMaze[0].length; i++) { // for each row in the matrix
                System.out.print("{ ");
                for (int j = 0; j < theMaze[0][0].length; j++) { // for each column in the matrix
                    // if start position, print S
                    if (i == startPosition.getRowIndex() && j == startPosition.getColumnIndex() && k == startPosition.getDepthIndex())
                        System.out.print('S' + " ");
                        // if end position, print E
                    else if (i == goalPosition.getRowIndex() && j == goalPosition.getColumnIndex() && k == goalPosition.getDepthIndex())
                        System.out.print('E'+" ");
                    else
                        System.out.print(theMaze[i][j][k]+" "); // if regular position, print the value of the cell
                }
                System.out.println("}");
            }
        }
    }

    // function that takes in index of position, and returns true if it's a valid move (inside the maze and a passage), or
    // false if it's not (outside of maze boundaries or a wall)
    public boolean isValidPassage(int row, int col, int depth) {
        // if out of boundaries
        if (row >= theMaze.length || row < 0 || col >= theMaze[0].length || col < 0 || depth >= theMaze[0][0].length || depth < 0)
            return false;
        // if wall returns false, if passage returns true
        return theMaze[row][col][depth] != 1;
    }

}