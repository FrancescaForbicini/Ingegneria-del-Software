package it.polimi.ingsw.model;

public class RequirementResource extends Requirement{
    private int quantity;
    private ResourceType resource;

    /**Initiliazes the Requirement Resource
     *
     * @param quantity: used to specify the quantity of the resource
     * @param resource: used to specify the type of the resource
     */
    public RequirementResource(int quantity, ResourceType resource) {
        this.quantity = quantity;
        this.resource = resource;
    }

    @Override
    public boolean isSatisfied() {
        return super.isSatisfied();
    }
}
