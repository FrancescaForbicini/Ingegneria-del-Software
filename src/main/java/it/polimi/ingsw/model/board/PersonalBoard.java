package it.polimi.ingsw.model.board;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
        // TODO setup basicProd with SETTINGS
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

    public Collection<TradingRule> getAdditionalRules() {
        return additionalRules;
    }

    public TradingRule getBasicProduction() { return basicProduction; }

    /**
     * Gets a type of resource from the strongbox
     * @param type the type of resource to get from the strongbox
     * @return the quantity of the resource in the strongbox
     */
    public int getResourceAmountFromStrongbox(ResourceType type) {
        return strongbox.get(type);
    }

    /**
     * Gets a type of resource from the warehouse
     * @param type the type of resource to get from the warehouse
     * @return the quantity of the resource in the warehouse
     */
    public int getResourceAmountFromWarehouse(ResourceType type) {
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
     * @param resourceType
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

    public void addStartingResourcesToWarehouse(ArrayList<ResourceType> pickedResources) {
        for (int i = 0; i < pickedResources.size(); i++) {
            addResourceToWarehouse(pickedResources.get(i), 1, i+2);
        }
    }


    /**
     * Remove resource from a depot and check if it is possible or not
     * @param type the type of resource that a player wants to remove
     * @param quantity the quantity of resource that a player wants to remove
     * @throws NotEnoughResourcesException in case of there is not enough quantity of resource to remove
     */
    public boolean removeResourceFromWarehouse(ResourceType type, int quantity, int depotId){
        return warehouse.removeResource(quantity, depotId);

    }

    /**
     * Checks if the warehouse if full
     * @return true if the warehouse is full, false if not
     */
    public boolean isWarehouseFull() {
        return warehouse.getWarehouseDepots().stream().
                allMatch(warehouseDepot -> warehouseDepot.getLevel()==warehouseDepot.getQuantity());
    }

    public boolean isWarehouseEmpty() {
        return warehouse.getWarehouseDepots().stream().allMatch(WarehouseDepot::isEmpty);
    }


    /**
     * Gets the level of the development card
     * @return the level of the development card
     */
    public Set<Integer> getValidDevelopmentCardLevels() {
        return Arrays.asList(developmentSlots).stream()
                .map(DevelopmentSlot::getNextLevel)
                .collect(Collectors.toSet());
    }

    /**
     * Adds development card to the development slot
     * @param card the development card to add to the development slot
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
    public int getResourceAmount(ResourceType resourceType) {
        return getResourceAmountFromStrongbox(resourceType) + getResourceAmountFromWarehouse(resourceType);
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

    public int getDevelopmentQuantity(DevelopmentColor developmentColor, int level){
        return (int) Arrays.stream(developmentSlots)
                .filter(developmentSlot -> developmentSlot.getDevelopmentQuantity(developmentColor,level)>=1)
                .count();
    }
}
