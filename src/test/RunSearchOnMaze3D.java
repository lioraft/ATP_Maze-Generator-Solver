package test;

import algorithms.maze3D.*;
import algorithms.search.*;

import java.util.ArrayList;

public class RunSearchOnMaze3D {
    public static void main(String[] args) {
        // first maze
        IMazeGenerator3D mg = new MyMaze3DGenerator();
        Maze3D maze = mg.generate(10, 10, 10);
        SearchableMaze3D searchableMaze = new SearchableMaze3D(maze);
        solveProblem(searchableMaze, new BreadthFirstSearch());

        // second maze - the maze from the example in the project instructions
        int[][][] mazeArr = {
                {
                        {0, 0, 1, 0, 1},
                        {0, 1, 1, 1, 0},
                        {0, 1, 1, 0, 0}
                },
                {
                        {1, 1, 1, 0, 1},
                        {1, 0, 0, 1, 0},
                        {0, 0, 1, 0, 1}
                },
                {
                        {1, 1, 1, 0, 1},
                        {1, 1, 0, 0, 0},
                        {1, 1, 1, 0, 1}
                }
        };

        Maze3D maze2 = new Maze3D(new Position3D(0, 0, 0), new Position3D(0, 2, 4), mazeArr);
        SearchableMaze3D searchableMaze2 = new SearchableMaze3D(maze2);
        solveProblem(searchableMaze2, new BreadthFirstSearch());

    }
    private static void solveProblem(ISearchable domain, ISearchingAlgorithm
            searcher) {
//Solve a searching problem with a searcher
        Solution solution = searcher.solve(domain);
        System.out.printf("'%s' algorithm - nodes evaluated: %s%n", searcher.getName(), searcher.getNumberOfNodesEvaluated());
//Printing Solution Path
            ArrayList<AState> solutionPath = solution.getSolutionPath();
            System.out.println("Solution path:");
            for (int i = 0; i < solutionPath.size(); i++) {
                System.out.printf("%s. %s%n",i,solutionPath.get(i));
        }
    }
}
