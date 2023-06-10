package View;

import javafx.event.ActionEvent;

public interface IView {
    void handleStartButtonClick(ActionEvent actionEvent); // handle start button click
    void handlePlayMove(String activator); // handle play move on board
}
