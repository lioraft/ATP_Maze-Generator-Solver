package algorithms.search;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Abstract class that implements ISearchingAlgorithm interface.
 * This class is the base class for all searching algorithms.
 * It contains the visited states, solution path and number of nodes evaluated.
 */
public abstract class ASearchingAlgorithm implements ISearchingAlgorithm {

    protected HashMap<String, AState> visited; // Keep track of visited states
    protected HashSet<AState> solution; // Keep track of states in solution path
    protected int numNodesEvaluated; // Keep track of number of nodes evaluated during search

    public ASearchingAlgorithm() {
        visited = new HashMap<>();
        solution = new HashSet<>();
        numNodesEvaluated = 0;
    }

    // implementation of each algorithm will be made in inherited classes
    public abstract Solution solve(ISearchable searchable);

    // function that returns the number of nodes evaluated
    public long getNumberOfNodesEvaluated() {
        return numNodesEvaluated;
    }
}
