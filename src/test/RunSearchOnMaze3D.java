package test;

import algorithms.maze3D.*;
import algorithms.search.*;

import java.util.ArrayList;

public class RunSearchOnMaze3D {
    public static void main(String[] args) {
        /*
        IMazeGenerator3D mg = new MyMaze3DGenerator();
        Maze3D maze = mg.generate(3, 3, 3);
        SearchableMaze3D searchableMaze = new SearchableMaze3D(maze);
        solveProblem(searchableMaze, new BreadthFirstSearch()); */

        int[][][] mazeAr = {
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

        Maze3D maze = new Maze3D(new Position3D(0, 0, 0), new Position3D(3, 3, 4), mazeAr);
        SearchableMaze3D searchableMaze = new SearchableMaze3D(maze);
        solveProblem(searchableMaze, new BreadthFirstSearch());
    }
    private static void solveProblem(ISearchable domain, ISearchingAlgorithm
            searcher) {
//Solve a searching problem with a searcher
        Solution solution = searcher.solve(domain);
        System.out.printf("'%s' algorithm - nodes evaluated: %s%n", searcher.getName(), searcher.getNumberOfNodesEvaluated());
//Printing Solution Path
        if (solution.getSolutionPath() != null) {
            ArrayList<AState> solutionPath = solution.getSolutionPath();
            System.out.println("Solution path:");
            for (int i = 0; i < solutionPath.size(); i++) {
                System.out.printf("%s. %s%n",i,solutionPath.get(i));
            }
        }
    }
}
