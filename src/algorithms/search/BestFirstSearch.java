package algorithms.search;

import java.util.*;

public class BestFirstSearch extends BreadthFirstSearch {
    // this class extends BreadthFirstSearch, and uses the same method to solve the maze. the only difference
    // is that this class uses priority queue sorted by cost, instead of a regular queue. this method
    // finds the lowest cost path from start goal to end goal.


    // function that returns the name of the search algorithm
    @Override
    public String getName() {
        return "Best First Search";
    }

    // this function is overriding breadth's first search function, and returning a priority queue
    @Override
    public Collection getDataStructure() {
        PriorityQueue<AState> stateQueue = new PriorityQueue<>(Comparator.comparingDouble(AState::getCost));
        return stateQueue;
    }
}
