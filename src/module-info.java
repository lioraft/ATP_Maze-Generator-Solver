module View {
    requires javafx.controls;
    requires javafx.fxml;
    requires ATPProjectJAR;
    requires javafx.media;

    opens View to javafx.fxml;

    exports View;
}