package it.polimi.ingsw.view;

import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.LeaderActionMessageDTO;
import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.*;


public class CLI implements View{
    private final PrintStream out = new PrintStream(System.out,true);
    private final BufferedInputStream in = new BufferedInputStream(System.in);
    final String welcome = "WELCOME TO MASTERS OF RENAISSANCE";

    /**
     * Prints the message of welcome to the player
     */
    @Override
    public void start(){
        out.println(welcome);
    }

    /**
     * Asks the username to the player
     * @return the userame of the player
     */
    @Override
    public String askUsername(){
        String username = null;
        while (username == null || username.equals("")){
            out.println("Enter your username: ");
            username = in.toString();
        }
        return username;
    }

    /**
     * Asks the ID of the game to the player
     * @return the ID of the game
     */
    @Override
    public String askGameID(){
        String ID = null;
        while (ID == null || ID.equals("")){
            out.println("Enter GAME ID: ");
            ID = in.toString();
        }
        return ID;
    }

    /**
     * Asks the IP of connection
     * @return the IP of the connection
     */
    @Override
    public String askIP(){
        String IP = null;
        while (IP == null || IP.equals("")){
            out.println("Enter IP: ");
            IP = in.toString();
        }
        return IP;
    }

    /**
     * Picks the two leader cards
     * @param proposedCards the four leader cards proposed to the client
     * @return the leader cards chosen
     * @throws IOException
     */
    @Override
    public List<LeaderCard> pickLeaderCards(List<LeaderCard> proposedCards) throws IOException {
        List<LeaderCard> pickedCards = new ArrayList<>();
        int choose;
        System.out.printf("Proposed cards: %s%n", proposedCards);
        out.println("Choose the first leader card: ");
        choose = in.read();
        pickedCards.add(proposedCards.get(choose-1));
        out.println("Choose the second leader card: ");
        choose = in.read();
        pickedCards.add(proposedCards.get(choose-1));
        return pickedCards;
    }

    /**
     * Prints that the game is starting
     */
    @Override
    public void startGame(){
        out.println("START GAME");
    }

    /**
     * Prints that there is an error
     */
    @Override
    public void errorStartGame(){
        out.println("ERROR START GAME");
    }

    /**
     * Prints that the client has to wait other players
     */
    @Override
    public void waitingPlayers(){
        out.println("Wait for players");
    }


    /**
     * Chooses to do a leader card action or a normal action
     * @return if to do a leader card action or a normal action
     * @throws IOException
     */
    @Override
    public MessageDTO chooseLeaderOrNormalAction() throws IOException {
        int response = 0;
        out.println("Choose one of this action: 1.Leader Action \n 2.Normal Action\n");
        response = in.read();
        while (response != 1 && response != 2 ){
            out.println("ERROR! Choose one of this action: 1.Leader Action \n 2.Normal Action\n");
            response = in.read();
        }
        if (response == 1)
            return new LeaderActionMessageDTO();
        else
            return new TurnActionMessageDTO();
    }

    /**
     * Chooses the leader cards to activate
     * @param leaderCards the leader cards available
     * @return the leader cards activated
     * @throws IOException
     */
    @Override
    public ArrayList<LeaderCard> chooseLeaderAction(List <LeaderCard> leaderCards) throws IOException {
        int response = 0;
        String secondLeaderCard = null;
        ArrayList <LeaderCard> leaderCardChosen = new ArrayList<>();
        out.println("Which leader cards do you want to activate: \n");
        out.print("\n 1. %s  \n 2. %s "+leaderCards.get(0) + leaderCards.get(1));
        while (response != 1 && response != 2){
            response = in.read();
        }
        out.println("Do you want activate another leader card ?");
        while (secondLeaderCard == null || !secondLeaderCard.equalsIgnoreCase("yes") && !secondLeaderCard.equalsIgnoreCase("no")){
            out.println("Enter 'yes' or 'no' : ");
            secondLeaderCard = in.toString();
        }
        if (secondLeaderCard.equalsIgnoreCase("yes"))
            leaderCardChosen.addAll(leaderCards);
        else
            leaderCardChosen.add(leaderCards.get(response-1));
        return leaderCardChosen;
    }

    /**
     * Chooses the action to do in a turn
     * @return the action chosen
     * @throws IOException
     */
    @Override
    public TurnActionMessageDTO chooseTurnAction() throws IOException {
        int response = -1;
        TurnActionMessageDTO turnActionMessageDTO;
        out.println("Choose one of these actions \n 1.ActivateProduction \n 2.BuyDevelopmentCards \n 3.TakeFromMarket");
        response = in.read();
        // TODO carattere -> escape code
        while (response<=0 || response >3) {
            out.println("Choose another action: ");
            response = in.read();
        }
        switch(response){
            case 1:
                return new it.polimi.ingsw.message.action_message.production_message.ActivateProduction();
            case 2:
                return new it.polimi.ingsw.message.action_message.development_message.BuyDevelopmentCard();
            case 3:
                return new it.polimi.ingsw.message.action_message.market_message.TakeFromMarket();
            default:
                return null;
            }
    }


    // ACTIVATE PRODUCTION
    /**
     * Chooses the trading rules that has to be activated
     * @param activeTradingRules the trading rules available
     * @return the trading rules that activated
     * @throws IOException
     */
    @Override
    public ArrayList<TradingRule> chooseTradingRulesToActivate(ArrayList<TradingRule> activeTradingRules) throws IOException {
        ArrayList <TradingRule> chosenTradingRules = new ArrayList<>();
        int response = 0;
        out.println(activeTradingRules);
        //TODO escape code
        while (response != -1 || chosenTradingRules.size() < activeTradingRules.size()){
            out.println("Which productions do you want to activate? If you want to stop to choose you can insert '-1'\n ");
            response = in.read();
            chosenTradingRules.add(activeTradingRules.get(response-1));
        }
        return chosenTradingRules;
    }


    /**
     * Chooses the resources from the strongbox to use a production
     * @param resources the type of the resources and the amount
     * @return the type of the resources and the amount that has been chosen from the strongbox
     * @throws IOException
     */
    @Override
    public Map<ResourceType,Integer> inputFromStrongbox(Map<ResourceType,Integer> resources) throws IOException {
        int amount;
        String resource = null;
        ResourceType resourceType;
        Map<ResourceType,Integer> resourcesChosen = new HashMap<>();
        out.println("In order to stop to select resources from strongbox you can write 'stop'\n");
        while (!resource.equals("stop")){
            out.println("These are the resources from the strongbox: ");
            out.print(resources);
            out.println("Which resource do you want to choose from strongbox: ");
            resource = in.toString();
            out.printf("Choose the amount of %s to take from strongbox: ",resource);
            amount = in.read();
            resource = resource.toLowerCase();
            resourceType = convertResource(resource);
            if (resourceType != null){
                resourcesChosen.put(resourceType,amount);
            }
        }
        return resourcesChosen;
    }

    /**
     * Chooses the resources from the warehouse to use a production
     * @param resources the type of the resources and the amount
     * @return the type of the resources and the depot that has been chosen from the warehouse
     * @throws IOException
     */
    @Override
    public Map<ResourceType,Integer> inputFromWarehouse (Map<ResourceType,Integer> resources) throws IOException {
        int depotID;
        String resource = null;
        ResourceType resourceType;
        Map<ResourceType,Integer> resourcesChosen = new HashMap<>();
        out.println("In order to stop to select resources from warehouse you can write 'stop'\n");
        while (!resource.equals("stop")){
            out.println("These are the resources from the warehouse: ");
            out.print(resources);
            out.println("Which resource do you want to choose from warehouse: ");
            resource = in.toString();
            out.printf("Choose the depot of %s to take from warehouse: ",resource);
            depotID = in.read();
            out.println("Choose the depot from where to take ");
            resourceType = convertResource(resource);
            if (resourceType != null)
                resourcesChosen.put(resourceType,depotID);
        }
        return resourcesChosen;
    }

    /**
     * Chooses the resource type not specified in the input of a production
     * @param chosenInputAny the amount of resource to choose
     * @return the resources chosen
     */
    @Override
    public ArrayList<ResourceType> chooseAnyInput(ArrayList <ResourceType> chosenInputAny){
        ArrayList<ResourceType> inputAny = new ArrayList<>();
        ResourceType resourceType = null;
        out.println("You have to decide %d resources"+chosenInputAny.size());
        for (ResourceType resource : chosenInputAny){
                out.println("Choose a resource to decide the input of the production:  ");
                while (resourceType == null){
                    resourceType= convertResource(in.toString());
                }
                inputAny.add(resourceType);
                resourceType = null;
            }
        return inputAny;
    }

    /**
     * Chooses the resource type not specified in the output of a production
     * @param chosenOutputAny the amount of resource to choose
     * @return the resources chosen
     */
    @Override
    public ArrayList<ResourceType> chooseAnyOutput(ArrayList <ResourceType> chosenOutputAny){
        ArrayList<ResourceType> outputAny = new ArrayList<>();
        ResourceType resourceType = null;
        out.println("You have to decide %d resources"+chosenOutputAny.size());
        for (ResourceType resource : chosenOutputAny){
            out.println("Choose a resource to decide the input of the production:  ");
            while (resourceType == null){
                resourceType= convertResource(in.toString());
            }
            outputAny.add(resourceType);
            resourceType = null;
        }
        return outputAny;
    }

    // BUY DEVELOPMENT CARDS
    /**
     * Chooses which development card has to be bought
     * @return the color and the level of the development card
     * @throws IOException
     */
    @Override
    public DevelopmentCard buyDevelopmentCards(ArrayList<ArrayList<Deck<DevelopmentCard>>> decks) throws IOException {
        DevelopmentCard card = null;
        int row = 0;
        int column = 0;
        out.println("This are the development cards available");
        for (int i = 0;i < 4; i++){
            for (int j = 0; j < 3; j++)
                out.print(decks.get(i).get(j).showFirstCard());
            out.print("\n");
        }
        while (card == null){
            out.println("Choose the card that you want to buy");
            while (column<=0 || column>4){
                out.println("Enter the column : ");
                column = in.read();
            }
            while (row<=0 || row>3){
                out.println("Enter the row : ");
                row = in.read();
            }
            card = decks.get(column).get(row).showFirstCard().get();
        }
        return decks.get(column).get(row).drawFirstCard().get();
    }

    @Override
    public int chooseSlot() throws IOException {
        int slot = 0;
        while (slot<= 0 || slot>3){
            out.println("Choose the slot where you want to put the development card bought: ");
            slot = in.read();
        }
        return slot;
    }

    //TAKE FROM MARKET
    /**
     * Chooses if the player wants to select a row or a column and the its number
     * @throws IOException
     */
    @Override
    public Map <String,Integer> chooseLine() throws IOException {
        Map <String,Integer> response = new HashMap<>();
        String rc = null;
        int num = 5;
        int numMax = 0;
        while (rc == null || !rc.equalsIgnoreCase("row") && !rc.equalsIgnoreCase("column")){
            out.println("Choose the 'row' or the 'column'");
            rc = in.toString();
        }
        if (rc.equalsIgnoreCase("row"))
            numMax = 3;
        else
            numMax = 4;
        while (num>numMax || num<=0) {
            out.printf("Enter the number of the %s", rc);
            num = in.read();
        }
        response.put(rc,num);
        return response;
    }

    /**
     * Puts the resources taken from the market to warehouse
     * @param resources the resources taken from the market
     * @return the resources and the depot to put in the warehouse
     */
    @Override
    public Map<ResourceType,Integer> resourceToDepot(ArrayList<ResourceType> resources) throws IOException {
        int i =0;
        int depot = 0;
        String resourceAnyChosen = null;
        ResourceType resourceType = null;
        Map<ResourceType,Integer> resourcesSet = new HashMap<>();
        while (i < resources.size()){
            if (resources.get(i).equals(ResourceType.Any)){
                while (resourceAnyChosen == null){
                    out.println("Choose the resource type : ");
                    resourceAnyChosen = in.toString();
                }
                resourceType = convertResource(resourceAnyChosen);
            }
            else
                resourceType = resources.get(i);
            out.printf("Enter the depot where you want put the %s",resourceType);
            while (depot <=0 || depot>3){
                depot = in.read();
            }
            resourcesSet.replace(resourceType,resourcesSet.get(resourceType),depot);
            i++;
            resourceType = null;
        }
        return resourcesSet;
    }

    /**
     * Sets the white marble with the resources chosen from the player based on the the conversions available
     * @param resources the white marbles to convert
     * @param activatedWhiteMarbles the conversions available
     * @return the resources and the amount that has been converted
     */
    @Override
    public ArrayList chooseResourceAny (ArrayList<ResourceType> resources, ArrayList<ResourceType> activatedWhiteMarbles){
        int i = 0;
        ResourceType resourceType = null;
        ArrayList resourcesChosen = new ArrayList();
        out.println("Choose the conversion of the white marbles activated");
        out.println("CONVERSION AVAILABLE: ");
        out.print(activatedWhiteMarbles);
        while (i<resources.size()){
            out.println("Choose the resource to convert a white marble: ");
            while (activatedWhiteMarbles.stream().noneMatch(resourceType::equals)){
                out.println("Choose the resources correct from your conversions available");
                resourceType = convertResource(in.toString());
            }
            resourcesChosen.add(resourceType);
            resourceType = null;
            i++;
        }
        return resourcesChosen;
    }

    /**
     * Converts the resource written by the player to the resource type
     * @param resource resource chosen by the player
     * @return the resource type that match with the resource chosen by the player
     */
    private ResourceType convertResource(String resource){
        resource = resource.toLowerCase();
        switch(resource){
            case "shields" :
                return ResourceType.Shields;
            case "coins" :
                return ResourceType.Coins;
            case "servants" :
                return ResourceType.Servants;
            case "stones":
                return ResourceType.Stones;
            default:
                out.println("Error");
                return null;
        }
    }

    /**
     * Converts the color inserted by the player into a DevelopmentColor
     * @param color the color inserted by the player
     * @return the Development Color corresponded to the color inserted
     */
    private DevelopmentColor convertColor(String color){
        color = color.toLowerCase();
        switch(color){
            case "yellow":
                return DevelopmentColor.Yellow;
            case "purple" :
                return DevelopmentColor.Purple;
            case "blue":
                return DevelopmentColor.Blue;
            case "green":
                return DevelopmentColor.Green;
            default:
                out.println("Error");
                return null;
        }
    }


}