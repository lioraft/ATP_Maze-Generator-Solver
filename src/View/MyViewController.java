package View;

import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/*
 * This class is the ViewModel layer of the MVVM architecture.
 * It is responsible for the presentation logic of the application.
 */

public class MyViewController extends Application implements IView {
    AnchorPane mainScene;
    @FXML
    ComboBox<Integer> width;
    @FXML
    ComboBox<Integer> height;
    AnchorPane mazeDisplayer;
    MyViewModel viewModel;
    int sizeOfCell = 0;


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // initialize fxml loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        mainScene = loader.load(); // load main anchor pane
        // load background image
        Image backgroundImage = new Image(getClass().getResource("/resources/spongebob-jellyfish-1000.jpg").toExternalForm());
        // set background image
        mainScene.setStyle("-fx-background-image: url('" + backgroundImage.getUrl() + "'); " + "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
        // set title
        primaryStage.setTitle("Maze Game");
        // initialize view model
        viewModel = new MyViewModel();
        // initialize menu bar
        MenuBar menuBar = new MenuBar();
        // initialize file menu
        Menu fileMenu = new Menu("File");
        // initialize file menu items
        MenuItem newGame = new MenuItem("New");
        MenuItem saveGame = new MenuItem("Save");
        MenuItem loadGame = new MenuItem("Load");
        // add file menu items to file menu
        fileMenu.getItems().addAll(newGame, saveGame, loadGame);
        // initialize options menu
        Menu optionsMenu = new Menu("Options");
        // initialize options menu items
        MenuItem properties = new MenuItem("Properties");
        // add options menu items to options menu
        optionsMenu.getItems().addAll(properties);
        // initialize exit menu
        Menu exitMenu = new Menu("Exit");
        // initialize exit menu items
        MenuItem exit = new MenuItem("Exit");
        // add exit menu items to exit menu
        exitMenu.getItems().addAll(exit);
        // initialize help menu
        Menu helpMenu = new Menu("Help");
        // initialize help menu items
        MenuItem help = new MenuItem("Help");
        // add help menu items to help menu
        helpMenu.getItems().addAll(help);
        // initialize about menu
        Menu aboutMenu = new Menu("About");
        // initialize about menu items
        MenuItem about = new MenuItem("About");
        // add about menu items to about menu
        aboutMenu.getItems().addAll(about);
        // set menu bar
        menuBar.getMenus().addAll(fileMenu, optionsMenu, exitMenu, helpMenu, aboutMenu);
        // set width and height comboboxes
        width = (ComboBox<Integer>) mainScene.lookup("#width");
        height = (ComboBox<Integer>) mainScene.lookup("#height");
        // set width and height comboboxes values
        for (int i = 10; i <= 100; i += 10) {
            width.getItems().add(i);
            height.getItems().add(i);
        }
        // initialize start button
        // get the button from the FXML file
        Button start_button = (Button) mainScene.lookup("#start_button");
        start_button.setStyle("-fx-background-color: transparent;");
        // create an image
        Image image = new Image(getClass().getResource("/resources/start-button-100.png").toExternalForm());
        // convert the image to an ImageView
        ImageView imageView = new ImageView(image);
        // set the ImageView as the graphic of the button
        start_button.setGraphic(imageView);
        // Set the event handler for the button
        start_button.setOnAction(this::handleStartButtonClick);
        // set scene
        Scene scene = new Scene(mainScene, 1000.0, 1000.0);
        primaryStage.setScene(scene);
        // show stage
        primaryStage.show();

    }

    public void handleStartButtonClick(ActionEvent actionEvent) {
        try {
            Stage primaryStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // load the FXML file for the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mazeDisplayer.fxml"));
            mazeDisplayer = loader.load();

            // set background color
            mazeDisplayer.setStyle("-fx-background-color: #6dcff6;");

            // get input dimensions from user
            int mazeWidth = width.getValue();
            int mazeHeight = height.getValue();

            // generate maze
            viewModel.generateMaze(mazeWidth, mazeHeight);

            // get current maze
            int[][] maze = viewModel.getMaze();

            // create maze in sizes and display it
            GridPane gridPane =(GridPane) mazeDisplayer.lookup("#mazeGrid");

            gridPane.setHgap(0); // remove horizontal gap between cells
            gridPane.setVgap(0); // remove vertical gap between cells
            gridPane.setPadding(new Insets(30));
            gridPane.setAlignment(Pos.TOP_LEFT);

            // create the maze
            for (int i = 0; i < mazeWidth; i++) {
                for (int j = 0; j < mazeHeight; j++) {
                    Rectangle cell = createCell(maze[i][j], mazeWidth, mazeHeight);
                    gridPane.add(cell, j, i);
                }
            }

            // set player position
            int playerCol = viewModel.getStartColumn();
            setPlayerPosition(0, playerCol, sizeOfCell);
            // set goal position
            int goalCol = viewModel.getGoalColumn();
            setExitItemPosition(mazeHeight - 1, goalCol, sizeOfCell);


            // set the constraints for the GridPane to fit within the AnchorPane
            gridPane.setMaxSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);
            gridPane.setPrefSize(Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE);

            AnchorPane.setTopAnchor(gridPane, 0.0);
            AnchorPane.setBottomAnchor(gridPane, 0.0);
            AnchorPane.setLeftAnchor(gridPane, 0.0);
            AnchorPane.setRightAnchor(gridPane, 0.0);

            // create a new scene with the loaded FXML content
            Scene mazeDisplayScene = new Scene(mazeDisplayer, 1000.0, 1000.0);

            // set the new scene as the scene of the primary stage
            primaryStage.setScene(mazeDisplayScene);

            mazeDisplayer.requestFocus(); // set focus on the maze

            // while solution is not found, keep playing
            //boolean solutionFound = false;
            //while(!solutionFound) {
                mazeDisplayer.setOnKeyPressed(event -> {
                    KeyCode keyCode = event.getCode();
                    if (keyCode.isKeypadKey()) {
                        String keyText = keyCode.getName();
                        handlePlayMove(keyText);
                    }
                });
          //  }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // helper method to create a cell in the maze:
    // 0 - passage as transparent cell, has no walls
    // 1 - wall as a cell with a thin border
    private Rectangle createCell(int value, int mazeWidth, int mazeHeight) {
        int cellSize = Math.min(900 / mazeWidth, 900 / mazeHeight); // get minimum cell size so it fits the screen
        Rectangle cell = new Rectangle(cellSize, cellSize); // adjust the size of the cell
        cell.setFill(value == 0 ? Color.WHITE : Color.TRANSPARENT); // set the cell background color: if passage, white. if wall, transparent

        // set the border width to 0 for all cells
        cell.setStrokeWidth(0);

        // save the size of the cell for later use
        sizeOfCell = cellSize;

        return cell;
    }

    // function that takes the position of the player and sets the picture from resources in this position in the maze (which is the gridpane)
    private void setPlayerPosition(int row, int col, int size) {
        GridPane gridPane = (GridPane) mazeDisplayer.lookup("#mazeGrid");
        ImageView player = new ImageView(new Image(getClass().getResource("/resources/spongebob.png").toExternalForm()));
        player.setFitWidth(size); // set width
        player.setFitHeight(size); // set height
        gridPane.add(player, row, col);
    }

    // function that takes the position of the exit and sets the picture from resources in this position in the maze (which is the gridpane)
    private void setExitItemPosition(int row, int col, int size) {
        GridPane gridPane = (GridPane) mazeDisplayer.lookup("#mazeGrid");
        ImageView exit = new ImageView(new Image(getClass().getResource("/resources/jellyfish.png").toExternalForm()));
        exit.setFitWidth(size); // set width
        exit.setFitHeight(size); // set height
        gridPane.add(exit, row, col);
    }

    // function that check if cell player wants to move to is a passage
    // if it is, move player to this cell
    // if it isn't, do nothing
    public void handlePlayMove(String activator) {
        switch (activator) {
            case "1": {
                if (viewModel.isPassage(viewModel.getPlayerRow() -1, viewModel.getPlayerCol() - 1)) {
                    moveOnBoard(viewModel.getPlayerRow() - 1, viewModel.getPlayerCol() - 1, viewModel.getPlayerRow(), viewModel.getPlayerCol());
                    viewModel.setPlayerPosition(viewModel.getPlayerRow() - 1, viewModel.getPlayerCol() - 1);
                }
                break;
            }
            case "2": {
                if (viewModel.isPassage(viewModel.getPlayerRow() -1, viewModel.getPlayerCol())) {
                    moveOnBoard(viewModel.getPlayerRow() - 1, viewModel.getPlayerCol(), viewModel.getPlayerRow(), viewModel.getPlayerCol());
                    viewModel.setPlayerPosition(viewModel.getPlayerRow() - 1, viewModel.getPlayerCol());
                }
                break;
            }
            case "3": {
                if (viewModel.isPassage(viewModel.getPlayerRow() -1, viewModel.getPlayerCol() + 1)) {
                    moveOnBoard(viewModel.getPlayerRow() - 1, viewModel.getPlayerCol() + 1, viewModel.getPlayerRow(), viewModel.getPlayerCol());
                    viewModel.setPlayerPosition(viewModel.getPlayerRow() - 1, viewModel.getPlayerCol() + 1);
                }
                break;
            }
            case "4": {
                if (viewModel.isPassage(viewModel.getPlayerRow(), viewModel.getPlayerCol() - 1)) {
                    moveOnBoard(viewModel.getPlayerRow(), viewModel.getPlayerCol() - 1, viewModel.getPlayerRow(), viewModel.getPlayerCol());
                    viewModel.setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol() - 1);
                }
                break;
            }
            case "6": {
                if (viewModel.isPassage(viewModel.getPlayerRow(), viewModel.getPlayerCol() + 1)) {
                    moveOnBoard(viewModel.getPlayerRow(), viewModel.getPlayerCol() + 1, viewModel.getPlayerRow(), viewModel.getPlayerCol());
                    viewModel.setPlayerPosition(viewModel.getPlayerRow(), viewModel.getPlayerCol() + 1);
                }
                break;
            }
            case "7": {
                if (viewModel.isPassage(viewModel.getPlayerRow() + 1, viewModel.getPlayerCol() - 1)) {
                    moveOnBoard(viewModel.getPlayerRow() + 1, viewModel.getPlayerCol() - 1, viewModel.getPlayerRow(), viewModel.getPlayerCol());
                    viewModel.setPlayerPosition(viewModel.getPlayerRow() + 1, viewModel.getPlayerCol() - 1);
                }
                break;
            }
            case "8": {
                if (viewModel.isPassage(viewModel.getPlayerRow() + 1, viewModel.getPlayerCol())) {
                    moveOnBoard(viewModel.getPlayerRow() + 1, viewModel.getPlayerCol(), viewModel.getPlayerRow(), viewModel.getPlayerCol());
                    viewModel.setPlayerPosition(viewModel.getPlayerRow() + 1, viewModel.getPlayerCol());
                }
                break;
            }
            case "9": {
                if (viewModel.isPassage(viewModel.getPlayerRow() + 1, viewModel.getPlayerCol() + 1)) {
                    moveOnBoard(viewModel.getPlayerRow() + 1, viewModel.getPlayerCol() + 1, viewModel.getPlayerRow(), viewModel.getPlayerCol());
                    viewModel.setPlayerPosition(viewModel.getPlayerRow() + 1, viewModel.getPlayerCol() + 1);
                }
                break;
            }
            default: {
                // Handle unrecognized key
                break;
            }
        }
    }

    // function that takes the position of the player and sets the picture from resources in this position in the maze (which is the gridpane)
    public void moveOnBoard(int destRow, int destCol, int sourceRow, int sourceCol) {
        GridPane gridPane = (GridPane) mazeDisplayer.lookup("#mazeGrid");
        ImageView player = new ImageView(new Image(getClass().getResource("/resources/spongebob.png").toExternalForm()));
        player.setFitWidth(sizeOfCell); // set width
        player.setFitHeight(sizeOfCell); // set height
        gridPane.add(player, destCol, destRow); // swap destRow and destCol
        gridPane.getChildren().remove(gridPane.getChildren().size() - 1);
    }

}
