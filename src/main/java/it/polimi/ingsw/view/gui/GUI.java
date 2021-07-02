package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.action.turn.ChosenLine;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.Credentials;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static javafx.application.Application.launch;


public class GUI implements View {

    /**
     * Starts the view
     *
     */
    @Override
    public void startView() {
        launch(GUIApp.class);
    }

    /**
     * Asks the IP of connection
     *
     * @return the IP of the connection
     */
    @Override
    public String askIP() {
        SceneManager.getInstance().waitStarted();
        SceneManager.getInstance().connection();
        String IP = GUIController.getInstance().getIp();
        return IP;
    }

    /**
     * Asks to the player: username and game ID
     *
     * @return the player with the username and the game ID
     */
    @Override
    public Credentials askCredentials() {
        SceneManager.getInstance().login();
        Credentials credentials = GUIController.getInstance().getCredentials();
        return credentials;
    }

    /**
     * Shows to the client the market, the development cards, the faith track or other players
     *
     * @param actions the action chosen from the player
     * @return what the client want to see
     */
    @Override
    public Optional<ClientAction> pickAnAction(ArrayList<ClientAction> actions) {
        SceneManager.getInstance().pickAnAction();
        ClientAction clientAction = GUIController.getInstance().getPickedAction();
        return Optional.of(clientAction);
    }

    /**
     * Picks the two leader cards
     *
     * @param proposedCards the four leader cards proposed to the client
     * @return the index of the leader cards chosen
     */
    @Override
    public int pickStartingLeaderCards(List<LeaderCard> proposedCards){
        return pickLeaderCard(proposedCards);
    }

    /**
     * Picks the leader cards
     *
     * @param proposedCards leader cards available
     * @return the index of the leader card chosen
     */
    @Override
    public int pickLeaderCard(List<LeaderCard> proposedCards) {
        SceneManager.getInstance().pickLeaderCards((ArrayList<LeaderCard>) proposedCards);
        return GUIController.getInstance().getPickedIndex();
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
        SceneManager.getInstance().showMarket();
        GUIController.getInstance().getAckMessage();
    }

    /**
     * Shows the development cards available
     *
     * @param developmentCards the development cards available
     */
    @Override
    public void showDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {
        SceneManager.getInstance().showShowDevelopmentCards(developmentCards);
        GUIController.getInstance().getAckMessage();
    }

    /**
     * Shows the non active leader cards of the player
     *
     * @param player the player that wants to see another player
     */
    @Override
    public void showPlayer(ClientPlayer player) {
        SceneManager.getInstance().showPlayer(player);
        GUIController.getInstance().getAckMessage();
    }

    /**
     * Prints a message to the player
     *
     * @param message the message to show
     */
    @Override
    public boolean showMessage(String message) {
        SceneManager.getInstance().showAlert(message);
        return GUIController.getInstance().getAckMessage();
    }

    /**
     * Chooses the production to activate
     *
     * @param availableProductions productions that the player can choose to activate
     * @return index of the production chosen
     */
    @Override
    public int chooseProductionToActivate(ArrayList<Eligible> availableProductions) {
        SceneManager.getInstance().chooseProductionToActivate(availableProductions);
        return GUIController.getInstance().getPickedIndex();
    }


    /**
     * Asks to choose
     *
     * @return true iff the player has chosen to activate another production
     */
    @Override
    public boolean chooseAnotherProduction() {
        SceneManager.getInstance().showConfirmation("Do you want choose another production?");
        return GUIController.getInstance().getAckMessage();
    }

    /**
     * Chooses a resource from the resources type available
     *
     * @param resourcesToChoose the resources that can be chosen
     * @return the index of the resource chosen
     */
    @Override
    public int chooseResource(ArrayList<ResourceType> resourcesToChoose){
        SceneManager.getInstance().chooseResource(resourcesToChoose);
        ResourceType resourceChosen = GUIController.getInstance().getPickedResource();
        return indexChosen(resourcesToChoose,resourceChosen);
    }

    /**
     * Chooses a resources from all the resources available
     *
     * @return resource chosen
     */
    @Override
    public ResourceType chooseResource() {
        SceneManager.getInstance().chooseResource();
        return GUIController.getInstance().getPickedResource();
    }


    /**
     * Chooses which development card has to be bought
     *
     * @return the index of the development card chosen
     */
    @Override
    public int buyDevelopmentCards(ArrayList<DevelopmentCard> cards) {
        SceneManager.getInstance().buyDevelopmentCards(cards);
        DevelopmentCard developmentCardChosen = GUIController.getInstance().getPickedDevelopmentCard();
        return indexChosen(cards,developmentCardChosen);
    }

    /**
     * Chooses the slot where to put the development card bought
     *
     * @return the index of the slot chosen
     */
    @Override
    public int chooseSlot(ArrayList<DevelopmentSlot> slotsAvailable) {
        SceneManager.getInstance().chooseSlot(slotsAvailable);
        return GUIController.getInstance().getPickedIndex();
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
        SceneManager.getInstance().chooseQuantity(resourceToTake, maxQuantity);
        return GUIController.getInstance().getChosenQuantity();
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
        SceneManager.getInstance().chooseLine();
        return GUIController.getInstance().getChosenLineQueue();
    }

    /**
     * Chooses which conversion of the white marble has to be used
     *
     * @param activeWhiteConversions the conversion available of the white marbles
     * @return the index of the conversion of the white marble chosen
     */
    @Override
    public int chooseWhiteMarble(ArrayList<ResourceType> activeWhiteConversions) {
        SceneManager.getInstance().chooseResource();
        ResourceType whiteMarbleChosen = GUIController.getInstance().getPickedResource();
        return indexChosen(activeWhiteConversions, whiteMarbleChosen);

    }

    /**
     * Chooses the depot where the resource has to be put
     *
     * @param depotsToChoose the depot available
     * @return the index of the depot chosen
     */
    @Override
    public int chooseDepot(ArrayList<WarehouseDepot> depotsToChoose) {
        SceneManager.getInstance().chooseDepot(depotsToChoose);
        return GUIController.getInstance().getPickedIndex();
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
        SceneManager.getInstance().choosePlayer(clientPlayersToChoose);
        return GUIController.getInstance().getPickedIndex();
    }

    /**
     * Notify that new actions are available
     */
    @Override
    public boolean notifyNewActions() {
        return showMessage("Loading new actions");
    }

    /**
     * Shows who has won
     *
     * @param winnerUsername the username of the winner
     */
    @Override
    public void showWinner(String winnerUsername) {
        SceneManager.getInstance().showAlert(winnerUsername);
    }


    /**
     * Injects the game observer into the view
     *
     * @param gameObserverProducer the game observer
     */
    @Override
    public void inject(ClientGameObserverProducer gameObserverProducer) {
        SceneManager.getInstance().start(gameObserverProducer);
    }

    /**
     * Gets the index of an element chosen from a generic list
     * @param listToChoose list where to choose the element
     * @param chosen element chosen from the list
     * @return index of the element chosen
     */
    private int indexChosen(ArrayList<?> listToChoose, Object chosen){
        for (int i = 0; i < listToChoose.size(); i++) {
            if (listToChoose.get(i).equals(chosen))
                return i;
        }
        return -1;
    }


}