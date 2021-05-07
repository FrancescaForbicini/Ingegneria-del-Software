package it.polimi.ingsw.message.action_message.ProductionMessage;

import it.polimi.ingsw.message.action_message.ActionMessage;
import it.polimi.ingsw.model.requirement.TradingRule;

import java.util.ArrayList;

public class ChooseTradingRules extends ActionMessage {
    ArrayList<TradingRule> chosenTradingRules;

    public void setChosenTradingRules(ArrayList<TradingRule> chosenTradingRules) {
        this.chosenTradingRules = chosenTradingRules;
    }

    public ArrayList<TradingRule> getChosenTradingRules() {
        return chosenTradingRules;
    }
}
