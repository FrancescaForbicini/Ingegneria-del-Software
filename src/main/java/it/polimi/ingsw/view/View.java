package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ChosenLine;
import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

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
    void showPlayer(ClientPlayer player, boolean isItself,ArrayList<LeaderCard> nonActiveLeaderCards);
    String askIP();
    Credentials askCredentials();
    int pickStartingLeaderCards(List<LeaderCard> proposedCards);
    int pickLeaderCard(List<LeaderCard> proposedCards);
    ResourceType pickStartingResources();
    void showStart() throws IOException;
    void showMessage(String message);
    ArrayList<DevelopmentCard> chooseDevelopmentCards(ArrayList<DevelopmentCard> developmentCards);
    ArrayList<ResourceType> chooseResourcesAny(int chosenInputAny);
    int chooseResource(ArrayList<ResourceType> resourcesToChoose);
    ResourceType chooseResource();
    Map<String,Integer> inputFrom(int quantityFromStrongbox, int quantityFromWarehouse, ResourceType resourceType, int total);
    DevelopmentCard buyDevelopmentCards(ArrayList<DevelopmentCard> cards);
    int chooseSlot() ;
    ChosenLine chooseLine(Market market);
    ResourceType chooseWhiteMarble(ArrayList<ResourceType> activeWhiteConversions);
    void notifyNewActions();
    public int choose (List<?> elemsToChoose);
    public int chooseDepot(ArrayList<WarehouseDepot> depotsToChoose, Warehouse warehouse);
    public int choosePlayer(ArrayList<ClientPlayer> playersToChoose);

        void showWinner(String winnerUsername);
}
