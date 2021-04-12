package it.polimi.ingsw.model.turn_taker;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCardStrategy;
import it.polimi.ingsw.model.board.PersonalBoard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class Player implements TurnTaker {
    private String username;
    private int personalVictoryPoints;
    private PersonalBoard personalBoard;
    private Collection<LeaderCardStrategy> leaderCards;
    private Collection<ResourceType> whiteMarbleResource;
    private Collection<ResourceType> discountOfOneResource;
    // private ActionStrategy turnAction; TODO

    public Player(String username) {
        this.username = username;
        leaderCards = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public int getPersonalVictoryPoints() {
        return personalVictoryPoints;
    }

    /**
     * Draws and sets the leader cards. This has side effects on deck
     * @param deck The deck to draw from
     */
    public void drawLeaderCard(Deck<LeaderCardStrategy> deck) {
        // TODO ??
    }

    /**
     * Notifies the view, allows to pick a strategy
     */
    public void pickStrategy() {
        // TODO ??
    }

    /**
     * Inspects the board for resources
     * @return the count of the resources
     */
    public int getResourceAmount() {
        // TODO
        return 0;
    }

    public int getResourceAmount(ResourceType resourceType) {
        return personalBoard.getResourceAmount(resourceType);
    }


    /**
     * Discards a leader card, add a step on the faith track
     * @param leaderCardStrategy the card to discard
     */
    public void discardLeaderCard(LeaderCardStrategy leaderCardStrategy) {
        processLeaderCard(leaderCardStrategy);
        // TODO increment faith
    }

    /**
     * Activates a leader card
     * @param leaderCardStrategy the card to activate
     */
    public void activateLeaderCard(LeaderCardStrategy leaderCardStrategy) {
        processLeaderCard(leaderCardStrategy);
        leaderCardStrategy.activate(this);
         // TODO activate/inject
    }

    private void processLeaderCard(LeaderCardStrategy leaderCardStrategy) {
        personalVictoryPoints += leaderCardStrategy.getVictoryPoints();
        leaderCards.remove(leaderCardStrategy);
    }

    @Override
    public void playTurn() {
        // TODO
    }
    public void addAdditionalDepot(WarehouseDepot warehouseDepot) {
        personalBoard.addAdditionalDepot(warehouseDepot);
    }

    public void addAdditionalRule(TradingRule tradingRule){
        personalBoard.addAdditionalRule(tradingRule);
    }

    public void addWhiteMarbleResource(ResourceType resourceType){
        whiteMarbleResource.add(resourceType);
    }

    public void addDiscount(ResourceType resourceType) {
        discountOfOneResource.add(resourceType);
    }

    public void addDevelopmentCard(DevelopmentCard card) {
        personalBoard.addDevelopmentCard(card);
    }

    public int applyDiscount(ResourceType resourceType){
        int discount = (int)discountOfOneResource.stream().filter(discountType -> discountType == resourceType).count();
        return -discount;
    }

    public int getMaxDevelopmentLevel(DevelopmentColor developmentColor) {
        return personalBoard.getMaxDevelopmentLevel(developmentColor);
    }

    public int getDevelopmentQuantity(DevelopmentColor developmentColor) {
        return personalBoard.getDevelopmentQuantity(developmentColor);
    }


}
