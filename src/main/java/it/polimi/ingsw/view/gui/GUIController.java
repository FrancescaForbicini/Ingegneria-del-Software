package it.polimi.ingsw.view.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.concurrent.ArrayBlockingQueue;

public class GUIController {
    private FXMLLoader loader;
    private ArrayBlockingQueue<String> ipQueue;
    private ArrayBlockingQueue<String> usernameQueue;
    private ArrayBlockingQueue<String> gameIDQueue;
    private static GUIController instance;
    private Stage stage;
    private LoginController loginController;
    private ConnectionController connectionController;

    public static GUIController getInstance(){
        if (instance == null) {
            instance = new GUIController();
        }
        return instance;
    }

    public void setConnectionController(ConnectionController connectionController) {
        this.connectionController = connectionController;
    }

    public void setIp(String ip){
        try {
            ipQueue.put(ip);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getIp() {
        synchronized (connectionController.getIp()) {
            try {
                if (connectionController.getIp().isEmpty())
                    connectionController.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return connectionController.getIp();
    }

    public void setUsername(String username){
        try {
            usernameQueue.put(username);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    private String getUsername(){
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

    public <T> Object getController(){
        return loader.getController();
    }

    public void setupScene(Stage stage , String file){
        this.stage = stage;
        loader = new FXMLLoader(GUIController.class.getClassLoader().getResource(file));
        Parent root;
        try{
            root = loader.load();
        } catch (Exception e){
            e.printStackTrace();
            root = new Pane();
        }
        Scene scene = new Scene(root);
        stage.setScene(scene);
        getController();
        stage.show();
    }
}
