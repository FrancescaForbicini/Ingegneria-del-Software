package it.polimi.ingsw.message.action_message.development_message;

import it.polimi.ingsw.message.action_message.ActionMessageDTO;

public class ChooseSlotDTO extends ActionMessageDTO {
    private final int slotID;

    public ChooseSlotDTO(int slotID) {
        this.slotID = slotID;
    }

    public int getSlotID() {
        return slotID;
    }

    @Override
    public String getRelatedAction() {
        return null;
    }
}
