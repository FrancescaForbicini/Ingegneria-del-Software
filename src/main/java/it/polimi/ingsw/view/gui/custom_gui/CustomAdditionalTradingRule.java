package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import javafx.scene.control.Spinner;

import java.util.Map;

public class CustomAdditionalTradingRule extends CustomLeaderCard{
    private Map<ResourceType, Spinner<Integer>> input;
    private Map<ResourceType,Spinner<Integer>> output;
    private Spinner<Integer> faithPoints;

    public CustomAdditionalTradingRule(LeaderCard originalLeaderCard) {
        super(originalLeaderCard);
    }
}
