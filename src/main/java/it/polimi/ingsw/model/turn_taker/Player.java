package it.polimi.ingsw.model.turn_taker;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.AssignWhiteMarble;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCardStrategy;
import it.polimi.ingsw.model.board.PersonalBoard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_action_strategy.TurnActionStrategy;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class Player implements TurnTaker {
    private String username;
    private int personalVictoryPoints;
    private PersonalBoard personalBoard;
    private Collection<LeaderCardStrategy> leaderCards;
    private Collection<LeaderCardStrategy> activeLeaderCards;
    private Collection<ResourceType> whiteMarbleResource;
    private Collection<ResourceType> discountOfOneResource;
    private TurnActionStrategy turnAction;

    public Player(String username) {
        this.username = username;
        personalBoard = new PersonalBoard();
        leaderCards = new ArrayList<>();
        whiteMarbleResource = new ArrayList<>();
        discountOfOneResource = new ArrayList<>();
    }

    public String getUsername () {
        return username;
    }

    public int getPersonalVictoryPoints() {
        return personalVictoryPoints;
    }

    public PersonalBoard getPersonalBoard(){
        return personalBoard;
    }


    /**
     * Draws and sets the leader cards. This has side effects on deck
     * @param deck The deck to draw from
     * @return the two leadercard of a player
     */
    public Collection<LeaderCardStrategy> drawLeaderCard(Deck<LeaderCardStrategy> deck) {
         leaderCards.add(Game.getInstance().getLeaderCardStrategy().drawFirstCard());
         leaderCards.add(Game.getInstance().getLeaderCardStrategy().drawFirstCard());
        return leaderCards;
    }
    /**
     * Notifies the view, allows to pick a strategy
     */
    public void pickStrategy() {
        // TODO ??
    }

    /**
     * Inspects the board for resources
     * @param resourceType the type of resource to get the amount
     * @return the count of the resources
     */
    public int getResourceAmount(ResourceType resourceType) {
        return personalBoard.getResourceAmount(resourceType);
    }

    /**
     * Discards a leader card, add a step on the faith track
     * @param leaderCardStrategy the card to discard
     */
    public void discardLeaderCard(LeaderCardStrategy leaderCardStrategy) {
        FaithTrack.getInstance().move(this,1);
        leaderCards.remove(leaderCardStrategy);
    }

    /**
     * Activates a leader card
     * @param leaderCardStrategy the card to activate
     */
    public void activateLeaderCard(LeaderCardStrategy leaderCardStrategy) {
        addPersonalVictoryPoints(leaderCardStrategy.getVictoryPoints());
        leaderCardStrategy.activate(this);
        activeLeaderCards.add(leaderCardStrategy);
        leaderCards.remove(leaderCardStrategy);
    }

    @Override
    public void playTurn() {
        turnAction.play(this);
    }

    public void addAdditionalDepot(WarehouseDepot warehouseDepot) { personalBoard.addAdditionalDepot(warehouseDepot);}

    public void addAdditionalRule(TradingRule tradingRule){
        personalBoard.addAdditionalRule(tradingRule);
    }

    /**
     * Manages if a white marble has to convert and in which resource
     * @param resourceType the resource that a white marble has to be converted, if it is possible
     */
    public void addWhiteMarbleResource(ResourceType resourceType){
        whiteMarbleResource.add(resourceType);
            //TODO Warehouse --> WhiteMarbleDepot
    }

    public void addPersonalVictoryPoints(int victoryPoints){
        personalVictoryPoints+=victoryPoints;
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

    public int getDevelopmentQuantity(DevelopmentColor developmentColor, int level){
        return personalBoard.getDevelopmentQuantity(developmentColor,level);
    }

}
