package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class ShowMarketController {
    @FXML
    private GridPane marketGrid;
    @FXML
    private GridPane extraMarble;
    @FXML
    private Button back;


    public void initialize(){
        Market market = GUIController.getInstance().getMarket();
        ArrayList<Marble> marbles = market.getMarket();
        Circle circle;
        for(Marble marble : marbles){
            circle = new Circle();
            circle.setFill(typeToPaint(marble.getType()));
            marketGrid.getChildren().add(circle);
        }
        circle = new Circle();
        circle.setFill(typeToPaint(market.getExtraMarble().getType()));
        extraMarble.getChildren().add(1,circle);
        back.setOnAction(actionEvent -> GUIController.getInstance().setAckMessage(true));
    }
    private Color typeToPaint(MarbleType type){
        Color color = null;
        switch (type){
            case Red:
                color = Color.RED;
                break;
            case Blue:
                color = Color.BLUE;
                break;
            case Grey:
                color = Color.GREY;
                break;
            case White:
                color = Color.WHITE;
                break;
            case Purple:
                color = Color.PURPLE;
                break;
            case Yellow:
                color = Color.YELLOW;
                break;
        }
        return color;
    }
}
