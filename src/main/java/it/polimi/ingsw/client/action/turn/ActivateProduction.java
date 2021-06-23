package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.AdditionalTradingRule;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.*;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ActivateProduction extends TurnAction {
    private Player playerClone;
    private final Player player;
    private final ResourcesChosen resourcesChosen;
    private Map<ResourceType,Integer> totalInput;
    private Map<ResourceType,Integer> totalOutput;
    private final ArrayList<ResourceType> inputAnyChosen;
    private final ArrayList<ResourceType> outputAnyChosen;
    private final DevelopmentCard basicProduction;
    private final ArrayList<DevelopmentCard> developmentCardsChosen;
    private final ArrayList<AdditionalTradingRule> additionalTradingRulesChosen;
    private final ArrayList<Eligible> productionsUsed;
    private ArrayList<Eligible> productionsAvailable;
    public ActivateProduction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        player = clientGameObserverProducer.getCurrentPlayer();
        playerClone = new Player(player.getUsername());
        totalInput = new HashMap<>();
        totalOutput = new HashMap<>();
        inputAnyChosen = new ArrayList<>();
        outputAnyChosen = new ArrayList<>();
        developmentCardsChosen = new ArrayList<>();
        additionalTradingRulesChosen = new ArrayList<>();
        productionsAvailable = new ArrayList<>();
        productionsUsed = new ArrayList<>();
        resourcesChosen = new ResourcesChosen(new HashMap<>(),new HashMap<>());
        Collection<Requirement> requirementsBasicProduction = new ArrayList<>();
        requirementsBasicProduction.add(new RequirementResource(2,ResourceType.Any));
        basicProduction = new DevelopmentCard(requirementsBasicProduction, DevelopmentColor.Any,0,0,player.getPersonalBoard().getBasicProduction());
    }

    @Override
    public boolean isDoable() {
        return !player.getWarehouse().isEmpty() || !player.isStrongboxEmpty();
    }

    @Override
    public void doAction() {
        TradingRule tradingRuleChosen = null;
        int cardIndex;
        boolean oneUsed = false;
        playerClone = Remove.clone(player);
        checkDevelopmentCardsAvailable();

        while (productionsAvailable.size() != 0) {
            if (productionsAvailable.size() == 1) {
                cardIndex = 0;
                view.showMessage("You will activate this production: \n" + productionsAvailable.get(cardIndex));
            }
            else {
                cardIndex = view.chooseAdditionalOrDevelopmentProduction(productionsAvailable,oneUsed);
                if (cardIndex == -1)
                    break;
            }
            tradingRuleChosen = checkType(cardIndex);
            oneUsed = true;
            if (tradingRuleChosen.getInput().containsKey(ResourceType.Any))
                chooseAnyResourceInput(tradingRuleChosen.getInput().get(ResourceType.Any));
            totalInput = getTotalResourceQuantity(tradingRuleChosen.getInput(), null);
            for (ResourceType resourceType : totalInput.keySet()) {
                Remove.inputFrom(view,resourcesChosen,resourceType,playerClone,totalInput.get(resourceType));
            }
            if (tradingRuleChosen.getOutput().containsKey(ResourceType.Any))
                chooseAnyResourcesOutput(tradingRuleChosen.getOutput().get(ResourceType.Any));
            totalOutput = getTotalResourceQuantity(tradingRuleChosen.getOutput(), outputAnyChosen);
            insertOutput();
            checkDevelopmentCardsAvailable();
        }
        clientConnector.sendMessage(new ActivateProductionDTO(developmentCardsChosen,additionalTradingRulesChosen,resourcesChosen.getResourcesTakenFromWarehouse(),resourcesChosen.getResourcesTakenFromStrongbox(),inputAnyChosen,outputAnyChosen));
    }

    private TradingRule checkType(int cardIndex) {
        productionsUsed.add(productionsAvailable.get(cardIndex));
        if (productionsAvailable.get(cardIndex).getClass().equals(DevelopmentCard.class)) {
            DevelopmentCard developmentCard = (DevelopmentCard) productionsAvailable.get(cardIndex);
            developmentCardsChosen.add(developmentCard);
            return developmentCard.getTradingRule();
        }
        additionalTradingRulesChosen.add((AdditionalTradingRule) productionsAvailable.get(cardIndex));
        return ((AdditionalTradingRule) productionsAvailable.get(cardIndex)).getAdditionalTradingRule();
    }

    private void checkDevelopmentCardsAvailable(){
        AdditionalTradingRule additionalTradingRule;
        productionsAvailable = new ArrayList<>();
        for (DevelopmentSlot slot: player.getDevelopmentSlots()){
            //add productions from trading rules
            if (slot.showCardOnTop().isPresent() && slot.showCardOnTop().get().getTradingRule().isUsable(player))
                if (productionsUsed.stream().noneMatch(developmentCard -> developmentCard.equals(slot.showCardOnTop().get()))) {
                    productionsAvailable.add(slot.showCardOnTop().get());
                }
        }

        if (productionsUsed.stream().noneMatch(developmentCard -> developmentCard.equals(basicProduction))) {
            //add basic production
            productionsAvailable.add(basicProduction);
        }

        for (LeaderCard leaderCard: player.getActiveLeaderCards())
            if (leaderCard.getClass().equals(AdditionalTradingRule.class)){
                //add additional trading rule production
                additionalTradingRule = (AdditionalTradingRule) leaderCard;
                if (additionalTradingRule.getAdditionalTradingRule().isUsable(player) && productionsUsed.stream().noneMatch(card -> card.equals(leaderCard))) {
                    productionsAvailable.add(additionalTradingRule);
                }
            }
    }

    private void chooseAnyResourceInput(int amountToChoose){
        Map<ResourceType,Integer> resourcesFromWarehouse = new HashMap<>();
        Map<ResourceType,Integer> resourcesFromStrongbox = new HashMap<>();
        ArrayList<ResourceType> resourceTypes = ResourceType.getAllValidResources();
        for (WarehouseDepot warehouseDepot: playerClone.getWarehouse().getAllDepots())
            resourcesFromWarehouse.merge(warehouseDepot.getResourceType(),warehouseDepot.getQuantity(),Integer::sum);
        for (ResourceType resourceType: playerClone.getStrongbox().keySet())
            resourcesFromStrongbox.put(resourceType,playerClone.getStrongbox().get(resourceType));
        ResourceType resourceType;
        while (amountToChoose != 0){
            view.showMessage("You have to choose "+ amountToChoose + " resources ");
            view.showMessage("You can choose from warehouse: " + resourcesFromWarehouse);
            view.showMessage("You can choose from strongbox: " + player.getStrongbox());
            resourceType = resourceTypes.get(view.chooseResource(resourceTypes));
            while (!resourcesFromWarehouse.containsKey(resourceType) && resourcesFromStrongbox.get(resourceType) == 0 ){
                view.showMessage("Error! Choose a resource that is present in the warehouse or in the strongbox");
                resourceType = resourceTypes.get(view.chooseResource(resourceTypes));
            }
            inputAnyChosen.add(resourceType);
            Remove.inputFrom(view,resourcesChosen,resourceType,playerClone,1);
            if (resourcesChosen.getResourcesTakenFromWarehouse().containsKey(resourceType))
                resourcesFromWarehouse.replace(resourceType,resourcesFromWarehouse.get(resourceType),resourcesFromWarehouse.get(resourceType) - 1);
            else
                resourcesFromStrongbox.replace(resourceType,resourcesFromStrongbox.get(resourceType),resourcesFromStrongbox.get(resourceType) - 1);
            amountToChoose--;
        }
    }

    private void insertOutput(){
        for (ResourceType resourceType: totalOutput.keySet()) {
            playerClone.getStrongbox().merge(resourceType,totalOutput.get(resourceType),Integer::sum);
        }
    }


    private void chooseAnyResourcesOutput(int amount){
        ArrayList<ResourceType> resourceTypes = ResourceType.getAllValidResources();
        ResourceType resourceType;
        view.showMessage("You have to choose " + amount + " resources to put in the strongbox");
        while (amount != 0){
            resourceType = resourceTypes.get(view.chooseResource(resourceTypes));
            outputAnyChosen.add(resourceType);
            amount --;
        }
    }
    /**
     * Collects all the input of the trading rules
     */
    private Map<ResourceType,Integer> getTotalResourceQuantity(Map<ResourceType,Integer> givenQuantity,ArrayList<ResourceType> chosenAny) {
        Map<ResourceType,Integer> totalResourceQuantity = new HashMap<>();
        for (ResourceType resourceType: givenQuantity.keySet()) {
            if (!resourceType.equals(ResourceType.Any)) {
                totalResourceQuantity.merge(resourceType, givenQuantity.get(resourceType), Integer::sum);
            }
            if (chosenAny != null && chosenAny.contains(resourceType))
                totalResourceQuantity.merge(resourceType, (int) chosenAny.stream().filter(resource -> resource.equals(resourceType)).count(), Integer::sum);
        }
        if (chosenAny != null) {
            for (ResourceType resourceType : chosenAny) {
                if (!totalResourceQuantity.containsKey(resourceType)) {
                    totalResourceQuantity.put(resourceType, (int) chosenAny.stream().filter(resource -> resource.equals(resourceType)).count());
                }
            }
        }
        return totalResourceQuantity;
    }
}
