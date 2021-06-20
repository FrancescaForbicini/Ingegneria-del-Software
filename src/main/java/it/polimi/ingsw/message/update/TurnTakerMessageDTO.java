package it.polimi.ingsw.message.update;

import it.polimi.ingsw.client.turn_taker.ClientTurnTaker;

// TODO remove class
public abstract class TurnTakerMessageDTO extends UpdateMessageDTO {
    public abstract ClientTurnTaker getClientTurnTaker();
}
