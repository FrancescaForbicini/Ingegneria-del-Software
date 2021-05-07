package it.polimi.ingsw.message.action_message.development_message;

import it.polimi.ingsw.message.action_message.ActionMessage;

public class ChooseSlot extends ActionMessage {
    int slotID;

    public void setSlotID(int slotID) {
        this.slotID = slotID;
    }

    public int getSlotID() {
        return slotID;
    }
}
