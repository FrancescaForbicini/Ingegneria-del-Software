package it.polimi.ingsw.message.action_message.production_message;

import it.polimi.ingsw.client.action.turn.ActivateProduction;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;
import java.util.Map;

public class ActivateProductionDTO extends ActionMessageDTO {
    private  ArrayList<ResourceType> inputAnyChosen;
    private  ArrayList<ResourceType> outputAnyChosen;
    private  Map<ResourceType,Integer> inputChosenFromWarehouse;
    private  Map<ResourceType,Integer> inputChosenFromStrongbox;
    private ArrayList<DevelopmentCard> developmentCardChosen;

    public ActivateProductionDTO(ArrayList<ResourceType> inputAnyChosen, ArrayList<ResourceType> outputAnyChosen, Map<ResourceType, Integer> inputChosenFromWarehouse, Map<ResourceType, Integer> inputChosenFromStrongbox, ArrayList<DevelopmentCard> developmentCardChosen) {
        this.inputAnyChosen = inputAnyChosen;
        this.outputAnyChosen = outputAnyChosen;
        this.inputChosenFromWarehouse = inputChosenFromWarehouse;
        this.inputChosenFromStrongbox = inputChosenFromStrongbox;
        this.developmentCardChosen = developmentCardChosen;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCardChosen() {
        return developmentCardChosen;
    }

    public ArrayList<ResourceType> getInputAnyChosen() {
        return inputAnyChosen;
    }

    public ArrayList<ResourceType> getOutputAnyChosen() {
        return outputAnyChosen;
    }

    public Map<ResourceType, Integer> getInputChosenFromWarehouse() {
        return inputChosenFromWarehouse;
    }

    public Map<ResourceType, Integer> getInputChosenFromStrongbox() {
        return inputChosenFromStrongbox;
    }

    @Override
    public String getRelatedAction() {
        return ActivateProduction.class.getName();
    }
}
