package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class ShowMessageController {
    @FXML
    private Label messageLabel;
    @FXML
    private Button okButton;

    public  void initialize(){
        String message = GUIController.getInstance().getMessageToShow();
        messageLabel.setText(message);
        okButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> setAck());
    }
    private void setAck(){
        GUIController.getInstance().setAckMessage(true);
    }
}
