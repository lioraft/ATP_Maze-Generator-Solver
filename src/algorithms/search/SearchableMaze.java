package algorithms.search;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;

import java.util.ArrayList;

public class SearchableMaze implements ISearchable{
    private Maze maze;

    public SearchableMaze(Maze maze) {
        this.maze = maze;
    }

    @Override
    public AState getStartState() {
        return new MazeState(maze.getStartPosition(), 0, null); //return the start state of the maze.
    }

    @Override
    public AState getGoalState() {
        return new MazeState(maze.getGoalPosition(), 0, null); //return the goal state of the maze.
    }

    @Override
    public ArrayList<AState> getAllSuccessors(AState state) {
        //return all the possible states that we can get from the given state.
        ArrayList<AState> successors = new ArrayList<>();
        Position pos = ((MazeState)state).getPosition();
        int row = pos.getRowIndex();
        int col = pos.getColumnIndex();
        int[][] mazeArr = maze.getMaze();
        if (row > 0 && mazeArr[row - 1][col] == 0) {
            //if the cell above the current cell is not a wall, add it to the successors list.
            Position p = new Position(row - 1, col);
            MazeState ms = new MazeState(p, state.getCost() + 1, (MazeState) state);
            successors.add(ms);
        }
        if (row < mazeArr.length - 1 && mazeArr[row + 1][col] == 0) {
            //if the cell below the current cell is not a wall, add it to the successors list.
            Position p = new Position(row + 1, col);
            MazeState ms = new MazeState(p, state.getCost() + 1, (MazeState) state);
            successors.add(ms);
        }
        if (col > 0 && mazeArr[row][col - 1] == 0) {
            //if the cell to the left of the current cell is not a wall, add it to the successors list.
            Position p = new Position(row, col - 1);
            MazeState ms = new MazeState(p, state.getCost() + 1, (MazeState) state);
            successors.add(ms);
        }
        if (col < mazeArr[0].length - 1 && mazeArr[row][col + 1] == 0) {
            //if the cell to the right of the current cell is not a wall, add it to the successors list.
            Position p = new Position(row, col + 1);
            MazeState ms = new MazeState(p, state.getCost() + 1, (MazeState) state);
            successors.add(ms);
        }
        return successors;

    }

    @Override
    public boolean FoundSolution(AState state) {
        //check if the given state is the goal state.
        if (state.equals(getGoalState()))
            return true;
        return false;
    }
}