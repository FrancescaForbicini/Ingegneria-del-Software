package it.polimi.ingsw.client.action.starting;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.PickStartingLeaderCardsDTO;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Lets pick the starting Leader Cards
 */
public class PickStartingLeaderCards extends StartingAction {
    public PickStartingLeaderCards(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    /**
     * {@inheritDoc}
     *
     * Lets the user choose the first 2 Leader Cards to take among a list of 4 randomly drawn from a deck
     *
     */
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
}
