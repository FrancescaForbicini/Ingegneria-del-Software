package it.polimi.ingsw.client.action.leader;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.action_message.leader_message.ActivateLeaderCardDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;
import java.util.ArrayList;


public class ActivateLeaderCard extends LeaderAction {
    private final ArrayList<LeaderCard> leaderCardsEligible;
    private Player player;

    public ActivateLeaderCard(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        leaderCardsEligible = new ArrayList<>();
    }

    @Override
    public boolean isDoable() {
        player = clientGameObserverProducer.getCurrentPlayer();
        ArrayList<LeaderCard> cards = (ArrayList<LeaderCard>) player.getNonActiveLeaderCards();
        return cards.size() > 0 && cards.stream().anyMatch(leaderCard -> leaderCard.isEligible(player));
    }

    @Override
    public void doAction() {
        LeaderCard pickedLeaderCard;
        player = clientGameObserverProducer.getCurrentPlayer();
        for (LeaderCard leaderCard: player.getNonActiveLeaderCards()){
            if (leaderCard.isEligible(player))
                leaderCardsEligible.add(leaderCard);
        }
        if (leaderCardsEligible.size() == 1) {
            view.showMessage("You will active this leader card: ");
            view.showMessage(leaderCardsEligible.get(0).toString());
            pickedLeaderCard = leaderCardsEligible.get(0);
        }
        else{
            int pickedLeaderCardIndex = view.pickLeaderCard(leaderCardsEligible);
            pickedLeaderCard = leaderCardsEligible.get(pickedLeaderCardIndex);
        }
        ActionMessageDTO actionMessageDTO = new ActivateLeaderCardDTO(pickedLeaderCard);
        clientConnector.sendMessage(actionMessageDTO);
    }
}
