package algorithms.maze3D;
import algorithms.search.AState;
import algorithms.search.ISearchable;
import algorithms.search.MazeState;

import java.util.ArrayList;

public class SearchableMaze3D implements ISearchable {
    private Maze3D maze;
    public SearchableMaze3D(Maze3D maze) {
        this.maze = maze;
    }

    @Override
    public AState getStartState() {
        return new Maze3DState(maze.getStartPosition(), 0, null); //return the start state of the maze.
    }

    @Override
    public AState getGoalState() {
        return new Maze3DState(maze.getGoalPosition(), 0, null); //return the goal state of the maze.
    }

    // function that returns all the possible states that can be reached from a given state
    @Override
    public ArrayList<AState> getAllSuccessors(AState state) {
        // create new list of successor states
        ArrayList<AState> successors = new ArrayList<>();
        // get row and column of maze state
        Position3D pos = ((Maze3DState) state).getPosition();
        int row = pos.getRowIndex();
        int col = pos.getColumnIndex();
        int depth = pos.getDepthIndex();

        // if can move up
        if (maze.isValidPassage(row - 1, col, depth)) {
            Position3D up_pos = new Position3D(row - 1, col, depth);
            Maze3DState up = new Maze3DState(up_pos, state.getCost() + 1, (Maze3DState) state);
            successors.add(up);
        }

        // if can move right
        if (maze.isValidPassage(row, col + 1, depth)) {
            Position3D right_pos = new Position3D(row, col+1, depth);
            Maze3DState right = new Maze3DState(right_pos, state.getCost() + 1, (Maze3DState) state);
            successors.add(right);
        }

        // if can move down
        if (maze.isValidPassage(row + 1, col, depth)) {
            Position3D down_pos = new Position3D(row + 1, col, depth);
            Maze3DState down = new Maze3DState(down_pos, state.getCost() + 1, (Maze3DState) state);
            successors.add(down);
        }

        // if can move left
        if (maze.isValidPassage(row, col - 1, depth)) {
            Position3D left_pos = new Position3D(row, col - 1, depth);
            Maze3DState left = new Maze3DState(left_pos, state.getCost() + 1, (Maze3DState) state);
            successors.add(left);
        }
        // if can move inside
        if (maze.isValidPassage(row, col, depth-1)) {
            Position3D in_pos = new Position3D(row, col, depth-1);
            Maze3DState in = new Maze3DState(in_pos, state.getCost() + 1, (Maze3DState) state);
            successors.add(in);
        }
        // if can move outside
        if (maze.isValidPassage(row, col, depth+1)) {
            Position3D out_pos = new Position3D(row, col, depth+1);
            Maze3DState out = new Maze3DState(out_pos, state.getCost() + 1, (Maze3DState) state);
            successors.add(out);
        }
        // return all list of possible moves
        return successors;
    }

    @Override
    public boolean FoundSolution(AState state) {
        //check if the given state is the goal state.
        return state.equals(getGoalState());
    }
}