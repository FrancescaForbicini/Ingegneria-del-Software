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
    public ResourceType chooseWhiteMarble(ArrayList<ResourceType> activeWhiteMarbleConversion){
        return activeWhiteMarbleConversion.get(choose(activeWhiteMarbleConversion));
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
        out.println("Enter a value between 1 and " + elemsToChoose.size() + " ");
        Map<Integer,?> elemMap = getMap(elemsToChoose);
        elemMap.forEach((i,elem) -> out.println(i + ". " + elem.toString()));
        alreadyTried = false;
        do {
            checkAlreadyTried();
            input = in.nextLine();
            if (input.matches("[0-9]+"))
                choice = Integer.parseInt(input);
            else {
                choice = -1;
            }
        }while(choice<1 || choice>elemsToChoose.size());
        return choice-1;
    }
}