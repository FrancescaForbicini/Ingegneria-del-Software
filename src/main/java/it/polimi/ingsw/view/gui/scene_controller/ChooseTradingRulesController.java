package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;

import java.util.ArrayList;


public class ChooseTradingRulesController {
    @FXML
    private Button button0;
    @FXML
    private Button button1;
    @FXML
    private Button button2;
    @FXML
    private ImageView card0;
    @FXML
    private ImageView card1;
    @FXML
    private ImageView card2;

    public void initialize(){
        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(button0);
        buttons.add(button1);
        buttons.add(button2);
        ArrayList<ImageView> cards = new ArrayList<>();
        cards.add(card0);
        cards.add(card1);
        cards.add(card2);
//        ClientPlayer self = GUIController.getInstance().getSelf();
        ArrayList<TradingRule> activeTradingRules = GUIController.getInstance().getActiveTradingRules();
        //TODO disable all cards which are not active
        //TODO better working with development cards instead of directly trading rules
    }

}
