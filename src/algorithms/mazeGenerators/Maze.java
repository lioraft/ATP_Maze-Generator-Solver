package algorithms.mazeGenerators;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.ArrayList;

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
        // calculate maze start index
        int bytesForRows = maze[0] + 1;
        // get number of rows
        byte[] rows = new byte[maze[0]];
        for (int i = 1; i < bytesForRows; i++) {
            rows[i - 1] = maze[i];
        }
        int bytesForColumns = maze[bytesForRows] + 1;
        // get number of columns
        byte[] columns = new byte[maze[bytesForRows]];
        for (int i = bytesForRows + 1; i < bytesForRows + bytesForColumns; i++) {
            columns[i - bytesForRows - 1] = maze[i];
        }
        int bytesForStartCol = maze[bytesForColumns] + 1;
        // get the start col index
        byte[] startCol = new byte[maze[bytesForColumns]];
        for (int i = bytesForRows + bytesForColumns + 1; i < bytesForRows + bytesForColumns + bytesForStartCol; i++) {
            startCol[i - bytesForRows - bytesForColumns - 1] = maze[i];
        }
        int bytesForEndCol = maze[bytesForStartCol] + 1;
        // get the end index
        byte[] endCol = new byte[maze[bytesForStartCol]];
        for (int i = bytesForRows + bytesForColumns + bytesForStartCol + 1; i < bytesForRows + bytesForColumns + bytesForStartCol + bytesForEndCol; i++) {
            endCol[i - bytesForRows - bytesForColumns - bytesForStartCol - 1] = maze[i];
        }
        int mazeStartIndex = bytesForEndCol + bytesForStartCol + bytesForColumns + bytesForRows;
        // now convert the bytes to integers
        int rowsNum = new BigInteger(rows).intValue(); // get the number of rows
        int columnsNum = new BigInteger(columns).intValue(); // get the number of columns
        int startColumnNumber= new BigInteger(startCol).intValue(); // get the start column
        int goalColumnNumber = new BigInteger(endCol).intValue(); // get the end column
        this.startPosition = new Position(0, startColumnNumber); // set the start position
        this.goalPosition = new Position(rowsNum-1, goalColumnNumber); // set the goal position
        int[][] theMaze = new int[rowsNum][columnsNum]; // create a new matrix with the number of rows and columns
        int index = mazeStartIndex; //set the index to maze start index
        for (int i = 0; i < rowsNum; i++) { // for each row
            for (int j = 0; j < columnsNum; j++) { // for each column
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

    // function that takes in a maze represented in int (0 and 1) and convert it to flat byte array:
    // representation structure:
    // <number of bytes for rows><number of rows><number of bytes for columns><number of columns><number of bytes for start column><start position column><number of bytes for end column><end position column><maze>
    public byte[] toByteArray() {
        ArrayList<Byte> byteList = new ArrayList<>(); // create an arraylist of bytes

        // get the number of rows in the maze
        int rows = theMaze.length;
        // indicate how many bytes will be needed to represent the number of rows
        if (rows < 256) {
            // if number of rows is smaller than 256, we only need one byte to represent it
            byteList.add((byte)1);
        }
        else {
            // if number of rows is bigger than 256, we need more than one byte to represent it
            if (rows % 256 != 0) { // adding extra byte if necessary
                byteList.add((byte)(1 + rows / 256));
            }
            else { // not adding extra byte if result is a natural number
                byteList.add((byte)(rows / 256));
            }
        }
        // add the number of rows to the byte array
        while (rows > 256) {
            byteList.add((byte)255);
            rows = rows - 255;
        }
        // add remainder of rows number to the byte array
        byteList.add((byte)rows);

        // get the number of columns in the maze
        int cols = theMaze[0].length;
        if (cols < 256) {
            // if number of columns is smaller than 256, we only need one byte to represent it
            byteList.add((byte)1);
        }
        else {
            // if number of columns is bigger than 256, we need more than one byte to represent it
            if (cols % 256 != 0) { // adding extra byte if necessary
                byteList.add((byte)(1 + cols / 256));
            }
            else { // not adding extra byte if result is a natural number
                byteList.add((byte)(cols / 256));
            }
        }
        // add the number of columns to the byte array
        while (cols > 256) {
            byteList.add((byte)255);
            cols = cols - 255;
        }
        // add remainder of columns number to the byte array
        byteList.add((byte)cols);

        // in all mazes the start row is 0 and the end row is the last row, so we don't need to save them.
        // we only need to save the start and end column
        // repeat same insertion process as above for the start column
        int startColByte = startPosition.getColumnIndex();
        if (startColByte < 256)
            byteList.add((byte)1);
        else {
            if (startColByte % 256 != 0) { // adding extra byte if necessary
                byteList.add((byte)(1 + startColByte / 256));
            }
            else { // not adding extra byte if result is a natural number
                byteList.add((byte)(startColByte / 256));
            }
        }
        // add the start column index to the byte array
        while (startColByte > 256) {
            byteList.add((byte)255);
            startColByte = startColByte - 255;
        }
        // add remainder of columns number to the byte array
        byteList.add((byte)startColByte);

        // for end column
        int endColByte = (byte)goalPosition.getColumnIndex();
        if (endColByte < 256)
            byteList.add((byte)1);
        else {
            if (endColByte % 256 != 0) { // adding extra byte if necessary
                byteList.add((byte)(1 + endColByte / 256));
            }
            else { // not adding extra byte if result is a natural number
                byteList.add((byte)(endColByte / 256));
            }
        }
        // add the end column index to the byte array
        while (endColByte > 256) {
            byteList.add((byte)255);
            endColByte = endColByte - 255;
        }
        // add remainder of columns number to the byte array
        byteList.add((byte)endColByte);

        // iterate the 2d array and convert each cell to a byte and add to array of bytes
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (theMaze[i][j] == 0)
                    byteList.add((byte)0);
                else
                    byteList.add((byte)1);
            }
        }
        // return result as byte array
        byte [] result = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            result[i] = byteList.get(i);
        }
        return result;
    }
}