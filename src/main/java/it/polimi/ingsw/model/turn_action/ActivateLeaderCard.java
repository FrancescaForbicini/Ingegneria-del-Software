package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.leader_cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;

/**
 * Activates a leader card
 */
public class ActivateLeaderCard implements TurnAction{
    private final LeaderCard leaderCardToActivate;

    public ActivateLeaderCard(LeaderCard leaderCardToActivate){
        this.leaderCardToActivate = leaderCardToActivate;
    }


    /**
     * Activates a leader card
     * @param player player that wants to activate the leader card
     */
    @Override
    public void play(Player player) {
        player.activateLeaderCard(leaderCardToActivate);
    }

}
