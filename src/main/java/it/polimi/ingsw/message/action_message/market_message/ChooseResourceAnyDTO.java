package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;

public class ChooseResourceAnyDTO extends ActionMessageDTO {
    private final ArrayList<ResourceType> chosenResourceAny;

    public ChooseResourceAnyDTO(ArrayList<ResourceType> chosenResourceAny){
        this.chosenResourceAny = chosenResourceAny;
    }
    public ArrayList<ResourceType> getChosenResourceAny() {
        return chosenResourceAny;
    }


    @Override
    public String getRelatedAction() {
        return null;
    }
}
