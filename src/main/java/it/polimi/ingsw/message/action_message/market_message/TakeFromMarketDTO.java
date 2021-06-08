package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.client.action.turn.TakeFromMarket;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

public class TakeFromMarketDTO extends ActionMessageDTO {
    private MarketAxis marketAxis;
    private int line;
    private Map<ResourceType,Integer> resourceToDepot;

    public TakeFromMarketDTO(MarketAxis marketAxis, int line, Map<ResourceType, Integer> resourceToDepot) {
        this.marketAxis = marketAxis;
        this.line = line;
        this.resourceToDepot = resourceToDepot;
    }

    public MarketAxis getMarketAxis() {
        return marketAxis;
    }

    public int getLine() {
        return line;
    }

    public Map<ResourceType, Integer> getResourceToDepot() {
        return resourceToDepot;
    }


    @Override
    public String getRelatedAction() {
        return TakeFromMarket.class.getName();
    }
}
