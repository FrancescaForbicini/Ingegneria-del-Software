package it.polimi.ingsw.view;

import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface View {

    void start();
    String askUsername();
    String askGameID();
    String askIP();
    List<LeaderCard> pickLeaderCards(List<LeaderCard> proposedCards) throws IOException;
    void startGame();
    void errorStartGame();
    void waitingPlayers();
    MessageDTO chooseLeaderOrNormalAction() throws IOException;
    ArrayList<LeaderCard> chooseLeaderAction(List <LeaderCard> leaderCards) throws IOException;
    TurnActionMessageDTO chooseTurnAction() throws IOException;
    ArrayList<TradingRule> chooseTradingRulesToActivate(ArrayList<TradingRule> activeTradingRules) throws IOException;
    ArrayList<ResourceType> chooseAnyInput(ArrayList <ResourceType> chosenInputAny);
    ArrayList<ResourceType> chooseAnyOutput(ArrayList <ResourceType> chosenOutputAny);
    Map<ResourceType,Integer> inputFromStrongbox(Map<ResourceType,Integer> resources) throws IOException;
    Map<ResourceType,Integer> inputFromWarehouse (Map<ResourceType,Integer> resources) throws IOException;
    DevelopmentCard buyDevelopmentCards(ArrayList<ArrayList<Deck<DevelopmentCard>>> decks) throws IOException;
    int chooseSlot() throws IOException;
    Map<String,Integer> chooseLine() throws IOException;
    ArrayList chooseResourceAny (ArrayList<ResourceType> resources, ArrayList<ResourceType> activatedWhiteMarbles);
    Map<ResourceType,Integer> resourceToDepot(ArrayList<ResourceType> resources) throws IOException;
}
