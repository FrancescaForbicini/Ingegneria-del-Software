package it.polimi.ingsw.message.action_message.production_message;

import it.polimi.ingsw.client.action.turn.ActivateProduction;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;
import java.util.Map;

public class ActivateProductionDTO extends ActionMessageDTO {
    private final ArrayList<DevelopmentCard> developmentCardChosen;
    private final Map<ResourceType,Integer> inputChosenFromWarehouse;
    private final Map<ResourceType,Integer> inputChosenFromStrongbox;
    private final Map<ResourceType,Integer> totalOutput;
    private final Map<ResourceType,Integer> inputTakenFromWarehouse;
    public ActivateProductionDTO(ArrayList<DevelopmentCard> developmentCardChosen,Map<ResourceType, Integer> inputChosenFromWarehouse, Map<ResourceType, Integer> quantityInputFromWarehouse, Map<ResourceType, Integer> inputChosenFromStrongbox, Map<ResourceType,Integer> totalOutput) {
        this.inputChosenFromWarehouse = inputChosenFromWarehouse;
        this.inputChosenFromStrongbox = inputChosenFromStrongbox;
        this.developmentCardChosen = developmentCardChosen;
        this.totalOutput = totalOutput;
        this.inputTakenFromWarehouse = quantityInputFromWarehouse;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCardChosen() {
        return developmentCardChosen;
    }

    public Map<ResourceType, Integer> getInputChosenFromWarehouse() {
        return inputChosenFromWarehouse;
    }

    public Map<ResourceType, Integer> getInputChosenFromStrongbox() {
        return inputChosenFromStrongbox;
    }
    public Map<ResourceType, Integer> getTotalOutput() {
        return totalOutput;
    }

    public Map<ResourceType, Integer> getQuantityInputFromWarehouse() {
        return inputTakenFromWarehouse;
    }

    @Override
    public String getRelatedAction() {
        return ActivateProduction.class.getName();
    }
}
