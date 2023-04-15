package algorithms.search;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Queue;

public class BreadthFirstSearch extends ASearchingAlgorithm { // Breadth-First Search algorithm

    // function that returns the string of
    @Override
    public String getName() {
        return "Breadth First Search";
    }

    // solve method returns a solution to the maze by implementing Breadth-First Search. It starts iterating neighbours of current state by pulling them out
    // of the queue they were pushed to in the previous iteration. by doing so, the algorithm sorts states by the number of moves it takes to reach them from start
    // position to end position, and returns the solution (which is the path that takes the most minimal moves), if found one. if not found, returns null
    @Override
    public Solution solve(ISearchable searchable) {
        Collection queue = getDataStructure(); // Initialize state queue with start state
        queue.add(searchable.getStartState()); // Mark start state as visited
        visited.put(searchable.getStartState().getState(), searchable.getStartState()); // While queue is not empty

        while (!queue.isEmpty()) { // while queue is not empty
            AState currState = ((Queue<AState>)queue).poll(); // Get next state from queue
            if (searchable.FoundSolution(currState)) {
                return new Solution(currState);
            } // Check if state is goal state

            for (AState successor : searchable.getAllSuccessors(currState)) { // for each successor of currState
                if (!visited.containsKey(successor.getState())) { // Check if successor has already been visited
                    visited.put(successor.getState(), successor); // Mark successor as visited
                    queue.add(successor); // Add successor to state queue
                    numNodesEvaluated++;
                }
            }
        }
        // if not found solution, return not found solution
        return new Solution(null);
    }

    // this function returns a queue, which is the collection that will be used to store the states
    public Collection getDataStructure() {
        Queue<AState> stateQueue = new LinkedList<>();
        return stateQueue;
    }
}
