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
        TradingRule tradingRuleChosen;
        int cardIndex;
        boolean oneUsed = false;
        playerClone = AbleToRemoveResources.clone(player);
        updateAvailableProductions();

        //Daniel's version
        boolean wantsToContinue = false;
        do{
            if(productionsAvailable.size()==1){
                //only one production can be activated
                cardIndex = 0;
                view.showMessage("You will activate this production: \n" + productionsAvailable.get(cardIndex));
            } else {
                cardIndex = view.chooseAdditionalOrDevelopmentProduction(productionsAvailable,oneUsed);
            }
            productionsUsed.add(productionsAvailable.get(cardIndex));
            tradingRuleChosen = getTradingRuleFromCard(cardIndex);
            if (tradingRuleChosen.getInput().containsKey(ResourceType.Any)) {
                //there are input Any to assign
                chooseAnyResourceInput(tradingRuleChosen.getInput().get(ResourceType.Any));
            }
            totalInput = getTotalResourceQuantity(tradingRuleChosen.getInput(), inputAnyChosen);
            for (ResourceType resourceType : totalInput.keySet()) {
                AbleToRemoveResources.removeResourceFromPlayer(view,resourcesChosen,resourceType,playerClone,totalInput.get(resourceType));
            }
            if (tradingRuleChosen.getOutput().containsKey(ResourceType.Any)) {
                chooseAnyResourcesOutput(tradingRuleChosen.getOutput().get(ResourceType.Any));
            }
            totalOutput = getTotalResourceQuantity(tradingRuleChosen.getOutput(), outputAnyChosen);
//TODO something to collect all the output to add it at the end is needed
            updateAvailableProductions();
            if(productionsAvailable.size()>0) {
                wantsToContinue = view.userWantToDoIt();
            } else {
                wantsToContinue = false;
            }
        }while(wantsToContinue);




        //Fra's version
        /*
        while (productionsAvailable.size() != 0) {
            if (productionsAvailable.size() == 1) {
                //only one production can be activated
                cardIndex = 0;
                view.showMessage("You will activate this production: \n" + productionsAvailable.get(cardIndex));
            }
            else {
                //player needs to choose one production to activate
                cardIndex = view.chooseAdditionalOrDevelopmentProduction(productionsAvailable,oneUsed);
                if (cardIndex == -1)
                    break;
            }
            productionsUsed.add(productionsAvailable.get(cardIndex));
            tradingRuleChosen = getTradingRuleFromCard(cardIndex);
            oneUsed = true;
            if (tradingRuleChosen.getInput().containsKey(ResourceType.Any))
                //there are input Any to assign
                chooseAnyResourceInput(tradingRuleChosen.getInput().get(ResourceType.Any));
            totalInput = getTotalResourceQuantity(tradingRuleChosen.getInput(), null);//TODO shouldn't be the result of chooseAny... instead of null?
            for (ResourceType resourceType : totalInput.keySet()) {
                AbleToRemoveResources.removeResourceFromPlayer(view,resourcesChosen,resourceType,playerClone,totalInput.get(resourceType));
                //TODO the converted any are already removed in chooseAny...
            }
            if (tradingRuleChosen.getOutput().containsKey(ResourceType.Any))
                chooseAnyResourcesOutput(tradingRuleChosen.getOutput().get(ResourceType.Any));
            totalOutput = getTotalResourceQuantity(tradingRuleChosen.getOutput(), outputAnyChosen);
            insertOutput();//TODO it needs to be done at the end of all choices, otherwise a player can use the resources just obtained to activate another production
            updateAvailableProductions();
        }
        */
        clientConnector.sendMessage(new ActivateProductionDTO(developmentCardsChosen,additionalTradingRulesChosen,resourcesChosen.getResourcesTakenFromWarehouse(),resourcesChosen.getResourcesTakenFromStrongbox(),inputAnyChosen,outputAnyChosen));
    }

    private TradingRule getTradingRuleFromCard(int cardIndex) {
        if (productionsAvailable.get(cardIndex).getClass().equals(DevelopmentCard.class)) {
            DevelopmentCard developmentCard = (DevelopmentCard) productionsAvailable.get(cardIndex);
            developmentCardsChosen.add(developmentCard);
            return developmentCard.getTradingRule();
        }
        additionalTradingRulesChosen.add((AdditionalTradingRule) productionsAvailable.get(cardIndex));
        return ((AdditionalTradingRule) productionsAvailable.get(cardIndex)).getAdditionalTradingRule();
    }

    private void updateAvailableProductions(){
        AdditionalTradingRule additionalTradingRule;
        for (DevelopmentSlot slot: player.getDevelopmentSlots()){
            //add productions from development cards
            if (slot.showCardOnTop().isPresent() && slot.showCardOnTop().get().getTradingRule().isUsable(player))//TODO merge ifs
                //there's a usable card in this slot
                if (productionsUsed.stream().noneMatch(developmentCard -> developmentCard.equals(slot.showCardOnTop().get()))) {
                    //this card is unused
                    productionsAvailable.add(slot.showCardOnTop().get());
                }
        }

        if (productionsUsed.stream().noneMatch(developmentCard -> developmentCard.equals(basicProduction))) {
            //basic production is unused
            productionsAvailable.add(basicProduction);
        }

        for (LeaderCard leaderCard: player.getActiveLeaderCards())
            //add productions from leader cards
            if (leaderCard.getClass().equals(AdditionalTradingRule.class)){
                //active leader card is AdditionalTradingRule
                additionalTradingRule = (AdditionalTradingRule) leaderCard;
                if (additionalTradingRule.getAdditionalTradingRule().isUsable(player) && productionsUsed.stream().noneMatch(card -> card.equals(leaderCard))) {
                    productionsAvailable.add(additionalTradingRule);
                }
            }
    }

    private void chooseAnyResourceInput(int amountToChoose){
        Map<ResourceType,Integer> resourcesFromWarehouse = new HashMap<>();
        Map<ResourceType,Integer> resourcesFromStrongbox = new HashMap<>();
        ArrayList<ResourceType> availableResourceTypes = ResourceType.getAllValidResources();
        for (WarehouseDepot warehouseDepot: playerClone.getWarehouse().getAllDepots())
            resourcesFromWarehouse.merge(warehouseDepot.getResourceType(),warehouseDepot.getQuantity(),Integer::sum);
        for (ResourceType resourceType: playerClone.getStrongbox().keySet())
            resourcesFromStrongbox.put(resourceType,playerClone.getStrongbox().get(resourceType));
        ResourceType chosenResource;
        while (amountToChoose != 0){
            //there are still some Any to be assigned
            view.showMessage("You have to choose " + amountToChoose + " resources ");//TODO 1 show message could pass all info at once
            /*view.showMessage("You can choose from warehouse: " + resourcesFromWarehouse);
            view.showMessage("You can choose from strongbox: " + player.getStrongbox());
             */
            availableResourceTypes = getPossibleResourceTypes(resourcesFromWarehouse, resourcesFromStrongbox);

            chosenResource = availableResourceTypes.get(view.chooseResource(availableResourceTypes));
            /*
            while (!resourcesFromWarehouse.containsKey(chosenResource) && resourcesFromStrongbox.get(chosenResource) == 0 ){//TODO maybe a filter on resourceTypes array could be better
                //TODO if no filter is applied to resource array a method ad hoc can be created: view.chooseResources();
                //user made an invalid choice (TODO maybe do-while if no filter?)
                view.showMessage("Error! Choose a resource that is present in the warehouse or in the strongbox");
                chosenResource = availableResourceTypes.get(view.chooseResource(availableResourceTypes));
            }
             */
            inputAnyChosen.add(chosenResource);
            //remove 1 chosenResource from playerClone
            //commented in Daniel's version
//            AbleToRemoveResources.removeResourceFromPlayer(view,resourcesChosen,chosenResource,playerClone,1);//TODO conflicts w/ same call outside this method
            //update mappings
            if (resourcesChosen.getResourcesTakenFromWarehouse().containsKey(chosenResource))
                resourcesFromWarehouse.replace(chosenResource,resourcesFromWarehouse.get(chosenResource),resourcesFromWarehouse.get(chosenResource) - 1);
            else
                resourcesFromStrongbox.replace(chosenResource,resourcesFromStrongbox.get(chosenResource),resourcesFromStrongbox.get(chosenResource) - 1);
            amountToChoose--;
        }
    }

    private ArrayList<ResourceType> getPossibleResourceTypes(Map<ResourceType,Integer> resourcesFromWarehouse, Map<ResourceType,Integer> resourcesFromStrongbox){
        ArrayList<ResourceType> possibleResourceTypes = ResourceType.getAllValidResources();
        possibleResourceTypes.removeIf(resourceType -> !resourcesFromWarehouse.containsKey(resourceType) ||
                (resourcesFromWarehouse.containsKey(resourceType) && resourcesFromWarehouse.get(resourceType)==0));
        possibleResourceTypes.removeIf(resourceType -> !resourcesFromStrongbox.containsKey(resourceType) ||
                (resourcesFromStrongbox.containsKey(resourceType) && resourcesFromStrongbox.get(resourceType)==0));
        return possibleResourceTypes;
    }

    private void insertOutput(){
        for (ResourceType resourceType: totalOutput.keySet()) {
            playerClone.getStrongbox().merge(resourceType,totalOutput.get(resourceType),Integer::sum);
        }
    }


    private void chooseAnyResourcesOutput(int amountToChoose){
       ResourceType chosenResourceType;
        view.showMessage("You have to choose " + amountToChoose + " resources to put in the strongbox");
        while (amountToChoose != 0){
            chosenResourceType = view.chooseResource();
            outputAnyChosen.add(chosenResourceType);
            amountToChoose --;
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
