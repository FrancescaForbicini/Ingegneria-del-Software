package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;

public class ChooseResourceAny extends TurnActionMessageDTO {
    private static final long serialVersionUID = 6108998096018892428L;
    ArrayList<ResourceType> chosenResourceAny;


    public void setChosenResourceAny(ArrayList<ResourceType> chosenResourceAny) {
        this.chosenResourceAny = chosenResourceAny;
    }

    public ArrayList<ResourceType> getChosenResourceAny() {
        return chosenResourceAny;
    }


}
