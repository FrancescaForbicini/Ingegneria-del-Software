package it.polimi.ingsw.message.update;

public class GameFinishedDTO extends UpdateMessageDTO{
    private String winnerUsername;

    public String getWinnerUsername() {
        return winnerUsername;
    }
}
