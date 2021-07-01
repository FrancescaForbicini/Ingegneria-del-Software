package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.action.turn.ChosenLine;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface View {

    void startView();
    Optional<ClientAction> pickAnAction(ArrayList<ClientAction> actions);
    void showMarket(Market market);
    void showDevelopmentCards(ArrayList<DevelopmentCard> developmentCards);
    void showPlayer(ClientPlayer player);
    String askIP();
    Credentials askCredentials();
    int pickStartingLeaderCards(List<LeaderCard> proposedCards);
    int pickLeaderCard(List<LeaderCard> proposedCards);
    ResourceType pickStartingResources();
    boolean showMessage(String message);
    int chooseResource(ArrayList<ResourceType> resourcesToChoose);
    ResourceType chooseResource();
    boolean chooseAnotherProduction();
    int chooseProductionToActivate(ArrayList<Eligible> availableProductions);
    int buyDevelopmentCards(ArrayList<DevelopmentCard> cards);
    int chooseSlot(ArrayList<DevelopmentSlot> slotsAvailable) ;
    ChosenLine chooseLine(Market market);
    int chooseWhiteMarble(ArrayList<ResourceType> activeWhiteConversions);
    boolean notifyNewActions();
    int chooseDepot(ArrayList<WarehouseDepot> depotsToChoose);
    int choosePlayer(ArrayList<ClientPlayer> clientPlayersToChoose);
    void showWinner(String winnerUsername);
    int chooseQuantity(int maxQuantity);
    void inject(ClientGameObserverProducer gameObserverProducer);
}
