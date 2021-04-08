package it.polimi.ingsw.model.warehouse;

import it.polimi.ingsw.model.ResourceType;

import java.util.ArrayList;
//TODO:add WarehouseTest class
/**
 * Represents the Player's storage for Resources obtained from the Market
 * Into warehouseDepots different depots with same type cannot coexist
 */
public class Warehouse {
    private final ArrayList<WarehouseDepot> warehouseDepots;
    private ArrayList<WarehouseDepot> additionalDepots;

    public Warehouse(){
        this.warehouseDepots = new ArrayList<>();
        //this.warehouseDepots = ArrayList<WarehouseDepot> Settings.getInstance().getDepots();
    }

    /**
     * Adds a depot to the additional ones
     * @param level level of the depot, usually 2
     * @param type type of the depot
     */
    public void addAdditionalDepot(int level,ResourceType type){
        additionalDepots.add(new WarehouseDepot(level,type,true));
    }

    /**
     * Adds the given quantity of resource to the depot passed
     * @param type type of resource needed to add, can respect the rules of single depots and warehouse
     * @param quantity how much quantity needs to be added
     * @param depot where adding the resource to
     * @return true iff it has been possible to add the given amount of resource to the given depot
     */
    public boolean addResource(ResourceType type, int quantity,WarehouseDepot depot){
        ArrayList<WarehouseDepot> sameTypeDepots = findDepot(warehouseDepots,type);
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
        return warehouseDepots;
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
        if(d1.getQuantity()>d2.getLevel() || d2.getQuantity()>d1.getLevel() || (d1.isAdditional()&&d2.isAdditional())){
            return false;
        }
        else{
            int q1 = d1.getQuantity();
            ResourceType t1 = d1.getResourceType();
            int q2 = d2.getQuantity();
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
}
