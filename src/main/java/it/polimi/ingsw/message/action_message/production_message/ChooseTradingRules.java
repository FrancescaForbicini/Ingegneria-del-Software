package it.polimi.ingsw.message.action_message.production_message;

import it.polimi.ingsw.message.action_message.ActionMessage;
import it.polimi.ingsw.model.requirement.TradingRule;

import java.util.ArrayList;

public class ChooseTradingRules extends ActionMessage {
    private static final long serialVersionUID = -8924572816639300538L;
    ArrayList<TradingRule> chosenTradingRules;

    public void setChosenTradingRules(ArrayList<TradingRule> chosenTradingRules) {
        this.chosenTradingRules = chosenTradingRules;
    }

    public ArrayList<TradingRule> getChosenTradingRules() {
        return chosenTradingRules;
    }
}
