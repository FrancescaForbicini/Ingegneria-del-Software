package it.polimi.ingsw.message;

// TODO this is not a UpdateMessageDTO
public class GameFinishedDTO extends MessageDTO {
    private String winnerUsername;

    public String getWinnerUsername() {
        return winnerUsername;
    }
}
