package View;

import ViewModel.MyViewModel;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.media.MediaView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.*;
import java.util.*;
import java.io.File;
import java.io.IOException;

/*
 * This class is the ViewModel layer of the MVVM architecture.
 * It is responsible for the presentation logic of the application.
 */

public class MyViewController extends Application implements IView {
    BorderPane mainScene;
    Scene scene;
    private ComboBox<Integer> width;
    private ComboBox<Integer> height;
    MyViewModel viewModel;
    String css;

    GraphicsContext gc;
    private static Stage ps;
    private static MediaPlayer mediaPlayer;
    @FXML
    Canvas mazeCanvas;
    private static HashMap<String, String[]> speakerQuotes;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // set hashmap
        setSpeakerQuotes();
        // initialize media player
        mediaPlayer = new MediaPlayer(new Media(getClass().getResource("/Spongebob_Theme.mp3").toExternalForm()));
        // set media player to loop
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        mediaPlayer.play();
        ps = primaryStage;
        ps.resizableProperty().setValue(Boolean.TRUE);
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
        Image backgroundImage = new Image(getClass().getResource("/spongebob-jellyfish.jpg").toExternalForm());
        // set background color
        mainScene.setStyle("-fx-background-color: #6dcff6;");
        // get imageview from fxml
        ImageView backgroundImageView = (ImageView) mainScene.lookup("#bgImage");
        // set image
        backgroundImageView.setImage(backgroundImage);
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
        // handle load game menu item click
        loadGame.setOnAction(this::handleLoadGameButtonClick);
        // initialize options menu
        Menu optionsMenu =  menuBar.getMenus().get(1);
        // initialize options menu items
        MenuItem properties = optionsMenu.getItems().get(0);
        // handle properties menu item click
        properties.setOnAction(this::handlePropertiesButtonClick);
        // get sound menu item
        MenuItem sound = optionsMenu.getItems().get(1);
        // handle sound menu item click
        sound.setOnAction(this::handleSoundButtonClick);
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
        // handle help menu item click
        help.setOnAction(this::handleHelpButtonClick);
        // initialize about menu
        Menu aboutMenu = menuBar.getMenus().get(4);
        // initialize about menu items
        MenuItem about = aboutMenu.getItems().get(0);
        // handle about menu item click
        about.setOnAction(this::handleAboutButtonClick);
        // initialize guess who menu
        Menu guessWhoMenu = menuBar.getMenus().get(5);
        // initialize guess who menu items
        MenuItem guessWho = guessWhoMenu.getItems().get(0);
        // handle guess who menu item click
        guessWho.setOnAction(this::handleQuotesButtonClick);
        // set designs for menu bar
        menuBar.getStyleClass().add("menu-bar");
        // set width and height comboboxes
        width = (ComboBox<Integer>) mainScene.lookup("#width");
        height = (ComboBox<Integer>) mainScene.lookup("#height");
        // set width and height comboboxes values
        for (int i = 10; i <= 300; i += 10) {
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
        scene = new Scene(mainScene);
        scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
        primaryStage.setScene(scene);
        // show stage
        primaryStage.show();
        // if user exists through the x button, stop the servers if started
        primaryStage.setOnCloseRequest(event -> {
            if (viewModel == null) {
                viewModel = MyViewModel.getInstance();
            }
            viewModel.exit();
        });
    }

    public void handleSoundButtonClick(ActionEvent actionEvent) {
        // create the alert
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Sound Properties");
        alert.setHeaderText(null);
        alert.setGraphic(null);
        alert.setContentText("Play/Pause");

        // create buttons with images
        Image imagePlay = new Image(getClass().getResource("/play.png").toExternalForm());
        Image imageStop = new Image(getClass().getResource("/stop.png").toExternalForm());

        // create custom buttons
        Button playButton = new Button();
        playButton.setGraphic(new ImageView(imagePlay));
        Button stopButton = new Button();
        stopButton.setGraphic(new ImageView(imageStop));

        // create button types with custom buttons
        ButtonType buttonTypePlay = new ButtonType("", ButtonBar.ButtonData.OK_DONE);
        ButtonType buttonTypeStop = new ButtonType("", ButtonBar.ButtonData.CANCEL_CLOSE);

        // set the button types in the alert
        alert.getButtonTypes().setAll(buttonTypePlay, buttonTypeStop);

        // retrieve the button nodes from the alert dialog
        Button playButtonNode = (Button) alert.getDialogPane().lookupButton(buttonTypePlay);
        Button stopButtonNode = (Button) alert.getDialogPane().lookupButton(buttonTypeStop);

        // set the custom buttons as the graphic for the button nodes
        playButtonNode.setGraphic(playButton.getGraphic());
        stopButtonNode.setGraphic(stopButton.getGraphic());

        // set the layout of the button nodes to center
        playButtonNode.getStyleClass().add("sound-button");
        stopButtonNode.getStyleClass().add("sound-button");
        alert.setResizable(true);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

        // set the width and height
        alert.getDialogPane().setPrefWidth(250);
        alert.getDialogPane().setPrefHeight(130);

        // show the alert and wait for the user's response
        alert.showAndWait().ifPresent(buttonType -> {
            if (mediaPlayer != null) {
                if (buttonType == buttonTypeStop) {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                        mediaPlayer.stop();
                    }
                } else if (buttonType == buttonTypePlay) {
                    if (mediaPlayer.getStatus() == MediaPlayer.Status.STOPPED) {
                        mediaPlayer.play();
                    }
                }
            }
        });
    }



    @Override
    public void handleAboutButtonClick(ActionEvent actionEvent) {
        try {
            // load fxml of help
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("about.fxml"));
            Parent root = fxmlLoader.load();
            // set same design from css as help scene
            // set help-label design from css
            Label descLabel = (Label) root.lookup("#descLabel");
            descLabel.getStyleClass().add("help-label");
            // get text area and set background color
            TextArea descText = (TextArea) root.lookup("#descText");
            descText.getStyleClass().add("transparent-text-area");
            // get close button
            Button closeButton = (Button) root.lookup("#closeButton");
            // set help-close-button design from css
            closeButton.getStyleClass().add("help-close-button");
            // set close button to close the window
            closeButton.setOnAction(event -> {
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
            });
            // set scene and show
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
            scene.getRoot().setStyle("-fx-background-color: #6dcff6;");
            Stage stage = new Stage();
            stage.setTitle("About");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // handle start game button click
    @Override
    public void handleStartButtonClick(ActionEvent actionEvent) {
        try {
            Stage primaryStage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();

            // load the FXML file for the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mazeDisplayer.fxml"));
            BorderPane mazeDisplayer = loader.load();

            // set background color
            mazeDisplayer.setStyle("-fx-background-color: #6dcff6;");
            // set css
            // get input dimensions from user
            if (width.getValue() == null || height.getValue() == null) {
                throw new Exception();
            }
            // get input dimensions from user
            Integer mazeWidth = width.getValue();
            Integer mazeHeight = height.getValue();

            // generate maze
            viewModel = MyViewModel.getInstance();
            viewModel.generateMaze(mazeWidth, mazeHeight);

            // get current maze
            int[][] maze = viewModel.getMaze();

            if (gc == null) {
                // get canvas in order to create maze in sizes and display it
                mazeCanvas = (Canvas) mazeDisplayer.lookup("#mazeCanvas");
                gc = mazeCanvas.getGraphicsContext2D();
                mazeCanvas.setOnScroll(event -> {
                    if (event.isControlDown()) {
                        double delta = event.getDeltaY();
                        if (delta > 0) {
                            // Zoom in
                            zoomIn();
                        } else {
                            // Zoom out
                            zoomOut();
                        }
                        event.consume();
                    }
                });
            }
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

            // create hint and solve buttons
            createHintAndSolveButtons(mazeDisplayer);

            // get timer label
            Label timerLabel = (Label) mazeDisplayer.lookup("#timerLabel");
            // set text for timer label
            timerLabel.setText("00:00:00");
            Timeline timeline;
            final Duration[] duration = {Duration.ZERO};
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                duration[0] = duration[0].add(Duration.seconds(1));
                long hours = (long) duration[0].toHours();
                long minutes = (long) (duration[0].toMinutes() % 60);
                long seconds = (long) (duration[0].toSeconds() % 60);
                timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();


            // create a new scene with the loaded FXML content
            Scene mazeDisplayScene = new Scene(mazeDisplayer);

            // load css file
            mazeDisplayScene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

            // set the new scene as the scene of the primary stage
            primaryStage.setScene(mazeDisplayScene);

            mazeDisplayer.requestFocus(); // set focus on the maze

            mazeDisplayer.setOnMouseDragged(event -> {
                double dragX = event.getX();
                double dragY = event.getY();

                if (viewModel == null) {
                    viewModel = MyViewModel.getInstance();
                }

                handleMouseClickMovement(viewModel.getPlayerRow(), viewModel.getPlayerCol(), dragX, dragY, spongebob);
            });

            mazeDisplayer.setOnKeyPressed(event -> {
                KeyCode keyCode = event.getCode();
                if (keyCode.isKeypadKey()) {
                    handlePlayMove(keyCode.getName().toUpperCase(), spongebob);
                }
            });

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please choose width and height");
            setAlertCSS(alert, "/attention.png");
            alert.showAndWait();
        }
    }

    // create the hint button and the solve button
    private void createHintAndSolveButtons(BorderPane mazeDisplayer) {
        // Create the hint button
        Button hintButton = (Button) mazeDisplayer.lookup("#hint_button");
        hintButton.getStyleClass().add("hint-button");
        hintButton.setOnAction(this::handleHintButtonClick);

        // Create the solve button
        Button solveButton = (Button) mazeDisplayer.lookup("#solve_button");
        solveButton.getStyleClass().add("hint-button");
        solveButton.setOnAction(this::handleSolveButtonClick);
    }

    @Override
    public void handleNewGameButtonClick(ActionEvent actionEvent) {
        try {
            setMainScene(ps);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    // handle load maze menu item click
    @Override
    public void handleLoadGameButtonClick(ActionEvent actionEvent) {
        // open file chooser
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load Maze");
        // set extension filter
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Maze files (*.maze)", "*.maze");
        fileChooser.getExtensionFilters().add(extFilter);
        // show open file dialog
        File file = fileChooser.showOpenDialog(null);
        // let user choose file to load
        if (file != null) {
            viewModel = MyViewModel.getInstance();
            viewModel.loadMaze(file);
            // get current maze
            int[][] maze = viewModel.getMaze();
            // get player position
            int playerRow = 0;
            int playerCol = viewModel.getStartColumn();
            // get goal position
            int goalRow = maze.length-1;
            int goalCol = viewModel.getGoalColumn();
            viewModel.setPlayerPosition(playerRow, playerCol);
            // get image from resources
            Image spongebob = new Image(getClass().getResource("/spongebob.png").toExternalForm());
            // load the FXML file for the new scene
            FXMLLoader loader = new FXMLLoader(getClass().getResource("mazeDisplayer.fxml"));
            BorderPane mazeDisplayer;
            try {
                mazeDisplayer = loader.load();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            // if gc is null, get it
            if (gc == null) {
                mazeCanvas = (Canvas) mazeDisplayer.lookup("#mazeCanvas");
                gc = mazeCanvas.getGraphicsContext2D();
                mazeCanvas.setOnScroll(event -> {
                    if (event.isControlDown()) {
                        double delta = event.getDeltaY();
                        if (delta > 0) {
                            // Zoom in
                            zoomIn();
                        } else {
                            // Zoom out
                            zoomOut();
                        }
                        event.consume();
                    }
                });
            }
            // draw maze
            drawMaze(maze);
            // draw player
            drawPlayer(spongebob, playerRow, playerCol, maze);
            // get image from resources
            Image jellyfish = new Image(getClass().getResource("/jellyfish.png").toExternalForm());
            // draw goal
            drawPlayer(jellyfish, goalRow, goalCol, maze);
            // Create the hint button
            createHintAndSolveButtons(mazeDisplayer);

            // get timer label
            Label timerLabel = (Label) mazeDisplayer.lookup("#timerLabel");
            // set text for timer label
            timerLabel.setText("00:00:00");
            Timeline timeline;
            final Duration[] duration = {Duration.ZERO};
            timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
                duration[0] = duration[0].add(Duration.seconds(1));
                long hours = (long) duration[0].toHours();
                long minutes = (long) (duration[0].toMinutes() % 60);
                long seconds = (long) (duration[0].toSeconds() % 60);
                timerLabel.setText(String.format("%02d:%02d:%02d", hours, minutes, seconds));
            }));
            timeline.setCycleCount(Timeline.INDEFINITE);
            timeline.play();

            // set background color
            mazeDisplayer.setStyle("-fx-background-color: #6dcff6;");

            // create a new scene with the loaded FXML content
            Scene mazeDisplayScene = new Scene(mazeDisplayer);

            // load css file
            mazeDisplayScene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());

            // set the new scene as the scene of the primary stage
            ps.setScene(mazeDisplayScene);

            mazeDisplayer.requestFocus(); // set focus on the maze

            mazeDisplayer.setOnMouseDragged(event -> {
                double dragX = event.getX();
                double dragY = event.getY();

                if (viewModel == null) {
                    viewModel = MyViewModel.getInstance();
                }

                handleMouseClickMovement(viewModel.getPlayerRow(), viewModel.getPlayerCol(), dragX, dragY, spongebob);
            });

            mazeDisplayer.setOnKeyPressed(event -> {
                KeyCode keyCode = event.getCode();
                if (keyCode.isKeypadKey()) {
                    handlePlayMove(keyCode.getName().toUpperCase(), spongebob);
                }
            });
        }
        else {
            // if file is null, show error
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please choose a valid file");
            setAlertCSS(alert, "/attention.png");
            alert.showAndWait();
        }
    }

    // handle mouse drag event - when user drags the character in the maze, translate it to keyboard movement and use handlePlayMove
    private void handleMouseClickMovement(int playerRow, int playerCol, double clickX, double clickY, Image playerImage) {
        // Calculate the cell size based on the maze dimensions and the canvas size
        if (viewModel == null) {
            viewModel = MyViewModel.getInstance();
        }
        double cellWidth = mazeCanvas.getWidth() / viewModel.getMazeCols();
        double cellHeight = mazeCanvas.getHeight() / viewModel.getMazeRows();

        // Calculate the clicked cell's position in the maze
        int clickCol = (int) (clickX / cellWidth);
        int clickRow = (int) (clickY / cellHeight);

// Determine the direction of movement based on the clicked cell's position relative to the player's position
        if (clickRow == playerRow - 1) {
            if (clickCol == playerCol - 1) {
                // Up-left movement
                handlePlayMove("NUMPAD7", playerImage);
            } else if (clickCol == playerCol + 1) {
                // Up-right movement
                handlePlayMove("NUMPAD9", playerImage);
            } else if (clickCol == playerCol) {
                // Up movement
                handlePlayMove("NUMPAD8", playerImage);
            }
        } else if (clickRow == playerRow + 1) {
            if (clickCol == playerCol - 1) {
                // Down-left movement
                handlePlayMove("NUMPAD1", playerImage);
            } else if (clickCol == playerCol + 1) {
                // Down-right movement
                handlePlayMove("NUMPAD3", playerImage);
            } else if (clickCol == playerCol) {
                // Down movement
                handlePlayMove("NUMPAD2", playerImage);
            }
        }
        else if (playerRow == clickRow) {
            if (clickCol == playerCol - 1) {
                // Left movement
                handlePlayMove("NUMPAD4", playerImage);
            } else if (clickCol == playerCol + 1) {
                // Right movement
                handlePlayMove("NUMPAD6", playerImage);
            }
        }
    }



    // handle save maze menu item click
    @Override
    public void handleSaveGameButtonClick(ActionEvent actionEvent) {
        viewModel = MyViewModel.getInstance();
        if (viewModel != null) {
            // Open window for asking for file name
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Maze");
            fileChooser.setInitialFileName("savedMaze");
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Maze Files (*.maze)", "*.maze");
            fileChooser.getExtensionFilters().add(extFilter);
            File file = fileChooser.showSaveDialog(new Stage());

            if (file != null) {
                boolean canSave = viewModel.saveMaze(file);
                if (canSave) {
                    // Maze saved successfully
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Maze saved successfully");
                    setAlertCSS(alert, "/success.png");
                    alert.showAndWait();
                } else {
                    // Error saving maze
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Failed to save the maze");
                    setAlertCSS(alert, "/attention.png");
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
    @Override
    public void handleHintButtonClick(ActionEvent actionEvent) {
        if (viewModel == null) {
            viewModel = MyViewModel.getInstance();
        }
        // get the next step in the solution
        int[] nextStep = viewModel.getNextStepInSolution();
        // update the maze display accordingly
        markCell(nextStep[0], nextStep[1]);
    }

    // handle the solve button click event
    @Override
    public void handleSolveButtonClick(ActionEvent actionEvent) {
        if (viewModel == null) {
            viewModel = MyViewModel.getInstance();
        }
        int[] nextStep = {0, 0};
        while (nextStep[0] != -1 && nextStep[1] != -1 ) {
            // get the next step in the solution
            nextStep = viewModel.getNextStepInSolution();
            // update the maze display accordingly
            markCell(nextStep[0], nextStep[1]);
        }
    }

    @Override
    public void handlePropertiesButtonClick(ActionEvent actionEvent) {
        // get the properties from the config file
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties")) {
            Properties prop = new Properties();
            // load a properties file
            prop.load(input);
            // if can retrieve, get the info from the config file
            int poolSize = Integer.parseInt(prop.getProperty("threadPoolSize"));
            String mazeGeneratingAlgorithm = prop.getProperty("mazeSearchingAlgorithm");
            String  mazeSolvingAlgorithm = prop.getProperty("mazeGeneratingAlgorithm");
            // create alert for properties
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Properties");

            // create the content VBox
            VBox propertiesBox = new VBox();
            propertiesBox.getStyleClass().add("properties");

            // create the property labels and values
            HBox mazeGeneratingRow = new HBox();
            mazeGeneratingRow.getStyleClass().add("property-row");
            Label mazeGeneratingLabel = new Label("Maze Generating Algorithm:");
            mazeGeneratingLabel.getStyleClass().add("property-label");
            Label mazeGeneratingValue = new Label(mazeGeneratingAlgorithm);
            mazeGeneratingValue.getStyleClass().add("property-value");
            mazeGeneratingRow.getChildren().addAll(mazeGeneratingLabel, mazeGeneratingValue);

            HBox mazeSearchingRow = new HBox();
            mazeSearchingRow.getStyleClass().add("property-row");
            Label mazeSearchingLabel = new Label("Maze Searching Algorithm:");
            mazeSearchingLabel.getStyleClass().add("property-label");
            Label mazeSearchingValue = new Label(mazeSolvingAlgorithm);
            mazeSearchingValue.getStyleClass().add("property-value");
            mazeSearchingRow.getChildren().addAll(mazeSearchingLabel, mazeSearchingValue);

            HBox threadPoolRow = new HBox();
            threadPoolRow.getStyleClass().add("property-row");
            Label threadPoolLabel = new Label("Thread Pool Size:");
            threadPoolLabel.getStyleClass().add("property-label");
            Label threadPoolValue = new Label(poolSize+"");
            threadPoolValue.getStyleClass().add("property-value");
            threadPoolRow.getChildren().addAll(threadPoolLabel, threadPoolValue);

            // add the property rows to the content VBox
            propertiesBox.getChildren().addAll(
                    mazeGeneratingRow,
                    mazeSearchingRow,
                    threadPoolRow
            );

            // set the content
            alert.getDialogPane().setContent(propertiesBox);
            alert.getDialogPane().getStyleClass().add("properties");
            alert.getDialogPane().getStylesheets().add(getClass().getResource("/properties.css").toExternalForm());
            alert.setHeaderText(null);
            alert.setResizable(true);
            // load the exit image
            Image image = new Image(getClass().getResource("/settings.png").toExternalForm());
            // create the ImageView
            ImageView imageView = new ImageView(image);
            // set the ImageView as the graphic for the DialogPane
            alert.getDialogPane().setGraphic(imageView);
            // show alert
            alert.showAndWait();



        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }

    // handle the exit button click event
    @Override
    public void handleExitButtonClick(ActionEvent actionEvent) {
        // alert user that the program is about to exit
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to exit?", ButtonType.YES, ButtonType.NO);
        setAlertCSS(alert, "/exit.jpg");
        // show alert
        alert.showAndWait();
        // if user clicked yes, exit the program
        if (alert.getResult() == ButtonType.YES) {
            if (viewModel == null)
                viewModel = MyViewModel.getInstance();
            // close the thread pool
            viewModel.exit();
            // close the program
            Platform.exit();
        }
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
            // set the background color for the cell
            gc.setFill(Color.LIGHTPINK); // color to pink
            // fill the cell with the background color
            gc.fillRect(x, y, cellWidth, cellHeight);
            // draw the player image in the cell
            gc.drawImage(playerImage, x, y, cellWidth, cellHeight);
        } else {
            gc.setFill(Color.BLUE);
            gc.fillRect(x, y, cellWidth, cellHeight);
        }
    }

    // function that check if cell player wants to move to is a passage
    // if it is, move player to this cell
    // if it isn't, do nothing
    @Override
    public void handlePlayMove(String key,Image playerImage) {
        // remove spaces from key name
        key = key.replaceAll("\\s+", "");
        int[][] maze = viewModel.getMaze();

        int currentRow = viewModel.getPlayerRow();
        int currentCol = viewModel.getPlayerCol();

        int newRow = currentRow;
        int newCol = currentCol;

        switch (key) {
            case "NUMPAD8":
                newRow--;
                break;
            case "NUMPAD7":
                newRow--;
                newCol--;
                break;
            case "NUMPAD2":
                newRow++;
                break;
            case "NUMPAD4":
                newCol--;
                break;
            case "NUMPAD6":
                newCol++;
                break;
            case "NUMPAD9":
                newRow--;
                newCol++;
                break;
            case "NUMPAD1":
                newRow++;
                newCol--;
                break;
            case "NUMPAD3":
                newRow++;
                newCol++;
                break;
            default:
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
                alert.showAndWait();

                // after alert show, open scene of maze solved
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MazeSolved.fxml"));
                    Parent root = fxmlLoader.load();
                    // pause the music
                    mediaPlayer.pause();
                    // new MediaPlayer for the video
                    Media videoMedia = new Media(getClass().getResource("/Sweet_Victory.mp4").toExternalForm());
                    MediaPlayer videoPlayer = new MediaPlayer(videoMedia);
                    // Play the video
                    MediaView mediaView = (MediaView) root.lookup("#video");
                    mediaView.setMediaPlayer(videoPlayer);
                    videoPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                    videoPlayer.play();
                    // set label design from css
                    Label label = (Label) root.lookup("#successLabel");
                    label.getStyleClass().add("solved-label");
                    // get close button
                    Button closeButton = (Button) root.lookup("#closeButton");
                    closeButton.getStyleClass().add("hint-button");
                    // set close button to close the window
                    closeButton.setOnAction(event -> {
                        // stop the video player
                        videoPlayer.stop();
                        // resume the background music
                        mediaPlayer.play();
                        Stage stage = (Stage) closeButton.getScene().getWindow();
                        stage.close();
                    });
                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
                    scene.getRoot().setStyle("-fx-background-color: #6dcff6;");
                    Stage stage = new Stage();
                    stage.setTitle("Maze Solved!");
                    stage.setScene(scene);
                    stage.show();
                    // if user exists through the x button, stop the video player and resume the background music as well
                    stage.setOnCloseRequest(event -> {
                        // Stop the video player
                        videoPlayer.stop();

                        // Resume the background music
                        mediaPlayer.play();
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    // function that opens help window
    public void handleHelpButtonClick(ActionEvent actionEvent) {
        try {
            // load fxml of help
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("help.fxml"));
            Parent root = fxmlLoader.load();
            // set images of characters
            ImageView spongebob = (ImageView) root.lookup("#spongebob");
            Image spongebobImage = new Image(getClass().getResource("/spongebob.png").toExternalForm());
            spongebob.setImage(spongebobImage);
            ImageView jellyfish = (ImageView) root.lookup("#jellyfish");
            Image jellyfishImage = new Image(getClass().getResource("/jellyfish.png").toExternalForm());
            jellyfish.setImage(jellyfishImage);
            // set main label design from css
            Label mainLabel = (Label) root.lookup("#mainLabel");
            mainLabel.getStyleClass().add("help-main-label");
            // set help-label design from css
            Label descLabel = (Label) root.lookup("#descLabel");
            descLabel.getStyleClass().add("help-label");
            Label navLabel = (Label) root.lookup("#navLabel");
            navLabel.getStyleClass().add("help-label");
            Label charLabel = (Label) root.lookup("#charLabel");
            charLabel.getStyleClass().add("help-label");
            Label menuLabel = (Label) root.lookup("#menuLabel");
            menuLabel.getStyleClass().add("help-label");
            // get text area and set background color
            TextArea descText = (TextArea) root.lookup("#descText");
            descText.getStyleClass().add("transparent-text-area");
            TextArea navText = (TextArea) root.lookup("#navText");
            navText.getStyleClass().add("transparent-text-area");
            TextArea charText = (TextArea) root.lookup("#charText");
            charText.getStyleClass().add("transparent-text-area");
            TextArea menuText = (TextArea) root.lookup("#menuText");
            menuText.getStyleClass().add("transparent-text-area");
            // get close button
            Button closeButton = (Button) root.lookup("#closeButton");
            // set help-close-button design from css
            closeButton.getStyleClass().add("help-close-button");
            // set close button to close the window
            closeButton.setOnAction(event -> {
                Stage stage = (Stage) closeButton.getScene().getWindow();
                stage.close();
            });
            // set scene and show
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
            scene.getRoot().setStyle("-fx-background-color: #6dcff6;");
            Stage stage = new Stage();
            stage.setTitle("Help");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
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

    // helper function to zoom in
    private void zoomIn() {
        // Adjust the scale factor to increase zoom level
        double newScale = mazeCanvas.getScaleX() * 1.1;
        mazeCanvas.setScaleX(newScale);
        mazeCanvas.setScaleY(newScale);
    }

    // helper function to zoom out
    private void zoomOut() {
        double newScale = mazeCanvas.getScaleX() * 0.9;
        mazeCanvas.setScaleX(newScale);
        mazeCanvas.setScaleY(newScale);
    }

    public void handleQuotesButtonClick(ActionEvent actionEvent) {
        try {
            // load fxml of quotes
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("quotes.fxml"));
            Parent root = fxmlLoader.load();
            // choose random character
            int randomIndex = new Random().nextInt(speakerQuotes.size());
            String key = (String)speakerQuotes.keySet().toArray(new Object[0])[randomIndex];
            // get character list of quotes
            String[] quotes = speakerQuotes.get(key);
            // choose random quote
            int randomQuoteIndex = new Random().nextInt(quotes.length);
            String quote = quotes[randomQuoteIndex];
            // set quote label
            Label quoteLabel = (Label) root.lookup("#quoteLabel");
            quoteLabel.setText(quote);
            // set character image
            ImageView characterImage = (ImageView) root.lookup("#characterImage");
            Image image = new Image(getClass().getResource("/characters/" + key + ".png").toExternalForm());
            characterImage.setImage(image);
            // set reveal button
            Button revealButton = (Button) root.lookup("#revealButton");
            revealButton.getStyleClass().add("hint-button");
            // set action for reveal button - reveal image
            revealButton.setOnAction(event -> {
                characterImage.visibleProperty().setValue(true);
            });
            // set action for try again button
            Button tryAgainButton = (Button) root.lookup("#repeatButton");
            tryAgainButton.getStyleClass().add("hint-button");
            tryAgainButton.setOnAction(event -> {
                // close window
                Stage stage = (Stage) tryAgainButton.getScene().getWindow();
                stage.close();
                // call function again
                handleQuotesButtonClick(actionEvent);
            });
            // set design for quote label
            quoteLabel.getStyleClass().add("help-main-label");
            // set scene and show
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/stylesheet.css").toExternalForm());
            scene.getRoot().setStyle("-fx-background-color: #6dcff6;");
            Stage stage = new Stage();
            stage.setTitle("Fun Quotes");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // initialize speaker quotes
    public void setSpeakerQuotes() {
        // initialize hashmap
        speakerQuotes = new HashMap<>();
        // add quotes to spongebob
        String[] spongebobQuotes = {
                "Can I be excused for the rest of my life?",
                "Hey Patrick, I thought of something funnier than 24… 25!",
                "The best time to wear a striped sweater is all the time.",
                "F is for friends who do stuff together!",
                "If you believe in yourself, with a tiny pinch of magic all your dreams can come true!",
                "I'm not just ready, I'm ready Freddy.",
                "Always follow your heart – unless your heart is bad with directions."
        };
        speakerQuotes.put("spongebob", spongebobQuotes);
        // add quotes to patrick
        String[] patrickQuotes = {
                "Is mayonnaise an instrument?",
                "The inner machinations of my mind are an enigma.",
                "Two words, SpongeBob. Na. Chos.",
                "I wumbo, you wumbo, he she me wumbo, Wumbology, The study of wumbo? It’s first grade SpongeBob!",
                "I can't see my forehead.",
                "No, this is Patrick."
        };

        speakerQuotes.put("patrick", patrickQuotes);
        // add quotes to squidward
        String[] squidwardQuotes = {
                "Wake me up when I care.",
                "I have no soul.",
                "I knew I shouldn't have gotten out of bed today.",
                "I might as well sleep for 100 years or so.",
                "Just when I thought they couldn't get any stupider."
        };

        speakerQuotes.put("squidward", squidwardQuotes);
        // add quotes to krabs
        String[] krabsQuotes = {
                "A 5 letter word for happiness... MONEY.",
                "I like money more than I like people.",
                "Give to the Children’s fund? What have the children ever done for me?",
                "What doesn't kill you, usually succeeds in the second attempt."
        };

        speakerQuotes.put("krabs", krabsQuotes);
        // add quotes to plankton
        String[] planktonQuotes = {
                "I went to college!",
                "Goodbye everyone, I’ll remember you all in therapy.",
                "Do instruments of torture count?",
                "No, I'm not on my way to the grand opening ceremony. I'm busy planning to rule the world!",
                "I command you to help me be a nicer person!"
        };

        speakerQuotes.put("plankton", planktonQuotes);
        // add quotes to sandy
        String[] sandyQuotes = {
                "I'm hotter than a hickory smoked sausage!",
                "Don’t you have to be stupid somewhere else?",
                "Stupidity isn’t a virus, but it sure is spreading like one.",
                "You’re nothing but pure evil... just like newspaper comics."
        };

        speakerQuotes.put("sandy", sandyQuotes);
        // add karen quotes
        String[] karenQuotes = {
                "Just blink.",
                "So, typical day of failure, I see, huh darling?",
                "SHELDON!! HA HA HA!",
                "I heard this joke before!",
                "Why do I even bother?"
        };

        speakerQuotes.put("karen", karenQuotes);
    }

}
