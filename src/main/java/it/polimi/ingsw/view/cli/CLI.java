package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.client.ChosenLine;
import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.action_message.market_message.MarketAxis;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.View;

import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class CLI implements View {
    private final PrintStream out = new PrintStream(System.out, true);
    private final Scanner in = new Scanner(System.in);
    private boolean sceneAlreadySeen = false;
    boolean alreadyTried;

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
    public void startView() {
        out.println("WELCOME TO MASTERS OF RENAISSANCE");
    }


    private static Map<Integer, ClientAction> getActionMap(List<ClientAction> actions) {
        return IntStream.range(1, actions.size() + 1).boxed().collect(Collectors.toMap(i -> i, i -> actions.get(i - 1)));
    }

    /**
     * Shows to the client the market, the development cards, the faith track or other players
     *
     * @param actions the action chosen from the player
     * @return what the client wanto to see
     */
    @Override
    public Optional<ClientAction> pickAnAction(ArrayList<ClientAction> actions) {
        int response = -1;
        Map<Integer, ClientAction> actionMap = getActionMap(actions);
        out.println("Possible actions : ");
        actionMap.forEach((i, action) -> out.println(i + ". " + action));
        while (!actionMap.containsKey(response)) {
            out.println("Pick one: ");
            response = checkInt();
            if (response == 0)
                return Optional.empty();

            if (!actionMap.containsKey(response))
                out.println("Error! Enter a number from 1 to " + actionMap.size());
        }
        return Optional.of(actionMap.get(response));
    }

    /**
     * Shows the market
     *
     * @param market the market to show
     */
    @Override
    public void showMarket(Market market) {
        out.println(market.toString());
    }

    /**
     * Shows the development cards available
     *
     * @param developmentCards the development cards available
     */
    @Override
    public void showDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {
        developmentCards.stream().filter(developmentCard -> developmentCard.getLevel()==1).forEach(developmentCard -> out.print(developmentCard.toString()));
        out.println();
        developmentCards.stream().filter(developmentCard -> developmentCard.getLevel()==2).forEach(developmentCard -> out.print(developmentCard.toString()));
        out.println();
        developmentCards.stream().filter(developmentCard -> developmentCard.getLevel()==3).forEach(developmentCard -> out.print(developmentCard.toString()));
        out.println();
        //out.println(developmentCards.toString());
    }

    /**
     * Shows a specific player
     *
     * @param players the players available
     */
    @Override
    public void showPlayer(Player currentPlayer, ArrayList<ClientPlayer> players, FaithTrack faithTrack) {
        int response = 0;
        Map<Integer, ClientPlayer> playerMap = IntStream.range(1, players.size() + 1).boxed().collect(Collectors.toMap(i -> i, i -> players.get(i - 1)));
        Map<ResourceType, Integer> strongbox;
        out.println("Which player do you want to see? ");
        playerMap.forEach((i, player) -> out.println(i + ". " + player.getUsername()));
        response = checkInt();
        while (!playerMap.containsKey(response)) {
            out.println("Error! Choose another player: ");
            response = checkInt();
        }
        response--;
        ClientPlayer chosenPlayer = players.get(response);
        if (chosenPlayer.getUsername().equals(chosenPlayer.getUsername()) && chosenPlayer.getNumberOfNonActiveCards() > 0) {
            out.println("This are your leader cards no activated:");
            currentPlayer.getNonActiveLeaderCards().forEach(out::println);
        }
        out.println("Faith track:");
        out.println(faithTrack.toString());
        out.println("The resources of " + chosenPlayer.getUsername() + " are : ");
        out.print("Warehouse: ");
        if (chosenPlayer.getWarehouse().getWarehouseDepots().stream().allMatch(WarehouseDepot::isEmpty))
            out.println("is empty");
        else {
            out.println();
            for (WarehouseDepot depot : chosenPlayer.getWarehouse().getWarehouseDepots()) {
                if (!depot.isEmpty())
                    out.println("Depot: " + depot.getDepotID() + " " + depot.getResourceType().convertColor() + ": " + depot.getQuantity());
            }
        }
        out.print("Strongbox: ");
        strongbox = chosenPlayer.getStrongbox();
        if (strongbox.isEmpty())
            out.println("is empty");
        else {
            out.println();
            for (ResourceType resource : strongbox.keySet()) {
                out.println(resource.convertColor() + ": " + strongbox.get(resource));
            }
        }
        out.print("The development cards are ");
        if (Arrays.stream(chosenPlayer.getDevelopmentSlots()).allMatch(developmentSlot -> developmentSlot.showCardOnTop().isEmpty()))
            out.println("not present");
        else {
            out.println();
            for (DevelopmentSlot slot : chosenPlayer.getDevelopmentSlots()) {
                if (slot.showCardOnTop().isPresent())
                    out.println("Slot: " + slot.toString() + " " + slot.showCardOnTop().toString());
            }
        }
        if (chosenPlayer.getNumberOfNonActiveCards() == 0) {
            out.println("The leader cards activated are: ");
            out.println(chosenPlayer.getActiveLeaderCards().toString());
            if (chosenPlayer.getActiveLeaderCards().size() == 1)
                out.println("The player has not activated the second leader card");
        } else {
            out.println("The player has not activated the leader cards");
            out.println("The leader card that can be activated are: " + chosenPlayer.getNumberOfNonActiveCards());
        }

    }

    /**
     * Asks the username to the player
     *
     * @return the userame of the player
     */
    private String askUsername() {
        String username = null;
        while (username == null || username.equals("")) {
            out.println("Enter your username: ");
            username = in.nextLine();
        }
        return username;
    }

    /**
     * Asks the ID of the game to the player
     *
     * @return the ID of the game
     */
    private String askGameID() {
        String ID = null;
        while (ID == null || ID.equals("")) {
            out.println("Enter GAME ID: ");
            ID = in.nextLine();
        }
        return ID;
    }

    /**
     * Asks the IP of connection
     *
     * @return the IP of the connection
     */
    @Override
    public String askIP() {
        String IP = null;
        out.println("Enter IP: ");
        IP = in.nextLine();
        return IP;
    }

    /**
     * Asks to the player: username and game ID
     *
     * @return the player with the username and the game ID
     */
    @Override
    public ClientPlayer askCredentials() {
        return new ClientPlayer(askUsername(), askGameID());
    }

    /**
     * Picks the two leader cards
     *
     * @param proposedCards the four leader cards proposed to the client
     * @return the leader cards chosen
     */
    @Override
    public ArrayList<LeaderCard> pickStartingLeaderCards(List<LeaderCard> proposedCards) {
        Map<Integer, LeaderCard> cardMap = enumerateLeaderCards(proposedCards);
        int choose = -1;
        ArrayList<LeaderCard> pickedCards = new ArrayList<>();
        out.println("Proposed cards: Enter a number from 1 to " + proposedCards.size());
        cardMap.forEach((i, card) -> out.println(i + ". " + card.toString()));
        while (choose < 0 || choose > proposedCards.size()) {
            out.print("Choose the first leader card: ");
            choose = checkInt();
        }
        pickedCards.add(proposedCards.get(choose - 1));
        proposedCards.remove(choose - 1);
        cardMap = enumerateLeaderCards(proposedCards);
        out.println("Now this are the proposed cards: Enter a number from 1 to 3");
        cardMap.forEach((i, card) -> out.println(i + ". " + card.toString()));
        choose = -1;
        while (choose < 0 || choose > proposedCards.size()) {
            out.print("Choose the second leader card: ");
            choose = checkInt();
        }
        pickedCards.add(proposedCards.get(choose - 1));
        proposedCards.remove(choose - 1);
        return pickedCards;
    }

    private Map<Integer, LeaderCard> enumerateLeaderCards(List<LeaderCard> proposedCards) {
        return IntStream.range(1, proposedCards.size() + 1).boxed().collect(Collectors.toMap(i -> i, i -> proposedCards.get(i - 1)));
    }

    private Map<Integer, ResourceType> enumerateResources(ArrayList<ResourceType> resourceTypes) {
        return IntStream.range(1, resourceTypes.size() + 1).boxed().collect(Collectors.toMap(i -> i, i -> resourceTypes.get(i - 1)));
    }

    @Override
    public LeaderCard pickLeaderCard(List<LeaderCard> proposedCards) {
        Map<Integer, LeaderCard> cardMap = enumerateLeaderCards(proposedCards);
        int choose;
        out.println("Proposed cards: Enter a number from 1 to " + proposedCards.size());
        cardMap.forEach((i, card) -> out.println(i + ". " + card.toString()));
        do {
            out.println("Choose a leader card: ");
            choose = checkInt();
        } while (choose < 0 || choose > proposedCards.size());
        return proposedCards.get(choose - 1);
    }


    /**
     * Picks the resource at the begin of the game
     *
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
        out.println(ResourceType.Servants.convertColor() + " " + ResourceType.Stones.convertColor() + " " + ResourceType.Coins.convertColor() + " " + ResourceType.Shields.convertColor());
        out.println("You can choose " + numberOfResources + "of these resources");
        while (numberOfResources != 0) {
            out.println("Choose a resource: ");
            response = in.nextLine();
            while (convertResource(response.toLowerCase()) == null) {
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
    public void showStart() {
        out.println("START GAME");
    }

    /**
     * Prints a message to the player
     *
     * @param message the message to show
     */
    @Override
    public void showMessage(String message) {
        out.println(message);
    }

    // ACTIVATE PRODUCTION

    /**
     * Chooses the trading rules that has to be activated
     *
     * @param developmentCards the card to choose the trading rules available
     * @return the cards where there are the trading rules to active
     */
    @Override
    public ArrayList<DevelopmentCard> chooseDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {
        ArrayList<DevelopmentCard> developmentCardsChosen = new ArrayList<>();
        int response = 0;
        out.println("Which productions do you want to activate? If you want to stop to choose you can insert '-1'\n ");
        out.println("This are the productions that you can activate: \n");
        developmentCards.forEach(card -> out.println(card.getTradingRule().toString()));
        while (developmentCards.size() != 0) {
            while (response < 1 || response > developmentCards.size()) {
                out.println("Enter a number from 1 to " + developmentCards.size());
                out.println("Enter -1 to stop to choose");
                response = checkInt();
                if (response == -1)
                    return developmentCardsChosen;
            }
            developmentCardsChosen.add(developmentCards.get(response - 1));
            developmentCards.remove(developmentCards.get(response - 1));
        }
        return developmentCardsChosen;
    }

    /**
     * Choose where to take the resource
     *
     * @param quantityFromStrongbox the quantity of the resource in the strongbox
     * @param quantityFromWarehouse the quantity of the resource in the warehouse
     * @param resourceType          the resource to take from warehouse or strongbox
     * @param total                 the amount of the resource to take
     * @return the quantity to take from strongbox and the quantity to take from warehouse
     */
    @Override
    public Map<String, Integer> inputFrom(int quantityFromStrongbox, int quantityFromWarehouse, ResourceType resourceType, int total) {
        int quantityChosenFromStrongbox = 0;
        int quantityChosenFromWarehouse = 0;
        String input;
        out.println("You can choose " + (quantityFromStrongbox + quantityFromWarehouse) + " " + resourceType.convertColor());
        out.println(" " + quantityFromStrongbox + " from strongbox ");
        out.println(" " + quantityFromWarehouse + " from warehouse");
        do {
            out.println("Choose if you want to take the resource from 'strongbox' or 'warehouse' ");
            input = in.nextLine().toLowerCase();
            if (input.equals("strongbox")) {
                do {
                    out.println("Enter the quantity  -> max: " + quantityChosenFromStrongbox);
                    quantityChosenFromStrongbox = checkInt();
                } while (quantityChosenFromStrongbox > quantityFromStrongbox);
                quantityChosenFromWarehouse = total - quantityChosenFromStrongbox;
            } else if (input.equals("warehouse")) {
                do {
                    out.println("Enter the quantity  -> max: " + quantityChosenFromWarehouse);
                    quantityChosenFromWarehouse = checkInt();
                } while (quantityChosenFromWarehouse > quantityFromStrongbox);
                quantityChosenFromStrongbox = total - quantityChosenFromWarehouse;
            }

        } while (quantityChosenFromStrongbox + quantityChosenFromWarehouse > total);
        Map<String, Integer> inputFrom = new HashMap<>();
        inputFrom.put("strongbox", quantityChosenFromStrongbox);
        inputFrom.put("warehouse", quantityChosenFromWarehouse);
        return inputFrom;
    }
    @Override
    public ResourceType chooseResource(){
        String resourceType = null;
        do {
            out.println("Choose a resource: ");
            resourceType = in.nextLine().toLowerCase();
        }while(convertResource(resourceType) == null);
        return convertResource(resourceType);
    }
    /**
     * Chooses the resource type not specified in the output of a production
     * @param resourceToChoose the amount of resource to choose
     * @return the resources chosen
     */
    @Override
    public ArrayList<ResourceType> chooseResourcesAny(int resourceToChoose){
        ArrayList<ResourceType> resourcesChosen = new ArrayList<>();
        ResourceType resourceType = null;
        out.println("You have to decide" + resourceToChoose + "resources");
        while(resourceToChoose != 0){
            out.println("Choose a resource to put in the strongbox: ");
            out.println(" "+ ResourceType.Shields.convertColor() + " " + ResourceType.Coins.convertColor() + " " + ResourceType.Stones.convertColor() + " "+ ResourceType.Servants.convertColor());
            while (resourceType == null){
                resourceType= convertResource(in.nextLine().toLowerCase());
            }
            resourcesChosen.add(resourceType);
            resourceType = null;
            resourceToChoose--;
        }
        return resourcesChosen;
    }

    // BUY DEVELOPMENT CARDS
    /**
     * Chooses which development card has to be bought
     * @return the color and the level of the development card
     */
    @Override
    public DevelopmentCard buyDevelopmentCards(ArrayList<DevelopmentCard> cards)  {
        Map<Integer,DevelopmentCard> cardMap = IntStream.range(1 , cards.size() + 1).boxed().collect(Collectors.toMap(i -> i, i -> cards.get(i-1)));
        int response = -1;
        out.println("This are the development cards available: ");
        cardMap.forEach((i,card) -> out.println(i + ". " + card.toString()));
        while (response < 0 || response > cards.size()){
            out.println("Choose the card that you want to buy");
            out.print("Enter a number from 1 to " +cards.size() + " : ");
            response = checkInt();
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
        while (slot < 0 || slot > 3){
            out.print("Enter a number from 1 to 3: ");
            slot = checkInt();
        }
        return slot - 1;
    }

    //TAKE FROM MARKET
    /**
     * Chooses if the player wants to select a row or a column and the its number
     * @param market: market to show to the player to choose the resources to take
     */
    @Override
    public ChosenLine chooseLine(Market market) {
        String rc = null;
        MarketAxis marketAxis;
        int num = 5;
        int numMax;
        alreadyTried = false;
        showMarket(market);
        do{
            checkAlreadyTried();
            out.print("Choose the 'row' or the 'column' : ");
            rc = in.nextLine();
        }while (rc == null || (!rc.equalsIgnoreCase("row") && !rc.equalsIgnoreCase("column")));
        if (rc.equalsIgnoreCase("row")) {
            numMax = 3;
            marketAxis = MarketAxis.ROW;
        }
        else {
            numMax = 4;
            marketAxis = MarketAxis.COL;
        }
        out.println();
        alreadyTried = false;
        do {
            checkAlreadyTried();
            out.print("Enter the number from 1 to "+ numMax + " of the " + rc + "  :  ");
            num = checkInt();
        }while (num > numMax || num <= 0);
            return new ChosenLine(marketAxis, num);
    }

    @Override
    public ArrayList<ResourceType> chooseWhiteMarble(int amount, ArrayList<ResourceType> activeWhiteMarbleConversion){
        ArrayList<ResourceType> resourcesToChoose = new ArrayList<>();
        ResourceType resourceType;
        while (amount != 0) {
            out.println("Choose the conversion of the white marbles activated");
            out.println("CONVERSION AVAILABLE: ");
            out.println(activeWhiteMarbleConversion.toString());
            alreadyTried = false;
            resourceType = null;
            do {
                checkAlreadyTried();
                out.println("Choose the resource type : ");
                resourceType = convertResource(in.nextLine().toLowerCase());
            }while (resourceType == null || !activeWhiteMarbleConversion.contains(resourceType));
            resourcesToChoose.add(resourceType);
            amount--;
        }
        return resourcesToChoose;
    }


    /**
     * Asks to the player to sort or not the warehouse
     * @return true if the player wants to sort the warehouse, false if not
     */
    private boolean askSortWarehouse(){
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
    public Map<ResourceType,Integer> sortWarehouse(Warehouse warehouse)  {
        if (!askSortWarehouse())
            return null;
        String response = null;
        boolean moved = true;
        int newDepot = -1;
        int depot;
        Map<ResourceType,Integer> sortWarehouse = new HashMap<>();
        out.println("This is your warehouse: ");
        out.print(warehouse.toString());
        for (WarehouseDepot warehouseDepot :
                warehouse.getWarehouseDepots()) {
            if (warehouseDepot.isEmpty())
                continue;
            depot = warehouseDepot.getDepotID();
            out.println("Do you want to move "+ warehouseDepot.getResourceType().toString()+" from "+ depot +" to another depot? ");
            while (response == null || !response.equalsIgnoreCase("yes") &&!response.equalsIgnoreCase("no")){
                out.println("Enter 'yes' or 'no'");
                response = in.nextLine();
            }
            newDepot = -1;
            if (response.equalsIgnoreCase("yes")) {
                while ( moved && (newDepot < 1 || newDepot > 4 || newDepot == depot || !warehouse.switchResource(depot,newDepot))) {
                    out.println("Choose the new depot: ");
                    newDepot = checkInt();
                    if (!warehouse.switchResource(depot,newDepot)){
                        out.println("Error! You can't move the resources into the depot "+ newDepot);
                        while (!response.equalsIgnoreCase("stop") && !response.equalsIgnoreCase("retry")) {
                            out.println("If you don't want to move anymore the resources you can write 'stop' or you can 'retry");
                            response = in.nextLine();
                        }
                        if (response.equalsIgnoreCase("stop"))
                            moved = false;
                    }
                    else
                        sortWarehouse.put(warehouse.getWarehouseDepots().get(depot-1).getResourceType(),newDepot);
                }
            }
            if (response.equalsIgnoreCase("no") || !moved)
                    sortWarehouse.put(warehouse.getWarehouseDepots().get(depot).getResourceType(),depot);
            response = null;
            moved = true;
        }
        return sortWarehouse;
    }

    public void notifyNewActions() {
        out.println("New actions. Press 0 to reload.");
    }

    /**
     * Shows who has won
     * @param winnerUsername the username of the winner
     */
    @Override
    public void showWinner(String winnerUsername) {
        out.println("The winner is: " + winnerUsername);
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

    private int checkInt (){
        String response = null;
        response = in.nextLine();
        if (response.matches("[0-9]+"))
            return Integer.parseInt(response);
        else{
            out.println("Error! Enter a valid number ");
            return -2;
        }
    }

    private void checkAlreadyTried(){
        if(alreadyTried){
            out.print("Wrong value, please retry ");
        }else{
            alreadyTried = true;
        }
    }
    @Override
    public int choose (ArrayList<?> elemsToChoose){
        int choice = 0;
        Map<Integer,?> elemMap = IntStream.range(1, elemsToChoose.size() + 1).boxed().collect(Collectors.toMap(i -> i, i -> elemsToChoose.get(i - 1)));
        elemMap.forEach((i,elem) -> out.println(i + ". " + elem.toString()));
        alreadyTried = false;
        do {
            checkAlreadyTried();
            choice = checkInt();
        }while(choice<1 || choice>elemsToChoose.size());
        return choice-1;
    }
}