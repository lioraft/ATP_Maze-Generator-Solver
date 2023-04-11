package algorithms.search;

import java.io.Serializable;
import java.util.ArrayList;

public class Solution implements Serializable { //the class represents a solution to a problem.
    // the class will have a function that gets a goal state and returns the path from the start
    // state to the goal state.
    ArrayList<AState> path; //the path from the start state to the goal state.
    Solution(AState Goal){ //the constructor gets a goal state and creates the path from the start state to the goal state.
        path = CreateAPath(Goal);
    }
    public ArrayList<AState> CreateAPath(AState Goal){ //the function gets a goal state and returns the path from the start state to the goal state.
        ArrayList<AState> ReverseSolution = new ArrayList<AState>(); //path
        ReverseSolution.add(Goal); //add the goal state to the path
        AState curr = Goal.getCameFrom(); //get the state that we came from to the goal state
        while(curr != null){ //while we didn't get to the start state
            ReverseSolution.add(curr); //add the state to the path
            curr = curr.getCameFrom(); //get the state that we came from to the current state
        }
        ArrayList<AState> FinalSolution = new ArrayList<>(); //the final path we will return back
        for (int i = ReverseSolution.size() - 1; i >= 0 ; i--) { //reverse the path
            FinalSolution.add(ReverseSolution.get(i)); //add the state to the final path
        }
        return FinalSolution; //return the final path
    }
    public ArrayList<AState> getSolutionPath(){
        return this.path;
    }
}
