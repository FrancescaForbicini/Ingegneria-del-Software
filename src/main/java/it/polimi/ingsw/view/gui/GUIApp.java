package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GUIApp extends Application {

    @Override
    public void start(Stage stage){
        stage.setScene(new Scene(new Pane()));
        GUIController.getInstance().setStage(stage);
        GUIController.getInstance().setupScene(GUIController.getInstance().getStage().getScene(), "Connection.fxml");
        stage.show();
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
