package it.polimi.ingsw.message.update;

public class TurnMessageDTO extends UpdateMessageDTO {
    private String turnOf;
    private boolean isGameEnded;

    public String getTurnOf() {
        return turnOf;
    }

    public void setTurnOf(String turnOf) {
        this.turnOf = turnOf;
    }

    public boolean isGameEnded() {
        return isGameEnded;
    }

    public void setGameEnded(boolean gameEnded) {
        isGameEnded = gameEnded;
    }
}
