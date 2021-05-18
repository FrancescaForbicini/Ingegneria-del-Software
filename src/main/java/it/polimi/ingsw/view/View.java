package it.polimi.ingsw.view;

import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.warehouse.Warehouse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface View {

    void start();
    MessageDTO show();
    String askUsername();
    String askGameID();
    String askIP();
    List<LeaderCard> pickLeaderCards(List<LeaderCard> proposedCards) throws IOException;
    void startGame();
    void errorStartGame();
    void waitingPlayers();
    MessageDTO chooseLeaderOrNormalAction() ;
    ArrayList<LeaderCard> chooseLeaderAction(List <LeaderCard> leaderCards) ;
    TurnActionMessageDTO chooseTurnAction() ;
    ArrayList<TradingRule> chooseTradingRulesToActivate(ArrayList<TradingRule> activeTradingRules);
    ArrayList<ResourceType> chooseAnyInput(ArrayList <ResourceType> chosenInputAny);
    ArrayList<ResourceType> chooseAnyOutput(ArrayList <ResourceType> chosenOutputAny);
    Map<ResourceType,Integer> inputFromStrongbox(Map<ResourceType,Integer> resources) ;
    Map<ResourceType,Integer> inputFromWarehouse (Map<ResourceType,Integer> resources) ;
    DevelopmentCard buyDevelopmentCards(ArrayList<ArrayList<Deck<DevelopmentCard>>> decks);
    int chooseSlot() ;
    boolean sortWarehouse();
    Warehouse sortWarehouse(Warehouse warehouse) ;
    Map<String,Integer> chooseLine();
    ArrayList<ResourceType> chooseResourceAny (ArrayList<ResourceType> resources, ArrayList<ResourceType> activatedWhiteMarbles);
    Map<ResourceType,Integer> resourceToDepot(ArrayList<ResourceType> resources);
}
