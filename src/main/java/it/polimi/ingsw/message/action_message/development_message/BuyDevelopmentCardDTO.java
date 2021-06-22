package it.polimi.ingsw.message.action_message.development_message;

import it.polimi.ingsw.client.action.turn.BuyDevelopmentCard;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

public class BuyDevelopmentCardDTO extends ActionMessageDTO {
    private final DevelopmentCard card;
    private final int slotID;
    private final Map<ResourceType,Map<Integer,Integer>> resourcesChosenFromWarehouse;
    private final Map<ResourceType,Integer> resourcesChosenFromStrongbox;

    public BuyDevelopmentCardDTO(DevelopmentCard card, int slotID, Map<ResourceType,Map<Integer,Integer>> resourcesChosenFromWarehouse, Map<ResourceType,Integer> resourcesChosenFromStrongbox) {
        this.card = card;
        this.slotID = slotID;
        this.resourcesChosenFromWarehouse = resourcesChosenFromWarehouse;
        this.resourcesChosenFromStrongbox = resourcesChosenFromStrongbox;
    }
    public int getSlotID() {
        return slotID;
    }

    public DevelopmentCard getCard() {
        return card;
    }

    public Map<ResourceType, Map<Integer,Integer>> getResourcesChosenFromWarehouse() {
        return resourcesChosenFromWarehouse;
    }

    public Map<ResourceType, Integer> getResourcesChosenFromStrongbox() {
        return resourcesChosenFromStrongbox;
    }

    @Override
    public String getRelatedAction() {
        return BuyDevelopmentCard.class.getName();
    }
}
