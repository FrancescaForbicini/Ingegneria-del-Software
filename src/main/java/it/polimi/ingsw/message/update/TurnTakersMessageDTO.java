package it.polimi.ingsw.message.update;

import it.polimi.ingsw.client.turn_taker.ClientTurnTaker;

import java.util.List;

public class TurnTakersMessageDTO extends UpdateMessageDTO{
    private List<ClientTurnTaker> clientTurnTakers;

    public List<ClientTurnTaker> getClientTurnTakers() {
        return clientTurnTakers;
    }

    public TurnTakersMessageDTO(List<ClientTurnTaker> clientTurnTakers) {
        this.clientTurnTakers = clientTurnTakers;
    }
}
