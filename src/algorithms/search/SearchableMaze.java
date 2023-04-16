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

    //set start state
    public void setStartState(AState startState) {
        this.getStartState().setCost(startState.getCost());
        this.getStartState().setCameFrom((MazeState) startState.getCameFrom());
    }

    @Override
    public AState getGoalState() {
        return new MazeState(maze.getGoalPosition(), 0, null); //return the goal state of the maze.
    }

    // function that returns all the possible states that can be reached from a given state
    @Override
    public ArrayList<AState> getAllSuccessors(AState state) {
        // create new list of successor states
        ArrayList<AState> successors = new ArrayList<>();
        // get row and column of maze state
        Position pos = ((MazeState) state).getPosition();
        int row = pos.getRowIndex();
        int col = pos.getColumnIndex();

        // if can move up
        if (maze.isValidPassage(row - 1, col)) {
            Position up_pos = new Position(row - 1, col);
            MazeState up = new MazeState(up_pos, state.getCost() + 10, (MazeState) state);
            successors.add(up);
            // if can move diagonal up left
            if (maze.isValidPassage(row - 1, col - 1)) {
                Position upleft_pos = new Position(row - 1, col - 1);
                MazeState upleft = new MazeState(upleft_pos, state.getCost() + 15, (MazeState) state);
                successors.add(upleft);
            }
            // if can move diagonal up right
            if (maze.isValidPassage(row - 1, col + 1)) {
                Position upright_pos = new Position(row - 1, col + 1);
                MazeState upright = new MazeState(upright_pos, state.getCost() + 15, (MazeState) state);
                successors.add(upright);
            }
        }

        // if can move right
        if (maze.isValidPassage(row, col + 1)) {
            Position right_pos = new Position(row, col + 1);
            MazeState right = new MazeState(right_pos, state.getCost() + 10, (MazeState) state);
            successors.add(right);
            // if can move diagonal right up, and not added to list yet
            if (maze.isValidPassage(row - 1, col + 1)) {
                Position rightup_pos = new Position(row - 1, col + 1);
                MazeState rightup = new MazeState(rightup_pos, state.getCost() + 15, (MazeState) state);
                if (!successors.contains(rightup))
                    successors.add(rightup);
            }
            // if can move diagonal right down
            if (maze.isValidPassage(row + 1, col + 1)) {
                Position rightdown_pos = new Position(row + 1, col + 1);
                MazeState rightdown = new MazeState(rightdown_pos, state.getCost() + 15, (MazeState) state);
                successors.add(rightdown);
            }
        }

            // if can move down
            if (maze.isValidPassage(row + 1, col)) {
                Position down_pos = new Position(row + 1, col);
                MazeState down = new MazeState(down_pos, state.getCost() + 10, (MazeState) state);
                successors.add(down);
                // if can move diagonal down right, and not added to list yet
                if (maze.isValidPassage(row + 1, col + 1)) {
                    Position downright_pos = new Position(row + 1, col + 1);
                    MazeState downright = new MazeState(downright_pos, state.getCost() + 15, (MazeState) state);
                    if (!successors.contains(downright))
                        successors.add(downright);
                }
                // if can move diagonal down left
                if (maze.isValidPassage(row + 1, col - 1)) {
                    Position downleft_pos = new Position(row + 1, col - 1);
                    MazeState downleft = new MazeState(downleft_pos, state.getCost() + 15, (MazeState) state);
                    successors.add(downleft);
                }
            }

        // if can move left
        if (maze.isValidPassage(row, col - 1)) {
            Position left_pos = new Position(row, col - 1);
            MazeState left = new MazeState(left_pos, state.getCost() + 10, (MazeState) state);
            successors.add(left);
            // if can move diagonal left up, and not added to list yet
            if (maze.isValidPassage(row - 1, col - 1)) {
                Position leftup_pos = new Position(row - 1, col - 1);
                MazeState leftup = new MazeState(leftup_pos, state.getCost() + 15, (MazeState) state);
                if (!successors.contains(leftup))
                    successors.add(leftup);
            }
            // if can move diagonal left down, and not added to list yet
            if (maze.isValidPassage(row + 1, col - 1)) {
                Position leftdown_pos = new Position(row + 1, col - 1);
                MazeState leftdown = new MazeState(leftdown_pos, state.getCost() + 15, (MazeState) state);
                if (!successors.contains(leftdown))
                    successors.add(leftdown);
            }
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