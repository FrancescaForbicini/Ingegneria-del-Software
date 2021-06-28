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

    private int numColumn = 4;
    private int numRow = 3;

    public void initialize(){
        Market market = GUIController.getInstance().getMarket();
        ArrayList<Marble> marbles = market.getMarket();
        System.out.println(market.toString());
        System.out.println(marbles);
        Circle circle;
        for(int row = 0; row < numRow; row++){
            for (int column = 0 ; column < numColumn; column++){
                circle = new Circle();
                circle.setStroke(Color.BLACK);
                circle.setStrokeWidth(0.5);
                circle.setFill(typeToPaint(marbles.get(column + row*numColumn).getType()));
                circle.setVisible(true);
                circle.setRadius(25);
                marketGrid.add(circle,column,row);
            }
        }
        circle = new Circle();
        circle.setRadius(30);
        circle.setStroke(Color.BLACK);
        circle.setStrokeWidth(0.5);
        circle.setFill(typeToPaint(market.getExtraMarble().getType()));
        extraMarble.add(circle,0,1);
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
