package View;

import javafx.event.ActionEvent;
import javafx.scene.image.Image;

public interface IView {
    void handleStartButtonClick(ActionEvent actionEvent); // handle start button click: a new game is generated
    void handlePlayMove(String activator, Image playerImage); // handle player move: needs player character to move, and the key pressed
    void handleSolveButtonClick(ActionEvent actionEvent); // handle solve button click: the solution is shown on the gameboard
    void handleHintButtonClick(ActionEvent actionEvent); // handle hint button click: the next step in the solution is shown on the gameboard
    void handleExitButtonClick(ActionEvent actionEvent); // handle exit button click: the game is closed
    void handleNewGameButtonClick(ActionEvent actionEvent); // handle new game button click: a new game is generated
    void handleSaveGameButtonClick(ActionEvent actionEvent); // handle save game button click: save current game to file
    void handleLoadGameButtonClick(ActionEvent actionEvent); // handle load game button click: load game from file
    void handlePropertiesButtonClick(ActionEvent actionEvent); // handle properties button click: show properties window
    void handleHelpButtonClick(ActionEvent actionEvent); // handle help button click: show help window
    void handleAboutButtonClick(ActionEvent actionEvent); // handle about button click: show about window
}
