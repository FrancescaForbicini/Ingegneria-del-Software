package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;


public class ShowMessageController {
    @FXML
    AnchorPane mainPane;
    @FXML
    private Button okButton;

    public void initialize(){
        //Alert alert = new Alert(Alert.AlertType.INFORMATION);
        //alert.setContentText(GUIController.getInstance().getMessageToShow());
        //okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> setAck());
    }
    private void setAck(){
        GUIController.getInstance().setAckMessage(true);
    }
}
