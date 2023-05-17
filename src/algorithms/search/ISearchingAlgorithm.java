package algorithms.search;


/**
 *  The class that implements this interface will be a searching algorithm.
 *  the class will have a function that gets a searchable object and returns a solution to the problem.
 */
public interface ISearchingAlgorithm {
    Solution solve(ISearchable searchable);
    String getName();
    long getNumberOfNodesEvaluated();


}
