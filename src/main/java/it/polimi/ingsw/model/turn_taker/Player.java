package it.polimi.ingsw.model.turn_taker;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.AssignWhiteMarble;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCardStrategy;
import it.polimi.ingsw.model.board.PersonalBoard;
import it.polimi.ingsw.model.cards.NoEligiblePlayerException;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_action_strategy.TurnActionStrategy;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.util.*;

public class Player implements TurnTaker {
    private String username;
    private int personalVictoryPoints;
    private PersonalBoard personalBoard;
    private Collection<LeaderCardStrategy> leaderCards;
    private Collection<LeaderCardStrategy> activeLeaderCards;
    private ArrayList<ResourceType> activeWhiteConversions;
    private Map<ResourceType,Integer> activeDiscounts;
    private TurnActionStrategy turnAction;

    public Player(String username) {
        this.username = username;
        personalBoard = new PersonalBoard();
        leaderCards = new ArrayList<>();
        activeWhiteConversions = new ArrayList<>();
        activeDiscounts = new HashMap<>();
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

    public ArrayList<ResourceType> getActiveWhiteConversions(){
        return activeWhiteConversions;
    }

    public Map<ResourceType,Integer> getActivateDiscounts(){
        return activeDiscounts;
    }
    /**
     * Draws and sets the leader cards. This has side effects on deck
     * @param deck The deck to draw from
     * @return the two leadercard of a player
     */
    public Collection<LeaderCardStrategy> drawLeaderCard(Deck<LeaderCardStrategy> deck) {
         leaderCards.add(Game.getInstance().getLeaderCards().drawFirstCard());
         leaderCards.add(Game.getInstance().getLeaderCards().drawFirstCard());
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
        try{
            leaderCardStrategy.activate(this);
        }
        catch (NoEligiblePlayerException e){
            e.printStackTrace();
        }

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
    public void addActiveWhiteConversion(ResourceType resourceType){
        activeWhiteConversions.add(resourceType);
            //TODO Warehouse --> WhiteMarbleDepot
    }

    public int getAmountActiveWhiteConversions(){
        return activeWhiteConversions.size();
    }

    public Collection<ResourceType> getWhiteMarbleResource (){
        return activeWhiteConversions;
    }

    public void addPersonalVictoryPoints(int victoryPoints){
        personalVictoryPoints+=victoryPoints;
    }
    public void addDiscount(ResourceType resourceType, Integer amount) {
        activeDiscounts.put(resourceType,amount);
    }

    public void addDevelopmentCard(DevelopmentCard card) {
        personalBoard.addDevelopmentCard(card);
    }

    public int applyDiscount(ResourceType resourceType){
        return -activeDiscounts.get(resourceType);
    }

    public boolean isDiscount(ResourceType resourceType){
        return getActiveDiscounts().containsKey(resourceType);
    }
    public Map<ResourceType, Integer> getActiveDiscounts(){ return activeDiscounts; }

    public int getMaxDevelopmentLevel(DevelopmentColor developmentColor) {
        return personalBoard.getMaxDevelopmentLevel(developmentColor);
    }

    public int getDevelopmentQuantity(DevelopmentColor developmentColor) {
        return personalBoard.getDevelopmentQuantity(developmentColor);
    }

    public int getDevelopmentQuantity(DevelopmentColor color, int level){
        return personalBoard.getDevelopmentLevel(color,level);
    }

}
