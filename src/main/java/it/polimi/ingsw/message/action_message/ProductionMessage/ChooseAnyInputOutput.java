package it.polimi.ingsw.message.action_message.ProductionMessage;

import it.polimi.ingsw.message.action_message.ActionMessage;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;

public class ChooseAnyInputOutput extends ActionMessage {
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
