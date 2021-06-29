package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.market.MarketAxis;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.turn_taker.TurnTaker;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class TakeFromMarket implements TurnAction{
    private final MarketAxis marketAxis;
    private final int numLine;
    private final Map<ResourceType,Integer> resourcesFromMarket;
    private final Map<ResourceType,ArrayList<Integer>> resourceToDepot;
    private int faithPointsToAssign;
    private int whiteMarblesFromMarket;
    private int quantityToDiscard;
    private ArrayList<MarbleType> marblesFromMarket;
    private final ArrayList<ResourceType> whiteMarbleChosen;
    int numRow = Game.getInstance().getMarket().getNumRow();
    int numCol = Game.getInstance().getMarket().getNumCol();


    public TakeFromMarket(MarketAxis marketAxis, int line, Map<ResourceType,ArrayList<Integer>> resourceToDepot, ArrayList<ResourceType> whiteMarbleChosen){
        this.marketAxis = marketAxis;
        this.numLine = line;
        this.resourceToDepot = resourceToDepot;
        this.whiteMarbleChosen = whiteMarbleChosen;
        this.faithPointsToAssign = 0;
        this.resourcesFromMarket = new HashMap<>();
        this.quantityToDiscard = 0;
    }

    @Override
    public void play(Player player) {
        marblesFromMarket = Game.getInstance().getMarket().getMarblesFromLine(marketAxis, numLine, false);
        if(isUserInputCorrect(player)) {
            getResourceFromMarket(marketAxis, numLine);
            addWhiteMarbleChosen();
            for (ResourceType resourceToAdd : resourcesFromMarket.keySet()) {
                int quantityAdded = 0;
                int quantityToAdd = resourcesFromMarket.get(resourceToAdd);
                if (resourceToDepot.containsKey(resourceToAdd)) {
                    for (Integer depotID : resourceToDepot.get(resourceToAdd)) {
                        if (player.getPersonalBoard().addResourceToWarehouse(resourceToAdd, 1, depotID)) {
                            quantityAdded++;
                        }
                    }
                    if (quantityAdded < quantityToAdd) {
                        quantityToDiscard += quantityToAdd - quantityAdded;
                    }
                }
            }
            assignFaithPoints(player);
        }
        else {
            //if the game is corrupted, the game will end
            Game.getInstance().setEnded();
        }
    }

    /**
     * Checks if all the user's input is valid
     * @param player to check on
     * @return true iff all the input is correct and the action can proceed without any other interruption
     */
    private boolean isUserInputCorrect(Player player){
        if(!isLineCorrect()){
            return false;
        }
        return areResourcesCorrect(player);
    }

    /**
     * Checks if marketAxis and the relative line are correct
     * @return true iff both marketAxis and numLine are correct and compatible
     */
    private boolean isLineCorrect(){
        return numLine >= 0 &&
                ((marketAxis.equals(MarketAxis.ROW) && numLine <= numRow) ||
                        ((marketAxis.equals(MarketAxis.COL) && numLine <= numCol)));
    }

    /**
     * Checks if all passed resources are correctly quantified, placed and converted
     * @param player to check on
     * @return true iff all resources are correct
     */
    private boolean areResourcesCorrect(Player player){
        if(!areWhiteMarblesCorrect(player)){
            return false;
        }
        return isResourceToDepotCorrect(player);
    }

    /**
     * Checks if the passed conversion for white marbles are correct
     * @param player to check on
     * @return true iff player can activate all the requested conversions and activates them with the right LeaderCard
     */
    private boolean areWhiteMarblesCorrect(Player player){
        whiteMarblesFromMarket = (int) marblesFromMarket.stream().filter(marble -> marble.equals(MarbleType.White)).count();
        if((whiteMarbleChosen.size()>0 && (whiteMarblesFromMarket==0 || player.getActiveWhiteConversions().size()==0)) ||
                //(user wants to convert white marbles, but there aren't white marbles or user has no available conversions) OR
                (whiteMarbleChosen.size()==0 && whiteMarblesFromMarket>0 && player.getActiveWhiteConversions().size()>0)){
                //user doesn't to convert white marbles but there are white marbles and user has available conversions
            return false;
        } else if(whiteMarbleChosen.size()>0 && whiteMarblesFromMarket>0 && player.getActiveWhiteConversions().size()>0){
            //user wants to convert white marbles and there are white marbles and user has available conversions
            if (whiteMarbleChosen.size() != whiteMarblesFromMarket){
                //user wants to convert an amount of white marbles different from the one from the market
                return false;
            }
            for (ResourceType chosenType : whiteMarbleChosen) {
                if (player.getActiveWhiteConversions().stream().noneMatch(activeConversion -> activeConversion.equals(chosenType))) {
                    //user wants to convert a white marble to a ResourceType which is not available to the user
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Checks if the resources the user wants to put in the depots are correct and assigned to a correct depot
     * @param player to make checks on
     * @return true iff all resources are correct and correctly placed
     */
    private boolean isResourceToDepotCorrect(Player player) {
        ArrayList<ResourceType> allResourcesFromMarket = new ArrayList<>();
        int whiteMarbleAdded = 0;
        for (MarbleType marbleType : marblesFromMarket) {
            if (!marbleType.equals(MarbleType.Red)) {
                if (marbleType.equals(MarbleType.White) && whiteMarbleChosen.size() > 0) {
                    allResourcesFromMarket.add(whiteMarbleChosen.get(whiteMarbleAdded));
                    whiteMarbleAdded++;
                }
                if (!marbleType.equals(MarbleType.White)) {
                    allResourcesFromMarket.add(marbleType.convertToResource());
                }
            }
        }
        for (ResourceType resourceToPlace : resourceToDepot.keySet()) {
            if (!allResourcesFromMarket.contains(resourceToPlace)) {
                //user tries to add an unavailable resource
                return false;
            } else {
                int quantityFromMarket = (int) allResourcesFromMarket.stream().filter(resourceType -> resourceType.equals(resourceToPlace)).count();
                int quantityToAdd = (int) resourceToDepot.get(resourceToPlace).stream().filter(depotID -> depotID != -1).count();
                if (quantityToAdd != quantityFromMarket) {
                    //user wants to discard some units of a resource that will be added
                    ArrayList<WarehouseDepot> allAvailableDepots = player.getWarehouse().getPossibleDepotsToMoveResources(resourceToPlace, true);
                    for (ResourceType otherResourceToPlace : resourceToDepot.keySet()) {
                        if (!otherResourceToPlace.equals(resourceToPlace)) {
                            for (Integer depotID : resourceToDepot.get(otherResourceToPlace)) {
                                allAvailableDepots.removeIf(availableDepot -> availableDepot.getDepotID() == depotID &&
                                        availableDepot.isPossibleToMoveResource(otherResourceToPlace, 1, true));
                            }
                        }
                    }
                    int availableSpots = 0;
                    for (WarehouseDepot availableDepot : allAvailableDepots) {
                        availableSpots += availableDepot.getAvailableSpace();
                    }
                    int quantityToDiscard = (int) resourceToDepot.get(resourceToPlace).stream().filter(depotID -> depotID == -1).count();
                    if (quantityToDiscard != quantityFromMarket - quantityToAdd || availableSpots > quantityFromMarket || quantityToDiscard != quantityFromMarket - availableSpots) {
                        return false;
                    }
                } else {
                    //user wants to add all the quantity from market
                    for (Integer depotID : resourceToDepot.get(resourceToPlace)) {
                        int quantityToAddPerDepot = (int) resourceToDepot.get(resourceToPlace).stream().filter(otherDepotID -> otherDepotID.equals(depotID)).count();
                        if (!player.getWarehouse().canMoveResource(resourceToPlace, quantityToAddPerDepot, depotID, true)) {
                            //user tries to add more resource then possible in a particular depot
                            return false;
                        }
                    }
                }
            }
        }
        //get resources to discard completely
        ArrayList<ResourceType> allResourcesToDiscard = new ArrayList<>();
        for (ResourceType resourceFromMarket : allResourcesFromMarket) {
            if (!resourceToDepot.containsKey(resourceFromMarket) || resourceToDepot.get(resourceFromMarket).stream().allMatch(depotID -> depotID == -1)) {
                //user wants to discard this resourceFromMarket entirely
                allResourcesToDiscard.add(resourceFromMarket);
            }
        }
        if (allResourcesToDiscard.size() > 0) {
            //user wants to discard entirely at least 1 resource
            for (ResourceType resourceToDiscard : allResourcesToDiscard) {
                if (player.getWarehouse().canMoveResource(resourceToDiscard, 1, true)) {
                    //user can potentially add at least 1 unit of this resource before adding the others
                    ArrayList<WarehouseDepot> allAvailableDepots = player.getWarehouse().getPossibleDepotsToMoveResources(resourceToDiscard, true);
                    for (ResourceType resourceToAdd : resourceToDepot.keySet()) {
                        for (Integer depotID : resourceToDepot.get(resourceToAdd)) {
                            allAvailableDepots.removeIf(availableDepot -> availableDepot.getDepotID() == depotID);
                        }
                    }
                    if (allAvailableDepots.size() > 0) {
                        //after adding other resources there will be still place for at least 1 unit of the resourceToDiscard
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * Checks if the conversion of the white marble is correct and adds the white marble chosen to the resource to put
     *
     * @return true iff the conversion of the white marble is correct
     */
    private boolean addWhiteMarbleChosen(){
            for (ResourceType resourceType: whiteMarbleChosen)
                resourcesFromMarket.merge(resourceType,1,Integer::sum);
        return true;
    }


    /**
     * Gets resources from the market
     *
     * @param marketAxis the row or the column chosen
     * @param num the number of the row or the column chosen
     */
    private void getResourceFromMarket(MarketAxis marketAxis, int num){
        ArrayList<MarbleType> marbles = Game.getInstance().getMarket().getMarblesFromLine(marketAxis,num,true);
        faithPointsToAssign = (int) marbles.stream().filter(marble -> marble.equals(MarbleType.Red)).count();
        marbles = (ArrayList<MarbleType>) marbles.stream().filter(marbleType -> !marbleType.equals(MarbleType.Red) && !marbleType.equals(MarbleType.White)).collect(Collectors.toList());
        marbles.forEach(marble -> resourcesFromMarket.merge(marble.convertToResource(),1,Integer::sum));
    }

    /**
     * Assigns the faith points to the players
     *
     * @param player the player to assign faith points
     */
    private void assignFaithPoints(Player player){
        if (faithPointsToAssign > 0)
            Game.getInstance().getFaithTrack().move(player, faithPointsToAssign);
        if (quantityToDiscard > 0){
            for(TurnTaker turnTaker : Game.getInstance().getTurnTakers()){
                if (!turnTaker.getUsername().equals(player.getUsername()))
                    Game.getInstance().getFaithTrack().move(turnTaker, quantityToDiscard);
            }
        }
    }

}
