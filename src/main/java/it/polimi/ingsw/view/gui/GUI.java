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
    @Override
    public String askIP() {
        SceneManager.getInstance().waitStarted();
        SceneManager.getInstance().connection();
        String IP = GUIController.getInstance().getIp();
        System.out.println(IP);
        return IP;
    }

    @Override
    public Credentials askCredentials() {
        SceneManager.getInstance().login();
        Credentials credentials = GUIController.getInstance().getCredentials();
        System.out.println(credentials);
        return credentials;
    }

    @Override
    public Optional<ClientAction> pickAnAction(ArrayList<ClientAction> actions) {
        SceneManager.getInstance().pickAnAction();
        ClientAction clientAction = GUIController.getInstance().getPickedAction();
        return Optional.of(clientAction);
    }

    @Override
    public ResourceType pickStartingResources() {
        return chooseResource();
    }

    @Override
    public int chooseProductionToActivate(ArrayList<Eligible> availableProductions) {
        SceneManager.getInstance().chooseProductionToActivate(availableProductions);
        return GUIController.getInstance().getPickedIndex();
    }


    @Override
    public boolean chooseAnotherProduction() {
        SceneManager.getInstance().showConfirmation("Do you want choose another production?");
        return GUIController.getInstance().getAckMessage();
    }


    @Override
    public void startView() {
        launch(GUIApp.class);
    }

    @Override
    public boolean showMessage(String message) {
        SceneManager.getInstance().showAlert(message);
        return GUIController.getInstance().getAckMessage();
    }

    @Override
    public void showMarket(Market market) {
        SceneManager.getInstance().showMarket();
        GUIController.getInstance().getAckMessage();
    }

    @Override
    public void showDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {
        SceneManager.getInstance().showShowDevelopmentCards(developmentCards);
        GUIController.getInstance().getAckMessage();
    }

    @Override
    public void showPlayer(ClientPlayer player) {
        SceneManager.getInstance().showPlayer(player);
        GUIController.getInstance().getAckMessage();
    }



    @Override
    public int pickStartingLeaderCards(List<LeaderCard> proposedCards){
        return pickLeaderCard(proposedCards);
    }

    @Override
    public int pickLeaderCard(List<LeaderCard> proposedCards) {
        SceneManager.getInstance().pickLeaderCards((ArrayList<LeaderCard>) proposedCards);
        return GUIController.getInstance().getPickedIndex();
    }



    @Override
    public int buyDevelopmentCards(ArrayList<DevelopmentCard> cards) {
        SceneManager.getInstance().buyDevelopmentCards(cards);
        DevelopmentCard developmentCardChosen = GUIController.getInstance().getPickedDevelopmentCard();
        return indexChosen(cards,developmentCardChosen);
    }

    @Override
    public int chooseSlot(ArrayList<DevelopmentSlot> slotsAvailable) {
        SceneManager.getInstance().chooseSlot(slotsAvailable);
        return GUIController.getInstance().getPickedIndex();
    }


    @Override
    public ChosenLine chooseLine(Market market) {
        SceneManager.getInstance().chooseLine();
        return GUIController.getInstance().getChosenLineQueue();
    }

    @Override
    public int chooseWhiteMarble(ArrayList<ResourceType> activeWhiteConversions) {
        SceneManager.getInstance().chooseResource();
        ResourceType whiteMarbleChosen = GUIController.getInstance().getPickedResource();
        return indexChosen(activeWhiteConversions, whiteMarbleChosen);

    }

    @Override
    public boolean notifyNewActions() {
        return showMessage("Loading new actions");
    }


    @Override
    public int chooseDepot(ArrayList<WarehouseDepot> depotsToChoose) {
        SceneManager.getInstance().chooseDepot(depotsToChoose);
        return GUIController.getInstance().getPickedIndex();
    }

    @Override
    public int choosePlayer(ArrayList<ClientPlayer> clientPlayersToChoose) {
        SceneManager.getInstance().choosePlayer(clientPlayersToChoose);
        return GUIController.getInstance().getPickedIndex();
    }

    @Override
    public int chooseResource(ArrayList<ResourceType> resourcesToChoose){
        SceneManager.getInstance().chooseResource(resourcesToChoose);
        ResourceType resourceChosen = GUIController.getInstance().getPickedResource();
        return indexChosen(resourcesToChoose,resourceChosen);
    }

    @Override
    public ResourceType chooseResource() {
        SceneManager.getInstance().chooseResource();
        return GUIController.getInstance().getPickedResource();
    }


    @Override
    public void showWinner(String winnerUsername) {
        SceneManager.getInstance().showAlert(winnerUsername);
    }

    @Override
    public int chooseQuantity(int maxQuantity) {
        return 0;
    }

    @Override
    public void inject(ClientGameObserverProducer gameObserverProducer) {
        SceneManager.getInstance().start(gameObserverProducer);
    }

    private int indexChosen(ArrayList<?> listToChoose, Object chosen){
        for (int i = 0; i < listToChoose.size(); i++) {
            if (listToChoose.get(i).equals(chosen))
                return i;
        }
        return -1;
    }


}