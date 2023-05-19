package algorithms.mazeGenerators;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * The Maze class represents a maze.
 * A maze is a 2D array of integers, where 0 represents a passage and 1 represents a wall.
 * The maze has a start position and an end position.
 * The maze can be converted to a byte array.
 * The maze can be created from a byte array.
 */
public class Maze implements Serializable { //the maze class
    private Position startPosition; //the start position
    private Position goalPosition; //the goal position
    private int[][] theMaze; //a matrix

    //the constructor of the maze
    public Maze(Position startPosition, Position goalPosition, int[][] maze) {
        this.startPosition = startPosition; //set the start position
        this.goalPosition = goalPosition; //set the goal position
        this.theMaze = maze; //set the matrix
    }

    // constructor that takes in a byte array and creates a maze
    public Maze(byte[] maze) {
        // calculate maze indexes
        int bytesForRows = maze[0] + 1;
        // get number of rows
        int rowsNum = 0;
        for (int i = 1; i < bytesForRows; i++) {
            // if negative value, add it's positive value
            if (maze[i] < 0) {
                int pos = 127-maze[i];
                rowsNum+= pos;
            }
            else
                rowsNum+= maze[i]; // if positive, add as is
        }
        int bytesForColumns = maze[bytesForRows] + 1;
        // get number of columns
        int columnsNum = 0;
        for (int i = bytesForRows + 1; i < bytesForRows + bytesForColumns; i++) {
            // if negative value, add it's positive value
            if (maze[i] < 0) {
                int pos = 127-maze[i];
                columnsNum+= pos;
            }
            else
                columnsNum+= maze[i]; // if positive, add as is
        }
        int bytesForStartCol = maze[bytesForColumns+bytesForRows] + 1;
        // get the start col index
        int startColumnNumber = 0;
        for (int i = bytesForRows + bytesForColumns + 1; i < bytesForRows + bytesForColumns + bytesForStartCol; i++) {
            // if negative value, add it's positive value
            if (maze[i] < 0) {
                int pos = 127-maze[i];
                startColumnNumber+= pos;
            }
            else
                startColumnNumber+= maze[i]; // if positive, add as is
        }
        int bytesForEndCol = maze[bytesForStartCol+bytesForColumns+bytesForRows] + 1;
        // get the end index
        int goalColumnNumber = 0;
        for (int i = bytesForRows + bytesForColumns + bytesForStartCol + 1; i < bytesForRows + bytesForColumns + bytesForStartCol + bytesForEndCol; i++) {
            // if negative value, add it's positive value
            if (maze[i] < 0) {
                int pos = 127-maze[i];
                goalColumnNumber+= pos;
            }
            else
                goalColumnNumber+= maze[i]; // if positive, add as is
        }
        int mazeStartIndex = bytesForEndCol + bytesForStartCol + bytesForColumns + bytesForRows;
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

    public int[][] getMaze() {
        return this.theMaze;
    } // get the matrix of the maze

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
            // if number of rows is bigger than 255, we need more than one byte to represent it
            if (rows % 255 != 0) { // adding extra byte if necessary
                byteList.add((byte)(1 + rows / 255));
            }
            else { // not adding extra byte if result is a natural number
                byteList.add((byte)(rows / 255));
            }
        }
        // add the number of rows to the byte array
        while (rows > 255) {
            byteList.add((byte)-128);
            rows = rows - 255;
        }
        // check if the last number is bigger than 127 - if so, represent it as a negative number
        if (rows > 127) {
            byteList.add((byte)(127-rows));
        }
        else {
            byteList.add((byte)rows);
        }

        // get the number of columns in the maze
        int cols = theMaze[0].length;
        if (cols < 256) {
            // if number of columns is smaller than 256, we only need one byte to represent it
            byteList.add((byte)1);
        }
        else {
            // if number of columns is bigger than 256, we need more than one byte to represent it
            if (cols % 255 != 0) { // adding extra bytes if necessary
                byteList.add((byte)(1 + cols / 255));
            }
            else { // not adding extra byte if result is a natural number
                byteList.add((byte)(cols / 255));
            }
        }
        // add the number of columns to the byte array
        while (cols > 255) {
            byteList.add((byte)-128);
            cols = cols - 255;
        }
        // check if the last number is bigger than 127 - if so, represent it as a negative number
        if (cols > 127) {
            byteList.add((byte)(127-cols));
        }
        else {
            byteList.add((byte)cols);
        }

        // in all mazes the start row is 0 and the end row is the last row, so we don't need to save them.
        // we only need to save the start and end column
        // repeat same insertion process as above for the start column
        int startColByte = startPosition.getColumnIndex();
        if (startColByte < 256)
            byteList.add((byte)1);
        else {
            if (startColByte % 255 != 0) { // adding extra byte if necessary
                byteList.add((byte)(1 + startColByte / 255));
            }
            else { // not adding extra byte if result is a natural number
                byteList.add((byte)(startColByte / 255));
            }
        }
        // add the start column index to the byte array
        while (startColByte > 255) {
            byteList.add((byte)-128);
            startColByte = startColByte - 255;
        }

        // check if the last number is bigger than 127 - if so, represent it as a negative number
        if (startColByte > 127) {
            byteList.add((byte)(127-startColByte));
        }
        else {
            byteList.add((byte)startColByte);
        }

        // for end column
        int endColByte = goalPosition.getColumnIndex();
        if (endColByte < 256)
            byteList.add((byte)1);
        else {
            if (endColByte % 256 != 0) { // adding extra byte if necessary
                byteList.add((byte)(1 + endColByte / 255));
            }
            else { // not adding extra byte if result is a natural number
                byteList.add((byte)(endColByte / 255));
            }
        }
        // add the end column index to the byte array
        while (endColByte > 255) {
            byteList.add((byte)-128);
            endColByte = endColByte - 255;
        }
        // check if the last number is bigger than 127 - if so, represent it as a negative number
        if (endColByte > 127) {
            byteList.add((byte)(127-endColByte));
        }
        else {
            byteList.add((byte)endColByte);
        }
        // iterate the 2d array and convert each cell to a byte and add to array of bytes
        for (int i = 0; i < theMaze.length; i++) {
            for (int j = 0; j < theMaze[0].length; j++) {
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