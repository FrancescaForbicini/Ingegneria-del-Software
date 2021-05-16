package it.polimi.ingsw.message.action_message.development_message;

import it.polimi.ingsw.message.action_message.ActionMessageDTO;

public class ChooseSlot extends ActionMessageDTO {
    private static final long serialVersionUID = -7168211724929865349L;
    int slotID;

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public int getSlotID() {
        return slotID;
    }
}
