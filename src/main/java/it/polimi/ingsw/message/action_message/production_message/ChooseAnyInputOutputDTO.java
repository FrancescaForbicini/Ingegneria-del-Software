package it.polimi.ingsw.message.action_message.production_message;

import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;

public class ChooseAnyInputOutputDTO extends ActionMessageDTO {
    private final ArrayList<ResourceType> chosenInputAny;
    private final ArrayList<ResourceType> chosenOutputAny;

    public ChooseAnyInputOutputDTO(ArrayList<ResourceType> chosenInputAny, ArrayList<ResourceType> chosenOutputAny) {
        this.chosenInputAny = chosenInputAny;
        this.chosenOutputAny = chosenOutputAny;
    }

    public ArrayList<ResourceType> getChosenInputAny() {
        return chosenInputAny;
    }

    public ArrayList<ResourceType> getChosenOutputAny() {
        return chosenOutputAny;
    }

    @Override
    public String getRelatedAction() {
        return null;
    }
}
