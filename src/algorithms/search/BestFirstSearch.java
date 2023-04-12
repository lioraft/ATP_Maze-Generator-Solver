package algorithms.search;

import java.util.Comparator;
import java.util.PriorityQueue;

public class BestFirstSearch extends ASearchingAlgorithm {

    @Override
    public String getName() {
        return "Best-First Search";
    }

    @Override
    public Solution solve(ISearchable searchable) { //Best-First Search. Uses a priority queue to sort states by their cost.
        PriorityQueue<AState> stateQueue = new PriorityQueue<>(Comparator.comparingDouble(AState::getCost)); // Initialize state queue with start state
        stateQueue.add(searchable.getStartState()); //Mark start state as visited
        visited.put(searchable.getStartState().getState(), searchable.getStartState()); //While queue is not empty

        while (!stateQueue.isEmpty()) { //while queue is not empty
            AState currState = stateQueue.poll(); //Get next state from queue
            if (searchable.FoundSolution(currState)) { //Check if state is goal state
                return new Solution(currState); //Return solution
            }

            for (AState successor : searchable.getAllSuccessors(currState)) { //for each successor of currState
                if (!visited.containsKey(successor.getState())) { //Check if successor has already been visited
                    visited.put(successor.getState(), successor); //Mark successor as visited
                    successor.setCost(successor.getCost() + currState.getCost()); //Set cost of successor
                    stateQueue.add(successor); //Add successor to state queue
                } else {
                    AState visitedSuccessor = visited.get(successor.getState()); //Get visited successor
                    if (visitedSuccessor.getCost() > successor.getCost() + currState.getCost()) { //Check if visited successor has a higher cost than the current path
                        visitedSuccessor.setCameFrom(currState); //Update visited successor's cameFrom
                        visitedSuccessor.setCost(successor.getCost() + currState.getCost()); //Update visited successor's cost
                        stateQueue.remove(visitedSuccessor); //Remove visited successor from queue
                        stateQueue.add(visitedSuccessor); //Add visited successor to queue
                    }
                }
            }
        }

        return null; // No solution found
    }
}
