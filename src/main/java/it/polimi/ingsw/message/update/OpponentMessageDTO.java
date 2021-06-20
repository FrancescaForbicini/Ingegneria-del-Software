package it.polimi.ingsw.message.update;

import it.polimi.ingsw.client.turn_taker.ClientOpponent;
import it.polimi.ingsw.client.turn_taker.ClientTurnTaker;

public class OpponentMessageDTO extends TurnTakerMessageDTO {
    private final ClientOpponent clientOpponent;


    public OpponentMessageDTO() {
        // TODO
        clientOpponent = new ClientOpponent();
    }

    public ClientOpponent getClientOpponent() {
        return clientOpponent;
    }

    @Override
    public ClientTurnTaker getClientTurnTaker() {
        return getClientOpponent();
    }
}
