package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.view.gui.scene_controller.LoginController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.concurrent.ArrayBlockingQueue;

public class GUIController {
    private FXMLLoader loader;
    private String messageToShow;
    private ArrayBlockingQueue<Boolean> ackMessageQueue;
    private ArrayBlockingQueue<String> ipQueue;
    private ArrayBlockingQueue<String> usernameQueue;
    private ArrayBlockingQueue<String> gameIDQueue;
    private int numberOfResources;
    private ArrayBlockingQueue<ArrayList<ResourceType>> pickedResourcesQueue;
    private static GUIController instance;
    private Stage stage;
    private LoginController loginController;

    public static GUIController getInstance(){
        if (instance == null) {
            instance = new GUIController();
        }
        return instance;
    }

    private GUIController(){
        ackMessageQueue = new ArrayBlockingQueue<>(1);
        ipQueue = new ArrayBlockingQueue<>(1);
        usernameQueue = new ArrayBlockingQueue<>(1);
        gameIDQueue = new ArrayBlockingQueue<>(1);
        pickedResourcesQueue = new ArrayBlockingQueue<>(1);
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public Stage getStage() {
        return stage;
    }

    public void setMessageToShow(String messageToShow) {
        this.messageToShow = messageToShow;
    }

    public String getMessageToShow() {
        return messageToShow;
    }

    public void setAckMessage(Boolean ackMessage) {
        try {
            ackMessageQueue.put(ackMessage);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void setIp(String ip){
        try {
            ipQueue.put(ip);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public String getIp() {
        String ip = null;
        try {
            ip = ipQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return ip;
    }
    public ClientPlayer getCredentials(){
        String username = null;
        String gameID = null;
        try {
            username = usernameQueue.take();
            gameID = gameIDQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new ClientPlayer(username,gameID);
    }
    public void setUsername(String username){
        try {
            usernameQueue.put(username);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setGameID(String gameID){
        try {
            gameIDQueue.put(gameID);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public void setNumberOfResources(int numberOfResources){
        this.numberOfResources = numberOfResources;
    }

    public int getNumberOfResources() {
        return numberOfResources;
    }

    public void setPickedResources(ArrayList<ResourceType> pickedResources) {
        try {
            pickedResourcesQueue.put(pickedResources);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<ResourceType> getPickedResources(){
        ArrayList<ResourceType> pickedResources = null;
        try {
            pickedResources = pickedResourcesQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return pickedResources;
    }

    public void setupScene(Scene scene , String file){
        System.out.println(file);
        loader = new FXMLLoader(GUIController.class.getClassLoader().getResource(file));
        Parent root;
        try{
            root = loader.load();
            scene.setRoot(root);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
