package it.polimi.ingsw.view.gui;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;


public class ConnectionController extends Pane {
    @FXML
    private TextField ipField;
    private String ip;
    private Boolean ended = false;

    public String getIp() {
        return ip;
    }

    public Boolean isEnded() {
        return ended;
    }

    @FXML
    private void setIp(){
        this.ip = ipField.getText();
        GUIApp.getInstance().setIp(ip);
        ended = true;
        //notifyAll();
    }
}
