package algorithms.search;

import java.io.Serializable;
import java.util.Objects;

/**
 * class that represents a state in the searchable problem.
 * the class implements the Serializable interface so we can save the state in a file.
 */

public class AState implements Serializable { //this class represents a state.
    // it has a name, a cost and a father. the state is the state's position in the maze. the cost is the
    // cost of the state. the cameFrom is the state that we came from to this state. the class implements the
    // Serializable interface so we can save the state in a file.
    protected int cost; //the cost of the state. the cost is the cost of the path from the start state to this state.
    protected AState cameFrom; //the state that we came from to this current state.
    protected String state; //the position of the state in the maze.

    public AState(int cost, AState cameFrom, String cf) { // constructor
        this.cost = cost;
        this.cameFrom = cameFrom;
        this.state = cf;
    }
    public void setState(String state) {
        this.state = state;
    } // setter of state
    public String getState() {
        return state;
    } // getter of state
    public void setCameFrom(AState cf) {
        this.cameFrom = cf;
    } // setter of cameFrom
    public AState getCameFrom() {
        return cameFrom;
    } // getter of cameFrom
    public void setCost(int cost) {
        this.cost = cost;
    } // setter of cost
    public int getCost() {
        return cost;
    } // getter of cost

    // function returns true if the given state is the same as this state, and false if they're not equal.
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || this.getClass() != obj.getClass())
            return false;
        AState other = (AState)obj;
        return Objects.equals(state, other.state);
    }

    public String toString() {
        //returns a string that represents the state.
        return this.state;
    }

}
