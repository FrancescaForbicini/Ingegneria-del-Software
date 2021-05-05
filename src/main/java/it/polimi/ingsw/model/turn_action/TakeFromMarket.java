package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class TakeFromMarket implements TurnAction{
    private String rc;
    private int num;
    private ArrayList<ResourceType> resources ;
    private boolean whiteDefined;
    private Map<ResourceType,Integer> resourceToDepot;

    public TakeFromMarket(){
        this.rc = null;
        this.num = 0;
        this.resources = null;
        this.whiteDefined = true;
        this.resourceToDepot = null;
    }

    public void setNum(int num) { this.num = num; }
    public void setRc(String rc) { this.rc = rc; }

    @Override
    public boolean isFinished(){
        return !isNumZero() && !isRCNull() && !isWhiteDefined();
    }

    public boolean isRCNull(){
        return rc == null;
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

    /**
     * Gets resources from the market
     * @param player the player that wants to take resources from the market
     * @param rc the row or the column chosen
     * @param num the number of the row or the column chosen
     * @return the resources taken from the market
     */
    public void getResourceFromMarket(Player player, String rc, int num){
        ArrayList<MarbleType> marbles = Game.getInstance().getMarket().getMarblesFromLine(rc,num);
        convertMarble(player,marbles);
    }

    /**
     * Converts the marble to the resource
     * @param player player that wants to convert a marble to the correspondent resource
     * @param marbles the marbles that the player has taken from the market
     */
    private void convertMarble(Player player, Collection<MarbleType> marbles ) {
        for (MarbleType marbleType : marbles) {
            if (marbleType.equals(MarbleType.Red)) {
                Game.getInstance().getFaithTrack().move(player, 1);
            } else {
                if(marbleType.equals(MarbleType.White)) {
                    int activeWhite = player.getAmountActiveWhiteConversions();
                    if (activeWhite == 1) {
                        resources.add(player.getActiveWhiteConversions().get(0));
                    } else if (activeWhite > 1) {
                        resources.add(conversion(marbleType));
                        whiteDefined = false;
                    }
                }
                else{
                    resources.add(conversion(marbleType));
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

    /**
     * Convert a marble to the own resource
     * @param marbleType type to be converted
     * @return the resource type that corresponds to the marble
     */
    private ResourceType conversion (MarbleType marbleType){
        switch(marbleType){
            case White:
                return ResourceType.Any;
            case Blue:
                return ResourceType.Shields;
            case Grey:
                return ResourceType.Stones;
            case Yellow:
                return ResourceType.Coins;
            case Purple:
                return ResourceType.Servants;
        }
        return null;
    }
}