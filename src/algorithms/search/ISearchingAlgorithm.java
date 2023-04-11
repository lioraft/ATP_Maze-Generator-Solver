package algorithms.search;

public interface ISearchingAlgorithm { //the class that implements this interface will be a searching algorithm.
    // the class will have a function that gets a searchable object and returns a solution to the problem.
    Solution solve(ISearchable searchable);
    String getName();
    long getNumberOfNodesEvaluated();


}
