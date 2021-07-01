package it.polimi.ingsw.view.gui.scene_controller;


import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.ReactiveObserver;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.action.FinishTurn;
import it.polimi.ingsw.client.action.leader.ActivateLeaderCard;
import it.polimi.ingsw.client.action.leader.DiscardLeaderCard;
import it.polimi.ingsw.client.action.show.ShowDevelopmentCards;
import it.polimi.ingsw.client.action.show.ShowMarket;
import it.polimi.ingsw.client.action.show.ShowOpponentLastAction;
import it.polimi.ingsw.client.action.show.ShowPlayer;
import it.polimi.ingsw.client.action.starting.PickStartingLeaderCards;
import it.polimi.ingsw.client.action.starting.PickStartingResources;
import it.polimi.ingsw.client.action.turn.ActivateProduction;
import it.polimi.ingsw.client.action.turn.BuyDevelopmentCard;
import it.polimi.ingsw.client.action.turn.SortWarehouse;
import it.polimi.ingsw.client.action.turn.TakeFromMarket;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.AdditionalDepot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.view.gui.GUIController;
import it.polimi.ingsw.view.gui.SceneManager;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

public class PickAnActionController extends ReactiveObserver {

    @FXML
    private ImageView cell0;
    @FXML
    private ImageView cell1;
    @FXML
    private ImageView cell2;
    @FXML
    private ImageView cell3;
    @FXML
    private ImageView cell4;
    @FXML
    private ImageView cell5;
    @FXML
    private ImageView cell6;
    @FXML
    private ImageView cell7;
    @FXML
    private ImageView cell8;
    @FXML
    private ImageView cell9;
    @FXML
    private ImageView cell10;
    @FXML
    private ImageView cell11;
    @FXML
    private ImageView cell12;
    @FXML
    private ImageView cell13;
    @FXML
    private ImageView cell14;
    @FXML
    private ImageView cell15;
    @FXML
    private ImageView cell16;
    @FXML
    private ImageView cell17;
    @FXML
    private ImageView cell18;
    @FXML
    private ImageView cell19;
    @FXML
    private ImageView cell20;
    @FXML
    private ImageView cell21;
    @FXML
    private ImageView cell22;
    @FXML
    private ImageView cell23;
    @FXML
    private ImageView cell24;
    @FXML
    private ImageView tile2;
    @FXML
    private ImageView tile3;
    @FXML
    private ImageView tile4;
    @FXML
    private FlowPane startingPane;
    @FXML
    private Button showMarket;
    @FXML
    private Button showPlayers;
    @FXML
    private Button showDevelopmentCard;
    @FXML
    private Button discardLeaderCard;
    @FXML
    private Button activateLeaderCard;
    @FXML
    private Button sortWarehouse;
    @FXML
    private Button activateProduction;
    @FXML
    private Button buyDevelopmentCard;
    @FXML
    private Button takeFromMarket;
    @FXML
    private Button finishTurn;

    @FXML
    private Button pickStartingLeaderCards;

    @FXML
    private Button pickStartingResources;

    @FXML
    private Button showOpponentLastAction;

    @FXML
    private ScrollBar logger;
    @FXML
    private GridPane leaderCards;

    @FXML
    private ImageView card00;
    @FXML
    private ImageView card01;
    @FXML
    private ImageView card02;
    @FXML
    private ImageView card10;
    @FXML
    private ImageView card11;
    @FXML
    private ImageView card12;
    @FXML
    private ImageView card20;
    @FXML
    private ImageView card21;
    @FXML
    private ImageView card22;
    @FXML
    private ImageView depot10;
    @FXML
    private ImageView depot20;
    @FXML
    private ImageView depot21;
    @FXML
    private ImageView depot30;
    @FXML
    private ImageView depot31;
    @FXML
    private ImageView depot32;
    @FXML
    private Label coinsStrongbox;
    @FXML
    private Label stonesStrongbox;
    @FXML
    private Label servantsStrongbox;
    @FXML
    private Label shieldsStrongbox;

    @FXML
    private ImageView leader0;
    @FXML
    private ImageView leader1;
    @FXML
    private ImageView additionalDepot00;
    @FXML
    private ImageView additionalDepot01;
    @FXML
    private ImageView additionalDepot10;
    @FXML
    private ImageView additionalDepot11;


    private Map<String, ImageView> previousPositions = new HashMap<>();

    private ArrayList<ButtonBase> showButtons;
    private ArrayList<ButtonBase> turnButtons;
    private ConcurrentLinkedDeque<ClientAction> possibleActions;
    private Map<String,Integer> leaderCardToShow = new HashMap<>();
    private static final int  HEIGHT_LEADER_CARD = 200;
    private static final int WIDTH_LEADER_CARD = 150;


    public PickAnActionController(ClientGameObserverProducer clientGameObserverProducer) {
        super(clientGameObserverProducer);
    }

    private void setupShowButtons() {
        showButtons = new ArrayList<>();
        showButtons.add(showDevelopmentCard);
        buttonActions.put(ShowDevelopmentCards.class, showDevelopmentCard);
        showButtons.add(showMarket);
        buttonActions.put(ShowMarket.class, showMarket);
        showButtons.add(showPlayers);
        buttonActions.put(ShowPlayer.class, showPlayers);
        showButtons.add(showOpponentLastAction);
        buttonActions.put(ShowOpponentLastAction.class, showOpponentLastAction);
    }

    private void setupTurnButtons() {
        turnButtons = new ArrayList<>();
        turnButtons.add(pickStartingLeaderCards);
        buttonActions.put(PickStartingLeaderCards.class, pickStartingLeaderCards);
        turnButtons.add(pickStartingResources);
        buttonActions.put(PickStartingResources.class, pickStartingResources);
        turnButtons.add(discardLeaderCard);
        buttonActions.put(DiscardLeaderCard.class, discardLeaderCard);
        turnButtons.add(sortWarehouse);
        buttonActions.put(SortWarehouse.class, sortWarehouse);
        turnButtons.add(activateLeaderCard);
        buttonActions.put(ActivateLeaderCard.class, activateLeaderCard);
        turnButtons.add(takeFromMarket);
        buttonActions.put(TakeFromMarket.class, takeFromMarket);
        turnButtons.add(activateProduction);
        buttonActions.put(ActivateProduction.class, activateProduction);
        turnButtons.add(buyDevelopmentCard);
        buttonActions.put(BuyDevelopmentCard.class, buyDevelopmentCard);
        turnButtons.add(finishTurn);
        buttonActions.put(FinishTurn.class, finishTurn);
    }

    private Map<Class<? extends ClientAction>, Button> buttonActions;

    public void initialize() {
        buttonActions = new HashMap<>();
        setupShowButtons();
        setupTurnButtons();
        buttonActions.forEach((klass, button) -> button.setOnAction(actionEvent -> setPickedAction(klass)));
    }

    /**
     * Setup all buttons to make available only the ones which correspond to an available action ones
     */
    private void react() {
        this.possibleActions = clientGameObserverProducer.getActions();
        if (!clientGameObserverProducer.getOpponent().isPresent())
            showOpponentLastAction.setDisable(true);
        turnButtons.forEach(turnButton -> turnButton.setDisable(true));
        possibleActions.stream()
                .map(possibleAction -> buttonActions.get(possibleAction.getClass()))
                .forEach(button -> button.setDisable(false));
        if (clientGameObserverProducer.getCurrentPlayer() != null)
            reactPersonalBoard(clientGameObserverProducer.getCurrentPlayer());
        if (clientGameObserverProducer.getFaithTrack() != null) {
            reactFaithTrack(clientGameObserverProducer.getUsername());
            if (clientGameObserverProducer.getOpponent().isPresent())
                reactFaithTrack(Opponent.USERNAME);
        }

    }

    private void reactFaithTrack(String player) {
        String path = player.equals(Opponent.USERNAME) ? "GUIResources/Punchboard/Faith/BlackCross.png" : "GUIResources/Punchboard/Faith/Faithpoint.png";
        if (previousPositions.containsKey(player)) {
            previousPositions.get(player).setImage(null);
        }
        ImageView cell = getCell(clientGameObserverProducer.getFaithTrack().getMarkers().get(player));
        cell.setImage(((ImageView)SceneManager.getInstance().getNode(path)).getImage());
        previousPositions.put(player, cell);
    }

    private void reactDevelopmentCards() {
        ImageView imageView;
        ImageView cacheDevelopmentCard;
        DevelopmentCard developmentCard;
        DevelopmentSlot[] developmentSlots = clientGameObserverProducer.getCurrentPlayer().getDevelopmentSlots();
        for (int i = 0; i < developmentSlots.length; i++) {
            for (int j = 0; j < developmentSlots[i].getCards().size(); j++) {
                ArrayList<DevelopmentCard> slot = new ArrayList<>(developmentSlots[i].getCards());
                developmentCard = slot.get(j);
                imageView = getDevelopmentImageView(i, j);
                cacheDevelopmentCard = (ImageView) SceneManager.getInstance().getNode(developmentCard.getPath());
                imageView.setImage(cacheDevelopmentCard.getImage());
            }
        }
    }

    private void reactStrongbox() {
        Label label;
        for (ResourceType resourceType : clientGameObserverProducer.getCurrentPlayer().getStrongbox().keySet()) {
            label = getStrongboxLabel(resourceType);
            label.setText(String.valueOf(clientGameObserverProducer.getCurrentPlayer().getStrongbox().get(resourceType)));
        }
    }

    private void reactWarehouse(){
        ArrayList<WarehouseDepot> depots = clientGameObserverProducer.getCurrentPlayer().getWarehouse().getAllDepots();
        for (WarehouseDepot depot : depots) {
            if (!depot.isAdditional())
                reactDepots(depot);
            else{
                for (int i = 0; i < clientGameObserverProducer.getCurrentPlayer().getActiveLeaderCards().size(); i++){
                    reactAdditionalDepot(depot,i);
                }
            }
        }
    }


    private void reactAdditionalDepot(WarehouseDepot additionalDepot, int leaderSlot){
        ResourceType resourceType = additionalDepot.getResourceType();
        ImageView imageView0;
        ImageView imageView1;
        if(leaderSlot==0){
            imageView0 = additionalDepot00;
            additionalDepot00.setVisible(true);
            imageView1 = additionalDepot01;
            additionalDepot00.setVisible(true);
        }else{
            imageView0 = additionalDepot10;
            additionalDepot00.setVisible(true);
            imageView1 = additionalDepot11;
            additionalDepot00.setVisible(true);
        }
        if(!additionalDepot.isEmpty()){
            imageView0.setImage(SceneManager.getInstance().getResourceImage(resourceType).getImage());
            if(additionalDepot.getQuantity()>1){
                imageView1.setImage(SceneManager.getInstance().getResourceImage(resourceType).getImage());
            }
        }
    }

    private ImageView getDevelopmentImageView(int slot, int card){
        if(slot==0){
            if(card==0){
                return card00;
            }
            if(card==1){
                return card01;
            }
            if(card==2){
                return card02;
            }
        }
        if(slot==1){
            if(card==0){
                return card10;
            }
            if(card==1){
                return card11;
            }
            if(card==2){
                return card12;
            }
        }
        if(slot==2){
            if(card==0){
                return card20;
            }
            if(card==1){
                return card21;
            }
            if(card==2){
                return card22;
            }
        }
        return card22;
    }

    private Label getStrongboxLabel(ResourceType resourceType){
        if(resourceType.equals(ResourceType.Coins)) {
            return coinsStrongbox;
        }
        if(resourceType.equals(ResourceType.Stones)) {
            return stonesStrongbox;
        }
        if(resourceType.equals(ResourceType.Servants)) {
            return servantsStrongbox;
        }
        if(resourceType.equals(ResourceType.Shields)) {
            return shieldsStrongbox;
        }
        return null;
    }

    private void reactDepots(WarehouseDepot depot){
        ImageView imageView;
        int level = depot.getLevel();
        if(!depot.isEmpty()){
            switch(level){
                case 1:
                    imageView = depot10;
                    imageView.setImage(SceneManager.getInstance().getResourceImage(depot.getResourceType()).getImage());
                    break;
                case 2:
                    imageView = depot20;
                    imageView.setImage(SceneManager.getInstance().getResourceImage(depot.getResourceType()).getImage());
                    if(depot.getQuantity()>1){
                        imageView = depot21;
                        imageView.setImage(SceneManager.getInstance().getResourceImage(depot.getResourceType()).getImage());
                    }
                    break;

                case 3:
                    imageView = depot30;
                    imageView.setImage(SceneManager.getInstance().getResourceImage(depot.getResourceType()).getImage());
                    if(depot.getQuantity()>1){
                        imageView = depot31;
                        imageView.setImage(SceneManager.getInstance().getResourceImage(depot.getResourceType()).getImage());
                        if(depot.getQuantity()>2){
                            imageView = depot32;
                            imageView.setImage(SceneManager.getInstance().getResourceImage(depot.getResourceType()).getImage());
                        }
                    }
                    break;

                default:

            }
        }
    }

    private void reactPersonalBoard(Player player){
        cleanWarehouse();
        cleanStrongbox();
        cleanAdditionalDepot();
        cleanLeaderCards();
        reactDevelopmentCards();
        reactStrongbox();
        reactWarehouse();
        reactLeaderCards(player);
    }

    private void reactLeaderCards(Player player){
        System.out.println(leaderCardToShow);
        if (player.getNonActiveLeaderCards().size() > 1){
            for (int i = 0 ; i < player.getNonActiveLeaderCards().size(); i++){
                LeaderCard leaderCard = player.getNonActiveLeaderCards().get(i);
                if (!leaderCardToShow.containsKey(leaderCard.getPath())) {
                    leaderCardToShow.put(leaderCard.getPath(), i);
                    setLeaderCardToShow(leaderCard.getPath(), i, false);
                    System.out.println("DENTRO " + leaderCardToShow);
                }
            }
        }
        else{
            for (LeaderCard leaderCard: player.getActiveLeaderCards()){
                if (leaderCardToShow.containsKey(leaderCard.getPath())){
                    setLeaderCardToShow(leaderCard.getPath(),leaderCardToShow.get(leaderCard.getPath()),true);
                }
                if (leaderCard.getClass().equals(AdditionalDepot.class)){
                    ResourceType resourceType = ((AdditionalDepot) leaderCard).getDepotResourceType();
                    WarehouseDepot additionalDepot = (WarehouseDepot) player.getWarehouse().getAdditionalDepots().stream().filter(depot -> depot.getResourceType().equals(resourceType));
                    reactAdditionalDepot(additionalDepot,leaderCardToShow.get(leaderCard.getPath()));
                }
            }
        }
    }
    private void setLeaderCardToShow(String path, int leaderID, boolean visible){
            ImageView leaderCardImage = new ImageView(path);
            leaderCardImage.setFitHeight(HEIGHT_LEADER_CARD);
            leaderCardImage.setFitWidth(WIDTH_LEADER_CARD);
            if (leaderID == 0) {
                leader0.setImage(new Image(path));
                leaderCards.getChildren().add(0,leader0);
            }
            else {
                leader1.setImage(new Image(path));
                leaderCards.getChildren().add(1,leader1);
            }
    }


    private void cleanWarehouse(){
        depot10.setImage(null);
        depot20.setImage(null);
        depot21.setImage(null);
        depot30.setImage(null);
        depot31.setImage(null);
        depot32.setImage(null);
    }

    private void cleanStrongbox(){
        coinsStrongbox.setText("0");
        stonesStrongbox.setText("0");
        servantsStrongbox.setText("0");
        shieldsStrongbox.setText("0");
    }

    private void cleanLeaderCards(){
        leader0.setImage(null);
        leader1.setImage(null);
    }

    private void cleanAdditionalDepot(){
        additionalDepot00.setImage(null);
        additionalDepot01.setImage(null);
        additionalDepot10.setImage(null);
        additionalDepot11.setImage(null);
    }

    private void setPickedAction(Class<? extends ClientAction> pickedActionClass){
        Optional<ClientAction> oPickedAction = possibleActions.stream()
                .filter(clientAction -> clientAction.getClass().equals(pickedActionClass)).findFirst();
        oPickedAction.ifPresent(pickedAction -> GUIController.getInstance().setPickedAction(pickedAction));
    }

    @Override
    public void update() {
        Platform.runLater(this::react);
    }

    private ImageView getCell (int position) {
        switch (position) {
            case 0:
                return cell0;
            case 1:
                return cell1;
            case 2:
                return cell2;
            case 3:
                return cell3;
            case 4:
                return cell4;
            case 5:
                return cell5;
            case 6:
                return cell6;
            case 7:
                return cell7;
            case 8:
                return cell8;
            case 9:
                return cell9;
            case 10:
                return cell10;
            case 11:
                return cell11;
            case 12:
                return cell12;
            case 13:
                return cell13;
            case 14:
                return cell14;
            case 15:
                return cell15;
            case 16:
                return cell16;
            case 17:
                return cell17;
            case 18:
                return cell18;
            case 19:
                return cell19;
            case 20:
                return cell20;
            case 21:
                return cell21;
            case 22:
                return cell22;
            case 23:
                return cell23;
            case 24:
                return cell24;
            default:
                return null;
        }
    }

}
