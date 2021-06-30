package it.polimi.ingsw.view.gui;

import javafx.application.Application;
import javafx.stage.Stage;

public class GUIApp extends Application {
    @Override
    public void start(Stage stage){
        SceneManager.getInstance().setup(stage);
    }

    @Override
    public void stop() {
        System.exit(0);
    }
}
