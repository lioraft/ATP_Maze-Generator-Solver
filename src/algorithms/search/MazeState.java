package algorithms.search;

import algorithms.mazeGenerators.Position;

/**
 * this class represent a position in the maze, in addition to the position cost
 */
public class MazeState extends AState{ //this class represents a state in the maze. the state is the position in the maze.
    private Position position; //the position of the state in the maze.

    public MazeState(Position position, int cost, MazeState cameFrom) {//constructor
        this.position = position;
        this.cost = cost;
        this.cameFrom = cameFrom;
        this.state = position.toString();
    }

    public Position getPosition() {
        return position;
    } //get the position of the state.

    public void setPosition(Position position) {
        this.position = position;
    } //set the position of the state.

    @Override
    public boolean equals(Object obj) {
        return this.getPosition().equals(((MazeState)obj).getPosition());
    } //returns true if the given state is the same as this state.

    public String toString() {
        return this.getPosition().toString();
    }
    //returns a string that represents the state.

}
