package it.polimi.ingsw.model.turn_taker;

import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.game_status.GameStatus;
import it.polimi.ingsw.message.game_status.GameStatusDTO;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.board.PersonalBoard;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.cards.NoEligiblePlayerException;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;

public class Player implements TurnTaker {
    private final String username;
    private int personalVictoryPoints;
    private PersonalBoard personalBoard;
    private List<LeaderCard> nonActiveLeaderCards;
    private List<LeaderCard> activeLeaderCards;
    private ArrayList<ResourceType> activeWhiteConversions;
    private Map<ResourceType,Integer> activeDiscounts;

    public Player(String username) {
        this.username = username;
        nonActiveLeaderCards = new ArrayList<>();
        activeWhiteConversions = new ArrayList<>();
        activeDiscounts = new HashMap<>();
        activeLeaderCards = new ArrayList<>();
        personalVictoryPoints = 0;
    }
    public List<LeaderCard> getNonActiveLeaderCards() {
        return nonActiveLeaderCards;
    }

    public void loadFromSettings() {
        personalBoard = new PersonalBoard();
    }

    public List<LeaderCard> getActiveLeaderCards() {
        return activeLeaderCards;
    }

    @Override
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

    public void setNonActiveLeaderCards(List<LeaderCard> nonActiveLeaderCards) {
        this.nonActiveLeaderCards = nonActiveLeaderCards;
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
        try {
            if (nonActiveLeaderCards.remove(leaderCard)) {
                Game.getInstance().getFaithTrack().move(this, 1);
                if (Game.getInstance().getFaithTrack().getPosition(this) == 24)
                    Game.getInstance().setEnded();
            }
        }catch(Exception ignored){}
    }

    /**
     * Activates a leader card
     * @param leaderCard the card to activate
     */
    public void activateLeaderCard(LeaderCard leaderCard) {
        try {
            if (leaderCard.activate(this)) {
                addPersonalVictoryPoints(leaderCard.getVictoryPoints());
                activeLeaderCards.add(leaderCard);
                nonActiveLeaderCards.remove(leaderCard);
            }
        }catch(NoEligiblePlayerException e){} // TODO
    }


    /**
     * Notifies the view
     */
    @Override
    public void playTurn() {
        VirtualView.getInstance().playTurn(this);
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

    public boolean hasDiscountForResource(ResourceType resourceType){
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

    public int getDevelopmentCardNumber() {
        return personalBoard.getDevelopmentCardNumber();
    }

    public Warehouse getWarehouse() {
        return personalBoard.getWarehouse();
    }

    public Map<ResourceType, Integer> getStrongbox() {
        return personalBoard.getStrongbox();
    }

    public boolean isStrongboxEmpty(){
        for(ResourceType resourceType: getStrongbox().keySet()){
            if (getStrongbox().containsKey(resourceType) && getStrongbox().get(resourceType) > 0)
                return true;
        }
        return false;
    }
    public DevelopmentSlot[] getDevelopmentSlots(){
        return personalBoard.getDevelopmentSlots();
    }

    public TurnTakerScore computeScore() {
        return new TurnTakerScore(personalVictoryPoints, personalBoard.getResourceTotal());
    }

    @Override
    public String getFaithId() {
        return "player." + getUsername(); // TODO Move prefix outside
    }
}
