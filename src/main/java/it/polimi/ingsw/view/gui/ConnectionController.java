package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


public class ConnectionController extends Pane {
    @FXML
    private TextField ipField;
    private String ip;
    private Boolean ended = false;
    private GUIController guiController;
    public String getIp() {
        return ip;
    }

    public Boolean isEnded() {
        return ended;
    }
    public void set(){

    }
    @FXML
    public void initialize(){
        guiController = GUIController.getInstance();
        guiController.setConnectionController(this);
    }

    @FXML
    private void setIp(){
        this.ip = ipField.getText();
        guiController.setIp(ip);
        ended = true;
        notifyAll();
    }
}
