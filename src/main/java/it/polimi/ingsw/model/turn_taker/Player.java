package it.polimi.ingsw.model.turn_taker;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.board.PersonalBoard;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.server.VirtualView;

import java.util.*;

/**
 * Represents the user inside the game
 */
public class Player implements TurnTaker {
    private final String username;
    private int personalVictoryPoints;
    private PersonalBoard personalBoard;
    private List<LeaderCard> nonActiveLeaderCards;
    private final List<LeaderCard> activeLeaderCards;
    private final ArrayList<ResourceType> activeWhiteConversions;
    private final Map<ResourceType,Integer> activeDiscounts;

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

    /**
     * Prepares a new PersonalBoard when Settings are loaded
     */
    public void createPersonalBoard() {
        personalBoard = new PersonalBoard();
    }

    public List<LeaderCard> getActiveLeaderCards() {
        return activeLeaderCards;
    }

    /**
     * {@inheritDoc}
     *
     * @return username
     */
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
     *
     * @param resourceType the type of resource to get the amount
     * @return the count of the resources
     */
    public int getResourceQuantity(ResourceType resourceType) {
        return personalBoard.getResourceQuantity(resourceType);
    }


    /**
     * Discards a leader card, add a step on the faith track
     *
     * @param leaderCard the card to discard
     */
    public void discardLeaderCard(LeaderCard leaderCard) {
        try {
            if (nonActiveLeaderCards.remove(leaderCard)) {
                Game.getInstance().getFaithTrack().move(this, 1);
                if (Game.getInstance().getFaithTrack().getPosition(this) == 24)
                    Game.getInstance().setEnded();
            }
        }catch(Exception e){
            //if the game is corrupted, the game will end
            Game.getInstance().setEnded();
            Game.getInstance().setCorrupted();
        }
    }

    /**
     * Gets all quantity of all resources into player's storages
     *
     * @return total amount of resources
     */
    public int getTotalAmount(){
        return personalBoard.getTotalQuantityFromStrongbox()+personalBoard.getTotalQuantityFromWarehouse();
    }

    /**
     * Activates a leader card
     *
     * @param leaderCard the card to activate
     */
    public void activateLeaderCard(LeaderCard leaderCard) {
        if (leaderCard.activate(this)) {
            addPersonalVictoryPoints(leaderCard.getVictoryPoints());
            activeLeaderCards.add(leaderCard);
            nonActiveLeaderCards.remove(leaderCard);
        } else {
            //if the game is corrupted, the game will end
            Game.getInstance().setEnded();
            Game.getInstance().setCorrupted();
        }
    }


    /**
     * Notifies the view
     */
    @Override
    public void playTurn() {
        VirtualView.getInstance().playTurn(this);
    }

    /**
     * Adds an additional Depot
     *
     * @param resourceType of the depot
     * @param level of the depot
     */
    public void addAdditionalDepot(ResourceType resourceType, int level) { personalBoard.addAdditionalDepot(resourceType, level);}

    /**
     * Adds an additional TradingRule
     *
     * @param tradingRule to be added
     */
    public void addAdditionalRule(TradingRule tradingRule){
        personalBoard.addAdditionalRule(tradingRule);
    }

    /**
     * Manages if a white marble has to convert and in which resource
     *
     * @param resourceType the resource that a white marble has to be converted, if it is possible
     */
    public void addActiveWhiteConversion(ResourceType resourceType){
        activeWhiteConversions.add(resourceType);
    }

    public Collection<ResourceType> getActiveWhiteMarbleConversions(){
        return activeWhiteConversions;
    }

    /**
     * Adds victory point to this
     *
     * @param victoryPoints to add
     */
    @Override
    public void addPersonalVictoryPoints(int victoryPoints){
        personalVictoryPoints+=victoryPoints;
    }


    /**
     * Adds a new discount for the Player
     *
     * @param resourceType discounted
     * @param amount to be discounted
     */
    public void addDiscount(ResourceType resourceType, Integer amount) {
        if (activeDiscounts.containsKey(resourceType))
            activeDiscounts.replace(resourceType,amount+activeDiscounts.get(resourceType));
        else
            activeDiscounts.put(resourceType,amount);
    }

    /**
     * Adds a DevelopmentCard to a certain slot
     *
     * @param card to add
     * @param slotID where to add the card
     * @return true iff the operation ends
     */
    public boolean addDevelopmentCard(DevelopmentCard card, int slotID) {
        return personalBoard.addDevelopmentCard(card,slotID);
    }

    /**
     * Checks if a DevelopmentCard can be added to a certain slot
     *
     * @param developmentCardToAdd to be added
     * @param slotID where to add the card
     * @return true iff the operation can be completely executed
     */
    public boolean canAddDevelopmentCard(DevelopmentCard developmentCardToAdd, int slotID){
        return personalBoard.canAddCardToSlot(developmentCardToAdd, slotID);
    }

    /**
     * Applies a discount when buying a card which requires a certain ResourceType
     *
     * @param resourceType to discounted
     * @return amount discounted
     */
    public int applyDiscount(ResourceType resourceType){
        return -activeDiscounts.get(resourceType);
    }

    /**
     * Checks if the Player has active discounts for a certain ResourceType
     *
     * @param resourceType of discount checked
     * @return true iff discounts are present
     */
    public boolean hasDiscountForResource(ResourceType resourceType){
        return getActiveDiscounts().containsKey(resourceType);
    }

    /**
     * Gets mapping between ResourceType and its active discount amount
     *
     * @return mapping
     */
    public Map<ResourceType, Integer> getActiveDiscounts(){ return activeDiscounts; }

    /**
     * Gets the maximum level for cards of a certain color
     *
     * @param developmentColor to check on
     * @return maximum level present
     */
    public int getMaxDevelopmentLevel(DevelopmentColor developmentColor) {
        return personalBoard.getMaxDevelopmentLevel(developmentColor);
    }

    /**
     * Gets the quantity of cards of a certain color
     *
     * @param developmentColor required
     * @return amount of cards of required color
     */
    public int getDevelopmentQuantity(DevelopmentColor developmentColor) {
        return personalBoard.getDevelopmentQuantity(developmentColor);
    }

    /**
     * Gets the quantity of cards of a certain color and a certain level
     *
     * @param color required
     * @param level required
     * @return amount of cards matching both color and level
     */
    public int getDevelopmentQuantity(DevelopmentColor color, int level){
        return personalBoard.getDevelopmentQuantity(color,level);
    }

    /**
     * Gets the total quantity of development cards
     *
     * @return amount of cards
     */
    public int getDevelopmentCardNumber() {
        return personalBoard.getDevelopmentCardNumber();
    }

    public Warehouse getWarehouse() {
        return personalBoard.getWarehouse();
    }

    /**
     * Gets total quantity of resources
     *
     * @return sum of quantities from both strongbox e warehouse
     */
    public int getTotalQuantity(){
        return personalBoard.getTotalQuantityFromStrongbox() + personalBoard.getTotalQuantityFromWarehouse();
    }


    public Map<ResourceType, Integer> getStrongbox() {
        return personalBoard.getStrongbox();
    }

    /**
     * Checks if the strongbox is empty
     *
     * @return true iff strongbox is empty
     */
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

    /**
     * Check if the player has a certain developmentCard
     *
     * @param developmentCard to be checked
     * @return true iff the player has the passed DevelopmentCard
     */
    public boolean hasDevelopmentCard(DevelopmentCard developmentCard){
        ArrayList<DevelopmentSlot> slots = new ArrayList<>(Arrays.asList(personalBoard.getDevelopmentSlots()));
        for(DevelopmentSlot slot : slots){
            if(slot.contains(developmentCard)){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if player has a certain LeaderCard
     *
     * @param leaderCard to be checked
     * @return true iff player has the passed LeaderCard
     */
    public boolean hasLeaderCard(LeaderCard leaderCard){
        return nonActiveLeaderCards.stream().anyMatch(nonActiveLeaderCard -> nonActiveLeaderCard.equals(leaderCard)) ||
                activeLeaderCards.stream().anyMatch(activeLeaderCard -> activeLeaderCard.equals(leaderCard));
    }

    /**
     * Check if player has activated a certain LeaderCard
     *
     * @param leaderCard to be checked
     * @return true iff player has the passed LeaderCard as activated
     */
    public boolean hasActiveLeaderCard(LeaderCard leaderCard){
        return activeLeaderCards.stream().anyMatch(activeLeaderCard -> activeLeaderCard.equals(leaderCard));
    }

    /**
     * Creates the TurnTakerScore about this
     *
     * @return computed TurnTakerScore
     */
    public TurnTakerScore computeScore() {
        return new TurnTakerScore(personalVictoryPoints, personalBoard.getResourceTotal());
    }
}
