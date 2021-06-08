package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.client.action.turn_action.TakeFromMarket;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

public class TakeFromMarketDTO extends ActionMessageDTO {
    private String rc;
    private int line;
    private Map<ResourceType,Integer> resourcesTaken;

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public Map<ResourceType, Integer> getResourcesTaken() {
        return resourcesTaken;
    }

    public void setResourcesTaken(Map<ResourceType, Integer> resourcesTaken) {
        this.resourcesTaken = resourcesTaken;
    }

    @Override
    public String getRelatedAction() {
        return TakeFromMarket.class.getName();
    }
}
