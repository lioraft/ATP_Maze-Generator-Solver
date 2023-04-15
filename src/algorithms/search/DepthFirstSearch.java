package algorithms.search;
import java.util.ArrayList;
import java.util.LinkedList;

public class DepthFirstSearch extends ASearchingAlgorithm {

    @Override
    public String getName() {
        return "Depth First Search";
    }

    @Override
    public Solution solve(ISearchable searchable) { // Depth-First Search. Uses a stack to sort states by their cost.
        AState startState = searchable.getStartState(); // Initialize state stack with start state
        visited.put(startState.getState(), startState); // Mark start state as visited

        LinkedList<AState> stateStack = new LinkedList<>(); // Initialize state stack with start state
        stateStack.add(startState); // Mark start state as visited

        while (!stateStack.isEmpty()) {
            AState currState = stateStack.removeLast();  // Get next state from stack

            if (searchable.FoundSolution(currState)) {  // Check if state is goal state
                return new Solution(currState);
            }

            ArrayList<AState> successors = searchable.getAllSuccessors(currState);
            for (AState successor : successors) {
                if (!visited.containsKey(successor.getState())) {  // Check if successor has already been visited
                    visited.put(successor.getState(), successor);  // Mark successor as visited
                    solution.add(successor);  // Add successor to solution path
                    stateStack.add(successor);  // Add successor to state stack
                    numNodesEvaluated++; // increase number of nodes evaluated
                }
            }
        }
        // if not found solution, return not found solution
        return new Solution(null);
    }
}
