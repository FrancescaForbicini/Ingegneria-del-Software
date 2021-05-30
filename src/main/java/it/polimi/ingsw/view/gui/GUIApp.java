package it.polimi.ingsw.view.gui;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.concurrent.ArrayBlockingQueue;

public class GUIApp extends Application {
    private static GUIApp instance;
    private FXMLLoader loader;
    private Stage stage;
    private ArrayBlockingQueue<String> ipQueue;
    private ArrayBlockingQueue<String> usernameQueue;
    private ArrayBlockingQueue<String> gameIDQueue;

    public static GUIApp getInstance(){
        if (instance == null) {
            instance = new GUIApp();
        }
        return instance;
    }

    @Override
    public void start(Stage stage) throws Exception {
        //primaryStage.setMaximized(true);
        ipQueue = new ArrayBlockingQueue<String>(1);
        this.stage = stage;
        System.out.println("start");
        stage.show();
    }

    @Override
    public void stop() {
        // GUIApp.getInstance().closeConnection();
        System.exit(0);
    }

    public void setIp(String ip){
        try {
            ipQueue.put(ip);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getIp(){
        try {
            return ipQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public void setUsername(String username){
        try {
            usernameQueue.put(username);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getUsername(){
        try {
            return usernameQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void setGameID(String gameID){
        try {
            gameIDQueue.put(gameID);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getGameID(){
        try {
            return gameIDQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Pane getController(){
        return loader.getController();
    }

    public void setupScene(String file){
        System.out.println("load " + file);
        //String dot = ".fxml";
        //file = file+dot;
/*            while (stage == null) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("here");
            }

         */
        loader = new FXMLLoader(GUI.class.getClassLoader().getResource(file));
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
    }

}
