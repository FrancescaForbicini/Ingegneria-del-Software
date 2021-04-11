package it.polimi.ingsw.model;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class RequirementResourceTest {
    private RequirementResource requirementResource;
    private Player player;

    @Before
    public void setUp() throws Exception {
        requirementResource=new RequirementResource(2,ResourceType.Any);
    }

    @Test
    public void isSatisfied() {
        int quantity;
        ResourceType resource;

        //Quantity is not satisfied
        quantity=requirementResource.getQuantity()+1;
        assertFalse(requirementResource.isSatisfied(player));

        //Resource is not satisfied
        resource=ResourceType.Servants;
        assertFalse(requirementResource.isSatisfied(player));
    }
}