package it.polimi.ingsw.view.gui;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


import java.util.concurrent.ArrayBlockingQueue;

public class GUIApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        //primaryStage.setMaximized(true);
        GUIController.getInstance().setupScene(stage,"Connection.fxml");
    }

    @Override
    public void stop() {
        // GUIApp.getInstance().closeConnection();
        System.exit(0);
    }



}
