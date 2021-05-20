package it.polimi.ingsw.message.action_message.production_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.requirement.TradingRule;

import java.util.ArrayList;

public class ChooseTradingRulesDTO extends TurnActionMessageDTO {
    private final ArrayList<TradingRule> chosenTradingRules;

    public ChooseTradingRulesDTO(ArrayList<TradingRule> chosenTradingRules) {
        this.chosenTradingRules = chosenTradingRules;
    }

    public ArrayList<TradingRule> getChosenTradingRules() {
        return chosenTradingRules;
    }
}
