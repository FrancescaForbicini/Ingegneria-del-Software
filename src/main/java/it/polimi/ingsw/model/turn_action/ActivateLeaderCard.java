package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;

public class ActivateLeaderCard implements TurnAction{
    private final LeaderCard leaderCardToActive;

    public ActivateLeaderCard(LeaderCard leaderCardToActive){
        this.leaderCardToActive = leaderCardToActive;
    }
    public LeaderCard getLeaderCardToActivate() {
        return leaderCardToActive;
    }

    @Override
    public void play(Player player) {
        player.activateLeaderCard(leaderCardToActive);
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
