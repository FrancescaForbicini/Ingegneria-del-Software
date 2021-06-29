package it.polimi.ingsw.message.action_message.production_message;

import it.polimi.ingsw.client.action.turn.ActivateProduction;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.cards.AdditionalTradingRule;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;
import java.util.Map;

public class ActivateProductionDTO extends ActionMessageDTO {
    private final ArrayList<DevelopmentCard> developmentCardsChosen;
    private final ArrayList<AdditionalTradingRule> additionalTradingRulesChosen;
    private final Map<ResourceType,Map<Integer,Integer>> inputChosenFromWarehouse;
    private final Map<ResourceType,Integer> inputChosenFromStrongbox;
    private final ArrayList<ResourceType> inputAny;
    private final ArrayList<ResourceType> outputAny;
    public ActivateProductionDTO(ArrayList<DevelopmentCard> developmentCardsChosen, ArrayList<AdditionalTradingRule> additionalTradingRulesChosen, Map<ResourceType, Map<Integer,Integer>> inputChosenFromWarehouse, Map<ResourceType, Integer> inputChosenFromStrongbox, ArrayList<ResourceType> inputAny, ArrayList<ResourceType> outputAny) {
        this.inputChosenFromWarehouse = inputChosenFromWarehouse;
        this.inputChosenFromStrongbox = inputChosenFromStrongbox;
        this.developmentCardsChosen = developmentCardsChosen;
        this.additionalTradingRulesChosen = additionalTradingRulesChosen;
        this.inputAny = inputAny;
        this.outputAny = outputAny;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCardsChosen() {
        return developmentCardsChosen;
    }

    public ArrayList<AdditionalTradingRule> getAdditionalTradingRulesChosen() {
        return additionalTradingRulesChosen;
    }

    public Map<ResourceType, Map<Integer,Integer>> getInputChosenFromWarehouse() {
        return inputChosenFromWarehouse;
    }

    public Map<ResourceType, Integer> getInputChosenFromStrongbox() {
        return inputChosenFromStrongbox;
    }

    public ArrayList<ResourceType> getInputAny(){return inputAny;};
    public ArrayList<ResourceType> getOutputAny(){return outputAny;};
    @Override
    public String getRelatedAction() {
        return ActivateProduction.class.getName();
    }
}
