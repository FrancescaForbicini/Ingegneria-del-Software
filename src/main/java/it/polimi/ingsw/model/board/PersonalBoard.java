package it.polimi.ingsw.model.board;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.warehouse.Warehouse;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Personal board of each player
 */
public class PersonalBoard {
    private final Warehouse warehouse;
    private final Map<ResourceType, Integer> strongbox;
    private final DevelopmentSlot[] developmentSlots;
    private final Collection<TradingRule> additionalRules;
    private final TradingRule basicProduction;


    public PersonalBoard() {
        warehouse = new Warehouse();
        basicProduction = Settings.getInstance().getBasicProduction();
        strongbox = new HashMap<>();
        strongbox.put(ResourceType.Coins, 0);
        strongbox.put(ResourceType.Stones, 0);
        strongbox.put(ResourceType.Servants, 0);
        strongbox.put(ResourceType.Shields, 0);
        developmentSlots = new DevelopmentSlot[3];
        for (int i = 0; i < 3; i++) {
            developmentSlots[i] = new DevelopmentSlot(i);
        }
        additionalRules = new ArrayList<>();
    }


    public Map<ResourceType, Integer> getStrongbox() {
        return strongbox;
    }

    public DevelopmentSlot[] getDevelopmentSlots() {
        return developmentSlots;
    }

    public Warehouse getWarehouse(){
        return warehouse;
    }

    public int getTotalQuantityFromWarehouse() {
        return warehouse.getTotalQuantity();
    }

    /**
     * Gets the total amount of all the resources contained in the strongbox and in the warehouse
     *
     * @return amount of the resources
     */
    public int getTotalQuantityFromStrongbox(){
        int totalAmount = 0;
        for (ResourceType resourceType: ResourceType.getAllValidResources())
            totalAmount += strongbox.get(resourceType);
        return totalAmount;
    }


    public Collection<TradingRule> getAdditionalRules() {
        return additionalRules;
    }

    public TradingRule getBasicProduction() { return basicProduction; }

    /**
     * Gets a type of resource from the strongbox
     * @param type the type of resource to get from the strongbox
     * @return the quantity of the resource in the strongbox
     */
    public int getResourceQuantityFromStrongbox(ResourceType type) {
        return strongbox.get(type);
    }

    /**
     * Gets a type of resource from the warehouse
     * @param type the type of resource to get from the warehouse
     * @return the quantity of the resource in the warehouse
     */
    public int getResourceQuantityFromWarehouse(ResourceType type) {
        return warehouse.getQuantity(type);
    }

    /**
     * Adds a new trading rule
     * @param rule the trading rule to add to the player
     */
    public void addAdditionalRule(TradingRule rule) {
        additionalRules.add(rule);
    }

    /**
     *  Adds a new depot
     * @param resourceType type of resource to add in the depot
     * @param level
     */
    public void addAdditionalDepot(ResourceType resourceType, int level){
        warehouse.addAdditionalDepot(resourceType, level);
    }

    /**
     * Adds resource to the strongbox
     * @param type the type of resource to add
     * @param quantity the quantity of resource to add
     */
    public void addResourceToStrongbox(ResourceType type, int quantity) {
        strongbox.merge(type, quantity, Integer::sum);
    }

    /**
     * Removes resource from the strongbox
     * @param type the type of resource to remove from the strongbox
     * @param quantity the quantity of resource to remove
     * @throws NotEnoughResourcesException exception to catch if the are not enough resource to remove from the strongbox
     */
    public void removeResourceFromStrongbox(ResourceType type, int quantity) throws NotEnoughResourcesException {
        if (strongbox.get(type) < quantity)
            throw new NotEnoughResourcesException();
        strongbox.merge(type, -quantity, Integer::sum);
    }

    /**
     * Add resource to the Warehouse and check if it is possible or not
     * @param type the type of resource that a player wants to add
     * @param quantity the quantity of the resource that a player wants to add
     */
    public boolean addResourceToWarehouse(ResourceType type, int quantity, int depotId) {
        return warehouse.addResource(type,quantity, depotId);
    }

    /**
     * Adds starting resources to the warehouse
     * @param pickedResources resources chosen from the player at the beginning of the game
     */
    public void addStartingResourcesToWarehouse(ArrayList<ResourceType> pickedResources) {
        for (int i = 0; i < pickedResources.size(); i++) {
            addResourceToWarehouse(pickedResources.get(i), 1, i+2);
        }
    }


    /**
     * Removes resource from a depot and check if it is possible or not
     * @param type the type of resource that a player wants to remove
     * @param quantity the quantity of resource that a player wants to remove
     */
    public boolean removeResourceFromWarehouse(ResourceType type, int quantity, int depotId){
        return warehouse.removeResource(quantity, depotId);

    }

    /**
     * Checks if the warehouse if full
     * @return true if the warehouse is full, false if not
     */
    public boolean isWarehouseFull() {
        return warehouse.isFull();
    }


    /**
     * Checks if a card can be added to a slot
     *
     * @param card development card to add to the slot
     * @param slotID slot where the card has to be added
     * @return true iff the card can be added to the slot
     */
    public boolean canAddCardToSlot(DevelopmentCard card, int slotID){
        return developmentSlots[slotID].canAddCard(card);
    }

    /**
     * Adds development card to the development slot
     *
     * @param card development card to add to the slot
     * @param slotID slot where the card has to be added
     * @return true iff the card can be added to the slot
     */
    public boolean addDevelopmentCard(DevelopmentCard card, int slotID) {
        return developmentSlots[slotID].addCard(card);
    }

    /**
     * Gets the trading rule activated for a player
     * @return the trading rule activated for a player
     */
    public List<TradingRule> getActiveTradingRules() {
        Stream<TradingRule> fromDevelopment = Arrays.asList(developmentSlots).stream()
                .map(DevelopmentSlot::showCardOnTop)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(DevelopmentCard::getTradingRule);

        return Stream.concat(Stream.of(basicProduction), Stream.concat(fromDevelopment, additionalRules.stream()))
                .collect(Collectors.toList());
    }

    /**
     * Gets the quantity of a particular resource
     * @param resourceType the type of the resource that the player wants to know the quantity
     * @return the amount of the resource type
     */
    public int getResourceQuantity(ResourceType resourceType) {
        return getResourceQuantityFromStrongbox(resourceType) + getResourceQuantityFromWarehouse(resourceType);
    }

    /**
     * Gets the max level of a development card based on the color
     * @param developmentColor the color of the development card to know the max level
     * @return the max level
     */
    public int getMaxDevelopmentLevel(DevelopmentColor developmentColor) {
        return Arrays.stream(developmentSlots)
                .mapToInt(developmentSlot -> developmentSlot.getMaxDevelopmentLevel(developmentColor))
                .max().getAsInt();
    }

    /**
     * Gets the quantity of a development card based on the color
     * @param developmentColor the color of the development card to know the max level
     * @return the quantity of the development card based on the color
     */
    public int getDevelopmentQuantity(DevelopmentColor developmentColor) {
        return Arrays.stream(developmentSlots)
                .mapToInt(developmentSlot -> developmentSlot.getDevelopmentQuantity(developmentColor))
                .sum();
    }

    /**
     * Gets the quantity of development card with a specific color and level
     *
     * @param developmentColor color of the development card
     * @param level level of the development card
     * @return amount of development card with a specific color and the level
     */
    public int getDevelopmentQuantity(DevelopmentColor developmentColor, int level){
        return (int) Arrays.stream(developmentSlots)
                .filter(developmentSlot -> developmentSlot.getDevelopmentQuantity(developmentColor,level)>=1)
                .count();
    }

    /**
     * Gets the quantity of the development cards in the slots
     *
     * @return amount of development cards
     */
    public int getDevelopmentCardNumber() {
        return Arrays.stream(developmentSlots).mapToInt(DevelopmentSlot::size).sum();
    }

    /**
     * Gets the total amount of all resources in the strongbox and in the warehouse
     *
     * @return amount of all resources
     */
    public int getResourceTotal() {
        return warehouse.getResourceTotal() + strongbox.values().stream().reduce(0, Integer::sum);
    }
}
