package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;

public class ActivateLeaderCard implements TurnAction{
    private final LeaderCard leaderCardToActivate;

    public ActivateLeaderCard(LeaderCard leaderCardToActivate){
        this.leaderCardToActivate = leaderCardToActivate;
    }
    public LeaderCard getLeaderCardToActivate() {
        return leaderCardToActivate;
    }

    @Override
    public void play(Player player) {
        player.activateLeaderCard(leaderCardToActivate);
    }

}
