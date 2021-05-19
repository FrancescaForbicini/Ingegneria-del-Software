package it.polimi.ingsw.view;

import it.polimi.ingsw.client.ClientAction;
import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.Warehouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

public interface View {

    void start();
    ClientAction pickAnAction(ConcurrentLinkedDeque<ClientAction> actions);
    void showMarket(Market market);
    void showDevelopmentCards(ArrayList<DevelopmentCard> developmentCards);
    void showFaithTrack(FaithTrack faithTrack);
    void showPlayer(ArrayList<ClientPlayer> players);
    String askUsername();
    String askGameID();
    String askIP();
    List<LeaderCard> pickLeaderCards(List<LeaderCard> proposedCards) throws IOException;
    void startGame();
    void errorStartGame();
    ArrayList<LeaderCard> activeLeaderCards(List <LeaderCard> leaderCards) ;
    ArrayList<LeaderCard> discardLeaderCards(List <LeaderCard> leaderCards);
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
    Map<ResourceType,Integer> resourceToDepot(ArrayList<ResourceType> resources);
}
