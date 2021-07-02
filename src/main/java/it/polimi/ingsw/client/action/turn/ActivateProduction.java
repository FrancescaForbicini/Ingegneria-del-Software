package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.Eligible;
import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.cards.AdditionalTradingRule;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
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
    private Player player;
    private final ResourcesChosen resourcesChosen;
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
        playerClone = RequireToRemoveResources.clone(player);
        inputAnyChosen = new ArrayList<>();
        outputAnyChosen = new ArrayList<>();
        developmentCardsChosen = new ArrayList<>();
        additionalTradingRulesChosen = new ArrayList<>();
        productionsAvailable = new ArrayList<>();
        productionsUsed = new ArrayList<>();
        resourcesChosen = new ResourcesChosen(new HashMap<>(),new HashMap<>());
        Collection<Requirement> requirementsBasicProduction = new ArrayList<>();
        requirementsBasicProduction.add(new RequirementResource(0,ResourceType.Any));
        basicProduction = new DevelopmentCard(requirementsBasicProduction, DevelopmentColor.Any,0,0,player.getPersonalBoard().getBasicProduction(),null);
    }

    /**
     * {@inheritDoc}
     *
     * Checks if the player can activate a production
     *
     * @return true iff the are production that can be activated
     */
    @Override
    public boolean isDoable() {
        player = clientGameObserverProducer.getCurrentPlayer();
        playerClone = RequireToRemoveResources.clone(player);
        updateAvailableProductions();
        return productionsAvailable.size() > 0;
    }

    /**
     * {@inheritDoc}
     *
     * Asks to the player to choose the production to activate
     */
    @Override
    public void doAction() {
        TradingRule tradingRuleChosen;
        int chosenProductionIndex;
        updateAvailableProductions();
        boolean chooseAnotherProduction;
        do{
            chosenProductionIndex = chooseProductionToActivateIndex(productionsAvailable);
            productionsUsed.add(productionsAvailable.get(chosenProductionIndex));
            tradingRuleChosen = getTradingRuleFromCard(chosenProductionIndex);
            manageInput(tradingRuleChosen);
            manageOutput(tradingRuleChosen);
            updateAvailableProductions();
            if(productionsAvailable.size()>0)
                chooseAnotherProduction = view.chooseAnotherProduction();
            else
                chooseAnotherProduction = false;
        }while(chooseAnotherProduction);

        clientConnector.sendMessage(new ActivateProductionDTO(developmentCardsChosen,additionalTradingRulesChosen,resourcesChosen.getResourcesTakenFromWarehouse(),resourcesChosen.getResourcesTakenFromStrongbox(),inputAnyChosen,outputAnyChosen));
    }

    /**
     * Let the user make the choice of the production to activate via View
     *
     * @param productionsAvailable list of possible production, among which the player has to choose one
     * @return chosen index representing the production the user wants to activate
     */
    private int chooseProductionToActivateIndex(ArrayList<Eligible> productionsAvailable){
        return view.chooseProductionToActivate(productionsAvailable);
    }

    /**
     * Gets the trading rule of the production activated from the player
     *
     * @param chosenProductionIndex the index of the production activated
     * @return the trading rule of the production
     */
    private TradingRule getTradingRuleFromCard(int chosenProductionIndex) {
        if (productionsAvailable.get(chosenProductionIndex).getClass().equals(DevelopmentCard.class)) {
            //development card is chosen
            DevelopmentCard developmentCard = (DevelopmentCard) productionsAvailable.get(chosenProductionIndex);
            developmentCardsChosen.add(developmentCard);
            return developmentCard.getTradingRule();
        }
        //additional trading rule card is chosen
        additionalTradingRulesChosen.add((AdditionalTradingRule) productionsAvailable.get(chosenProductionIndex));
        return ((AdditionalTradingRule) productionsAvailable.get(chosenProductionIndex)).getAdditionalTradingRule();
    }

    /**
     * Manage all the input of a chosen TradingRule: convert each Resource.Any into a valid ResourceType
     * and remove all needed Resources from the playerClone storages
     *
     * @param chosenTradingRule whose input has to be managed
     */
    private void manageInput(TradingRule chosenTradingRule){
        if (chosenTradingRule.getInput().containsKey(ResourceType.Any)) {
            //there are input Any to assign
            convertInputAnyToResources(chosenTradingRule.getInput().get(ResourceType.Any));
        }
        for (ResourceType resourceToTake : chosenTradingRule.getInput().keySet()) {
            if (!resourceToTake.equals(ResourceType.Any)) {
                RequireToRemoveResources.removeResourceFromPlayerClone(view, resourcesChosen, resourceToTake, playerClone, chosenTradingRule.getInput().get(resourceToTake));
            }
        }
        //totalInput = getTotalQuantitiesOfResources(chosenTradingRule.getInput(), inputAnyChosen);
    }

    /**
     * Manage all the output from a chosen TradingRule: convert each Resource.Any into a valid ResourceType
     *
     * @param chosenTradingRule whose output has to be managed
     */
    private void manageOutput(TradingRule chosenTradingRule){
        if (chosenTradingRule.getOutput().containsKey(ResourceType.Any)) {
            convertOutputAnyToResources(chosenTradingRule.getOutput().get(ResourceType.Any));
        }
    }

    /**
     * Updates the productions that can be still activate
     */
    private void updateAvailableProductions(){
        productionsAvailable = new ArrayList<>();
        AdditionalTradingRule additionalTradingRule;
        for (DevelopmentSlot slot: player.getDevelopmentSlots()){
            //add productions from development cards
            if (slot.showCardOnTop().isPresent() && slot.showCardOnTop().get().getTradingRule().isUsable(player) && slot.showCardOnTop().get().getTradingRule().isUsable(playerClone))
                //there's a usable card in this slot
                if (productionsUsed.stream().noneMatch(developmentCard -> developmentCard.equals(slot.showCardOnTop().get()))) {
                    //this card is unused
                    productionsAvailable.add(slot.showCardOnTop().get());
                }
        }

        if (productionsUsed.stream().noneMatch(production -> production.equals(basicProduction)) &&
            player.getTotalQuantity()>1 && playerClone.getTotalQuantity() > 1) {
            //basic production is unused
            productionsAvailable.add(basicProduction);
        }

        for (LeaderCard leaderCard: player.getActiveLeaderCards())
            //add productions from leader cards
            if (leaderCard.getClass().equals(AdditionalTradingRule.class)){
                //active leader card is AdditionalTradingRule
                additionalTradingRule = (AdditionalTradingRule) leaderCard;
                if (additionalTradingRule.getAdditionalTradingRule().isUsable(player) && additionalTradingRule.getAdditionalTradingRule().isUsable(playerClone) && productionsUsed.stream().noneMatch(card -> card.equals(leaderCard))) {
                    //it is usable and unused
                    productionsAvailable.add(additionalTradingRule);
                }
            }
    }

    /**
     * Make the user to choose how convert an amount of Resource.Any into a valid ResourceType
     * For each Resource.Any converted the user will be asked to choose also the depot from which take the resource
     * (if more than one are presents) and then the chosen Resource converted is added to inputAnyChosen attribute
     * and removed from the playerClone storages (warehouse or strongbox)
     *
     * @param amountToChoose the amount of resources to choose
     */
    private void convertInputAnyToResources(int amountToChoose){
        ArrayList<ResourceType> availableResourceTypes;
        ResourceType chosenResource;
        while (amountToChoose != 0){
            //there are still some Any to be assigned
            Map<ResourceType,Integer> resourcesFromWarehouse = new HashMap<>();
            Map<ResourceType,Integer> resourcesFromStrongbox = new HashMap<>();
            updateResourcesFrom(resourcesFromWarehouse, resourcesFromStrongbox);
            view.showMessage("You have to choose " + amountToChoose + " resources ");
            availableResourceTypes = getPossibleResourceTypes(resourcesFromWarehouse, resourcesFromStrongbox);
            if(availableResourceTypes.size() > 1) {
                view.showMessage("Choose which resource: ");
                chosenResource = availableResourceTypes.get(view.chooseResource(availableResourceTypes));
            }
            else {
                chosenResource = availableResourceTypes.get(0);
                view.showMessage("You can sell only " + chosenResource + "\n");
            }
            inputAnyChosen.add(chosenResource);
            RequireToRemoveResources.removeResourceFromPlayerClone(view,resourcesChosen,chosenResource,playerClone,1);
            amountToChoose--;
        }
    }

    /**
     * Update the structure which contains all quantities available inside the player's storages
     * It is called to be sure user cannot make conflicting choices
     *
     * @param resourcesFromWarehouse maps each resource with all the relative quantity contained into all the warehouse
     * @param resourcesFromStrongbox maps each quantity with all the relative quantity contained in the strongbox
     */
    private void updateResourcesFrom(Map<ResourceType,Integer> resourcesFromWarehouse, Map<ResourceType,Integer> resourcesFromStrongbox){
        for (WarehouseDepot warehouseDepot: playerClone.getWarehouse().getAllDepots())
            resourcesFromWarehouse.merge(warehouseDepot.getResourceType(),warehouseDepot.getQuantity(),Integer::sum);
        for (ResourceType resourceType: playerClone.getStrongbox().keySet())
            resourcesFromStrongbox.put(resourceType,playerClone.getStrongbox().get(resourceType));
    }

    /**
     * Gets a list of possible resources the user can take from the storages
     *
     * @param resourcesFromWarehouse resources available from warehouse
     * @param resourcesFromStrongbox resources available from strongbox
     * @return the resources available
     */
    private ArrayList<ResourceType> getPossibleResourceTypes(Map<ResourceType,Integer> resourcesFromWarehouse, Map<ResourceType,Integer> resourcesFromStrongbox){
        ArrayList<ResourceType> possibleResourceTypes = ResourceType.getAllValidResources();
        possibleResourceTypes.removeIf(resourceType -> (!resourcesFromWarehouse.containsKey(resourceType) || resourcesFromWarehouse.get(resourceType) == 0) && resourcesFromStrongbox.get(resourceType) == 0);
        return possibleResourceTypes;
    }

    /**
     * Chooses the resources to put in the strongbox because of an activation of a production with generic output
     *
     * @param amountToChoose the amount of resources to choose
     */
    private void convertOutputAnyToResources(int amountToChoose){
       ResourceType chosenResourceType;
        view.showMessage("You have to choose " + amountToChoose + " resources to put in the strongbox");
        while (amountToChoose != 0){
            chosenResourceType = view.chooseResource();
            outputAnyChosen.add(chosenResourceType);
            amountToChoose --;
        }
    }

}
