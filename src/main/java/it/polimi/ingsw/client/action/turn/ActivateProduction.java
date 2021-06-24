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

/**
 * Activates production of the leader card or development cards
 */
public class ActivateProduction extends TurnAction {
    private Player playerClone;
    private final Player player;
    private final ResourcesChosen resourcesChosen;
    private Map<ResourceType,Integer> totalInput;
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

    /**
     * Checks if the player can activate a production
     * @return true iff the are production that can be activated
     */
    @Override
    public boolean isDoable() {
        updateAvailableProductions();
        return productionsAvailable.size() > 0;
    }

    /**
     * Asks to the player to choose the production to activate
     */
    @Override
    public void doAction() {
        TradingRule tradingRuleChosen;
        int cardIndex;
        boolean oneUsed = false;
        playerClone = AbleToRemoveResources.clone(player);
        updateAvailableProductions();
        boolean wantsToContinue = false;

        do{
            if(productionsAvailable.size()==1 && !oneUsed){
                //only basic production can be activated
                cardIndex = 0;
                view.showMessage("You will activate this production: \n" + productionsAvailable.get(cardIndex));
            } else {
                cardIndex = view.chooseAdditionalOrDevelopmentProduction(productionsAvailable,oneUsed);
            }

            productionsUsed.add(productionsAvailable.get(cardIndex));

            oneUsed = true;

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

            updateAvailableProductions();

            if(productionsAvailable.size()>0) {
                //asks to the player if he wants to choose another production
                wantsToContinue = view.userWantToDoIt();
            }
            else {
                wantsToContinue = false;
            }
        }while(wantsToContinue);

        clientConnector.sendMessage(new ActivateProductionDTO(developmentCardsChosen,additionalTradingRulesChosen,resourcesChosen.getResourcesTakenFromWarehouse(),resourcesChosen.getResourcesTakenFromStrongbox(),inputAnyChosen,outputAnyChosen));
    }

    /**
     * Gets the trading rule of the production activated from the player
     * @param cardIndex the index of the production activated
     * @return the trading rule of the production
     */
    private TradingRule getTradingRuleFromCard(int cardIndex) {
        if (productionsAvailable.get(cardIndex).getClass().equals(DevelopmentCard.class)) {
            DevelopmentCard developmentCard = (DevelopmentCard) productionsAvailable.get(cardIndex);
            developmentCardsChosen.add(developmentCard);
            return developmentCard.getTradingRule();
        }
        additionalTradingRulesChosen.add((AdditionalTradingRule) productionsAvailable.get(cardIndex));
        return ((AdditionalTradingRule) productionsAvailable.get(cardIndex)).getAdditionalTradingRule();
    }

    /**
     * Updates the productions that can be still activate
     */
    private void updateAvailableProductions(){
        productionsAvailable = new ArrayList<>();
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

    /**
     * Chooses which resources use to activate the production, if it is possible
     * @param amountToChoose the amount of resources to choose
     */
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
            view.showMessage("You have to choose " + amountToChoose + " resources ");
            availableResourceTypes = getPossibleResourceTypes(resourcesFromWarehouse, resourcesFromStrongbox);
            do{
                view.showMessage("Choose one of this resource: ");
                chosenResource = availableResourceTypes.get(view.chooseResource(availableResourceTypes));
            }while (!resourcesFromWarehouse.containsKey(chosenResource) && resourcesFromStrongbox.get(chosenResource) == 0 );
            inputAnyChosen.add(chosenResource);
            //update resources
            if (resourcesFromWarehouse.containsKey(chosenResource))
                resourcesFromWarehouse.replace(chosenResource,resourcesFromWarehouse.get(chosenResource),resourcesFromWarehouse.get(chosenResource) - 1);
            else
                resourcesFromStrongbox.replace(chosenResource,resourcesFromStrongbox.get(chosenResource),resourcesFromStrongbox.get(chosenResource) - 1);
            amountToChoose--;
        }
    }

    /**
     * Possible resources that can be taken
     * @param resourcesFromWarehouse resources available from warehouse
     * @param resourcesFromStrongbox resources available from strongbox
     * @return the resources available
     */
    private ArrayList<ResourceType> getPossibleResourceTypes(Map<ResourceType,Integer> resourcesFromWarehouse, Map<ResourceType,Integer> resourcesFromStrongbox){
        ArrayList<ResourceType> possibleResourceTypes = ResourceType.getAllValidResources();
        possibleResourceTypes.removeIf(resourceType -> !resourcesFromWarehouse.containsKey(resourceType) || resourcesFromWarehouse.get(resourceType) == 0 && resourcesFromStrongbox.get(resourceType) == 0);
        return possibleResourceTypes;
    }

    /**
     * Chooses the resources to put in the strongbox because of an activation of a production
     * @param amountToChoose the amount of resources to choose
     */
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
     * Collects all the resources of the trading rules
     *
     * @param givenQuantity the quantity of the resources
     * @param chosenAny the generic resources chosen
     * @return the total amount of the resources
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
