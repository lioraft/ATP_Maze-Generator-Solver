package algorithms.search;


import java.io.Serializable;

/**
 * class that represents a move in the maze solution
 */

public class AState implements Serializable { //this class represents a state in the searchable problem.
    // it has a name, a cost and a father. the state is the state's position in the maze. the cost is the
    // cost of the state. the cameFrom is the state that we came from to this state. the class implements the
    // Serializable interface so we can save the state in a file.
    protected int cost; //the cost of the state. the cost is the cost of the path from the start state to this state.
    protected AState cameFrom; //the state that we came from to this current state.
    protected String state; //the position of the state in the maze.

    public void setState(String state) {
        this.state = state;
    }

    public String getState() {
        return state;
    }

    public AState getCameFrom() {
        return cameFrom;
    }
    public void setCameFrom(AState cf) {
        this.cameFrom = cf;
    }


    public int getCost() {
        return cost;
    }


    public void setCost(int cost) {
        this.cost = cost;
    }

    public boolean equals(Object obj) {
        return this.getState().equals(((AState)obj).getState());
    } //returns true if the given state is the same as this state.

}
