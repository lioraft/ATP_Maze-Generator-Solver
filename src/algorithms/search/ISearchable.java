package algorithms.search;

import java.util.ArrayList;

public interface ISearchable { //the class that implements this interface
    // will be searchable by the search algorithms. the class will have a start state, a goal state and a
    // function that returns all the possible states from a given state.
    AState getStartState(); //returns the start state of the searchable
    AState getGoalState(); //returns the goal state of the searchable
    ArrayList<AState> getAllSuccessors(AState state); //returns all the possible states from a given state
    boolean FoundSolution(AState state); //returns true if the given state is the goal state


}
