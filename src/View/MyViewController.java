package View;

import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.io.IOException;

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
    int mazeWidth = 0;
    int mazeHeight = 0;
    AnchorPane mazeDisplayer;
    MyViewModel viewModel;
    String css;

    GraphicsContext gc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // initialize view model
        viewModel = MyViewModel.getInstance();
        // set main scene
        setMainScene(primaryStage);
    }

    public void setMainScene(Stage primaryStage) throws IOException {
        // initialize fxml loader
        FXMLLoader loader = new FXMLLoader(getClass().getResource("MyView.fxml"));
        mainScene = loader.load(); // load main anchor pane
        // load css file
        css = getClass().getResource("/stylesheet.css").toExternalForm();
        // load background image
        Image backgroundImage = new Image(getClass().getResource("/spongebob-jellyfish-1000.jpg").toExternalForm());
        // set background image
        mainScene.setStyle("-fx-background-image: url('" + backgroundImage.getUrl() + "'); " + "-fx-background-position: center center; " + "-fx-background-repeat: stretch;");
        // set title
        primaryStage.setTitle("Maze Game");
        // get main title and set design
        Label mainTitle = (Label) mainScene.lookup("#mainTitle");
        mainTitle.getStyleClass().add("title");
        // get labels and set designs from css
        Label widthLabel = (Label) mainScene.lookup("#widthTitle");
        widthLabel.getStyleClass().add("sub-label");
        Label heightLabel = (Label) mainScene.lookup("#heightTitle");
        heightLabel.getStyleClass().add("sub-label");
        // initialize menu bar
        MenuBar menuBar = (MenuBar) mainScene.lookup("#menuBar");
        // initialize file menu
        Menu fileMenu = menuBar.getMenus().get(0);
        // initialize new game item
        MenuItem newGame = fileMenu.getItems().get(0);
        // handle new game menu item click
        newGame.setOnAction(this::handleNewGameButtonClick);
        MenuItem saveGame = fileMenu.getItems().get(1);
        // handle save game menu item click
        saveGame.setOnAction(this::handleSaveGameButtonClick);
        MenuItem loadGame = fileMenu.getItems().get(2);
        // initialize options menu
        Menu optionsMenu =  menuBar.getMenus().get(1);
        // initialize options menu items
        MenuItem properties = optionsMenu.getItems().get(0);
        // initialize exit menu
        Menu exitMenu = menuBar.getMenus().get(2);
        // initialize exit menu item
        MenuItem exit = exitMenu.getItems().get(0);
        // handle exit menu item click
        exit.setOnAction(this::handleExitButtonClick);
        // initialize help menu
        Menu helpMenu = menuBar.getMenus().get(3);
        // initialize help menu items
        MenuItem help = helpMenu.getItems().get(0);
        // initialize about menu
        Menu aboutMenu = menuBar.getMenus().get(4);
        // initialize about menu items
        MenuItem about = aboutMenu.getItems().get(0);
        // set designs for menu bar
        menuBar.getStyleClass().add("menu-bar");
        // set width and height comboboxes
        width = (ComboBox<Integer>) mainScene.lookup("#width");
        height = (ComboBox<Integer>) mainScene.lookup("#height");
        // set width and height comboboxes values
        for (int i = 10; i <= 100; i += 10) {
            width.getItems().add(i);
            height.getItems().add(i);
        }
        /*
        // add more width and height values
        for (int i = 200; i <= 1000; i += 100) {
            width.getItems().add(i);
            height.getItems().add(i);
        }*/
        // initialize start button
        // get the button from the FXML file
        Button start_button = (Button) mainScene.lookup("#start_button");
        start_button.setStyle("-fx-background-color: transparent;");
        // create an image
        Image image = new Image(getClass().getResource("/start-button-100.png").toExternalForm());
        // convert the image to an ImageView
        ImageView imageView = new ImageView(image);
        // set the ImageView as the graphic of the button
        start_button.setGraphic(imageView);
        // Set the event handler for the button
        start_button.setOnAction(this::handleStartButtonClick);
        // set scene
        Scene scene = new Scene(mainScene, 1000.0, 1000.0);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
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
            // set css
            // get input dimensions from user
            if (width.getValue() == null || height.getValue() == null) {
                throw new Exception();
            }
            // get input dimensions from user
            mazeWidth = width.getValue();
            mazeHeight = height.getValue();

            // generate maze
            viewModel.generateMaze(mazeWidth, mazeHeight);

            // get current maze
            int[][] maze = viewModel.getMaze();

            // get canvas in order to create maze in sizes and display it
            Canvas mazeCanvas = (Canvas) mazeDisplayer.lookup("#mazeCanvas");
            gc = mazeCanvas.getGraphicsContext2D();
            drawMaze(maze);


            // set player position
            int playerCol = viewModel.getStartColumn();
            // get image from resources
            Image spongebob = new Image(getClass().getResource("/spongebob.png").toExternalForm());
            drawPlayer(spongebob, 0, playerCol, maze);
            // set goal position
            int goalCol = viewModel.getGoalColumn();
            // get image from resources
            Image jellyfish = new Image(getClass().getResource("/jellyfish.png").toExternalForm());
            drawPlayer(jellyfish,maze.length-1, goalCol, maze);

            // Create the hint button
            Button hintButton = (Button) mazeDisplayer.lookup("#hint_button");
            hintButton.getStyleClass().add("hint-button");
            hintButton.setOnAction(this::handleHintButtonClick);

            // Create the solve button
            Button solveButton = (Button) mazeDisplayer.lookup("#solve_button");
            solveButton.getStyleClass().add("hint-button");
            solveButton.setOnAction(this::handleSolveButtonClick);


            // create a new scene with the loaded FXML content
            Scene mazeDisplayScene = new Scene(mazeDisplayer, 1000.0, 1000.0);

            // load css file
            mazeDisplayScene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

            // set the new scene as the scene of the primary stage
            primaryStage.setScene(mazeDisplayScene);

            mazeDisplayer.requestFocus(); // set focus on the maze

            mazeDisplayer.setOnKeyPressed(event -> {
                KeyCode keyCode = event.getCode();
                if (keyCode.isLetterKey()) {
                    handlePlayMove(keyCode.getName().toLowerCase(), spongebob);
                }
            });

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please choose width and height");
            setAlertCSS(alert, "/attention.png");
            alert.showAndWait();
        }
    }

    // helper function to set alert design
    public void setAlertCSS(Alert alert, String imageName) {
        // add css to alert
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
        dialogPane.getStyleClass().add("alert");
        // set header and header text to null
        alert.setHeaderText(null);
        dialogPane.setHeaderText(null);
        // load the exit image
        Image image = new Image(getClass().getResource(imageName).toExternalForm());
        // create the ImageView
        ImageView imageView = new ImageView(image);
        // set the ImageView as the graphic for the DialogPane
        dialogPane.setGraphic(imageView);
    }

    // handle new game menu item click
    public void handleNewGameButtonClick(ActionEvent actionEvent) {
        // Get the source MenuItem
        MenuItem menuItem = (MenuItem) actionEvent.getSource();

        // Find the parent MenuBar
        MenuBar menuBar = null;
        Parent parent = menuItem.getParentPopup().getOwnerNode().getParent();
        while (parent != null && !(parent instanceof MenuBar)) {
            parent = parent.getParent();
        }
        if (parent instanceof MenuBar) {
            menuBar = (MenuBar) parent;
        }

        // Find the parent Stage
        Stage primaryStage = null;
        Scene scene = menuBar != null ? menuBar.getScene() : null;
        if (scene != null) {
            Window window = scene.getWindow();
            if (window instanceof Stage) {
                primaryStage = (Stage) window;
            }
        }

        try {
            if (primaryStage != null) {
                setMainScene(primaryStage);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // handle save menu item click
    public void handleSaveGameButtonClick(ActionEvent actionEvent) {
        viewModel = MyViewModel.getInstance();
        if (viewModel != null) {
            // open window for asking for file name
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Maze");
            fileChooser.setInitialFileName("savedMaze");
            File file = fileChooser.showSaveDialog(new Stage());
            // get string of file name
            String fileName = file.getName();
            if (fileName != null && !fileName.equals("")) {
                // if the file name is valid, try to save the maze
                boolean canSave = viewModel.saveMaze(fileName);
                if (!canSave) {
                    // if game didn't start, alert user that there isn't a maze to save
                    Alert alert = new Alert(Alert.AlertType.ERROR, "No maze to save");
                    setAlertCSS(alert, "/attention.png");
                    alert.showAndWait();
                } else {
                    // if saved, alert user that the maze was saved successfully
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Maze saved successfully");
                    setAlertCSS(alert, "/success.png");
                    alert.showAndWait();
                }
            }
        } else {
            // Alert the user that the maze is not available to save
            Alert alert = new Alert(Alert.AlertType.ERROR, "Maze is not available to save yet. Please wait a minute before trying again");
            setAlertCSS(alert, "/attention.png");
            alert.showAndWait();
        }
    }



    // Handle the hint button click event
    public void handleHintButtonClick(ActionEvent actionEvent) {
        // get the next step in the solution
        int[] nextStep = viewModel.getNextStepInSolution();
        // update the maze display accordingly
        markCell(nextStep[0], nextStep[1]);
    }

    // handle the solve button click event
    public void handleSolveButtonClick(ActionEvent actionEvent) {
        int[] nextStep = {0, 0};
        while (nextStep[0] != -1 && nextStep[1] != -1 ) {
            // get the next step in the solution
            nextStep = viewModel.getNextStepInSolution();
            // update the maze display accordingly
            markCell(nextStep[0], nextStep[1]);
        }
    }

    // handle the exit button click event
    public void handleExitButtonClick(ActionEvent actionEvent) {
        // alert user that the program is about to exit
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        setAlertCSS(alert, "/exit.png");
        // show alert
        alert.showAndWait();
        // if user clicked yes, exit the program
        if (alert.getResult() == ButtonType.YES)
            Platform.exit();
        // if user clicked no, do nothing
        else
            alert.close();
    }

    // function takes in row and column, and fills the cell in the maze with yellow color
    public void markCell(int row, int col) {
        double cellWidth = gc.getCanvas().getWidth() / viewModel.getMaze()[0].length;
        double cellHeight = gc.getCanvas().getHeight() / viewModel.getMaze().length;

        double x = col * cellWidth;
        double y = row * cellHeight;

        gc.setFill(Color.YELLOW);
        gc.fillRect(x, y, cellWidth, cellHeight);
    }

    // helper method to create a cell in the maze:
    // 0 - passage
    // 1 - wall
    private void drawMaze(int[][] maze) {
        double cellWidth = gc.getCanvas().getWidth() / maze[0].length;
        double cellHeight = gc.getCanvas().getHeight() / maze.length;

        for (int row = 0; row < maze.length; row++) {
            for (int col = 0; col < maze[row].length; col++) {
                double x = col * cellWidth;
                double y = row * cellHeight;

                if (maze[row][col] == 1) {
                    gc.setFill(Color.TRANSPARENT);
                    gc.fillRect(x, y, cellWidth, cellHeight);
                } else {
                    gc.setFill(Color.WHITE);
                    gc.fillRect(x, y, cellWidth, cellHeight);
                }
            }
        }
    }

    // function that takes the position of the player and sets the picture from resources in this position in the maze (which is the gridpane)
    private void drawPlayer(Image playerImage, int row, int col, int[][] maze) {
        double cellWidth = gc.getCanvas().getWidth() / maze[0].length;
        double cellHeight = gc.getCanvas().getHeight() / maze.length;

        double x = col * cellWidth;
        double y = row * cellHeight;

        if (playerImage != null) {
            gc.drawImage(playerImage, x, y, cellWidth, cellHeight);
        } else {
            gc.setFill(Color.BLUE);
            gc.fillRect(x, y, cellWidth, cellHeight);
        }
    }

    // function that check if cell player wants to move to is a passage
    // if it is, move player to this cell
    // if it isn't, do nothing
    public void handlePlayMove(String key,Image playerImage) {
        int[][] maze = viewModel.getMaze();

        int currentRow = viewModel.getPlayerRow();
        int currentCol = viewModel.getPlayerCol();

        int newRow = currentRow;
        int newCol = currentCol;

        switch (key) {
            case "w":
                newRow--;
                break;
            case "q":
                newRow--;
                newCol--;
                break;
            case "s":
                newRow++;
                break;
            case "a":
                newCol--;
                break;
            case "d":
                newCol++;
                break;
            case "e":
                newRow--;
                newCol++;
                break;
            case "x":
                newRow++;
                newCol--;
                break;
            case "z":
                newRow++;
                newCol++;
                break;
            default:
                // Handle unrecognized key
                break;
        }

        if (viewModel.isPassage(newRow, newCol)) {
            viewModel.setPlayerPosition(newRow, newCol);

            double cellWidth = gc.getCanvas().getWidth() / maze[0].length;
            double cellHeight = gc.getCanvas().getHeight() / maze.length;

            // Clear the current cell
            double currentX = currentCol * cellWidth;
            double currentY = currentRow * cellHeight;
            gc.clearRect(currentX, currentY, cellWidth, cellHeight);

            // Redraw the white background in the current cell
            gc.setFill(Color.WHITE);
            gc.fillRect(currentX, currentY, cellWidth, cellHeight);

            // Draw the player in the new position
            drawPlayer(playerImage, newRow, newCol, maze);

            // if player reached goal, show alert
            if (viewModel.getGoalColumn() == viewModel.getPlayerCol() && viewModel.getPlayerRow() == viewModel.getMazeRows()-1) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Maze solved!", ButtonType.OK);
                setAlertCSS(alert, "/win.png");
                alert.show();
            }
        }
    }


}
