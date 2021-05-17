package it.polimi.ingsw.model.turn_taker;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.board.PersonalBoard;
import it.polimi.ingsw.model.cards.NoEligiblePlayerException;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_action.TurnAction;

import java.util.*;

public class Player implements TurnTaker {
    private String username;
    private int personalVictoryPoints;
    private PersonalBoard personalBoard;
    private Collection<LeaderCard> leaderCards;
    private Collection<LeaderCard> activeLeaderCards;
    private ArrayList<ResourceType> activeWhiteConversions;
    private Map<ResourceType,Integer> activeDiscounts;
    private TurnAction turnAction;

    public Player(String username) {
        this.username = username;
        personalBoard = new PersonalBoard();
        leaderCards = new ArrayList<>();
        activeWhiteConversions = new ArrayList<>();
        activeDiscounts = new HashMap<>();

        // TODO USE below to setup player correcty
        // Settings.getInstance();
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
    /**d
     * TODO deprecated, "view" picks tsehem
     * Draws and sets the leader cards. This has side effects on deck
     * @return the two leadercard of a player
     */
    public Collection<LeaderCard> drawLeaderCard() {
         leaderCards.add(Game.getInstance().getLeaderCards().drawFirstCard().get());
         leaderCards.add(Game.getInstance().getLeaderCards().drawFirstCard().get());
        return leaderCards;
    }

    public void setLeaderCards(Collection<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    /**
     * Notifies the view, allows to pick an action
     */
    public void setTurnAction(TurnAction turnActionChosen) {
        this.turnAction = turnActionChosen;
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
     * @param leaderCard the card to discard
     */
    public void discardLeaderCard(LeaderCard leaderCard) {
        Game.getInstance().getFaithTrack().move(this,1);
        leaderCards.remove(leaderCard);
    }

    /**
     * Activates a leader card
     * @param leaderCard the card to activate
     */
    public void activateLeaderCard(LeaderCard leaderCard) {
        addPersonalVictoryPoints(leaderCard.getVictoryPoints());
        try{
            leaderCard.activate(this);
        }
        catch (NoEligiblePlayerException e){
            e.printStackTrace();
        }

        activeLeaderCards.add(leaderCard);
        leaderCards.remove(leaderCard);
    }

    @Override
    public void playTurn() {
        turnAction.play(this);
    }

    public void addAdditionalDepot(ResourceType resourceType, int level) { personalBoard.addAdditionalDepot(resourceType, level);}

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
    @Override
    public void addPersonalVictoryPoints(int victoryPoints){
        personalVictoryPoints+=victoryPoints;
    }
    public void addDiscount(ResourceType resourceType, Integer amount) {
        if (activeDiscounts.containsKey(resourceType))
            activeDiscounts.replace(resourceType,amount+activeDiscounts.get(resourceType));
        else
            activeDiscounts.put(resourceType,amount);
    }

    public boolean addDevelopmentCard(DevelopmentCard card, int slotID) {
        return personalBoard.addDevelopmentCard(card,slotID);
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
        return personalBoard.getDevelopmentQuantity(color,level);
    }

}
