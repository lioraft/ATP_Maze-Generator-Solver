import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.BestFirstSearch;
import algorithms.search.SearchableMaze;
import algorithms.search.Solution;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

class BestFirstSearchTest {
    BestFirstSearch bfs = new BestFirstSearch(); // creater search algorithm to test

    // class methods:
    @Test
    void getName() {
        assertEquals("Best First Search", bfs.getName()); // test it returns the right name
    }

    @Test
    void solve() {
        int[][] armaze = {{0, 1, 1},
                          {0, 0, 0},
                          {0, 1, 0}}; // maze with no solution
        Maze mazeobj = new Maze(new Position(0, 0), new Position(2, 2), armaze); // create maze object
        SearchableMaze maze = new SearchableMaze(mazeobj); // create searchable maze
        Solution sol = bfs.solve(maze); // get solution
        assertNotNull(sol); // test it's not returning null
        ArrayList<AState> solutionPath = sol.getSolutionPath(); // get solution path
        // assert its getting the right path
        assertEquals("{0,0}", solutionPath.get(0).toString());
        assertEquals("{1,1}", solutionPath.get(1).toString());
        assertEquals("{2,2}", solutionPath.get(2).toString());
    }

    @Test
    void getDataStructure() {
        assertInstanceOf(PriorityQueue.class, bfs.getDataStructure()); // test it returns right data structure
    }

    // test that the program is not throwing exceptions when solution is null
    @Test
    void testNullValues() {
        int[][] armaze = {{0, 1, 1}, {0, 1, 0}, {1,1,0}}; // maze with no solution
        Maze mazeobj = new Maze(new Position(0, 0), new Position(2, 2), armaze); // create maze object
        SearchableMaze maze = new SearchableMaze(mazeobj); // create searchable maze
        Solution sol = bfs.solve(maze); // get solution
        assertNotNull(sol); // test it's not returning null
        ArrayList<AState> solutionPath = sol.getSolutionPath(); // get solution path
        assertEquals("No solution found", solutionPath.get(0).toString()); // assert it's returning valid value
    }

}