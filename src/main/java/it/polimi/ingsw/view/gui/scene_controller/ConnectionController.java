package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class ConnectionController{
    @FXML
    AnchorPane mainPane;
    @FXML
    private TextField ipField;
    @FXML
    private Button connectionButton;


    @FXML
    public void initialize(){
        connectionButton.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> setIp());
    }

    private void setIp(){
        String ip = ipField.getText();
        GUIController.getInstance().setIp(ip);
        //connectionButton.setDisable(true);
    }
}
