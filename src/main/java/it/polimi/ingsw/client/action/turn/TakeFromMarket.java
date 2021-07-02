package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.model.market.MarketAxis;
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

/**
 * Takes resources from market
 */
public class TakeFromMarket extends TurnAction {
    private Player player;
    private MarketAxis marketAxis;
    private int line;
    ArrayList<ResourceType> resourcesToPlace = new ArrayList<>();
    ArrayList<ResourceType> chosenConversions = new ArrayList<>();

    public TakeFromMarket(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }


    /**
     * {@inheritDoc}
     *
     * Checks if a player can take resources from the market
     *
     * @return always true, because this action can be done each turn
     */
    @Override
    public boolean isDoable() {
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * Takes resources from market
     */
    @Override
    public void doAction() {
        ArrayList<MarbleType> takenMarbles;
        player = clientGameObserverProducer.getCurrentPlayer();
        chooseLine(clientGameObserverProducer.getMarket());
        takenMarbles = clientGameObserverProducer.getMarket().getMarblesFromLine(marketAxis,line,false);
        filterAndConvertTakenMarbles(takenMarbles);
        Map<ResourceType,ArrayList<Integer>> resourceToDepot = resourceToDepot(resourcesToPlace,player.getWarehouse());
        clientConnector.sendMessage(new TakeFromMarketDTO(marketAxis, line, resourceToDepot, chosenConversions));
    }

    /**
     * Chooses the line and the row or the column to take resources from the market
     *
     * @param market from which the line is chosen
     */
    private void chooseLine(Market market){
        //messages are showed inside the chooseLine method
        ChosenLine chooseLine = view.chooseLine(market);
        marketAxis = chooseLine.getMarketAxis();
        line = chooseLine.getLine();
    }

    /**
     * Converts white marble into valid resources
     *
     * @param takenMarbles white marbles taken
     */
    private void filterAndConvertTakenMarbles(ArrayList<MarbleType> takenMarbles){
        int whiteMarbles = 0;
        takenMarbles = (ArrayList<MarbleType>) takenMarbles.stream().filter(marbleType -> !marbleType.equals(MarbleType.Red)).collect(Collectors.toList());
        for(MarbleType marbleType : takenMarbles){
            if(marbleType.equals(MarbleType.White)){
                if(player.getActiveWhiteConversions().size() == 1){
                    resourcesToPlace.add(player.getActiveWhiteConversions().get(0));
                    chosenConversions.add(player.getActiveWhiteConversions().get(0));
                } else if (player.getActiveWhiteConversions().size() > 1){
                    whiteMarbles++;
                }
            } else {
                resourcesToPlace.add(marbleType.convertToResource());
            }
        }
        while(whiteMarbles > 0){
            int chosenConvertedWhiteMarbleIndex = view.chooseWhiteMarble(player.getActiveWhiteConversions());
            ResourceType chosenConvertedResource = player.getActiveWhiteConversions().get(chosenConvertedWhiteMarbleIndex);
            chosenConversions.add(chosenConvertedResource);
            resourcesToPlace.add(chosenConvertedResource);
            whiteMarbles--;
        }
    }

    /**
     * Puts the resources in the warehouse
     * All resources that can be automatically placed are placed, the others (if any) need the interaction with the user
     *
     * @param resourcesToPlace the resources taken from the market that must be put in the warehouse
     * @param warehouse where the resources has to be put
     * @return mapping of the resources and the depots correspondents
     */
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
        Map<ResourceType, Integer> amountToPlacePerResource = new HashMap<>();

        for(ResourceType resourceType : resourcesToPlace){
            amountToPlacePerResource.merge(resourceType,1,Integer::sum);
        }

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
                possibleDepots = warehouse.getPossibleDepotsToMoveResources(chosenResource, true);
                int toPlaceOfThisResource = Math.max(0,amountToPlacePerResource.get(chosenResource));
                updatePossibleDepots(depots, possibleDepots, chosenResource, toPlaceOfThisResource);
                if (possibleDepots.size() == 0) {
                    //there's no possible place
                    view.showMessage("You can't put the " + chosenResource + " in the warehouse so it will be discarded");
                    resourcesToDepot.get(chosenResource).add(-1);
                    autoItr.remove();
                    amountToPlacePerResource.replace(chosenResource, amountToPlacePerResource.get(chosenResource), amountToPlacePerResource.get(chosenResource)-1);
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
                        amountToPlacePerResource.replace(chosenResource, amountToPlacePerResource.get(chosenResource), amountToPlacePerResource.get(chosenResource)-1);
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
                possibleDepots = warehouse.getPossibleDepotsToMoveResources(chosenResource,true);
                int toPlaceOfThisResource = Math.max(0,amountToPlacePerResource.get(chosenResource));
                updatePossibleDepots(depots, possibleDepots, chosenResource, toPlaceOfThisResource);
                if(possibleDepots.size()==1) {
                    //must be put in there
                    chosenDepotID = possibleDepots.get(0).getDepotID();
                    view.showMessage(chosenResource + " can be put only in depot " + chosenDepotID + ", so it will be done");
                } else {
                    view.showMessage("Choose which depot do you want to put 1 " + chosenResource + " in: ");
                    chosenDepotIndex = view.chooseDepot(possibleDepots);
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
                amountToPlacePerResource.replace(chosenResource, amountToPlacePerResource.get(chosenResource), amountToPlacePerResource.get(chosenResource)-1);
                placed++;
            }
        }
        return resourcesToDepot;
    }

    /**
     * Updates the depots available where the resources can be put
     *
     * @param depots all the depots that are in the warehouse
     * @param possibleDepots the depots where resource can be put
     * @param chosenResource the resource to put in the depots
     */
    private void updatePossibleDepots(ArrayList<WarehouseDepot> depots, ArrayList<WarehouseDepot> possibleDepots, ResourceType chosenResource, int amountToPlaceOfThisResource){
        for (WarehouseDepot depot : depots) {
            possibleDepots.removeIf(possibleDepot -> (possibleDepot.getDepotID() == depot.getDepotID() && depot.isFull()));
            //the possible depot is full
            possibleDepots.removeIf(possibleDepot -> (possibleDepot.getDepotID() == depot.getDepotID() &&
                    !depot.getResourceType().equals(chosenResource) && !depot.getResourceType().equals(ResourceType.Any)));
            //the possible depot cannot contain the chosenResource
            possibleDepots.removeIf(possibleDepot -> (possibleDepot.getDepotID() == depot.getDepotID() &&
                    (!possibleDepot.isAdditional() &&
                            depots.stream().anyMatch(d -> !d.isAdditional() && d.getResourceType().equals(chosenResource) && d.getDepotID()!=possibleDepot.getDepotID()))));
            //exists another depot (not additional) which contains the chosenResource
            possibleDepots.removeIf(possibleDepot -> (possibleDepot.getDepotID() == depot.getDepotID() &&
                    depot.getAvailableSpace()<amountToPlaceOfThisResource &&
                    depots.stream().anyMatch(d -> d.getDepotID()!=depot.getDepotID() && d.getAvailableSpace()>=amountToPlaceOfThisResource &&
                            (d.getResourceType().equals(chosenResource) || d.getResourceType().equals(ResourceType.Any)))));
            //exists another depot which can contain all the given resource without discarding
        }
    }
}
