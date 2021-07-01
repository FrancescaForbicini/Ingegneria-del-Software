package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.client.Client;
import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.cards.*;
import it.polimi.ingsw.model.faith.Cell;
import it.polimi.ingsw.model.faith.CellGroup;
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
    private Settings settings;
    private ArrayList<CustomDevelopmentCard> customDevelopmentCards;
    private ArrayList<DevelopmentCard> modifiedDevelopmentCards;
    private ArrayList<CustomEligibleCard> customLeaderCards;
    private ArrayList<LeaderCard> modifiedLeaderCards;
    private CustomTradingRule customBasicProduction;
    private TradingRule modifiedBasicProduction;
    private ArrayList<CustomCell> customCells;
    private ArrayList<Cell> modifiedCells;
    private ArrayList<CustomCellGroup> customCellGroups;
    private ArrayList<CellGroup> modifiedCellGroups;

    @Override
    public void start(Stage stage) throws Exception {
        settings = Settings.getInstance();
        window = stage;
        initializeScenes();

        window.setScene(developmentCardsScene);

        window.setFullScreen(true);
        window.setX(0);
        window.setY(0);
        window.show();
    }



    private void initializeScenes(){
        initializeDevelopmentScene();
        initializeLeaderScene();
        initializeBasicProductionScene();
        initializeFaithTrackScene();
    }


    private void addCustomLeaderCard(int i,LeaderCard leaderCard, GridPane allLeadCards) {
        if (leaderCard.getClass().equals(AdditionalDepot.class)) {
            CustomAdditionalDepot customAdditionalDepot = new CustomAdditionalDepot(leaderCard, true);
            customLeaderCards.add(i, customAdditionalDepot);
            allLeadCards.add(customAdditionalDepot.getNodeToModify(),1,i);
        }
        if (leaderCard.getClass().equals(AdditionalTradingRule.class)) {
            CustomAdditionalTradingRule customAdditionalTradingRule = new CustomAdditionalTradingRule(leaderCard, true);
            customLeaderCards.add(i, customAdditionalTradingRule);
            allLeadCards.add(customAdditionalTradingRule.getNodeToModify(),1,i);
        }
        if (leaderCard.getClass().equals(AssignWhiteMarble.class)) {
            CustomAssignWhiteMarble customAssignWhiteMarble = new CustomAssignWhiteMarble(leaderCard, true);
            customLeaderCards.add(i, customAssignWhiteMarble);
            allLeadCards.add(customAssignWhiteMarble.getNodeToModify(),1,i);
        }
        if (leaderCard.getClass().equals(Discount.class)) {
            CustomDiscount customDiscount = new CustomDiscount(leaderCard, true);
            customLeaderCards.add(i, customDiscount);
            allLeadCards.add(customDiscount.getNodeToModify(),1,i);
        }

    }

    private void loadDevelopmentCards(){
        modifiedDevelopmentCards = new ArrayList<>();
        for(CustomDevelopmentCard customDevelopmentCard : customDevelopmentCards){
            modifiedDevelopmentCards.add((DevelopmentCard) customDevelopmentCard.getModified());
        }
        System.out.println(modifiedDevelopmentCards);
        window.setScene(leaderCardsScene);
        window.setFullScreen(true);
    }

    private void loadLeaderCards(){
        modifiedLeaderCards = new ArrayList<>();
        for(CustomEligibleCard customEligibleCard : customLeaderCards){
            modifiedLeaderCards.add((LeaderCard) customEligibleCard.getModified());
        }
        System.out.println(modifiedLeaderCards);
        window.setScene(basicProductionScene);
        window.setFullScreen(true);
    }
    private void loadBasicProduction(){
        modifiedBasicProduction = (TradingRule) customBasicProduction.getModified();
        System.out.println(modifiedBasicProduction);
        window.setScene(faithTrackScene);
        window.setFullScreen(true);
    }
    private void loadFaithTrack(){
        modifiedCells = new ArrayList<>();
        for(CustomCell customCell : customCells){
            modifiedCells.add((Cell)customCell.getModified());
        }
        modifiedCells.forEach(cell -> System.out.println(cell.getCellVictoryPoints()));
        modifiedCellGroups = new ArrayList<>();
        for(CustomCellGroup customCellGroup : customCellGroups){
            modifiedCellGroups.add((CellGroup) customCellGroup.getModified());
        }
        modifiedCellGroups.forEach(cellGroup -> System.out.println(cellGroup.getTileVictoryPoints()));
        endCustomSettingsGUI();
    }
    private void endCustomSettingsGUI(){
        Client.customSettings.loadCustomSettings(modifiedDevelopmentCards,modifiedLeaderCards,modifiedCells,modifiedCellGroups,modifiedBasicProduction);
        stop();
    }


    @Override
    public void stop(){
        Platform.exit();
    }


    private void initializeDevelopmentScene(){
        //dev cards
        GridPane allDevCards = new GridPane();
        allDevCards.setAlignment(Pos.CENTER);
        allDevCards.setGridLinesVisible(true);
        ArrayList<DevelopmentCard> developmentCards = settings.getDevelopmentCards();
        customDevelopmentCards = new ArrayList<>();
        for(int i=0; i<developmentCards.size(); i++){
            DevelopmentCard developmentCard = developmentCards.get(i);
            CustomDevelopmentCard customDevelopmentCard = new CustomDevelopmentCard(developmentCard, true);
            customDevelopmentCards.add(i,customDevelopmentCard);
            allDevCards.add(customDevelopmentCard.getNodeToModify(),1,i);
        }
        developmentPane = new FlowPane();

        Button leaderCardsButton = new Button("Modify Development Cards");
        leaderCardsButton.setOnAction(actionEvent -> loadDevelopmentCards());
        developmentPane.getChildren().add(leaderCardsButton);

        developmentPane.getChildren().add(allDevCards);
        ScrollPane scrollDevPane = new ScrollPane();
        scrollDevPane.setContent(developmentPane);
        developmentCardsScene = new Scene(scrollDevPane);
    }
    private void initializeLeaderScene(){
        //leader cards
        GridPane allLeadCards = new GridPane();
        allLeadCards.setAlignment(Pos.CENTER);
        allLeadCards.setGridLinesVisible(true);
        ArrayList<LeaderCard> leaderCards = settings.getLeaderCards();
        customLeaderCards = new ArrayList<>();
        for(int i=0; i<leaderCards.size(); i++){
            LeaderCard leaderCard = leaderCards.get(i);
            addCustomLeaderCard(i,leaderCard, allLeadCards);
        }
        leaderPane = new FlowPane();
        Button basicProductionButton = new Button("Modify Leader Cards");
        basicProductionButton.setOnAction(actionEvent -> loadLeaderCards());
        leaderPane.getChildren().add(basicProductionButton);
        leaderPane.getChildren().add(allLeadCards);
        ScrollPane scrollLeadPane = new ScrollPane();
        scrollLeadPane.setContent(leaderPane);
        leaderCardsScene = new Scene(scrollLeadPane);
    }
    private void initializeBasicProductionScene(){
        //basic production
        FlowPane basicProduction = new FlowPane();
        basicProduction.setAlignment(Pos.CENTER);
        customBasicProduction = new CustomTradingRule(settings.getBasicProduction(), true);
        basicProduction.getChildren().add(customBasicProduction.getNodeToModify());
        basicPane = new FlowPane();
        Button faithTrackButton = new Button("Modify Basic Production");
        faithTrackButton.setOnAction(actionEvent -> loadBasicProduction());
        basicPane.getChildren().add(faithTrackButton);
        basicPane.getChildren().add(basicProduction);
        ScrollPane scrollBasicPane = new ScrollPane();
        scrollBasicPane.setContent(basicPane);
        basicProductionScene = new Scene(scrollBasicPane);
    }
    private void initializeFaithTrackScene(){
        //faith
        HBox allFaithTrack = new HBox();
        GridPane allCells = new GridPane();
        allCells.setAlignment(Pos.CENTER);
        allCells.setGridLinesVisible(true);
        ArrayList<Cell> cells = settings.getCells();
        customCells = new ArrayList<>();
        for(int i=0; i<cells.size(); i++){
            Cell cell = cells.get(i);
            CustomCell customCell = new CustomCell(cell, true);
            customCells.add(i,customCell);
            allCells.add(customCell.getNodeToModify(),1,i);
        }
        allFaithTrack.getChildren().add(allCells);
        GridPane allCellGroups = new GridPane();
        allCellGroups.setAlignment(Pos.CENTER);
        allCellGroups.setGridLinesVisible(true);
        ArrayList<CellGroup> cellGroups = settings.getGroups();
        customCellGroups = new ArrayList<>();
        for(int i=0; i<cellGroups.size(); i++){
            CellGroup cellGroup = cellGroups.get(i);
            CustomCellGroup customCellGroup = new CustomCellGroup(cellGroup, true);
            customCellGroups.add(i,customCellGroup);
            allCellGroups.add(customCellGroup.getNodeToModify(),1,i);
        }
        allFaithTrack.getChildren().add(allCellGroups);
        faithPane = new FlowPane();
        Button endButton = new Button("Modify Faith Track");
        endButton.setOnAction(actionEvent -> loadFaithTrack());
        faithPane.getChildren().add(endButton);

        faithPane.getChildren().add(allFaithTrack);
        ScrollPane scrollFaithPane = new ScrollPane();
        scrollFaithPane.setContent(faithPane);
        faithTrackScene = new Scene(scrollFaithPane);
    }
}
