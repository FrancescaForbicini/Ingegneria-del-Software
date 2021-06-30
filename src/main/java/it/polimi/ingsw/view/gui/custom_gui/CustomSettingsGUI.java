package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.faith.Cell;
import it.polimi.ingsw.model.faith.CellGroup;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.requirement.TradingRule;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.*;

public class CustomSettingsGUI extends Application {
    private Stage window;
    private FlowPane developmentPane;
    private FlowPane leaderPane;
    private FlowPane basicPane;
    private FlowPane faithPane;

    private Scene developmentCardsScene, leaderCardsScene, basicProductionScene, faithTrackScene, endScene;
    private Settings defaultSettings;
    private ArrayList<CustomDevelopmentCard> customDevelopmentCards;
    private ArrayList<DevelopmentCard> modifiedDevelopmentCards;
    private ArrayList<CustomLeaderCard> customLeaderCards;
    private ArrayList<LeaderCard> modifiedLeaderCards;
    private CustomTradingRule customBasicProduction;
    private TradingRule modifiedBasicProduction;
    private ArrayList<CustomCell> customCells;
    private ArrayList<Cell> modifiedCells;
    private ArrayList<CustomCellGroup> customCellGroups;
    private ArrayList<CellGroup> modifiedCellGroups;

    @Override
    public void start(Stage stage) throws Exception {
        defaultSettings = Settings.getInstance();
        window = stage;
        initializeScenes();



        Button endButton = new Button("End modify");
        endButton.setOnAction(actionEvent -> loadFaithTrack());
        faithPane.getChildren().add(endButton);

        window.setScene(developmentCardsScene);
        window.setMaximized(true);
        window.show();
    }



    private void initializeScenes(){
        //dev cards
        GridPane allDevCards = new GridPane();
        allDevCards.setAlignment(Pos.CENTER);
        allDevCards.setGridLinesVisible(true);
        ArrayList<DevelopmentCard> developmentCards = defaultSettings.getDevelopmentCards();
        customDevelopmentCards = new ArrayList<>();
        for(int i=0; i<developmentCards.size(); i++){
            DevelopmentCard developmentCard = developmentCards.get(i);
            CustomDevelopmentCard customDevelopmentCard = new CustomDevelopmentCard(developmentCard);
            customDevelopmentCards.add(i,customDevelopmentCard);
            allDevCards.add(customDevelopmentCard.getToModify(),1,i);
        }
        developmentPane = new FlowPane();

        Button leaderCardsButton = new Button("Modify Leader Cards");
        leaderCardsButton.setOnAction(actionEvent -> loadDevelopmentCards());
        developmentPane.getChildren().add(leaderCardsButton);

        developmentPane.getChildren().add(allDevCards);
        ScrollPane scrollDevPane = new ScrollPane();
        scrollDevPane.setContent(developmentPane);
        developmentCardsScene = new Scene(scrollDevPane);

        //leader cards
        GridPane allLeadCards = new GridPane();
        allLeadCards.setAlignment(Pos.CENTER);
        allLeadCards.setGridLinesVisible(true);
        ArrayList<LeaderCard> leaderCards = defaultSettings.getLeaderCards();
        customLeaderCards = new ArrayList<>();
        for(int i=0; i<leaderCards.size(); i++){
            LeaderCard leaderCard = leaderCards.get(i);
            addCustomLeaderCard(i,leaderCard, allLeadCards);
        }
        leaderPane = new FlowPane();
        Button basicProductionButton = new Button("Modify Basic Production");
        basicProductionButton.setOnAction(actionEvent -> loadLeaderCards());
        leaderPane.getChildren().add(basicProductionButton);
        leaderPane.getChildren().add(allLeadCards);
        ScrollPane scrollLeadPane = new ScrollPane();
        scrollLeadPane.setContent(leaderPane);
        leaderCardsScene = new Scene(scrollLeadPane);

        //basic production
        FlowPane basicProduction = new FlowPane();
        basicProduction.setAlignment(Pos.CENTER);
        customBasicProduction = new CustomTradingRule(defaultSettings.getBasicProduction());
        basicProduction.getChildren().add(customBasicProduction.getToModify());
        basicPane = new FlowPane();
        Button faithTrackButton = new Button("Modify Faith Track");
        faithTrackButton.setOnAction(actionEvent -> loadBasicProduction());
        basicPane.getChildren().add(faithTrackButton);
        basicPane.getChildren().add(basicProduction);
        ScrollPane scrollBasicPane = new ScrollPane();
        scrollBasicPane.setContent(basicPane);
        basicProductionScene = new Scene(scrollBasicPane);

        //faith
        faithPane = new FlowPane();
    }

    private void addCustomLeaderCard(int i,LeaderCard leaderCard, GridPane allLeadCards) {
        if (leaderCard.getClass().equals(AdditionalDepot.class)) {
            CustomAdditionalDepot customAdditionalDepot = new CustomAdditionalDepot(leaderCard);
            customLeaderCards.add(i, customAdditionalDepot);
            allLeadCards.add(customAdditionalDepot.getToModify(),1,i);
        }
        if (leaderCard.getClass().equals(AdditionalTradingRule.class)) {
            CustomAdditionalTradingRule customAdditionalTradingRule = new CustomAdditionalTradingRule(leaderCard);
            customLeaderCards.add(i, customAdditionalTradingRule);
            allLeadCards.add(customAdditionalTradingRule.getToModify(),1,i);
        }
        if (leaderCard.getClass().equals(AssignWhiteMarble.class)) {
            CustomAssignWhiteMarble customAssignWhiteMarble = new CustomAssignWhiteMarble(leaderCard);
            customLeaderCards.add(i, customAssignWhiteMarble);
            allLeadCards.add(customAssignWhiteMarble.getToModify(),1,i);
        }
        if (leaderCard.getClass().equals(Discount.class)) {
            CustomDiscount customDiscount = new CustomDiscount(leaderCard);
            customLeaderCards.add(i, customDiscount);
            allLeadCards.add(customDiscount.getToModify(),1,i);
        }

    }

    private void loadDevelopmentCards(){
        modifiedDevelopmentCards = new ArrayList<>();
        for(CustomDevelopmentCard customDevelopmentCard : customDevelopmentCards){
            modifiedDevelopmentCards.add((DevelopmentCard) customDevelopmentCard.getModified());
        }
        System.out.println(modifiedDevelopmentCards);
        window.setScene(leaderCardsScene);
        window.setMaximized(true);
    }

    private void loadLeaderCards(){
        modifiedLeaderCards = new ArrayList<>();
        for(CustomLeaderCard customLeaderCard : customLeaderCards){
            modifiedLeaderCards.add((LeaderCard) customLeaderCard.getModified());
        }
        System.out.println(modifiedLeaderCards);
        window.setScene(basicProductionScene);
        window.setMaximized(true);
    }
    private void loadBasicProduction(){
        modifiedBasicProduction = (TradingRule) customBasicProduction.getModified();
        System.out.println(modifiedBasicProduction);
        window.setScene(faithTrackScene);
        window.setMaximized(true);
    }
    private void loadFaithTrack(){
        modifiedCells = new ArrayList<>();
        for(CustomCell customCell : customCells){
            modifiedCells.add((Cell)customCell.getModified());
        }
        modifiedCellGroups = new ArrayList<>();
        for(CustomCellGroup customCellGroup : customCellGroups){
            modifiedCellGroups.add((CellGroup) customCellGroup.getModified());
        }
        endCustomSettingsGUI();
    }
    private void endCustomSettingsGUI(){
        //TODO create json
        //TODO maybe clean
        stop();
    }

    @Override
    public void stop(){
        Platform.exit();
    }
}
