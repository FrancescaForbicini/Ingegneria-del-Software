package it.polimi.ingsw.message.action_message;


import it.polimi.ingsw.message.MessageDTO;

public abstract class ActionMessageDTO extends MessageDTO {
    public abstract String getRelatedAction();
}
