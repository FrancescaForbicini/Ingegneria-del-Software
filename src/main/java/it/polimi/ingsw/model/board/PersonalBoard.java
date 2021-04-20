package it.polimi.ingsw.model.board;

import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PersonalBoard {
    private Warehouse warehouse;
    private Map<ResourceType, Integer> strongbox;
    private DevelopmentSlot developmentSlots[];
    private Collection<TradingRule> additionalRules;
    private TradingRule basicProduction;


    public PersonalBoard() {
        warehouse = new Warehouse();
        strongbox = new HashMap<>();
        strongbox.put(ResourceType.Coins, 0);
        strongbox.put(ResourceType.Stones, 0);
        strongbox.put(ResourceType.Servants, 0);
        strongbox.put(ResourceType.Shields, 0);
        developmentSlots = new DevelopmentSlot[3];
        for (int i = 0; i < 3; i++) {
            developmentSlots[i] = new DevelopmentSlot();
        }
        additionalRules = new ArrayList<>();
        // TODO setup basicProd with SETTINGS
    }

    public Collection<TradingRule> getAdditionalRules() {
        return additionalRules;
    }

    public void addAdditionalRule(TradingRule rule) {
        additionalRules.add(rule);
    }

    public void addAdditionalDepot(WarehouseDepot warehouseDepot){
        warehouse.addAdditionalDepot(warehouseDepot);
    }


    public void addResourceToStrongbox(ResourceType type, int quantity) {
        strongbox.merge(type, quantity, Integer::sum);
    }

    public void removeResourceFromStrongbox(ResourceType type, int quantity) throws NotEnoughResourcesException {
        if (strongbox.get(type) < quantity)
            throw new NotEnoughResourcesException();
        strongbox.merge(type, -quantity, Integer::sum);
    }

    public int getResourceFromStrongbox(ResourceType type) {
        return strongbox.get(type);
    }

    public int getResourceFromWarehouse(ResourceType type) {
        return warehouse.getQuantity(type);
    }


    public void addResourceToWarehouse(ResourceType type, int quantity) {
        // TODO
    }

    public void removeResourceFromWarehouse(ResourceType type, int quantity) {
        // TODO
    }
    public void isWarehouseFull() {
        // TODO
    }

    public DevelopmentSlot[] getDevelopmentSlots() {
        return developmentSlots;
    }

    public Set<Integer> getValidDevelopmentCardLevels() {
        return Arrays.asList(developmentSlots).stream()
                .map(DevelopmentSlot::getNextLevel)
                .collect(Collectors.toSet());
    }
    public void addDevelopmentCard(DevelopmentCard card) {
        Arrays.asList(developmentSlots).stream()
                .filter(developmentSlot -> developmentSlot.getNextLevel() == card.getLevel())
                .findFirst().ifPresent(developmentSlot -> developmentSlot.addCard(card));
    }

    public List<TradingRule> getActiveTradingRules() {
        Stream<TradingRule> fromDevelopment = Arrays.asList(developmentSlots).stream()
                .map(DevelopmentSlot::showCardOnTop)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(DevelopmentCard::getTradingRule);

        return Stream.concat(Stream.of(basicProduction), Stream.concat(fromDevelopment, additionalRules.stream()))
                .collect(Collectors.toList());
    }

    public TradingRule getBasicProduction() {
        return basicProduction;
    }

    public int getResourceAmount(ResourceType resourceType) {
        return getResourceFromStrongbox(resourceType) + getResourceFromWarehouse(resourceType);
    }

    public int getMaxDevelopmentLevel(DevelopmentColor developmentColor) {
        return Arrays.stream(developmentSlots)
                .mapToInt(developmentSlot -> developmentSlot.getMaxDevelopmentLevel(developmentColor))
                .max().getAsInt();
    }
    public int getDevelopmentQuantity(DevelopmentColor developmentColor) {
        return Arrays.stream(developmentSlots)
                .mapToInt(developmentSlot -> developmentSlot.getDevelopmentQuantity(developmentColor))
                .sum();
    }
    public int getDevelopmentQuantity(DevelopmentColor developmentColor, int level){
        return Arrays.stream(developmentSlots)
                .mapToInt(developmentSlot -> developmentSlot.getDevelopmentQuantity(developmentColor,level))
                .sum();
    }
}
