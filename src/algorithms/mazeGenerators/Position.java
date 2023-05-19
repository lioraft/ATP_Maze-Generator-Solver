package algorithms.mazeGenerators;


import java.io.Serializable;

/**
 * Position class represents a position in a maze.
 * It has two fields: rows and columns.
 */
public class Position implements Serializable {
    protected int rows;
    protected int columns;

    // constructor
    public Position(int row, int column) {
        this.rows = row;
        this.columns = column;
    }
    public int getRowIndex() {return this.rows;}
    public int getColumnIndex() {return this.columns;}

    // print function
    @Override
    public String toString() { return ("{" + rows + "," + columns + "}"); }
    // overriding equals: function that takes in another object, and returns true if it's a position that has equal coordinates
    // to current position. if not, returns false
    @Override
    public boolean equals(Object o) {
        // if comparing object to itself
        if (o == this)
            return true;
        // if not object of type position, return false
        if (o.getClass() != Position.class)
            return false;
        // if identical coordinates, return true
        if (((Position) o).getColumnIndex() == columns && ((Position) o).getRowIndex() == rows)
            return true;
        return false;
    }

}
