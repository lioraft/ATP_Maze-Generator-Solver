package View;

import javafx.event.ActionEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public interface IView {
    void handleStartButtonClick(ActionEvent actionEvent); // handle start button click
    void handlePlayMove(String activator, GraphicsContext gc, Image playerImage); // handle player move: needs gameboard, player character to move, and the key pressed
}
