package it.polimi.ingsw.client.turn_taker;

import it.polimi.ingsw.model.faith.FaithTrack;

/**
 * Representation of Player and Opponent client side
 */
public abstract class ClientTurnTaker {
    private FaithTrack faithTrack;
    private final String username;

    public String getUsername() {
        return username;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public ClientTurnTaker(String username) {
        this.username = username;
    }

    public void setFaithTrack(FaithTrack faithTrack) {
        this.faithTrack = faithTrack;
    }
}
