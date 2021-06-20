package it.polimi.ingsw.message.update;

import it.polimi.ingsw.client.turn_taker.ClientTurnTaker;

public abstract class TurnTakerMessageDTO extends UpdateMessageDTO {
    public abstract ClientTurnTaker getClientTurnTaker();
}
