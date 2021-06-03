package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class ShowMarketController {
    @FXML
    private Circle marble00;
    @FXML
    private Circle marble01;
    @FXML
    private Circle marble02;
    @FXML
    private Circle marble03;
    @FXML
    private Circle marble10;
    @FXML
    private Circle marble11;
    @FXML
    private Circle marble12;
    @FXML
    private Circle marble13;
    @FXML
    private Circle marble20;
    @FXML
    private Circle marble21;
    @FXML
    private Circle marble22;
    @FXML
    private Circle marble23;
    @FXML
    private Circle marbleExtra;

    public void initialize(){
        Market market = GUIController.getInstance().getMarket();
        ArrayList<Marble> marbles = market.getMarket();
        marble00.setFill(typeToColor(marbles.get(0).getType()));
        marble01.setFill(typeToColor(marbles.get(1).getType()));
        marble02.setFill(typeToColor(marbles.get(2).getType()));
        marble03.setFill(typeToColor(marbles.get(3).getType()));
        marble10.setFill(typeToColor(marbles.get(4).getType()));
        marble11.setFill(typeToColor(marbles.get(5).getType()));
        marble12.setFill(typeToColor(marbles.get(6).getType()));
        marble13.setFill(typeToColor(marbles.get(7).getType()));
        marble20.setFill(typeToColor(marbles.get(8).getType()));
        marble21.setFill(typeToColor(marbles.get(9).getType()));
        marble22.setFill(typeToColor(marbles.get(10).getType()));
        marble23.setFill(typeToColor(marbles.get(11).getType()));
        marbleExtra.setFill(typeToColor(market.getExtraMarble().getType()));


    }
    private Color typeToColor(MarbleType type){
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
