package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.input.MouseEvent;


public class ShowMessageController {
    @FXML
    private DialogPane message;
    @FXML
    private Button okButton;

    public void initialize(){
        message.setContentText(GUIController.getInstance().getMessageToShow());
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> setAck());
    }
    private void setAck(){
        GUIController.getInstance().setAckMessage(true);
    }
}
