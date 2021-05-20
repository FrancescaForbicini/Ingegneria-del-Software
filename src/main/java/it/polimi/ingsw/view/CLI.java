package it.polimi.ingsw.view;

import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.warehouse.Warehouse;

import java.io.PrintStream;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CLI implements View{
    private final PrintStream out = new PrintStream(System.out,true);
    private final Scanner in = new Scanner(System.in);
    final String welcome = "WELCOME TO MASTERS OF RENAISSANCE";

    /**
     * Prints the message of welcome to the player
     */
    @Override
    public void start(){
        out.println(welcome);
    }

    /**
     * Shows to the client the market, the development cards, the faith track or other players
     * @return what the client wanto to see
     */
    @Override
    public ClientAction pickAnAction(ConcurrentLinkedDeque <ClientAction> actions){
        int response = 0;
        ArrayList<ClientAction> clientActions = new ArrayList<>(actions);
        Map<Integer,ClientAction> actionMap = IntStream.range(1 , actions.size() + 1).boxed().collect(Collectors.toMap(i->i, clientActions::get));
        out.println("Possible actions : ");
        //TODO Checks to string
        actionMap.forEach((i,action) -> out.println(i+". "+action));
        response = in.nextInt();
        while (!actionMap.containsKey(response)){
            out.println("Error! Choose another action: ");
            response = in.nextInt();
        }
        return actionMap.get(response);
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
        int response = 0;
        Map<Integer,ClientPlayer> playerMap = IntStream.range(1 , players.size() + 1).boxed().collect(Collectors.toMap(i->i, players::get));
        Map<ResourceType,Integer> strongbox;
        out.println("Which player do you want to see? ");
        playerMap.forEach((i,player) -> out.println(i+". "+player));
        response = in.nextInt();
        while (!playerMap.containsKey(response)){
            out.println("Error! Choose another player: ");
            response = in.nextInt();
        }
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
    @Override
    public String askUsername(){
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
    @Override
    public String askGameID(){
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
        while (IP == null || IP.equals("")){
            out.println("Enter IP: ");
            IP = in.nextLine();
        }
        return IP;
    }

    /**
     * Picks the two leader cards
     * @param proposedCards the four leader cards proposed to the client
     * @return the leader cards chosen
     */
    @Override
    public List<LeaderCard> pickLeaderCards(List<LeaderCard> proposedCards) {
        List<LeaderCard> pickedCards = new ArrayList<>();
        int choose;
        System.out.printf("Proposed cards: %s%n", proposedCards);
        out.println("Choose the first leader card: ");
        choose = in.nextInt();
        pickedCards.add(proposedCards.get(choose-1));
        out.println("Choose the second leader card: ");
        choose = in.nextInt();
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
        int response = 0;
        String secondLeaderCard = null;
        ArrayList <LeaderCard> leaderCardChosen = new ArrayList<>();
        out.println("Which leader cards do you want to activate: \n");
        out.print("\n 1. %s  \n 2. %s "+leaderCards.get(0) + leaderCards.get(1));
        while (response != 1 && response != 2){
            response = in.nextInt();
        }
        out.println("Do you want activate another leader card ?");
        while (secondLeaderCard == null || !secondLeaderCard.equalsIgnoreCase("yes") && !secondLeaderCard.equalsIgnoreCase("no")){
            out.println("Enter 'yes' or 'no' : ");
            secondLeaderCard = in.nextLine();
        }
        if (secondLeaderCard.equalsIgnoreCase("yes"))
            leaderCardChosen.addAll(leaderCards);
        else
            leaderCardChosen.add(leaderCards.get(response-1));
        return leaderCardChosen;
    }

    @Override
    public ArrayList<LeaderCard> pickLeaderCardToDiscard(List <LeaderCard> leaderCards) {
        int response = 0;
        String secondLeaderCard = null;
        ArrayList <LeaderCard> leaderCardToDiscard = new ArrayList<>();
        out.println("Which leader cards do you want to discard: \n");
        out.print("\n 1. %s  \n 2. %s "+leaderCards.get(0) + leaderCards.get(1));
        while (response != 1 && response != 2){
            response = in.nextInt();
        }
        out.println("Do you want activate another leader card ?");
        while (secondLeaderCard == null || !secondLeaderCard.equalsIgnoreCase("yes") && !secondLeaderCard.equalsIgnoreCase("no")){
            out.println("Enter 'yes' or 'no' : ");
            secondLeaderCard = in.nextLine();
        }
        if (secondLeaderCard.equalsIgnoreCase("yes"))
            leaderCardToDiscard.addAll(leaderCards);
        else
            leaderCardToDiscard.add(leaderCards.get(response-1));
        return leaderCardToDiscard;
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
        out.println(activeTradingRules);
        while (response != -1 || chosenTradingRules.size() < activeTradingRules.size()){
            out.println("Which productions do you want to activate? If you want to stop to choose you can insert '-1'\n ");
            response = in.nextInt();
            chosenTradingRules.add(activeTradingRules.get(response-1));
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
        String resource = null;
        ResourceType resourceType;
        Map<ResourceType,Integer> resourcesChosen = new HashMap<>();
        out.println("In order to stop to select resources from strongbox you can write 'stop'\n");
        while (!resource.equals("stop")){
            out.println("These are the resources from the strongbox: ");
            out.print(resources);
            out.println("Which resource do you want to choose from strongbox: ");
            resource = in.nextLine();
            out.print("Choose the amount of"+resource+" to take from strongbox: ");
            amount = in.nextInt();
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
     */
    @Override
    public Map<ResourceType,Integer> inputFromWarehouse (Map<ResourceType,Integer> resources)  {
        int depotID = -1;
        String resource = null;
        ResourceType resourceType;
        Map<ResourceType,Integer> resourcesChosen = new HashMap<>();
        out.println("In order to stop to select resources from warehouse you can write 'stop'\n");
        while (!resource.equals("stop")){
            out.println("These are the resources from the warehouse: ");
            out.print(resources);
            while (!resources.containsKey(convertResource(resource)) && !resource.equalsIgnoreCase("stop")){
                out.println("Which resource do you want to choose from warehouse? ");
                resource = in.nextLine();
            }
            if (resource.equalsIgnoreCase("stop"))
                break;
            while (depotID<0 || depotID>3){
                out.print("Choose the depot of" + resources.keySet()+ " to take from warehouse: ");
                depotID = in.nextInt();
            }
            resourceType = convertResource(resource);
            if (resourceType != null)
                resourcesChosen.put(resourceType,depotID);
            resource = null;
            depotID = -1;
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
        out.println("You have to decide" +  chosenInputAny.size() + " resources");
        for (ResourceType resource : chosenInputAny){
                out.println("Choose a resource to decide the input of the production:  ");
                while (resourceType == null){
                    resourceType= convertResource(in.nextLine());
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
        out.println("You have to decide" +chosenOutputAny.size() + "resources");
        for (ResourceType resource : chosenOutputAny){
            out.println("Choose a resource to decide the input of the production:  ");
            while (resourceType == null){
                resourceType= convertResource(in.nextLine());
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
        cardMap.forEach((i,card) -> out.println(i+". "+card));
        while (response<=0 || response >= cards.size()){
            out.println("Choose the card that you want to buy");
            response = in.nextInt();
        }
        return cards.get(response+1);
    }

    /**
     * Chooses the slot where to put the development card bought
     * @return the slot chosen
     */
    @Override
    public int chooseSlot() {
        int slot = -1;
        while (slot<= 0 || slot>3){
            out.println("Choose the slot where you want to put the development card bought: ");
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
        int numMax = 0;
        while (rc == null || !rc.equalsIgnoreCase("row") && !rc.equalsIgnoreCase("column")){
            out.println("Choose the 'row' or the 'column'");
            rc = in.nextLine();
        }
        if (rc.equalsIgnoreCase("row"))
            numMax = 3;
        else
            numMax = 4;
        while (num>numMax || num<=0) {
            out.print("Enter the number of the"+rc+ ": ");
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
    public Map<ResourceType,Integer> resourceToDepot(ArrayList<ResourceType> resources){
        int i =0;
        int depot = 0;
        String resourceAnyChosen = null;
        ResourceType resourceType = null;
        Map<ResourceType,Integer> resourcesSet = new HashMap<>();
        while (i < resources.size()){
            if (resources.get(i).equals(ResourceType.Any)){
                while (resourceType == null){
                    out.println("Choose the resource type : ");
                    resourceAnyChosen = in.nextLine();
                    resourceType = convertResource(resourceAnyChosen);
                }

            }
            else
                resourceType = resources.get(i);
            out.printf("Enter the depot where you want put the %s",resourceType);
            while (depot <=0 || depot>3){
                depot = in.nextInt();
            }
            resourcesSet.replace(resourceType,resourcesSet.get(resourceType),depot);
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
        int newDepot = -1;
        Warehouse warehouseSorted = new Warehouse();
        out.println("This is your warehouse: ");
        out.print(warehouse);
        for (int depot = 0; depot < 3; depot ++){
            out.println("Do you want to move "+warehouse.getWarehouseDepots().get(depot).getResourceType().toString()+"to another depot? ");

            while (response == null || !response.equalsIgnoreCase("yes") &&!response.equalsIgnoreCase("no")){
                out.println("Enter 'yes' or 'no'");
                response = in.nextLine();
            }
            if (response.equalsIgnoreCase("yes")){
                while (newDepot<0 || newDepot>3){
                    out.println("Choose the new depot: ");
                    newDepot = in.nextInt();
                }
                warehouseSorted.addResource(warehouse.getWarehouseDepots().get(depot).getResourceType(),warehouse.getWarehouseDepots().get(depot).getQuantity(),newDepot-1);
            }
            else{
                warehouseSorted.addResource(warehouse.getWarehouseDepots().get(depot).getResourceType(),warehouse.getWarehouseDepots().get(depot).getQuantity(),depot);
            }
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
        out.print(activatedWhiteMarbles);
        while (i<resources.size()){
            out.println("Choose the resource to convert a white marble: ");
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