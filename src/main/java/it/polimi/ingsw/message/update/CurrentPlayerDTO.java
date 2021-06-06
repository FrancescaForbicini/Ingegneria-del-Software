package it.polimi.ingsw.message.update;

import it.polimi.ingsw.model.turn_taker.Player;

public class CurrentPlayerDTO extends UpdateMessageDTO{
    private Player currentPlayer;


    public CurrentPlayerDTO(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }
}
