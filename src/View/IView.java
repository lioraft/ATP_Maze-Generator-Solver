package View;

import javafx.event.ActionEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public interface IView {
    void handleStartButtonClick(ActionEvent actionEvent); // handle start button click: a new game is generated
    void handlePlayMove(String activator, Image playerImage); // handle player move: needs player character to move, and the key pressed
    void handleSolveButtonClick(ActionEvent actionEvent); // handle solve button click: the solution is shown on the gameboard
    void handleHintButtonClick(ActionEvent actionEvent); // handle hint button click: the next step in the solution is shown on the gameboard
    void handleExitButtonClick(ActionEvent actionEvent); // handle exit button click: the game is closed
}
