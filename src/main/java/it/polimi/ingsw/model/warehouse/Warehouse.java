package it.polimi.ingsw.model.warehouse;

import it.polimi.ingsw.model.ResourceType;

import java.util.ArrayList;
import java.util.LinkedHashSet;
/**
 * Represents the Player's storage for Resources obtained from the Market
 * Into warehouseDepots different depots with same type cannot coexist
 */
public class Warehouse {
    private final LinkedHashSet<WarehouseDepot> warehouseDepots;
    private ArrayList<WarehouseDepot> additionalDepots;

    public Warehouse(){
        this.warehouseDepots = new LinkedHashSet<>();
        for(int i=0;i<3;i++){
            warehouseDepots.add(new WarehouseDepot(i+1,ResourceType.Any,false));
        }
        this.additionalDepots = new ArrayList<>();
    }

    /**
     * Adds a depot to the additional ones
     * @param additionalDepot the one to be added (only if it is actually additional)
     */
    public boolean addAdditionalDepot(WarehouseDepot additionalDepot){
        if(additionalDepot.isAdditional()) {
            additionalDepots.add(additionalDepot);
            return true;
        }
        return false;
    }

    /**
     * Adds the given quantity of resource to the depot passed
     * @param type type of resource needed to add, can respect the rules of single depots and warehouse
     * @param quantity how much quantity needs to be added
     * @param depot where adding the resource to
     * @return true iff it has been possible to add the given amount of resource to the given depot
     */
    public boolean addResource(ResourceType type, int quantity,WarehouseDepot depot){
        ArrayList<WarehouseDepot> sameTypeDepots = findDepot(new ArrayList<>(warehouseDepots),type);
        if(depot.isAdditional() || (!depot.isAdditional() && (sameTypeDepots.isEmpty() || sameTypeDepots.stream().allMatch(warehouseDepot -> warehouseDepot.equals(depot))))){
            return depot.addResource(type,quantity);
        }
        return false;
    }

    /**
     * Removes the given quantity of resource from the given depot
     * @param quantity how much quantity needs to be removed
     * @param depot where removing the resource from
     * @return true iff it has been possible to remove the given amount of resource from the given depot
     */
    public boolean removeResource(int quantity, WarehouseDepot depot){
        return depot.removeResource(quantity);
    }

    public  ArrayList<WarehouseDepot> getWarehouseDepots(){
        return new ArrayList<>(warehouseDepots);
    }

    public ArrayList<WarehouseDepot> getAdditionalDepots(){
        return additionalDepots;
    }

    /**
     * Switches resources between two given depots
     * @param d1 first depot
     * @param d2 second depot
     * @return true iff it has been possible to switch resources according to the rules
     */
    public boolean switchResource(WarehouseDepot d1,WarehouseDepot d2){
        int q1 = d1.getQuantity();
        int q2 = d2.getQuantity();
        if(q1>d2.getLevel() || q2>d1.getLevel() || (d1.isAdditional()&&d2.isAdditional())){
            return false;
        }
        else{
            ResourceType t1 = d1.getResourceType();
            ResourceType t2 = d2.getResourceType();
            if(!t1.equals(t2) && (d1.isAdditional() || d2.isAdditional())){
                return false;
            }
            else{
                return d1.removeResource(q1) && d1.addResource(t2, q2) && d2.removeResource(q2) && d1.addResource(t1, q1);
            }
        }
    }

    /**
     * Finds a list of depots, from a given list, which has the same resource type as the given one
     * @param list where to search the depots
     * @param type type needed to be matched
     * @return list of all matching depots found
     */
    private ArrayList<WarehouseDepot> findDepot(ArrayList<WarehouseDepot> list, ResourceType type){
        ArrayList<WarehouseDepot> depots = new ArrayList<>();
        for (WarehouseDepot warehouseDepot : list) {
            if (warehouseDepot.getResourceType().equals(type)) {
                depots.add(warehouseDepot);
            }
        }
        return depots;
    }

    /**
     * Gives the amount of given resource which is in the warehouse
     * @param type resource asked for
     * @return total amount of given resource
     */
    public int getQuantity(ResourceType type){
        int tot = 0;
        ArrayList<WarehouseDepot> depots = findDepot(new ArrayList<>(warehouseDepots),type);
        depots.addAll(findDepot(additionalDepots,type));
        for(WarehouseDepot warehouseDepot : depots){
            tot += warehouseDepot.getQuantity();
        }
        return tot;
    }

    /**
     * Gives a depot from the warehouse which matches the requested level
     * @param level level requested
     * @return the matching depot, in case the level is over the boundaries the closest depot is returned,
     * null if there is no depot of given level between 1 and 3 (it should be never returned)
     */
    public WarehouseDepot getDepot(int level){
        level = (level<1) ? 1 : Math.min(level, 3);
        for(WarehouseDepot warehouseDepot : warehouseDepots){
            if(warehouseDepot.getLevel()==level){
                return warehouseDepot;
            }
        }
        return null;
    }
}
