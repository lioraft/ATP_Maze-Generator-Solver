package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public interface IModel {

    void generateGame(int width, int height); // function for generating game board by dimensions
    void solveGame(); // function to solve the game
    void setCharacterPosition(int row, int col); // function to save current character position in the game
}
