package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.solo_game_action.SoloGameAction;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.solo_game.SoloToken;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.view.LeaderCardChoice;
import it.polimi.ingsw.view.SoloTokenChoice;
import it.polimi.ingsw.view.View;

import java.io.IOException;
import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CLI implements View {
    private final PrintStream out = new PrintStream(System.out,true);
    private final Scanner in = new Scanner(System.in);
    private boolean sceneAlreadySeen = false;
    final String welcome = "WELCOME TO MASTERS OF RENAISSANCE";

    @Override
    public void setSceneAlreadySeen(boolean sceneAlreadySeen) {
        this.sceneAlreadySeen = sceneAlreadySeen;
    }

    @Override
    public boolean isSceneAlreadySeen() {
        return sceneAlreadySeen;
    }

    /**
     * Prints the message of welcome to the player
     */
    @Override
    public void startView(){
        out.println(welcome);
    }


    public static Map<Integer,ClientAction> getActionMap(List<ClientAction> actions) {
        return IntStream.range(1 , actions.size() + 1).boxed().collect(Collectors.toMap(i->i, i -> actions.get(i-1)));
    }


    @Override
    public void showAvailableActions(ArrayList<ClientAction> actions) {
        Map<Integer,ClientAction> actionMap = getActionMap(actions);
        out.println("Possible actions : ");
        actionMap.forEach((i,action) -> out.println(i+". "+action));
    }

    /**
     * Shows to the client the market, the development cards, the faith track or other players
     * @return what the client wanto to see
     * @param actions
     */
    @Override
    public Optional<ClientAction> pickAnAction(ArrayList<ClientAction> actions){
        int response;
        Map<Integer,ClientAction> actionMap = getActionMap(actions);

        out.println("Pick one: ");
        response = in.nextInt();
        while (!actionMap.containsKey(response)){
            if (response == 0)
                return Optional.empty();
            out.println("Error! Choose another action: ");
            response = in.nextInt();
        }
        return Optional.of(actionMap.get(response));
    }

    /**
     * Shows the market
     * @param market the market to show
     */
    @Override
    public void showMarket(Market market){
        //TODO check toString
        out.println(market);
    }

    /**
     * Shows the development cards available
     * @param developmentCards the development cards available
     */
    @Override
    public void showDevelopmentCards(ArrayList<DevelopmentCard> developmentCards){
        out.println(developmentCards);
    }

    /**
     * Shows the faith tack
     * @param faithTrack the faith track to show
     */
    @Override
    public void showFaithTrack(FaithTrack faithTrack){
        out.println(faithTrack);
    }

    /**
     * Shows a specific player
     * @param players the players available
     */
    @Override
    public void showPlayer(ArrayList<ClientPlayer> players){
        int response;
        Map<Integer,ClientPlayer> playerMap = IntStream.range(1 , players.size() + 1).boxed().collect(Collectors.toMap(i->i, i -> players.get(i-1)));
        Map<ResourceType,Integer> strongbox;
        out.println("Which player do you want to see? ");
        playerMap.forEach((i,player) -> out.println(i+". "+player));
        response = in.nextInt();
        while (!playerMap.containsKey(response)){
            out.println("Error! Choose another player: ");
            response = in.nextInt();
        }
        response--;
        out.println("The resource of the player are : "+playerMap.get(response));
        out.println("Warehouse: ");
        for (int depot = 0 ; depot < 3 ; depot ++)
            out.println(""+players.get(response).getWarehouse().getWarehouseDepots().get(depot).getResourceType() + ": "+players.get(response).getWarehouse().getWarehouseDepots().get(depot).getQuantity());
        out.println("Strongbox: ");
        strongbox = players.get(response).getStrongbox();
        out.println(strongbox);
        out.println("The development cards are : ");
        out.println(players.get(response).getDevelopmentSlots());
        if (players.get(response).getNumberOfNonActiveCards() != 0){
            out.println("The leader cards activated are: ");
            out.println(players.get(response).getActiveLeaderCards());
            if (players.get(response).getActiveLeaderCards().size() == 1)
                out.println("The player has not activated the second leader card");
        }
        else{
            out.println("The player has not activated the leader cards");
        }
    }

    /**
     * Asks the username to the player
     * @return the userame of the player
     */
    private String askUsername(){
        String username = null;
        while (username == null || username.equals("")){
            out.println("Enter your username: ");
            username = in.nextLine();
        }
        return username;
    }

    /**
     * Asks the ID of the game to the player
     * @return the ID of the game
     */
    private String askGameID(){
        String ID = null;
        while (ID == null || ID.equals("")){
            out.println("Enter GAME ID: ");
            ID = in.nextLine();
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
        out.println("Enter IP (default localhost)");
        IP = in.nextLine();
        if (IP.equals(""))
            IP = "localhost";
        return IP;
    }

    /**
     * Asks to the player: username and game ID
     * @return the player with the username and the game ID
     */
    @Override
    public ClientPlayer askCredentials(){
        return  new ClientPlayer(askUsername(),askGameID());
    }
    /**
     * Picks the two leader cards
     * @param proposedCards the four leader cards proposed to the client
     * @return the leader cards chosen
     */
    @Override
    public List<LeaderCard> pickLeaderCards(List<LeaderCard> proposedCards) {
        List<LeaderCard> pickedCards = new ArrayList<>();
        int choose = -1;
        System.out.print("Proposed cards: Enter a number from 1 to 4\n");
        proposedCards.forEach(leaderCard -> out.println(leaderCard.toString()));
        while (choose <0 || choose>proposedCards.size()){
            out.println("Choose the first leader card: ");
            choose = in.nextInt();
        }
        pickedCards.add(proposedCards.get(choose-1));
        proposedCards.remove(choose-1);
        System.out.print("Now this are the proposed cards: Enter a number from 1 to 3\n");
        proposedCards.forEach(leaderCard -> out.println(leaderCard.toString()));
        choose = -1;
        while (choose <0 || choose>proposedCards.size()){
            out.println("Choose the second leader card: ");
            choose = in.nextInt();
        }
        pickedCards.add(proposedCards.get(choose-1));
        proposedCards.remove(choose-1);
        return pickedCards;
    }

    /**
     * Picks the resource at the begin of the game
     * @param numberOfResources : the number of the resources that the player can choose
     * @return the resources chosen by the player
     */
    @Override
    public ArrayList<ResourceType> pickStartingResources(int numberOfResources) {
        ArrayList<ResourceType> resourceTypes = new ArrayList<>();
        if (numberOfResources == 0)
            return resourceTypes;
        String response = null;
        out.println("These are the resources that you can choose: ");
        out.println(ResourceType.Servants + " " + ResourceType.Stones + " " + ResourceType.Coins + " " + ResourceType.Shields);
        out.println("You can choose " + numberOfResources + "of these resources");
        while (numberOfResources!=0){
            out.println("Choose a resource: ");
            response = in.nextLine();
            while (convertResource(response.toLowerCase()) == null){
                out.println("Enter a correct resource: ");
                response = in.nextLine();
            }
            resourceTypes.add(convertResource(response.toLowerCase()));
            numberOfResources--;
        }
        return resourceTypes;
    }

    /**
     * Prints that the game is starting
     */
    @Override
    public void showStart(){
        out.println("START GAME");
    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);
    }

    @Override
    public LeaderCardChoice chooseLeaderCardAction() {
        String response  = null;
        while (!response.equalsIgnoreCase("active") && !response.equalsIgnoreCase("discard")){
            out.println("Choose to 'active' you leader cards or to 'discard' them: ");
            response = in.nextLine();
        }
        if (response.equalsIgnoreCase("active"))
            return LeaderCardChoice.ACTIVATE;
        else
            return LeaderCardChoice.DISCARD;
    }


    //LEADER CARDS
    /**
     * Chooses the leader cards to activate
     * @param leaderCards the leader cards available
     * @return the leader cards activated
     */
    @Override
    public ArrayList<LeaderCard> pickLeaderCardToActivate(List <LeaderCard> leaderCards) {
        String response = null;
        ArrayList <LeaderCard> leaderCardChosen = new ArrayList<>();
        for (int i = 0 ; i < leaderCards.size(); i++){
            out.println("\nDo you want activate : \n"+leaderCards.get(i).toString());
            while (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")) {
                out.println("Enter yes or no: ");
                response = in.nextLine();
            }
            if (response.equalsIgnoreCase("yes"))
                leaderCardChosen.add(leaderCards.get(i));
        }
        return leaderCardChosen;
    }

    @Override
    public ArrayList<LeaderCard> pickLeaderCardToDiscard(List <LeaderCard> leaderCards) {
        String response = null;
        ArrayList <LeaderCard> leaderCardChosen = new ArrayList<>();
        for (int i = 0 ; i < leaderCards.size(); i++){
            out.println("\nDo you want discard : \n"+leaderCards.get(i).toString());
            while (response.equalsIgnoreCase("yes") || response.equalsIgnoreCase("no")) {
                out.println("Enter yes or no: ");
                response = in.nextLine();
            }
            if (response.equalsIgnoreCase("yes"))
                leaderCardChosen.add(leaderCards.get(i));
        }
        return leaderCardChosen;
    }


    // ACTIVATE PRODUCTION
    /**
     * Chooses the trading rules that has to be activated
     * @param activeTradingRules the trading rules available
     * @return the trading rules that activated
     */
    @Override
    public ArrayList<TradingRule> chooseTradingRulesToActivate(ArrayList<TradingRule> activeTradingRules) {
        ArrayList <TradingRule> chosenTradingRules = new ArrayList<>();
        int response = 0;
        out.println("Which productions do you want to activate? If you want to stop to choose you can insert '-1'\n ");
        while (activeTradingRules.isEmpty()){
            out.println("This are the productions that you can activate: \n");
            out.println("Enter a number from 1 to " + activeTradingRules.size());
            activeTradingRules.forEach(tradingRule -> out.println(tradingRule.toString()));
            response = in.nextInt();
            if (response == -1)
                return chosenTradingRules;
            chosenTradingRules.add(activeTradingRules.get(response-1));
            activeTradingRules.remove(activeTradingRules.get(response-1));
        }
        return chosenTradingRules;
    }


    /**
     * Chooses the resources from the strongbox to use a production
     * @param resources the type of the resources and the amount
     * @return the type of the resources and the amount that has been chosen from the strongbox
     */
    @Override
    public Map<ResourceType,Integer> inputFromStrongbox(Map<ResourceType,Integer> resources)  {
        int amount;
        String resource = "";
        ResourceType resourceType;
        Map<ResourceType,Integer> resourcesChosen = new HashMap<>();
        out.println("In order to stop to select resources from strongbox you can write 'stop'\n");
        while (resources.isEmpty()){
            out.println("These are the resources from the strongbox: ");
            out.println(resources.toString());
            out.println("Which resource do you want to choose from strongbox: ");
            resourceType = null;
            while(resourceType == null || !resources.containsKey(resourceType) && !resource.equalsIgnoreCase("stop")){
                resource = in.nextLine();
                resource = resource.toLowerCase();
                resourceType = convertResource(resource);
            }
            if (resource.equalsIgnoreCase("stop"))
                return resourcesChosen;
            amount = -1;
            while ( amount < 0 || amount > resources.get(resourceType) ){
                out.print("Choose the amount of " + resourceType + " to take from strongbox: ");
                amount = in.nextInt();
            }
            resources.remove(resourceType);
            resourcesChosen.put(resourceType,amount);
        }
        return resourcesChosen;
    }

    /**
     * Chooses the resources from the warehouse to use a production
     * @param resources the type of the resources and the amount
     * @return the type of the resources and the amount of the resources taken
     */
    @Override
    public Map<ResourceType,Integer> inputFromWarehouse (Map<ResourceType,Integer> resources)  {
        int amount = -1;
        String response = null;
        ResourceType resourceType;
        Map<ResourceType,Integer> resourcesChosen = new HashMap<>();
        out.println("In order to stop to select resources from warehouse you can write 'stop'\n");
        while (resources.isEmpty()){
            out.println("These are the resources from the warehouse: ");
            out.println(response);
            while (!resources.containsKey(convertResource(response.toLowerCase()))){
                out.println("Which response do you want to choose from warehouse? Enter the resource chosen or 'stop': ");
                response = in.nextLine();
                if (response.equalsIgnoreCase("stop"))
                    return resourcesChosen;
            }
            resourceType = convertResource(response.toLowerCase());
            while (amount < 0 || amount > resources.get(resourceType)) {
                out.print("Choose the amount of" + resourceType + " to take from warehouse: ");
                amount = in.nextInt();
            }
            resourcesChosen.put(resourceType, amount);
            resources.remove(resourceType);
            response = null;
            resourceType = null;
            amount = -1;
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
        out.println("You have to decide" + chosenInputAny.size() + " resources");
        for (ResourceType resource : chosenInputAny){
                out.println("Choose a resource to decide the input of the production:  ");
                while (resourceType == null){
                    resourceType= convertResource(in.nextLine().toLowerCase());
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
        out.println("You have to decide" + chosenOutputAny.size() + "resources");
        for (ResourceType resource : chosenOutputAny){
            out.println("Choose a resource to decide the input of the production:  ");
            while (resourceType == null){
                resourceType= convertResource(in.nextLine().toLowerCase());
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
     */
    @Override
    public DevelopmentCard buyDevelopmentCards(ArrayList<DevelopmentCard> cards)  {
        Map<Integer,DevelopmentCard> cardMap = IntStream.range(1 , cards.size() + 1).boxed().collect(Collectors.toMap(i->i, cards::get));
        int response = 0;
        out.println("This are the development cards available");
        cardMap.forEach((i,card) -> out.println(i+". "+card.toString()));
        while (response<=0 || response >= cards.size()){
            out.println("Choose the card that you want to buy\nEnter a number from 1 to " +cards.size());
            response = in.nextInt();
        }
        return cards.get(response-1);
    }

    /**
     * Chooses the slot where to put the development card bought
     * @return the slot chosen
     */
    @Override
    public int chooseSlot() {
        int slot = -1;
        out.println("Choose the slot where you want to put the development card bought");
        while (slot< 1 || slot>3){
            out.println("Enter a number from 1 to 3");
            slot = in.nextInt();
        }
        return slot;
    }

    //TAKE FROM MARKET
    /**
     * Chooses if the player wants to select a row or a column and the its number
     */
    @Override
    public Map <String,Integer> chooseLine() {
        Map <String,Integer> response = new HashMap<>();
        String rc = null;
        int num = 5;
        int numMax;
        while (rc == null || (!rc.equalsIgnoreCase("row") && !rc.equalsIgnoreCase("column"))){
            out.println("Choose the 'row' or the 'column'");
            rc = in.nextLine();
        }
        if (rc.equalsIgnoreCase("row"))
            numMax = 3;
        else
            numMax = 4;
        while (num > numMax || num <= 0) {
            out.print("Enter the number from 1 to "+ numMax + "of the" +rc+ ": ");
            num = in.nextInt();
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
    public Map<ResourceType,Integer> resourceToDepot(ArrayList<ResourceType> resources,ClientPlayer player){
        int i = 0;
        int depot = 0;
        ResourceType resourceType = null;
        Map<ResourceType,Integer> resourcesSet = new HashMap<>();
        while (i < resources.size()){
            if (resources.get(i).equals(ResourceType.Any)){
                while (resourceType == null){
                    out.println("Choose the resource type : ");
                    resourceType = convertResource(in.nextLine().toLowerCase());
                }
            }
            else
                resourceType = resources.get(i);

            ResourceType finalResourceType = resourceType;
            int count = (int) resources.stream().filter(resourceType1 -> resourceType1.equals(finalResourceType)).count();
            while (depot < 1 || depot > 4 || !player.getWarehouse().addResource(resourceType,count,depot-1)){
                out.println("Enter the depot where you want put " + count +" "+ resourceType);
                depot = in.nextInt();
            }
            resourcesSet.replace(resourceType,resourcesSet.get(resourceType),depot-1);
            i++;
            resourceType = null;
        }
        return resourcesSet;
    }

    /**
     * Asks to the player to sort or not the warehouse
     * @return true if the player wants to sort the warehouse, false if not
     */
    @Override
    public boolean askSortWarehouse(){
        String response = null;
        out.println("Do you want to sort the warehouse? ");
        out.println("Enter 'yes' or 'no'");
        while (response == null || !response.equalsIgnoreCase("yes") && !response.equalsIgnoreCase("no")){
            response = in.nextLine();
        }
        return response.equalsIgnoreCase("yes");
    }

    /**
     * Sorts the warehouse
     * @param warehouse the warehouse of the player
     * @return the warehouse sorted by the player
     */
    @Override
    public Warehouse sortWarehouse(Warehouse warehouse)  {
        String response = null;
        boolean moved = true;
        int newDepot = -1;
        Warehouse warehouseSorted = new Warehouse();
        out.println("This is your warehouse: ");
        out.print(warehouse.toString());
        for (int depot = 1; depot < 4; depot ++){
            out.println("Do you want to move "+warehouse.getWarehouseDepots().get(depot).getResourceType().toString()+" from "+ (depot+1) +"to another depot? ");
            while (response == null || !response.equalsIgnoreCase("yes") &&!response.equalsIgnoreCase("no")){
                out.println("Enter 'yes' or 'no'");
                response = in.nextLine();
            }
            newDepot = -1;
            moved = true;
            if (response.equalsIgnoreCase("yes")) {
                while ( moved && (newDepot < 1 || newDepot > 4 || newDepot == depot || !warehouse.switchResource(depot,newDepot))) {
                    out.println("Choose the new depot: ");
                    newDepot = in.nextInt();
                    if (!warehouse.switchResource(depot,newDepot)){
                        out.println("Error! You can't move the resources into the depot "+ newDepot);
                        while (!response.equalsIgnoreCase("stop") && !response.equalsIgnoreCase("retry")) {
                            out.println("If you don't want to move anymore the resources you can write 'stop' or you can 'retry");
                            response = in.nextLine();
                        }
                        if (response.equalsIgnoreCase("stop"))
                            moved = false;
                    }
                }
            }
            else
                warehouseSorted.addResource(warehouse.getWarehouseDepots().get(depot).getResourceType(),warehouse.getWarehouseDepots().get(depot).getQuantity(),depot);
            response = null;

        }
        return warehouseSorted;
    }

    /**
     * Sets the white marble with the resources chosen from the player based on the the conversions available
     * @param resources the white marbles to convert
     * @param activatedWhiteMarbles the conversions available
     * @return the resources and the amount that has been converted
     */
    @Override
    public ArrayList<ResourceType> chooseResourceAny (ArrayList<ResourceType> resources, ArrayList<ResourceType> activatedWhiteMarbles){
        int i = 0;
        ResourceType resourceType = null;
        ArrayList<ResourceType> resourcesChosen = new ArrayList<>();
        out.println("Choose the conversion of the white marbles activated");
        out.println("CONVERSION AVAILABLE: ");
        out.println(activatedWhiteMarbles.toString());
        out.println("You have to convert " + resources.size() + " resources");
        while (i < resources.size()){
            while (activatedWhiteMarbles.stream().noneMatch(resourceType::equals)){
                out.println("Choose the resources correct from your conversions available");
                resourceType = convertResource(in.nextLine());
            }
            resourcesChosen.add(resourceType);
            resourceType = null;
            i++;
        }
        return resourcesChosen;
    }

    //SOLO GAME
    @Override
    public SoloTokenChoice pickSoloToken(ConcurrentLinkedDeque<SoloGameAction> soloTokens){
        out.println("Pick a solo token card: ");
        out.println("You have taken the : "+ soloTokens.getLast());
        switch (soloTokens.getLast().toString()){
            case "MoveBlackCross" :
                return SoloTokenChoice.MOVEBLACKCROSS;
            case "MoveBlackShuffle":
                return SoloTokenChoice.MOVEBLACKSHUFFLE;
            case "DiscardDevelopmentCards":
                return SoloTokenChoice.DISCARDDEVELOPMENTCARDS;
            default: return null;
        }
    }

    /**
     * Discards two development cards from the deck
     * @param developmentCardsAvailable : the development cards that can be removed
     * @param developmentCardsToDiscard : the development cards to remove
     * @return the development cards left
     */
    @Override
    public ArrayList<DevelopmentCard> DevelopmentCardsToDiscard(ArrayList<DevelopmentCard> developmentCardsAvailable, ArrayList<DevelopmentCard> developmentCardsToDiscard){
        out.println("This are the development cards available: " + developmentCardsAvailable);
        out.println("Will be discarded this two development cards: ");
        out.println("1. "+developmentCardsToDiscard.get(0).toString()+"\n2. "+developmentCardsToDiscard.get(1).toString());
        out.println("Now this are the development cards available: ");
        developmentCardsAvailable.forEach(card->out.println(card.toString()));
        return developmentCardsAvailable;
    }

    /**
     * Moves the black cross and shuffles the solo tokens deck
     * @param soloTokenDeck the solo token deck to shuffle
     * @param faithTrack the faith track with the new position of the black cross
     */
    @Override
    public void showMoveBlackShuffle(Deck<SoloToken> soloTokenDeck, FaithTrack faithTrack){
        out.println("Now all the solo tokens are available : ");
        out.println("And Lorenzo the Magnificent is here: "+ faithTrack.toString());
    }

    /**
     * Moves the black cross
     * @param faithTrack the faith track with the new position of the black cross
     */
    @Override
    public void showMoveBlackCross(FaithTrack faithTrack){
        out.println("Lorenzo the Magnificent now is here: "+faithTrack.toString());
    }

    public void notifyNewActions() {
        out.println("New actions. Press 0 to reload.");
    }

    @Override
    public void showWinner(String winnerUsername) {
        // TODO
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

}