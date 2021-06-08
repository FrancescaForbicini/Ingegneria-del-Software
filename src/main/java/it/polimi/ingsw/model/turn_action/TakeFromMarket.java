package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.message.action_message.market_message.MarketAxis;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class TakeFromMarket implements TurnAction{
    private MarketAxis marketAxis;
    private int num;
    private ArrayList<ResourceType> resources ;
    private boolean whiteDefined;
    private Map<ResourceType,Integer> resourceToDepot;
    private final int faithPoints;

    public TakeFromMarket(MarketAxis marketAxis, int line, Map<ResourceType,Integer> resourceToDepot){
        this.marketAxis = marketAxis;
        this.num = line;
        this.whiteDefined = true;
        this.resourceToDepot = resourceToDepot;
        this.faithPoints = 0;
    }

    @Override
    public boolean isFinished(){
        return !isNumZero() && !isRCNull() && !isWhiteDefined();
    }

    public boolean isRCNull(){
        return marketAxis == null;
    }

    public boolean isNumZero(){
        return num == 0;
    }

    public boolean isWhiteDefined() { return whiteDefined; }

    @Override
    public void play(Player player) {
        for (ResourceType resourceType : resourceToDepot.keySet()){
            player.getPersonalBoard().addResourceToWarehouse(resourceType,1,resourceToDepot.get(resourceType));
        }
    }

    /**
     * Sets for each resource the depot of the warehouse where the player wants to put it
     * @param resourceToDepot mapping between resources and depot ID
     */
    public void setResourceToDepot(Map<ResourceType,Integer> resourceToDepot){
        this.resourceToDepot = resourceToDepot;
    }

    public ArrayList<ResourceType> getResources(){
        return this.resources;
    }
    /**
     * Gets resources from the market
     * @param player the player that wants to take resources from the market
     * @param marketAxis the row or the column chosen
     * @param num the number of the row or the column chosen
     * @return the resources taken from the market
     */
    public void getResourceFromMarket(Player player, MarketAxis marketAxis, int num){
        ArrayList<MarbleType> marbles = Game.getInstance().getMarket().getMarblesFromLine(marketAxis,num);
        convertMarble(player,marbles);
    }

    /**
     * Converts the marble to the resource
     * @param player player that wants to convert a marble to the correspondent resource
     * @param marbles the marbles that the player has taken from the market
     */
    protected void convertMarble(Player player, Collection<MarbleType> marbles ) {
        for (MarbleType marbleType : marbles) {
            if (marbleType.equals(MarbleType.Red)) {
                Game.getInstance().getFaithTrack().move(player, 1);
            } else {
                if(marbleType.equals(MarbleType.White)) {
                    int activeWhite = player.getAmountActiveWhiteConversions();
                    if (activeWhite == 1) {
                        resources.add(player.getActiveWhiteConversions().get(0));
                    } else if (activeWhite > 1) {
                        resources.add(marbleType.conversion());
                        whiteDefined = false;
                    }
                }
                else{
                    resources.add(marbleType.conversion());
                }
            }
        }
    }

    /**
     * Counts the amount of the "Any" resources to be assigned
     * @return the amount of the "Any" resources
     */
    public int resourceAnyAmount(){
        return (int) resources.stream().filter(resourceType -> resourceType.equals(ResourceType.Any)).count();
    }

    /**
     * Sets the "Any" resources to a given resource
     * @param chosenResourcesAny the resources to convert the "Any" resources to
     */
    public void setResourceAny(ArrayList<ResourceType> chosenResourcesAny){
        resources.removeIf(resourceType -> resourceType.equals(ResourceType.Any));
        //TODO check if (resourceAnyConverted.size() == resources.stream().filter(resourceType -> resourceType.equals(ResourceType.Any).count())
        resources.addAll(chosenResourcesAny);
        whiteDefined = true;
    }


}
