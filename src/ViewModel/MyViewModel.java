package ViewModel;

import Model.MyModel;

/*
    * This class is the ViewModel layer of the MVVM architecture.
    * It is responsible for the presentation logic of the application.
    * implemented as a singleton.
 */
public class MyViewModel {
    static MyViewModel instance = null;
    MyModel model;

    private MyViewModel() {
        model = new MyModel();
        model.startServers();
    }

    public static MyViewModel getInstance() {
        if (instance == null) {
            instance = new MyViewModel();
        }
        return instance;
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

    public void setPlayerPosition(int row, int col) {
        model.setCharacterPosition(row, col);
    }

    public int getPlayerRow() {
        return model.getCurrentRow();
    }

    public int getPlayerCol() {
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

    public boolean isPassage(int row, int col) {
        return model.isPassage(row, col);
    }

    public boolean saveMaze(String fileName) {
        return model.saveCurrentMazeToFile(fileName);
    }

    public boolean loadMaze(String fileName) {
        return model.loadCurrentMazeFromFile(fileName);
    }

}
