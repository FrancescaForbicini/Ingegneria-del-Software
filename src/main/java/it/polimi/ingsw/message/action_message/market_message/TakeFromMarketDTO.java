package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.client.action.turn.TakeFromMarket;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.ArrayList;
import java.util.Map;

public class TakeFromMarketDTO extends ActionMessageDTO {
    private final MarketAxis marketAxis;
    private final  int line;
    private final Map<ResourceType,Integer> resourceToDepot;
    private final ArrayList<ResourceType> whiteMarbleChosen;

    public TakeFromMarketDTO(MarketAxis marketAxis, int line, Map<ResourceType, Integer> resourceToDepot, ArrayList<ResourceType> whiteMarbleChosen) {
        this.marketAxis = marketAxis;
        this.line = line;
        this.resourceToDepot = resourceToDepot;
        this.whiteMarbleChosen = whiteMarbleChosen;
    }

    public ArrayList<ResourceType> getWhiteMarbleChosen() {
        return whiteMarbleChosen;
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
