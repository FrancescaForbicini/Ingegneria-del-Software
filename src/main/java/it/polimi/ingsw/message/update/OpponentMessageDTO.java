package it.polimi.ingsw.message.update;

import it.polimi.ingsw.client.turn_taker.ClientOpponent;
import it.polimi.ingsw.client.turn_taker.ClientTurnTaker;
import it.polimi.ingsw.model.turn_taker.Opponent;


// TODO remove class
public class OpponentMessageDTO extends TurnTakerMessageDTO {
    private final ClientOpponent clientOpponent;


    public OpponentMessageDTO(Opponent opponent) {
        clientOpponent = new ClientOpponent(opponent.getUsername());
    }

    public ClientOpponent getClientOpponent() {
        return clientOpponent;
    }

    @Override
    public ClientTurnTaker getClientTurnTaker() {
        return getClientOpponent();
    }
}
