package algorithms.maze3D;

import algorithms.mazeGenerators.Position;

public class Position3D extends Position {
    // this class represents a 3D Position in the maze. it extends Position class by adding depth to existing 2D Position.
    int depth; // the depth of the position
    public Position3D(int row, int column, int depthOfPos) {
        super(row, column);
        this.depth = depthOfPos;
    }

    public int getDepthIndex() {return depth;} // getter of depth


    // print function
    @Override
    public String toString() {
        return ("{" + this.rows + "," + this.columns + "," + this.depth + "}"); }
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
        if (((Position3D) o).getColumnIndex() == columns && ((Position3D) o).getRowIndex() == rows && ((Position3D) o).getDepthIndex() == depth)
            return true;
        return false;
    }
}
