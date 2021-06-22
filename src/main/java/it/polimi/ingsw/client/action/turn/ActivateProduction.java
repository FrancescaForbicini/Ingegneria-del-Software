package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.AdditionalTradingRule;
import it.polimi.ingsw.model.cards.DevelopmentCard;
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
    private final ArrayList<DevelopmentCard> developmentCardsUsed;
    private ArrayList<DevelopmentCard> developmentCardsAvailable;
    private ArrayList<AdditionalTradingRule> additionalTradingRulesAvailable;
    private final ArrayList<AdditionalTradingRule> additionalTradingRulesUsed;
    public ActivateProduction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        player = clientGameObserverProducer.getCurrentPlayer();
        playerClone = new Player(player.getUsername());
        totalInput = new HashMap<>();
        totalOutput = new HashMap<>();
        inputAnyChosen = new ArrayList<>();
        outputAnyChosen = new ArrayList<>();
        developmentCardsUsed = new ArrayList<>();
        developmentCardsAvailable = new ArrayList<>();
        additionalTradingRulesAvailable = new ArrayList<>();
        additionalTradingRulesUsed = new ArrayList<>();
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
        DevelopmentCard developmentCardChosen = null;
        AdditionalTradingRule additionalTradingRuleChosen = null;
        TradingRule tradingRuleChosen = null;
        int cardIndex;
        playerClone = Remove.clone(player);
        checkDevelopmentCardsAvailable();
        boolean onlyOneDevelopment = false;
        boolean onlyOneAdditional = false;
        boolean onlyBasic = false;
        boolean oneUsed = false;
        if (developmentCardsAvailable.size()==1 && additionalTradingRulesAvailable.size() == 0)
            onlyOneDevelopment = true;

        if (developmentCardsAvailable.size() == 0 && additionalTradingRulesAvailable.size() == 0){
            onlyBasic = true;
        }
        if (developmentCardsAvailable.size()==0 && additionalTradingRulesAvailable.size() == 1)
            onlyOneAdditional = true;
        checkDevelopmentCardsAvailable();
        while (developmentCardsAvailable.size() != 0 || additionalTradingRulesAvailable.size() != 0) {
            if (onlyOneDevelopment){
                view.showMessage("You will activate this production:\n " + developmentCardsAvailable.get(0).getTradingRule().toString());
                developmentCardChosen = developmentCardsAvailable.get(0);
                tradingRuleChosen = developmentCardChosen.getTradingRule();
                developmentCardsUsed.add(developmentCardChosen);
            }
            else if (onlyBasic){
                view.showMessage("You can only activate the basic production");
                developmentCardChosen = basicProduction;
                tradingRuleChosen = basicProduction.getTradingRule();
                developmentCardsUsed.add(developmentCardChosen);
            }
            else if (onlyOneAdditional){
                view.showMessage("You will activate this production: " + additionalTradingRulesAvailable.get(0).toString());
                additionalTradingRuleChosen = additionalTradingRulesAvailable.get(0);
                tradingRuleChosen = additionalTradingRuleChosen.getAdditionalTradingRule();
                additionalTradingRulesUsed.add(additionalTradingRuleChosen);
            }
            else {
                if (developmentCardsAvailable.size() == 0){
                    view.showMessage("Choose Additional Trading Rules ");
                    cardIndex = view.chooseAdditionalTradingRule(additionalTradingRulesAvailable,oneUsed);
                    if (cardIndex == -1)
                        break;
                    tradingRuleChosen = additionalTradingRulesAvailable.get(cardIndex).getAdditionalTradingRule();
                }
                else
                    if (additionalTradingRulesAvailable.size() == 0){
                        view.showMessage("Choose Development Card Trading Rules ");
                        cardIndex = view.chooseDevelopmentCardProduction(developmentCardsAvailable,oneUsed);
                        if (cardIndex == -1)
                            break;
                        tradingRuleChosen = developmentCardsAvailable.get(cardIndex).getTradingRule();
                    }
                    else {
                        int response = view.chooseAdditionalOrDevelopmentProduction(developmentCardsAvailable, additionalTradingRulesAvailable);
                        if (!oneUsed && response == 0) {
                            view.showMessage("You have to choose at least a production");
                            do {
                                response = view.chooseAdditionalOrDevelopmentProduction(developmentCardsAvailable, additionalTradingRulesAvailable);
                            } while (response == 0);
                        }
                        if (response == 0)
                            break;
                        else if (response == 1) {
                            cardIndex = view.chooseDevelopmentCardProduction(developmentCardsAvailable,oneUsed);
                            developmentCardChosen = developmentCardsAvailable.get(cardIndex);
                            tradingRuleChosen = developmentCardChosen.getTradingRule();
                            developmentCardsUsed.add(developmentCardChosen);
                        } else if (response == 2) {
                            cardIndex = view.chooseAdditionalTradingRule(additionalTradingRulesAvailable,oneUsed);
                            additionalTradingRuleChosen = additionalTradingRulesAvailable.get(cardIndex);
                            tradingRuleChosen = additionalTradingRuleChosen.getAdditionalTradingRule();
                            additionalTradingRulesUsed.add(additionalTradingRuleChosen);
                        }
                        oneUsed = true;
                    }
            }
            checkDevelopmentCardsAvailable();
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
        }

        clientConnector.sendMessage(new ActivateProductionDTO(developmentCardsUsed,additionalTradingRulesUsed,resourcesChosen.getResourcesTakenFromWarehouse(),resourcesChosen.getResourcesTakenFromStrongbox(),inputAnyChosen,outputAnyChosen));
    }


    private void checkDevelopmentCardsAvailable(){
        AdditionalTradingRule additionalTradingRule;
        developmentCardsAvailable = new ArrayList<>();
        additionalTradingRulesAvailable = new ArrayList<>();
        for (DevelopmentSlot slot: player.getDevelopmentSlots()){
            if (slot.showCardOnTop().isPresent() && slot.showCardOnTop().get().getTradingRule().isUsable(player))
                if (developmentCardsUsed.stream().noneMatch(developmentCard -> developmentCard.equals(slot.showCardOnTop().get())))
                    developmentCardsAvailable.add(slot.showCardOnTop().get());
        }
        for (LeaderCard leaderCard: player.getActiveLeaderCards())
            if (leaderCard.getClass().equals(AdditionalTradingRule.class)){
                additionalTradingRule = (AdditionalTradingRule) leaderCard;
                if (additionalTradingRule.getAdditionalTradingRule().isUsable(player) && additionalTradingRulesUsed.stream().noneMatch(card -> card.equals(leaderCard)))
                    additionalTradingRulesAvailable.add(additionalTradingRule);
            }
        if (developmentCardsUsed.stream().noneMatch(developmentCard -> developmentCard.equals(basicProduction)))
            developmentCardsAvailable.add(basicProduction);
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
                if (chosenAny != null && chosenAny.contains(resourceType))
                    totalResourceQuantity.merge(resourceType, (int) chosenAny.stream().filter(resource -> resource.equals(resourceType)).count(), Integer::sum);
            }
        }
        if (chosenAny != null) {
            for (ResourceType resourceType : chosenAny) {
                if (!totalResourceQuantity.containsKey(resourceType)) {
                    totalResourceQuantity.put(resourceType, (int ) chosenAny.stream().filter(resource -> resource.equals(resourceType)).count());
                }
            }
        }
        return totalResourceQuantity;
    }
}
