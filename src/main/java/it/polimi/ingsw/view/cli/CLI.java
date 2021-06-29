package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.client.action.turn.ChosenLine;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.model.market.MarketAxis;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.leader_cards.AdditionalTradingRule;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.model.cards.leader_cards.LeaderCard;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.model.turn_taker.Player;
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

    @Override
    public void updateCurrentPlayer(Player currentPlayer) {

    }

    @Override
    public void startView() {

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

    @Override
    public void canNotDoTheAction(){
        out.println("You can't do the action");
    }

    /**
     * Picks the leader cards
     *
     * @param proposedCards leader cards available
     * @return the index of the leader card chosen
     */
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
     * Prints a message to the player
     *
     * @param message the message to show
     */
    @Override
    public boolean showMessage(String message) {
        out.println(message);
        return true;
    }

    // ACTIVATE PRODUCTION

    @Override
    public int chooseProductionToActivate(ArrayList<Eligible> availableProductions){
    //public int chooseProductionToActivate(ArrayList<Eligible> availableProductions, boolean oneUsed){
        ArrayList<TradingRule> tradingRulesToChoose = new ArrayList<>();
        for (Eligible card: availableProductions){
            if (card.getClass().equals(DevelopmentCard.class))
                tradingRulesToChoose.add(((DevelopmentCard) card).getTradingRule());
            else
                tradingRulesToChoose.add(((AdditionalTradingRule) card).getAdditionalTradingRule());
        }
        return choose(tradingRulesToChoose);




        /*ArrayList<TradingRule> tradingRulesToChoose = new ArrayList<>();
        //create the array of trading rules to choose
        for (Eligible card: availableProductions){
            if (card.getClass().equals(DevelopmentCard.class))
                tradingRulesToChoose.add(((DevelopmentCard) card).getTradingRule());
            else
                tradingRulesToChoose.add(((AdditionalTradingRule) card).getAdditionalTradingRule());
        }
        if (oneUsed) {
            //the player can not activate a production if he has already activated one
            out.println("This are the productions available: ");
            tradingRulesToChoose.forEach(tradingRule -> out.println(tradingRule.toString()));
            out.println("Do you want to choose a card? ");
            if (!wantsToContinue())
                return -1;
        }
        return choose(tradingRulesToChoose);
         */
    }

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
     * Asks to choose
     *
     * @return true iff the player has chosen 'yes'
     */
    @Override
    public boolean wantsToContinue(){
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

    @Override
    public ResourceType chooseResource() {
        ArrayList<ResourceType> validResourceTypes = ResourceType.getAllValidResources();
        return validResourceTypes.get(choose(validResourceTypes));
    }


    // BUY DEVELOPMENT CARDS
    /**
     * Chooses which development card has to be bought
     *
     * @return the index of the development card chosen
     */
    @Override
    public int buyDevelopmentCards(ArrayList<DevelopmentCard> cards)  {
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

    //TAKE FROM MARKET
    /**
     * Chooses if the player wants to select a row or a column and the its number
     *
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

    @Override
    public int chooseQuantity(int maxQuantity) {
        int chosenQuantity;
        alreadyTried = false;
        do{
            checkAlreadyTried();
            chosenQuantity = checkInt();
        } while(chosenQuantity < 0 || chosenQuantity > maxQuantity);
        return chosenQuantity;
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