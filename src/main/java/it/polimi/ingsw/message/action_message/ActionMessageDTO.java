package it.polimi.ingsw.message.action_message;


import it.polimi.ingsw.message.MessageDTO;

// TODO remove DTO from this names...n
public abstract class ActionMessageDTO extends MessageDTO {
    public abstract String getRelatedAction();
}
