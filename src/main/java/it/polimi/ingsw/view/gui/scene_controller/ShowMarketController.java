package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.ReactiveObserver;
import it.polimi.ingsw.client.action.turn.ChosenLine;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.market.MarketAxis;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.ArrayList;

public class ShowMarketController extends ReactiveObserver {
    @FXML
    private GridPane marketGrid;
    @FXML
    private GridPane extraMarble;
    @FXML
    private Button back; // TODO

    @FXML
    private Label r1;
    @FXML
    private Label r2;
    @FXML
    private Label r3;
    @FXML
    private Label c1;
    @FXML
    private Label c2;
    @FXML
    private Label c3;
    @FXML
    private Label c4;

    private int numColumn = 4;
    private int numRow = 3;
    private boolean isPick = false;
    public ShowMarketController(ClientGameObserverProducer clientGameObserverProducer) {
        this(clientGameObserverProducer, false);
    }
    public ShowMarketController(ClientGameObserverProducer clientGameObserverProducer, boolean isPick) {
        super(clientGameObserverProducer);
        this.isPick = true;
    }
    private void initializeLabelsForPick() {
        c1.setOnMouseClicked(mouseEvent -> GUIController.getInstance().setChosenLine(new ChosenLine(MarketAxis.COL, 1)));
        c2.setOnMouseClicked(mouseEvent -> GUIController.getInstance().setChosenLine(new ChosenLine(MarketAxis.COL, 2)));
        c3.setOnMouseClicked(mouseEvent -> GUIController.getInstance().setChosenLine(new ChosenLine(MarketAxis.COL, 3)));
        c4.setOnMouseClicked(mouseEvent -> GUIController.getInstance().setChosenLine(new ChosenLine(MarketAxis.COL, 4)));
        r1.setOnMouseClicked(mouseEvent -> GUIController.getInstance().setChosenLine(new ChosenLine(MarketAxis.ROW, 1)));
        r2.setOnMouseClicked(mouseEvent -> GUIController.getInstance().setChosenLine(new ChosenLine(MarketAxis.ROW, 2)));
        r3.setOnMouseClicked(mouseEvent -> GUIController.getInstance().setChosenLine(new ChosenLine(MarketAxis.ROW, 3)));
    }

    public void initialize() {
        back.setOnAction(actionEvent -> GUIController.getInstance().setAckMessage(true));
        if (isPick)
            initializeLabelsForPick();
        update();
    }

    public void react(ArrayList<Marble> marbles, Marble extraMable){
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
        circle.setFill(typeToPaint(extraMable.getType()));
        extraMarble.add(circle,0,1);
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

    @Override
    public void update() {
        ArrayList<Marble> marbles = clientGameObserverProducer.getMarket().getActualMarket();
        Marble extraMarble = clientGameObserverProducer.getMarket().getExtraMarble();
        if (marbles != null && extraMarble != null)
            Platform.runLater(() -> react(marbles, extraMarble));
    }
}
