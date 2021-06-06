package it.polimi.ingsw.message.game_status;

import it.polimi.ingsw.message.MessageDTO;

// TODO this is not a UpdateMessageDTO
public class GameStatusDTO extends MessageDTO {
    private String winnerUsername;
    private GameStatus status;

    public GameStatusDTO(GameStatus status) {
        this.status = status;
    }

    public GameStatusDTO(String winnerUsername, GameStatus status) {
        this.winnerUsername = winnerUsername;
        this.status = status;
    }

    public String getWinnerUsername() {
        return winnerUsername;
    }

    public GameStatus getStatus() {
        return status;
    }
}
