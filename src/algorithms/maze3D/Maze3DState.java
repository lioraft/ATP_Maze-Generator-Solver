package algorithms.maze3D;
import algorithms.search.AState;

import java.util.Objects;

public class Maze3DState extends AState {
    Position3D position;
    public Maze3DState(Position3D pos, int cost, Maze3DState cameFrom) {//constructor
        super(cost, cameFrom, pos.toString());
        this.position = pos;
    }

    public Position3D getPosition() {
        return position;
    } //get the position of the state.
    public void setPosition(Position3D position) {
        this.position = position;
    } //set the position of the state.

    // function returns true if the given state is the same as this state, and false if they're not equal.
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        Maze3DState other = (Maze3DState)obj;
        return Objects.equals(state, other.state);
    }

    public String toString() {
        return this.getPosition().toString();
    } //returns a string that represents the state.

}
