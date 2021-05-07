package it.polimi.ingsw.message.action_message.MarketMessage;

import it.polimi.ingsw.message.action_message.ActionMessage;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;

public class ChooseResourceAny extends ActionMessage {
    ArrayList<ResourceType> chosenResourceAny;

    public void setChosenResourceAny(ArrayList<ResourceType> chosenResourceAny) {
        this.chosenResourceAny = chosenResourceAny;
    }

    public ArrayList<ResourceType> getChosenResourceAny() {
        return chosenResourceAny;
    }
}
