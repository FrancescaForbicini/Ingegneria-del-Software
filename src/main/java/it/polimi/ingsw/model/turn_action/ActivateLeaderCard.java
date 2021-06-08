package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;

public class ActivateLeaderCard implements TurnAction{
    private final ArrayList<LeaderCard> leaderCardToActive;

    public ActivateLeaderCard(ArrayList<LeaderCard> leaderCardToActive){
        this.leaderCardToActive = leaderCardToActive;
    }
    public ArrayList<LeaderCard> getLeaderCardToActivate() {
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
