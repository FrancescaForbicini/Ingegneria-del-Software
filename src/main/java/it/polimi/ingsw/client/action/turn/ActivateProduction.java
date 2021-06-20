package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
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
    ArrayList<DevelopmentCard> developmentCardsUsed = new ArrayList<>();
    ArrayList<DevelopmentCard> developmentCardsAvailable = new ArrayList<>();

    public ActivateProduction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        player = clientGameObserverProducer.getCurrentPlayer();
        playerClone = new Player(player.getUsername());
        totalInput = new HashMap<>();
        totalOutput = new HashMap<>();
        inputAnyChosen = new ArrayList<>();
        outputAnyChosen = new ArrayList<>();
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
        DevelopmentCard developmentCardChosen;
        playerClone = Remove.clone(player);
        checkDevelopmentCardsAvailable();
        boolean onlyOne = false;
        boolean onlyBasic = false;
        boolean oneUsed = false;
        if (developmentCardsAvailable.size()==1)
            onlyOne = true;

        if (developmentCardsAvailable.size() == 0){
            onlyBasic = true;
        }
        checkDevelopmentCardsAvailable();
        while (developmentCardsAvailable.size() != 0) {
            if (onlyOne){
                view.showMessage("You will activate this production:\n " + developmentCardsAvailable.get(0).getTradingRule().toString());
                developmentCardChosen = developmentCardsAvailable.get(0);
            }
            else if (onlyBasic){
                view.showMessage("You can only activate the basic production");
                developmentCardChosen = basicProduction;
            }
            else {
                view.showMessage("This are the cards that you can activate: ");
                developmentCardsAvailable.forEach(developmentCard -> view.showMessage(developmentCard.toString()));
                view.showMessage("Do you want to choose a card?  ");
                boolean response = view.askToChoose();
                if (oneUsed && !response)
                    break;
                if (!oneUsed && !response)
                    view.showMessage("You have to choose at least a production");
                int chosenDevelopmentCardIndex;
                chosenDevelopmentCardIndex = view.choose(developmentCardsAvailable);
                developmentCardChosen = developmentCardsAvailable.get(chosenDevelopmentCardIndex);
                oneUsed = true;
            }
            developmentCardsUsed.add(developmentCardChosen);
            checkDevelopmentCardsAvailable();
            if (developmentCardChosen.getTradingRule().getInput().containsKey(ResourceType.Any))
                chooseAnyResourceInput(developmentCardChosen.getTradingRule().getInput().get(ResourceType.Any));
            totalInput = getTotalResourceQuantity(developmentCardChosen.getTradingRule().getInput(), null);
            for (ResourceType resourceType : totalInput.keySet()) {
                Remove.inputFrom(view,resourcesChosen,resourceType,playerClone,totalInput.get(resourceType));
            }
            if (developmentCardChosen.getTradingRule().getOutput().containsKey(ResourceType.Any))
                chooseAnyResourcesOutput(developmentCardChosen.getTradingRule().getOutput().get(ResourceType.Any));
            totalOutput = getTotalResourceQuantity(developmentCardChosen.getTradingRule().getOutput(), outputAnyChosen);
            insertOutput();
        }

        clientConnector.sendMessage(new ActivateProductionDTO(developmentCardsUsed,resourcesChosen.getResourcesTakenFromWarehouse(),resourcesChosen.getResourcesTakenFromStrongbox(),inputAnyChosen,outputAnyChosen));
    }

    private void checkDevelopmentCardsAvailable(){
        developmentCardsAvailable = new ArrayList<>();
        for (DevelopmentSlot slot: player.getDevelopmentSlots()){
            if (slot.showCardOnTop().isPresent() && slot.showCardOnTop().get().getTradingRule().isUsable(player))
                if (developmentCardsUsed.stream().noneMatch(developmentCard -> developmentCard.equals(slot.showCardOnTop().get())))
                    developmentCardsAvailable.add(slot.showCardOnTop().get());
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
