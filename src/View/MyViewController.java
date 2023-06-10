package View;

import ViewModel.MyViewModel;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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
    String css;

    GraphicsContext gc;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
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
        // initialize view model
        viewModel = new MyViewModel();
        // initialize menu bar
        MenuBar menuBar = (MenuBar) mainScene.lookup("#menuBar");
        // initialize file menu
        Menu fileMenu = menuBar.getMenus().get(0);
        // initialize file menu items
        MenuItem newGame = fileMenu.getItems().get(0);
        MenuItem saveGame = fileMenu.getItems().get(1);
        MenuItem loadGame = fileMenu.getItems().get(2);
        // initialize options menu
        Menu optionsMenu =  menuBar.getMenus().get(1);
        // initialize options menu items
        MenuItem properties = optionsMenu.getItems().get(0);
        // initialize exit menu
        Menu exitMenu = menuBar.getMenus().get(2);
        // initialize exit menu items
        MenuItem exit = exitMenu.getItems().get(0);
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

            // get input dimensions from user
            int mazeWidth = width.getValue();
            int mazeHeight = height.getValue();

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
            e.printStackTrace();
        }
    }

    // Handle the hint button click event
    public void handleHintButtonClick(ActionEvent actionEvent) {
        // get the next step in the solution
        int[] nextStep = viewModel.getNextStepInSolution();
        // update the maze display accordingly
        markCell(nextStep[0], nextStep[1]);
    }

    // Handle the solve button click event
    public void handleSolveButtonClick(ActionEvent actionEvent) {
        int[] nextStep = {0, 0};
        while (nextStep[0] != -1 && nextStep[1] != -1 ) {
            // get the next step in the solution
            nextStep = viewModel.getNextStepInSolution();
            // update the maze display accordingly
            markCell(nextStep[0], nextStep[1]);
        }
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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setHeaderText(null);
                alert.setContentText("Maze solved!");
                DialogPane dialogPane = alert.getDialogPane();
                dialogPane.setHeaderText(null);
                dialogPane.setGraphic(null);

                // apply the CSS to the alert dialog
                alert.getDialogPane().getStylesheets().add(css);

                alert.show();
            }
        }
    }


}
