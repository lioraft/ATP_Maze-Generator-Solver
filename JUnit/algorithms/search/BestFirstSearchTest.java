package algorithms.search;

import algorithms.mazeGenerators.*;
import algorithms.search.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.PriorityQueue;

import static org.junit.jupiter.api.Assertions.*;

class BestFirstSearchTest {
    BestFirstSearch bfs = new BestFirstSearch(); // create search algorithm to test

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

    // test that the program is not throwing exceptions when there are invalid parameters, but sends no solution found message
    // to the user. if there is solution (path of 0) even with other invalid cells, the program will return the solution
    @Test
    void testInvalidValues() {
        int[][] mazeArr = {{-1, -1, 1}, {0, 1, 0}, {1,1,0}}; // maze with invalid values and no solution
        Maze mazeobj = new Maze(new Position(0, 0), new Position(2, 2), mazeArr); // create maze object
        SearchableMaze maze = new SearchableMaze(mazeobj); // create searchable maze
        Solution sol = bfs.solve(maze); // get solution
        assertNotNull(sol); // test it's not returning null
        ArrayList<AState> solutionPath = sol.getSolutionPath(); // get solution path
        assertEquals("No solution found", solutionPath.get(0).toString()); // assert it's returning no solution

        int[][] mazeArr2 = {{0, -1}, {1, 0}}; // maze with some invalid values that has a solution
        Maze mazeobj2 = new Maze(new Position(0, 0), new Position(1, 1), mazeArr2); // create maze object
        SearchableMaze maze2 = new SearchableMaze(mazeobj2); // create searchable maze
        Solution sol2 = bfs.solve(maze2); // get solution
        assertNotNull(sol2); // test it's not returning null
        ArrayList<AState> solutionPath2 = sol2.getSolutionPath(); // get solution path
        assertEquals("{0,0}", solutionPath2.get(0).toString()); // assert it's returning right position
        assertEquals("{1,1}", solutionPath2.get(1).toString());
    }

    void testOutOfRangePosition() {
        int[][] mazeArr = {{0, 1, 1},
                {0, 0, 0},
                {0, 1, 0}}; // valid maze
        Maze mazeobj = new Maze(new Position(0, 0), new Position(2, 2), mazeArr); // create maze object
        SearchableMaze maze = new SearchableMaze(mazeobj); // create searchable maze

        // create out-of-range position
        Position outOfRange = new Position(4, 4);
        AState outOfRangeState = new MazeState(outOfRange, 0, null);
        maze.setStartState(outOfRangeState); // set the start position to out-of-range
        Solution sol = bfs.solve(maze); // get solution
        assertNotNull(sol); // test it's not returning null
        ArrayList<AState> solutionPath = sol.getSolutionPath(); // get solution path
        assertEquals("No solution found", solutionPath.get(0).toString()); // assert it's returning no solution
    }
}