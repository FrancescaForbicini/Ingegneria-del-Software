package it.polimi.ingsw.message.action_message.development_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;

public class ChooseSlotDTO extends TurnActionMessageDTO {
    int slotID;

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public int getSlotID() {
        return slotID;
    }
}
