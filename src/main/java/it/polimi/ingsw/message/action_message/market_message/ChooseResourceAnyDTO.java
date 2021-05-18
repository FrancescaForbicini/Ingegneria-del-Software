package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;

public class ChooseResourceAnyDTO extends TurnActionMessageDTO {
    private ArrayList<ResourceType> chosenResourceAny;


    public void setChosenResourceAny(ArrayList<ResourceType> chosenResourceAny) {
        this.chosenResourceAny = chosenResourceAny;
    }

    public ArrayList<ResourceType> getChosenResourceAny() {
        return chosenResourceAny;
    }


}
