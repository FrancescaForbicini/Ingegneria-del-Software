package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ChosenLine;
import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.market_message.MarketAxis;
import it.polimi.ingsw.message.action_message.market_message.TakeFromMarketDTO;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.*;
import java.util.stream.Collectors;

public class TakeFromMarket extends TurnAction {
    private Player player;
    private MarketAxis marketAxis;
    private int line;
    ArrayList<ResourceType> resourcesToChoose = new ArrayList<>();
    ArrayList<ResourceType> chosenConversions = new ArrayList<>();//TODO useful?

    public TakeFromMarket(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }


    @Override
    public boolean isDoable() {
        return true;
    }

    @Override
    public void doAction() {
        ArrayList<MarbleType> takenMarbles;
        player = clientGameObserverProducer.getCurrentPlayer();
        chooseLine(clientGameObserverProducer.getMarket());
        takenMarbles = clientGameObserverProducer.getMarket().getMarblesFromLine(marketAxis,line,false);
        filterAndConvertTakenMarbles(takenMarbles);
        Map<ResourceType,ArrayList<Integer>> resourceToDepot = resourceToDepot(resourcesToChoose,player.getWarehouse());
        clientConnector.sendMessage(new TakeFromMarketDTO(marketAxis, line, resourceToDepot, chosenConversions));
    }

    private void chooseLine(Market market){
        //messages are showed inside the chooseLine method
        ChosenLine chooseLine = view.chooseLine(market);
        marketAxis = chooseLine.getMarketAxis();
        line = chooseLine.getLine();
    }

    private void filterAndConvertTakenMarbles(ArrayList<MarbleType> takenMarbles){
        int whiteMarbles = 0;
        takenMarbles = (ArrayList<MarbleType>) takenMarbles.stream().filter(marbleType -> !marbleType.equals(MarbleType.Red)).collect(Collectors.toList());
        for(MarbleType marbleType : takenMarbles){
            if(marbleType.equals(MarbleType.White)){
                if(player.getActiveWhiteConversions().size() == 1){
                    resourcesToChoose.add(player.getActiveWhiteConversions().get(0));
                    //TODO need to add also to chosenConversions?
                } else if (player.getActiveWhiteConversions().size() > 1){
                    whiteMarbles++;
                }
            } else {
                resourcesToChoose.add(marbleType.convertToResource());
            }
        }
        while(whiteMarbles > 0){
            view.showMessage("Choose which type of resource assign to the white marbles: ");
            ResourceType chosenConvertedResource = view.chooseWhiteMarble(player.getActiveWhiteConversions());
            chosenConversions.add(chosenConvertedResource);
            resourcesToChoose.add(chosenConvertedResource);
            whiteMarbles--;
        }
    }

    private Map<ResourceType,ArrayList<Integer>> resourceToDepot(ArrayList<ResourceType> resourcesToPlace, Warehouse warehouse){
        Map<ResourceType,ArrayList<Integer>> resourcesToDepot = new HashMap<>();
        int chosenDepotID;
        int chosenDepotIndex;
        ResourceType chosenResource;
        int chosenResourceIndex;
        int placed = 0;
        int toPlace = resourcesToPlace.size();
        ArrayList<WarehouseDepot> depots = new ArrayList<>();
        ArrayList<ResourceType> resources = new ArrayList<>();
        ArrayList<WarehouseDepot> possibleDepots;

        //set all available spaces
        for(WarehouseDepot depot : warehouse.getAllDepots()){
            WarehouseDepot depotToAdd = new WarehouseDepot(depot.getResourceType(),depot.getLevel(),depot.isAdditional(),depot.getDepotID());
            depotToAdd.addResource(depot.getResourceType(),depot.getQuantity());
            depots.add(depotToAdd);
        }
        for(ResourceType type : resourcesToPlace){
            resources.add(type);
            resourcesToDepot.put(type,new ArrayList<>());
        }

        ListIterator<ResourceType> extItr = resources.listIterator();
        while(placed<toPlace) {
            //still resources to place
            ListIterator<ResourceType> autoItr = resources.listIterator();
            while (autoItr.hasNext()) {
                //auto placing
                chosenResource = autoItr.next();
                possibleDepots = warehouse.getPossibleDepotsToMoveResources(chosenResource, 1, true);
                cleanPossibleDepots(depots, possibleDepots, chosenResource);
                if (possibleDepots.size() == 0) {
                    //thre's no possible place
                    view.showMessage("You can't put the " + chosenResource + " in the warehouse so it will be discarded");
                    resourcesToDepot.get(chosenResource).add(-1);
                    autoItr.remove();
                    placed++;
                } else {
                    ArrayList<WarehouseDepot> finalPossibleDepots = possibleDepots;
                    if (possibleDepots.size() == 1 &&
                            (depots.stream().anyMatch(depot -> depot.getDepotID()== finalPossibleDepots.get(0).getDepotID()
                                                                        && !depot.isEmpty()))){
                        //must be put in there
                        chosenDepotID = possibleDepots.get(0).getDepotID();
                        view.showMessage("Depot " + chosenDepotID + " can contain only " + chosenResource +
                                " and " + chosenResource + " can be put only in depot " + chosenDepotID + ", so it will be done");
                        resourcesToDepot.get(chosenResource).add(chosenDepotID);
                        for(WarehouseDepot depot : depots) {
                            if (depot.getDepotID() == chosenDepotID) {
                                depot.addResource(chosenResource, 1);
                            }
                        }
                        autoItr.remove();
                        placed++;
                    }
                }
            }
            while(autoItr.hasPrevious()){
                autoItr.previous();
            }
            if (placed < toPlace) {
                //auto placing wasn't enough
                if(resources.size()==1){
                    chosenResourceIndex = 0;
                } else {
                    view.showMessage("Choose which resource do you want to put in your warehouse: ");
                    chosenResourceIndex = view.chooseResource(resources);
                }
                chosenResource = resources.get(chosenResourceIndex);
                possibleDepots = warehouse.getPossibleDepotsToMoveResources(chosenResource,1,true);
                cleanPossibleDepots(depots, possibleDepots, chosenResource);
                if(possibleDepots.size()==1) {
                    //must be put in there
                    chosenDepotID = possibleDepots.get(0).getDepotID();
                    view.showMessage(chosenResource + " can be put only in depot " + chosenDepotID + ", so it will be done");
                } else {
                    view.showMessage("Choose which depot do you want to put 1 " + chosenResource + "in: ");
                    chosenDepotIndex = view.chooseDepot(possibleDepots, warehouse);
                    chosenDepotID = possibleDepots.get(chosenDepotIndex).getDepotID();
                }
                resourcesToDepot.get(chosenResource).add(chosenDepotID);
                view.showMessage("1 " + chosenResource + " is added in depot " + chosenDepotID);
                for(WarehouseDepot depot : depots) {
                    if (depot.getDepotID() == chosenDepotID) {
                        depot.addResource(chosenResource, 1);
                    }
                }
                resources.remove(chosenResourceIndex);
                placed++;
            }
        }
        return resourcesToDepot;
    }

    private void cleanPossibleDepots(ArrayList<WarehouseDepot> depots, ArrayList<WarehouseDepot> possibleDepots, ResourceType chosenResource){
        for (WarehouseDepot depot : depots) {
            possibleDepots.removeIf(possibleDepot -> (possibleDepot.getDepotID() == depot.getDepotID() &&
                    (depot.isFull() ||
                            (!depot.getResourceType().equals(chosenResource) && !depot.getResourceType().equals(ResourceType.Any)) ||
                            (!possibleDepot.isAdditional() && depots.stream().anyMatch(d -> !d.isAdditional() &&
                                            d.getResourceType().equals(chosenResource) && d.getDepotID()!=possibleDepot.getDepotID())))));
        }
    }

}
