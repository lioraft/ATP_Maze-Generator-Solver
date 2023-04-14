package algorithms.search;

import algorithms.mazeGenerators.Position;

import java.util.Objects;

/**
 * class that represents a state in the maze
 */
public class MazeState extends AState{ //this class represents a state in the maze. the state is the position in the maze.
    private Position position; //the position of the state in the maze.

    public MazeState(Position pos, int cost, MazeState cameFrom) {//constructor
        super(cost, cameFrom, pos.toString());
        this.position = pos;
    }

    public Position getPosition() {
        return position;
    } //get the position of the state.
    public void setPosition(Position position) {
        this.position = position;
    } //set the position of the state.

    // function returns true if the given state is the same as this state, and false if they're not equal.
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        MazeState other = (MazeState)obj;
        return Objects.equals(state, other.state);
    }

    public String toString() {
        return this.getPosition().toString();
    } //returns a string that represents the state.

}
