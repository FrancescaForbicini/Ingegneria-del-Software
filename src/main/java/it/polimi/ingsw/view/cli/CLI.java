package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.action.turn.ChosenLine;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.market.MarketAxis;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Opponent;
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
    private boolean alreadyTried;
    private boolean firstReload = true;

    /**
     * Starts the view
     *
     */
    @Override
    public void startView() {}

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
     * Asks the username to the player
     *
     * @return the username of the player
     */
    private String askUsername() {
        String username = null;
        while (username == null || username.equals("") || username.equals(Opponent.USERNAME)) {
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
     * Asks the max number of the players
     *
     * @return the number of the player of the game
     */
    private int askMaxPlayers() {
        out.println("Enter the number of players, if you are joining a game, this number will be discarded: ");
        int response;
        do {
            response = checkInt();
        } while(response == -2);
        if (response >= 1 && response <= 4)
            return response;
        out.println("Invalid choice. If you are creating a game, a default one will be created (solo-game)");
        return 1;
    }


    /**
     * Shows to the client the market, the development cards, the faith track or other players
     *
     * @param actions the action chosen from the player
     * @return what the client want to see
     */
    @Override
    public Optional<ClientAction> pickAnAction(ArrayList<ClientAction> actions) {
        if (actions.isEmpty() || firstReload)
            return Optional.empty();
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
     * Picks the two leader cards
     *
     * @param proposedCards the four leader cards proposed to the client
     * @return the index of the leader cards chosen
     */
    @Override
    public int pickStartingLeaderCards(List<LeaderCard> proposedCards) {
        return choose(proposedCards);
    }

    /**
     * Picks the leader cards
     *
     * @param proposedCards leader cards available
     * @return the index of the leader card chosen
     */
    @Override
    public int pickLeaderCard(List<LeaderCard> proposedCards) {
        if (proposedCards.size() == 1) {
            out.println("You will active this leader card: " + proposedCards.get(0) + "\n");
            return 0;
        }
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

    /**
     * Shows the non active leader cards of the player
     *
     * @param player the player that wants to see another player
     */
    public void showPlayer(ClientPlayer player) {
        out.print(player.toString());
        List<LeaderCard> nonActiveLeaderCards = player.getNonActiveLeaderCards();
        if(nonActiveLeaderCards.size()>0) {
            nonActiveLeaderCards.forEach(out::print);
        }
    }

    /**
     * Prints a message to the player
     *
     * @param message the message to show
     */
    @Override
    public boolean showMessage(String message) {
        out.println(message);
        return true;
    }

    /**
     * Chooses the production to activate
     *
     * @param availableProductions productions that the player can choose to activate
     * @return index of the production chosen
     */
    @Override
    public int chooseProductionToActivate(ArrayList<Eligible> availableProductions){
        if(availableProductions.size()==1)
            out.println("You will activate this production: \n" + availableProductions.get(0));
        else {
            ArrayList<TradingRule> tradingRulesToChoose = new ArrayList<>();
            for (Eligible card : availableProductions) {
                if (card.getClass().equals(DevelopmentCard.class))
                    tradingRulesToChoose.add(((DevelopmentCard) card).getTradingRule());
                else
                    tradingRulesToChoose.add(((AdditionalTradingRule) card).getAdditionalTradingRule());
            }
            return choose(tradingRulesToChoose);
        }
        return 0;
    }

    /**
     * Asks to choose
     *
     * @return true iff the player has chosen to activate another production
     */
    @Override
    public boolean chooseAnotherProduction(){
        out.println("Do you want to activate another production? ");
        String response = null;
        do {
            out.println("Enter 'yes' or 'no': ");
            response = in.nextLine();
        }while (!response.equals("yes") && !response.equals("no"));
        return response.equals("yes");
    }

    /**
     * Chooses a resource from the resources type available
     *
     * @param resourcesToChoose the resources that can be chosen
     * @return the index of the resource chosen
     */
    @Override
    public int chooseResource(ArrayList<ResourceType> resourcesToChoose){
        return choose(resourcesToChoose);
    }

    /**
     * Chooses a resources from all the resources available
     *
     * @return resource chosen
     */
    @Override
    public ResourceType chooseResource() {
        ArrayList<ResourceType> validResourceTypes = ResourceType.getAllValidResources();
        return validResourceTypes.get(choose(validResourceTypes));
    }

    /**
     * Chooses which development card has to be bought
     *
     * @return the index of the development card chosen
     */
    @Override
    public int buyDevelopmentCards(ArrayList<DevelopmentCard> cards)  {
        if (cards.size() == 1){
            out.println("You can buy only this card: " + cards.get(0).toString() + "\n");
            return 0;
        }
        return choose(cards);
    }

    /**
     * Chooses the slot where to put the development card bought
     *
     * @return the index of the slot chosen
     */
    @Override
    public int chooseSlot(ArrayList<DevelopmentSlot> slotsAvailable) {
        return choose(slotsAvailable);
    }

    /**
     * Chooses the amount of a resource from the strongbox
     *
     * @param resourceToTake resource to choose the amount
     * @param maxQuantity available quantity of the resource in the stronbox
     * @return quantity of the resource chosen from the strongbox
     */
    @Override
    public int chooseQuantityFromStrongbox(ResourceType resourceToTake, int maxQuantity) {
        out.println("Choose the quantity of " + resourceToTake + " from the strongbox: ");
        int chosenQuantity;
        alreadyTried = false;
        do{
            checkAlreadyTried();
            chosenQuantity = checkInt();
        } while(chosenQuantity < 0 || chosenQuantity > maxQuantity);
        return chosenQuantity;
    }

    /**
     * Chooses if the player wants to select a row or a column and the its number
     *
     * @param market: market to show to the player to choose the resources to take
     *
     * @return the row or the column chosen and the line
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


    /**
     * Chooses which conversion of the white marble has to be used
     *
     * @param activeWhiteMarbleConversion the conversion available of the white marbles
     * @return the index of the conversion of the white marble chosen
     */
    @Override
    public int chooseWhiteMarble(ArrayList<ResourceType> activeWhiteMarbleConversion){
        out.println("Choose which type of resource assign to the white marbles: ");
        return choose(activeWhiteMarbleConversion);
    }

    /**
     * Chooses the depot where the resource has to be put
     *
     * @param depotsToChoose the depot available
     * @return the index of the depot chosen
     */
    @Override
    public int chooseDepot(ArrayList<WarehouseDepot> depotsToChoose){
        return choose(depotsToChoose);
    }

    /**
     * Chooses which player do the current player wants to see
     *
     * @param clientPlayersToChoose the turn taker that che be showed
     *
     * @return the index of the player that has be chosen
     */
    @Override
    public int choosePlayer(ArrayList<ClientPlayer> clientPlayersToChoose) {
        ArrayList<String> players = new ArrayList<>();
        clientPlayersToChoose.forEach(clientPlayer -> players.add(clientPlayer.getUsername()));
        return choose(players);
    }

    /**
     * Notify that new actions are available
     */
    public boolean notifyNewActions() {
        if (!firstReload)
            out.println("New actions. Press 0 to reload.");
        else
            out.println("These are the actions available");
        firstReload = false;
        return true;
    }

    /**
     * Shows who has won
     *
     * @param winnerUsername the username of the winner
     */
    @Override
    public void showWinner(String winnerUsername) {
        out.println("The winner is: " + winnerUsername);
    }


    /**
     * Injects the game observer into the view
     *
     * @param gameObserverProducer the game observer
     */
    @Override
    public void inject(ClientGameObserverProducer gameObserverProducer) {}

    /**
     * Mapping between the element and its index of a generic list
     *
     * @param list generic list to map
     * @return mapping between an element and its index of a list
     */
    private static Map<Integer, ?> getMap(List<?> list){
        return IntStream.range(1, list.size() + 1).boxed().collect(Collectors.toMap(i -> i, i -> list.get(i - 1)));
    }

    /**
     * Checks if the response of the player is a valid number
     *
     * @return the response of the player if it is a valid number, if not return -2
     */
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

    /**
     * Checks if the player has already done a try of a choice
     *
     */
    private void checkAlreadyTried(){
        if(alreadyTried){
            out.print("Wrong value, please retry ");
        }else{
            alreadyTried = true;
        }
    }

    /**
     * Chooses a element from a list
     *
     * @param elemsToChoose the elements from which the player has to choose
     * @return the index of the element chosen
     */
    private int choose (List<?> elemsToChoose){
        int choice = 0;
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