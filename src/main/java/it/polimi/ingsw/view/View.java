package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ChosenLine;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface View {
    void setSceneAlreadySeen(boolean sceneAlreadySeen);
    boolean isSceneAlreadySeen();

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
    void showStart() throws IOException;
    void showMessage(String message);
    ArrayList<DevelopmentCard> chooseDevelopmentCards(ArrayList<DevelopmentCard> developmentCards);
    int chooseResource(ArrayList<ResourceType> resourcesToChoose);
    ResourceType chooseResource();
    boolean userWantToDoIt();
    int chooseAdditionalOrDevelopmentProduction(ArrayList<Eligible> developmentCardsAvailable, boolean oneUsed);
    int buyDevelopmentCards(ArrayList<DevelopmentCard> cards);
    int chooseSlot(ArrayList<Integer> slotsAvailable) ;
    ChosenLine chooseLine(Market market);
    int chooseWhiteMarble(ArrayList<ResourceType> activeWhiteConversions);
    void notifyNewActions();
    int choose (List<?> elemsToChoose);
    int chooseDepot(ArrayList<WarehouseDepot> depotsToChoose);
    int choosePlayer(ArrayList<ClientPlayer> clientPlayersToChoose);
    void showWinner(String winnerUsername);
    int chooseQuantity(int maxQuantity);
}
