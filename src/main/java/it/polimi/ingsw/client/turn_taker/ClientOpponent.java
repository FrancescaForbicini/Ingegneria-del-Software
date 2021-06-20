package it.polimi.ingsw.client.turn_taker;

public class ClientOpponent extends ClientTurnTaker{
    private final String lastAction;

    public ClientOpponent(String username, String lastAction) {
        super(username);
        this.lastAction = lastAction;
    }

    public String getLastAction() {
        return lastAction;
    }
}
