package it.polimi.ingsw.view.gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GUIApp extends Application {
    private FXMLLoader loader;
    private Stage stage;
    private LoginController loginController;
    private static GUIApp instance = null;

    public static GUIApp getInstance(){
        if (instance == null)
            instance = new GUIApp();
        return instance;
    }


    @Override
    public void start(Stage stage) throws Exception {
        //primaryStage.setMaximized(true);
        this.stage = stage;
        login();
    }

    @Override
    public void stop() {
        // GUIApp.getInstance().closeConnection();
        System.exit(0);
    }

    public void login(){
        loader = new FXMLLoader(GUI.class.getClassLoader().getResource("Login.fxml"));
        Parent root;
        try{
            root = loader.load();
        } catch (Exception e){
            e.printStackTrace();
            root = new Pane();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        loginController = loader.getController();
    }
}
