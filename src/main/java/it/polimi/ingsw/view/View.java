package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
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
    void showAvailableActions(ArrayList<ClientAction> actions);
    Optional<ClientAction> pickAnAction(ArrayList<ClientAction> actions);
    void showMarket(Market market);
    void showDevelopmentCards(ArrayList<DevelopmentCard> developmentCards);
    void showPlayer(ArrayList<ClientPlayer> players, FaithTrack faithTrack);
    String askIP();
    ClientPlayer askCredentials();
    List<LeaderCard> pickLeaderCards(List<LeaderCard> proposedCards) throws IOException;
    ArrayList<ResourceType> pickStartingResources(int numberOfResources);
    void showStart() throws IOException;
    void showMessage(String message);
    LeaderCardChoice chooseLeaderCardAction();
    ArrayList<LeaderCard> pickLeaderCardToActivate(List <LeaderCard> leaderCards) ;
    ArrayList<LeaderCard> pickLeaderCardToDiscard(List <LeaderCard> leaderCards);
    ArrayList<TradingRule> chooseTradingRulesToActivate(ArrayList<TradingRule> activeTradingRules);
    ArrayList<ResourceType> chooseAnyInput(ArrayList <ResourceType> chosenInputAny);
    ArrayList<ResourceType> chooseAnyOutput(ArrayList <ResourceType> chosenOutputAny);
    Map<ResourceType,Integer> inputFromStrongbox(Map<ResourceType,Integer> resources) ;
    Map<ResourceType,Integer> inputFromWarehouse (Map<ResourceType,Integer> resources) ;
    DevelopmentCard buyDevelopmentCards(ArrayList<DevelopmentCard> cards);
    int chooseSlot() ;
    boolean askSortWarehouse();
    Warehouse sortWarehouse(Warehouse warehouse) ;
    Map<String,Integer> chooseLine();
    ArrayList<ResourceType> chooseResourceAny (ArrayList<ResourceType> resources, ArrayList<ResourceType> activatedWhiteMarbles);
    Map<ResourceType,Integer> resourceToDepot(ArrayList<ResourceType> resources,ClientPlayer player);
    void notifyNewActions();

    void showWinner(String winnerUsername);
}
