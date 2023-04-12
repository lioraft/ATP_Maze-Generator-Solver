package algorithms.search;

import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearch extends ASearchingAlgorithm { // Breadth-First Search algorithm

    @Override
    public String getName() {
        return "Breadth-First Search";
    }

    @Override
    public Solution solve(ISearchable searchable) { // Breadth-First Search. Uses a queue to sort states by their cost.
        Queue<AState> stateQueue = new LinkedList<>(); // Initialize state queue with start state
        stateQueue.add(searchable.getStartState()); // Mark start state as visited
        visited.put(searchable.getStartState().getState(), searchable.getStartState()); // While queue is not empty

        while (!stateQueue.isEmpty()) { // while queue is not empty
            AState currState = stateQueue.poll(); // Get next state from queue

            if (searchable.FoundSolution(currState)) {
                return new Solution(currState);
            } // Check if state is goal state

            for (AState successor : searchable.getAllSuccessors(currState)) { // for each successor of currState
                if (!visited.containsKey(successor.getState())) { // Check if successor has already been visited
                    visited.put(successor.getState(), successor); // Mark successor as visited
                    stateQueue.add(successor); // Add successor to state queue
                }
            }
        }
        return null; // No solution found
    }
}
