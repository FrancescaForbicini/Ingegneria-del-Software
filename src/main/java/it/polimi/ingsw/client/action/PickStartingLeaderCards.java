package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.PickStartingLeaderCardsDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PickStartingLeaderCards extends ClientAction{
    public PickStartingLeaderCards(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        PickStartingLeaderCardsDTO loginMessageDTO = (PickStartingLeaderCardsDTO) clientGameObserverProducer.getPendingTurnDTOs().pop();
        List<LeaderCard> givenCards = loginMessageDTO.getCards();
        int alreadyPickedCardIndex = -1;
        ArrayList<LeaderCard> proposedLeaderCards = new ArrayList<>();
        ArrayList<LeaderCard> pickedLeaderCards = new ArrayList<>();
        view.showMessage("Pick your first Leader Card: ");
        alreadyPickedCardIndex = view.pickStartingLeaderCards(givenCards);
        pickedLeaderCards.add(givenCards.get(alreadyPickedCardIndex));
        for(int i=0; i<givenCards.size(); i++){
            if(i!=alreadyPickedCardIndex){
                proposedLeaderCards.add(givenCards.get(i));
            }
        }
        view.showMessage("Pick your second Leader Card: ");
        pickedLeaderCards.add(proposedLeaderCards.get(view.pickStartingLeaderCards(proposedLeaderCards)));
        clientConnector.sendMessage(new PickStartingLeaderCardsDTO(pickedLeaderCards));
    }

    @Override
    public void consumeFrom(ConcurrentLinkedDeque<ClientAction> from) {
        from.remove(this);
    }
}
