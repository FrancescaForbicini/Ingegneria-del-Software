package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.client.ChosenLine;
import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.action.turn.ResourcesChosen;
import it.polimi.ingsw.message.action_message.market_message.MarketAxis;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.Credentials;
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


    private static Map<Integer, ?> getMap(List<?> list){
        return IntStream.range(1, list.size() + 1).boxed().collect(Collectors.toMap(i -> i, i -> list.get(i - 1)));
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
        Map<Integer, ClientAction> actionMap = (Map<Integer, ClientAction>) getMap(actions);
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
    }

    @Override
    public void showPlayer(ClientPlayer currentPlayer, boolean isItself, ArrayList<LeaderCard> nonActiveLeaderCards) {
        out.print(currentPlayer.toString());
        if(isItself && nonActiveLeaderCards.size()>0) {
            nonActiveLeaderCards.forEach(out::print);
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

    private int askMaxPlayers() {
        // TODO
        return 1;
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
    public Credentials askCredentials() {
        return new Credentials(askUsername(), askGameID(), askMaxPlayers());
    }

    /**
     * Picks the two leader cards
     *
     * @param proposedCards the four leader cards proposed to the client
     * @return the leader cards chosen
     */
    @Override
    public int pickStartingLeaderCards(List<LeaderCard> proposedCards) {
        return choose(proposedCards);
    }

    private Map<Integer, LeaderCard> enumerateLeaderCards(List<LeaderCard> proposedCards) {
        return IntStream.range(1, proposedCards.size() + 1).boxed().collect(Collectors.toMap(i -> i, i -> proposedCards.get(i - 1)));
    }

    private Map<Integer, ResourceType> enumerateResources(ArrayList<ResourceType> resourceTypes) {
        return IntStream.range(1, resourceTypes.size() + 1).boxed().collect(Collectors.toMap(i -> i, i -> resourceTypes.get(i - 1)));
    }

    @Override
    public int pickLeaderCard(List<LeaderCard> proposedCards) {
        return choose(proposedCards);
    }


    /**
     * Picks the resource at the begin of the game
     *
     * @return the resource chosen by the player
     */
    @Override
    public ResourceType pickStartingResources() {
        return chooseResource();
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

    @Override
    public boolean askToChoose(){
        String response = null;
        do {
            out.println("Enter 'yes' or 'no': ");
            response = in.nextLine();
        }while (!response.equals("yes") && !response.equals("no"));
        return response.equals("yes");
    }
    /**
     *
     * @param resourceType
     * @param quantityStrongbox
     * @param quantityWarehouse
     * @return
     */
    @Override
    public ResourcesChosen inputFrom(ResourceType resourceType,int quantityStrongbox ,int quantityWarehouse) {
        int quantityChosenFromStrongbox = 0;
        int quantityChosenFromWarehouse = 0;
        Map<ResourceType,Integer> resourcesFromStrongbox = new HashMap<>();
        Map<ResourceType,Integer> resourcesFromWarehouse = new HashMap<>();
        out.println("You have to choose where you want to take " +resourceType.convertColor());
        if (quantityStrongbox > 0) {
            out.println("You can choose from strongbox: " + quantityStrongbox);
            out.println("Enter the quantity from the strongbox: ");
            out.println("If you don't want to take from strongbox enter 0");
            quantityChosenFromStrongbox = checkInt();
            if (quantityChosenFromStrongbox > 0)
                resourcesFromStrongbox.put(resourceType,quantityChosenFromStrongbox);
            else
                resourcesFromStrongbox.put(resourceType,0);
        }
        if (quantityWarehouse > 0) {
            out.println("You can choose from warehouse: " + quantityWarehouse);
            out.println("If you don't want to take from warehouse enter 0");
            out.println("Enter the quantity from the warehouse: ");
            quantityChosenFromWarehouse = checkInt();
            if (quantityChosenFromWarehouse > 0)
                resourcesFromWarehouse.put(resourceType,quantityChosenFromWarehouse);
            else
                resourcesFromWarehouse.put(resourceType,0);
        }
        return new ResourcesChosen(resourcesFromWarehouse,resourcesFromStrongbox);
    }

    @Override
    public int chooseResource(ArrayList<ResourceType> resourcesToChoose){
        return choose(resourcesToChoose);
    }

    @Override
    public ResourceType chooseResource() {
        ArrayList<ResourceType> allValidResources = ResourceType.getAllValidResources();
        return allValidResources.get(choose(allValidResources));
    }

    /**
     * Chooses the resource type not specified in the output of a production
     * @param resourceToChoose the amount of resource to choose
     * @return the resources chosen
     */
    @Override
    public int chooseResourcesAny(ArrayList<ResourceType> resourceToChoose){
        return choose(resourceToChoose);
    }

    // BUY DEVELOPMENT CARDS
    /**
     * Chooses which development card has to be bought
     * @return the color and the level of the development card
     */
    @Override
    public int buyDevelopmentCards(ArrayList<DevelopmentCard> cards)  {
        return choose(cards);
    }

    /**
     * Chooses the slot where to put the development card bought
     * @return the slot chosen
     */
    @Override
    public int chooseSlot(ArrayList<Integer> slotsAvailable) {
        return choose(slotsAvailable);
    }

    //TAKE FROM MARKET
    /**
     * Chooses if the player wants to select a row or a column and the its number
     * @param market: market to show to the player to choose the resources to take
     */
    @Override
    public ChosenLine chooseLine(Market market) {
        String rc;
        MarketAxis marketAxis;
        int num;
        int numMax;
        showMarket(market);
        alreadyTried = false;
        do {
            checkAlreadyTried();
            out.print("Choose the 'row' or the 'column' : ");
            rc = in.nextLine();
        } while (rc == null || (!rc.equalsIgnoreCase("row") && !rc.equalsIgnoreCase("column")));
        if (rc.equalsIgnoreCase("row")) {
            marketAxis = MarketAxis.ROW;
            numMax = 3;
        } else {
            marketAxis = MarketAxis.COL;
            numMax = 4;
        }
        alreadyTried = false;
        do {
            checkAlreadyTried();
            out.print("Enter a number from 1 to " + numMax + ":  ");
            num = checkInt();
        } while (num > numMax || num <= 0);
        return new ChosenLine(marketAxis, num);
    }


    @Override
    public int chooseWhiteMarble(ArrayList<ResourceType> activeWhiteMarbleConversion){
        return choose(activeWhiteMarbleConversion);
    }

    @Override
    public int chooseDepot(ArrayList<WarehouseDepot> depotsToChoose, Warehouse warehouse){
        return choose(depotsToChoose);
    }

    @Override
    public int choosePlayer(ArrayList<ClientPlayer> playersToChoose) {
        return 0;
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
    public int choose (List<?> elemsToChoose){
        int choice = 0;
        String input = null;
        Map<Integer,?> elemMap = getMap(elemsToChoose);
        elemMap.forEach((i,elem) -> out.println(i + ". " + elem.toString()));
        out.println("Enter a value between 1 and " + elemsToChoose.size() + " ");
        alreadyTried = false;
        do {
            checkAlreadyTried();
            choice = checkInt();
        }while(choice<1 || choice>elemsToChoose.size());

        return choice-1;
    }
}