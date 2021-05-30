package it.polimi.ingsw.view.gui;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedDeque;

import static javafx.application.Application.launch;


public class GUI implements View{

    @Override
    public void startView() {
        launch(GUIApp.class);
    }

    //TODO implements methods

    @Override
    public ClientAction pickAnAction(ConcurrentLinkedDeque<ClientAction> actions) {
        return null;
    }

    @Override
    public void showMarket(Market market) {

    }

    @Override
    public void showDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {

    }

    @Override
    public void showFaithTrack(FaithTrack faithTrack) {

    }

    @Override
    public void showPlayer(ArrayList<ClientPlayer> players) {

    }

    @Override
    public ClientPlayer askCredentials(){
        GUIApp.getInstance().setupScene("Login.fxml");
        return new ClientPlayer(GUIApp.getInstance().getUsername(),GUIApp.getInstance().getGameID());
    }


    @Override
    public String askIP() {
        GUIApp.getInstance().setupScene("Connection.fxml");
        return GUIApp.getInstance().getIp();
    }

    @Override
    public List<LeaderCard> pickLeaderCards(List<LeaderCard> proposedCards) throws IOException {
        return null;
    }

    @Override
    public ArrayList<ResourceType> pickStartingResources(int numberOfResources) {
        return null;
    }

    @Override
    public void showStart()  {

    }

    @Override
    public void errorStartGame() throws IOException {
        /*
        stage.close();
        Stage error = new Stage();
        error.setFullScreen(true);
        error.setMaximized(true);
        VBox errorLabel = new VBox();
        Label label = new Label("Error, your username is already present");
        errorLabel.setAlignment(Pos.CENTER);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font(null, FontWeight.BOLD, 20));
        label.setTextFill(Color.RED);
        label.setStyle("-fx-background-color: rgba(149,21,21,0.67)");
        HBox hBox = new HBox(10);
        Button back = new Button("Back");
        //per definire proprietà grafiche bottone
        hBox.setAlignment(Pos.BOTTOM_CENTER);
        back.setStyle("-fx-background-color: green");
        back.setPrefSize(100,100);
        back.setFont(Font.font(null,FontWeight.NORMAL,20));
        hBox.getChildren().add(back);
        //Dopo click visualizzo
        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                startGame();
            }
        });
        errorLabel.getChildren().add(label);
        errorLabel.getChildren().add(hBox);
        errorLabel.setStyle("-fx-background-color: #800000");
        Scene scene = new Scene(errorLabel,100,100);
        error.setScene(scene);
        stage = error;
        error.show();
        */
    }

    @Override
    public LeaderCardChoice chooseLeaderCardAction() {
        return null;
    }

    @Override
    public ArrayList<LeaderCard> pickLeaderCardToActivate(List<LeaderCard> leaderCards) {
        return null;
    }

    @Override
    public ArrayList<LeaderCard> pickLeaderCardToDiscard(List<LeaderCard> leaderCards) {
        return null;
    }

    @Override
    public ArrayList<TradingRule> chooseTradingRulesToActivate(ArrayList<TradingRule> activeTradingRules) {
        return null;
    }

    @Override
    public ArrayList<ResourceType> chooseAnyInput(ArrayList<ResourceType> chosenInputAny) {
        return null;
    }

    @Override
    public ArrayList<ResourceType> chooseAnyOutput(ArrayList<ResourceType> chosenOutputAny) {
        return null;
    }

    @Override
    public Map<ResourceType, Integer> inputFromStrongbox(Map<ResourceType, Integer> resources) {
        return null;
    }

    @Override
    public Map<ResourceType, Integer> inputFromWarehouse(Map<ResourceType, Integer> resources) {
        return null;
    }

    @Override
    public DevelopmentCard buyDevelopmentCards(ArrayList<DevelopmentCard> cards) {
        return null;
    }

    @Override
    public int chooseSlot() {
        return 0;
    }

    @Override
    public boolean askSortWarehouse() {
        return false;
    }

    @Override
    public Warehouse sortWarehouse(Warehouse warehouse) {
        return null;
    }

    @Override
    public Map<String, Integer> chooseLine() {
        return null;
    }

    @Override
    public ArrayList<ResourceType> chooseResourceAny(ArrayList<ResourceType> resources, ArrayList<ResourceType> activatedWhiteMarbles) {
        return null;
    }

    @Override
    public Map<ResourceType, Integer> resourceToDepot(ArrayList<ResourceType> resources,ClientPlayer player) {
        return null;
    }

    @Override
    public SoloTokenChoice pickSoloToken(ConcurrentLinkedDeque<SoloGameAction> soloTokens) {
        return null;
    }

    @Override
    public ArrayList<DevelopmentCard> DevelopmentCardsToDiscard(ArrayList<DevelopmentCard> developmentCardsAvailable, ArrayList<DevelopmentCard> developmentCardsToDiscard) {
        return null;
    }

    @Override
    public void showMoveBlackShuffle(Deck<SoloToken> soloTokenDecks, FaithTrack faithTrack) {

    }

    @Override
    public void showMoveBlackCross(FaithTrack faithTrack) {

    }
}

