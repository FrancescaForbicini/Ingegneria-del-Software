package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ChosenLine;
import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.Warehouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface View {
    void setSceneAlreadySeen(boolean sceneAlreadySeen);
    boolean isSceneAlreadySeen();

    void startView();
    Optional<ClientAction> pickAnAction(ArrayList<ClientAction> actions);
    void showMarket(Market market);
    void showDevelopmentCards(ArrayList<DevelopmentCard> developmentCards);
    void showPlayer(ArrayList<ClientPlayer> players, FaithTrack faithTrack);
    String askIP();
    ClientPlayer askCredentials();
    ArrayList<LeaderCard> pickStartingLeaderCards(List<LeaderCard> proposedCards);
    LeaderCard pickLeaderCard(List<LeaderCard> proposedCards);
    ArrayList<ResourceType> pickStartingResources(int numberOfResources);
    void showStart() throws IOException;
    void showMessage(String message);
    ArrayList<DevelopmentCard> chooseDevelopmentCards(ArrayList<DevelopmentCard> developmentCards);
    ArrayList<ResourceType> chooseResourcesAny(int chosenInputAny);
    Map<ResourceType,Integer> inputFromStrongbox(Map<ResourceType,Integer> resources) ;
    Map<ResourceType,Integer> inputFromWarehouse (Map<ResourceType,Integer> resources) ;
    DevelopmentCard buyDevelopmentCards(ArrayList<DevelopmentCard> cards);
    int chooseSlot() ;
    Map<ResourceType,Integer> sortWarehouse(Warehouse warehouse) ;
    ChosenLine chooseLine();
    ArrayList<ResourceType> chooseWhiteMarble(int amount, ArrayList<ResourceType> activeWhiteConversions);
    Map<ResourceType,Integer> resourceToDepot(ArrayList<ResourceType> resources, Warehouse warehouse);
    void notifyNewActions();

    void showWinner(String winnerUsername);
}
