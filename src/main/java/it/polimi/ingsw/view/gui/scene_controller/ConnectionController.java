package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class ConnectionController{
    @FXML
    AnchorPane mainPane;
    @FXML
    private TextField ipField;
    @FXML
    private Button connectionButton;


    @FXML
    public void initialize(){
        GridPane box = new GridPane();
        mainPane.getChildren().add(box);
        box.setGridLinesVisible(true);
        box.addColumn(7, new Rectangle());
        Rectangle rectangle = new Rectangle(100,100);
        ColumnConstraints columnConstraints = new ColumnConstraints();
        columnConstraints.setHalignment(HPos.CENTER);
        box.getColumnConstraints().add(columnConstraints);
        box.getChildren().add(rectangle);
        box.setAlignment(Pos.CENTER_RIGHT);
        rectangle.setFill(Color.AQUA);

        connectionButton.addEventHandler(MouseEvent.MOUSE_CLICKED,event -> setIp());
    }

    private void setIp(){
        String ip = ipField.getText();
        GUIController.getInstance().setIp(ip);
    }
}
