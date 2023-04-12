package algorithms.search;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {

    protected HashMap<String, AState> visited; // Keep track of visited states
    protected HashSet<AState> solution; // Keep track of states in solution path
    protected int numNodesEvaluated; // Keep track of number of nodes evaluated during search

    public ASearchingAlgorithm() {
        visited = new HashMap<>();
        solution = new HashSet<>();
        numNodesEvaluated = 0;
    }

    public Solution solve(ISearchable searchable) {
        AState startState = searchable.getStartState();
        visited.put(startState.getState(), startState); // Mark start state as visited

        LinkedList<AState> stateQueue = new LinkedList<>(); // Initialize state queue with start state
        stateQueue.add(startState);

        while (!stateQueue.isEmpty()) {
            AState currState = stateQueue.removeFirst();  // Get next state from queue

            if (searchable.FoundSolution(currState)) {  // Check if state is goal state
                return new Solution(currState);
            }

            ArrayList<AState> successors = searchable.getAllSuccessors(currState);
            for (AState successor : successors) {
                if (!visited.containsKey(successor.getState())) {  // Check if successor has already been visited
                    visited.put(successor.getState(), successor);  // Mark successor as visited
                    solution.add(successor);  // Add successor to solution path
                    stateQueue.add(successor);  // Add successor to state queue
                    numNodesEvaluated++;
                }
            }
        }

        return null;  // No solution found
    }

    public long getNumberOfNodesEvaluated() {
        return numNodesEvaluated;
    }
}
