package ViewModel;

import Model.MyModel;
import View.MyViewController;
import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/*
    * This class is the ViewModel layer of the MVVM architecture.
    * It is responsible for the presentation logic of the application.
 */
public class MyViewModel {
    MyModel model;

    public MyViewModel() {
        model = new MyModel();
        model.startServers();
    }

    public void generateMaze(int width, int height) {
        model.generateGame(width, height);
    }

    public int getMazeRows() {
        return model.getMazeRows();
    }

    public int getMazeCols() {
        return model.getMazeCols();
    }

    public int[] getNextStepInSolution() {
        return model.getHint();
    }

    public void setNewPosition(int row, int col) {
        model.setCharacterPosition(row, col);
    }

    public int getCharacterPositionRow() {
        return model.getCurrentRow();
    }

    public int getCharacterPositionColumn() {
        return model.getCurrentCol();
    }

    public int getGoalColumn() {
        return model.getGoalCol();
    }

    public int getStartColumn() {
        return model.getStartingCol();
    }

    public int[][] getMaze() {
        return model.getCurrentMaze();
    }

    public void exit() {
        model.shutDownServers();
    }

}
