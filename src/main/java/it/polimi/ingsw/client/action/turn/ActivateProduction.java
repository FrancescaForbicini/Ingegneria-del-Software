package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.requirement.TradingRule;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.*;

public class ActivateProduction extends TurnAction {
    private final Player player;
    private ArrayList<DevelopmentCard> developmentCardChosen;
    private final Map<ResourceType,Integer> totalInput;
    private final Map<ResourceType,Integer> totalOutput;
    private ArrayList<ResourceType> inputAnyChosen;
    private ArrayList<ResourceType> outputAnyChosen;
    private final Map <ResourceType,Integer> resourcesChosenFromStrongBox;
    private final Map <ResourceType,Integer> resourcesChosenFromWarehouse;
    Map<ResourceType,Integer> resourceWarehouse = new HashMap<>();
    Map<ResourceType,Integer> resourceStrongbox = new HashMap<>();
    ArrayList<DevelopmentCard> developmentCardsUsed = new ArrayList<>();
    ArrayList<DevelopmentCard> developmentCardsAvailable = new ArrayList<>();

    public ActivateProduction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
        player = clientGameObserverProducer.getCurrentPlayer();
        totalInput = new HashMap<>();
        totalOutput = new HashMap<>();
        inputAnyChosen = new ArrayList<>();
        outputAnyChosen = new ArrayList<>();
        resourcesChosenFromStrongBox = new HashMap<>();
        resourcesChosenFromWarehouse = new HashMap<>();
        developmentCardChosen = new ArrayList<>();
    }

    @Override
    public boolean isDoable() {
        return Arrays.stream(player.getPersonalBoard().getDevelopmentSlots()).anyMatch(developmentSlot -> developmentSlot.showCardOnTop().isPresent() && developmentSlot.showCardOnTop().get().getTradingRule().isUsable(player));
    }

    @Override
    public void doAction() {
        ArrayList<TradingRule> tradingRulesChosen = new ArrayList<>();
        for (WarehouseDepot warehouseDepot: player.getWarehouse().getWarehouseDepots()){
            if (!resourceWarehouse.containsKey(warehouseDepot.getResourceType()))
                resourceWarehouse.put(warehouseDepot.getResourceType(),warehouseDepot.getQuantity());
            else
                //case of additional depot
                resourceWarehouse.replace(warehouseDepot.getResourceType(),resourceWarehouse.get(warehouseDepot.getResourceType()),resourceWarehouse.get(warehouseDepot.getResourceType())+warehouseDepot.getQuantity());
        }
        for (ResourceType resourceType: player.getStrongbox().keySet())
            resourceStrongbox.put(resourceType,player.getStrongbox().get(resourceType));

        developmentCardsAvailable = new ArrayList<>();
        checkDevelopmentCardsAvailable();
        while(developmentCardsAvailable.size() != 0) {
            if (developmentCardsAvailable.size() == 1) {
                view.showMessage("You will activate this production:\n " + developmentCardsAvailable.get(0).getTradingRule().toString());
                developmentCardChosen.add(developmentCardsAvailable.get(0));
            }
            else
                chooseTradingRule(developmentCardsAvailable);
            if (developmentCardChosen.isEmpty())
                break;
            else
                developmentCardChosen.forEach(developmentCard -> tradingRulesChosen.add(developmentCard.getTradingRule()));
            checkChoice();
            developmentCardsAvailable = new ArrayList<>();
            checkDevelopmentCardsAvailable();
            getTotalInput(tradingRulesChosen);
            getTotalOutput(tradingRulesChosen);
        }
        clientConnector.sendMessage(new ActivateProductionDTO(developmentCardsUsed,resourcesChosenFromWarehouse,resourcesChosenFromStrongBox,inputAnyChosen,outputAnyChosen));
    }

    private void checkDevelopmentCardsAvailable(){
        for (DevelopmentSlot developmentSlot: player.getDevelopmentSlots()){
            if (developmentSlot.showCardOnTop().isPresent())
                if (developmentCardsUsed.stream().noneMatch(developmentCard -> developmentCard.equals(developmentSlot.showCardOnTop().get()))){
                    if (checkUsable(developmentSlot.showCardOnTop().get()))
                        developmentCardsAvailable.add(developmentSlot.showCardOnTop().get());
                }
        }
    }

    private boolean checkUsable(DevelopmentCard developmentCard){
        int quantity = 0;
        for (ResourceType resourceType: developmentCard.getTradingRule().getInput().keySet()) {
            if (resourceWarehouse.containsKey(resourceType))
                quantity += resourceWarehouse.get(resourceType);
            if (resourceStrongbox.containsKey(resourceType))
                quantity += resourceStrongbox.get(resourceType);
            if (quantity < developmentCard.getTradingRule().getInput().get(resourceType) && !resourceType.equals(ResourceType.Any))
                return false;
            if (developmentCard.getTradingRule().getInput().containsKey(ResourceType.Any) && developmentCard.getTradingRule().getInput().get(ResourceType.Any) < totalAmount() - quantity)
                return false;
            quantity = 0;
        }
        return true;
    }

    private void chooseTradingRule(ArrayList<DevelopmentCard> developmentCards){
        developmentCardChosen = view.chooseDevelopmentCards(developmentCards);
    }

    private void checkChoice(){
        for (DevelopmentCard developmentCard: developmentCardChosen){
            if (!checkUsable(developmentCard)) {
                developmentCardChosen.remove(developmentCard);
                view.showMessage("The trading rule has been discarded because you do not have enough resources \n" +developmentCard.getTradingRule());
            }
            else{
                for (ResourceType resourceType: developmentCard.getTradingRule().getInput().keySet()){
                    if (!resourceType.equals(ResourceType.Any))
                        inputFrom(resourceType,developmentCard.getTradingRule().getInput().get(resourceType));
                }
                if (developmentCard.getTradingRule().getInput().containsKey(ResourceType.Any)) {
                    int amount = developmentCard.getTradingRule().getInput().get(ResourceType.Any);
                    while (amount != 0) {
                        view.showMessage("You have " + amount + " resources to choose");
                        view.showMessage("This are the resources from warehouse \n" + resourceWarehouse);
                        view.showMessage("This are the resources from strongbox \n " + resourceStrongbox);
                        ResourceType resourceType;
                        do {
                            resourceType = view.chooseResource();
                        } while (!resourceWarehouse.containsKey(resourceType) && !resourceStrongbox.containsKey(resourceType));
                        inputFrom(resourceType,1);
                        inputAnyChosen.add(resourceType);
                        amount--;
                    }
                }
                developmentCardsUsed.add(developmentCard);
                if (!developmentCard.getTradingRule().getOutput().isEmpty())
                    insertOutput(developmentCard);
            }
        }
    }

    private void inputFrom(ResourceType resourceType, int amount){
        int quantityStrongbox;
        int quantityWarehouse;
        Map<String,Integer> inputFrom;
        quantityWarehouse = resourceWarehouse.get(resourceType);
        quantityStrongbox = resourceStrongbox.get(resourceType);
        if (quantityStrongbox == 0)
            resourcesChosenFromWarehouse.put(resourceType,amount);
        else if (quantityWarehouse == 0)
            resourcesChosenFromStrongBox.put(resourceType,amount);
        else if (quantityWarehouse + quantityStrongbox == amount){
            resourcesChosenFromStrongBox.put(resourceType,quantityStrongbox);
            resourcesChosenFromWarehouse.put(resourceType,quantityWarehouse);
        }
        else{
            inputFrom = view.inputFrom(quantityStrongbox,quantityWarehouse,resourceType,amount);
            if (inputFrom.containsKey("strongbox")) {
                if (resourcesChosenFromStrongBox.containsKey(resourceType)){
                    resourcesChosenFromStrongBox.merge(resourceType,inputFrom.get("strongbox"),Integer::sum);
                }
                else
                    resourcesChosenFromStrongBox.put(resourceType, inputFrom.get("strongbox"));
                resourceStrongbox.remove(resourceType,inputFrom.get("strongbox"));
            }
            if (inputFrom.containsKey("warehouse")) {
                if (resourcesChosenFromWarehouse.containsKey(resourceType))
                    resourcesChosenFromWarehouse.merge(resourceType,inputFrom.get("warehouse"),Integer::sum);
                else
                    resourcesChosenFromWarehouse.put(resourceType, inputFrom.get("warehouse"));
                resourceWarehouse.remove(resourceType,inputFrom.get("warehouse"));
            }
        }
    }

    private void insertOutput(DevelopmentCard developmentCard){
        int quantity = 0;
        if (developmentCard.getTradingRule().getOutput().containsKey(ResourceType.Any)) {
            outputAnyChosen = view.chooseResourcesAny(developmentCard.getTradingRule().getOutput().get(ResourceType.Any));
            developmentCard.getTradingRule().getOutput().remove(ResourceType.Any);
        }
        for (ResourceType resourceType: developmentCard.getTradingRule().getOutput().keySet()){
            if (outputAnyChosen.contains(resourceType)){
                quantity = (int) outputAnyChosen.stream().filter(resource -> resource.equals(resourceType)).count();
            }
            quantity += developmentCard.getTradingRule().getOutput().get(resourceType);
            resourceStrongbox.merge(resourceType,quantity,Integer::sum);
            quantity = 0;
        }
    }

    private int totalAmount(){
        int amount = 0;
        for (ResourceType resourceType: resourceWarehouse.keySet())
            amount += resourceWarehouse.get(resourceType);
        for (ResourceType resourceType: resourceStrongbox.keySet())
            amount += resourceStrongbox.get(resourceType);
        return amount;
    }

    /**
     * Collects all the input of the trading rules
     */
    private void getTotalOutput(ArrayList<TradingRule> tradingRulesChosen) {
        for(TradingRule tradingRule: tradingRulesChosen){
            for (ResourceType resourceType: tradingRule.getOutput().keySet())
                if (!resourceType.equals(ResourceType.Any)){
                    totalOutput.merge(resourceType,tradingRule.getOutput().get(resourceType),Integer::sum);
                }
            if (tradingRule.getOutput().containsKey(ResourceType.Any)){
                outputAnyChosen.forEach(resource -> totalOutput.merge(resource,1,Integer::sum));
            }
        }
    }

    /**
     * Collects all the input of the trading rules
     */
    private void getTotalInput(ArrayList<TradingRule> tradingRulesChosen) {
        for(TradingRule tradingRule: tradingRulesChosen){
            for (ResourceType resourceType: tradingRule.getInput().keySet())
                if (!resourceType.equals(ResourceType.Any)){
                    totalInput.merge(resourceType,tradingRule.getInput().get(resourceType),Integer::sum);
                }
            if (tradingRule.getOutput().containsKey(ResourceType.Any)){
                inputAnyChosen.forEach(resource -> totalInput.merge(resource,1,Integer::sum));
            }
        }
    }
}
