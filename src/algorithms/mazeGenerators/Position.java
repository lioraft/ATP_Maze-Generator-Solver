package algorithms.mazeGenerators;

public class Position {

    private int row;
    private int column;

    // constructor
    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }
    public int getRowIndex() {return this.row;}
    public int getColumnIndex() {return this.column;}

    // print function
    @Override
    public String toString() { return ("{" + row + "," + column + "}"); }
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
        if (((Position) o).getColumnIndex() == column && ((Position) o).getRowIndex() == row)
            return true;
        return false;
    }



}
