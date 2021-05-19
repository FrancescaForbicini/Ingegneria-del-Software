package it.polimi.ingsw.message.update;

public class TurnMessageDTO extends UpdateMessageDTO {
    private String turnOf;

    public String getTurnOf() {
        return turnOf;
    }

    public void setTurnOf(String turnOf) {
        this.turnOf = turnOf;
    }
}
