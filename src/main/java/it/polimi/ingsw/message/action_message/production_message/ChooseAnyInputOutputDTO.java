package it.polimi.ingsw.message.action_message.production_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;

public class ChooseAnyInputOutputDTO extends TurnActionMessageDTO {
    ArrayList<ResourceType> chosenInputAny;
    ArrayList<ResourceType> chosenOutputAny;

    public void setChosenInputAny(ArrayList<ResourceType> chosenInputAny) {
        this.chosenInputAny = chosenInputAny;
    }

    public void setChosenOutputAny(ArrayList<ResourceType> chosenOutputAny) {
        this.chosenOutputAny = chosenOutputAny;
    }

    public ArrayList<ResourceType> getChosenInputAny() {
        return chosenInputAny;
    }

    public ArrayList<ResourceType> getChosenOutputAny() {
        return chosenOutputAny;
    }
}
