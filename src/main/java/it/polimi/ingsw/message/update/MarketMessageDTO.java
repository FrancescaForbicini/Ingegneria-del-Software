package it.polimi.ingsw.message.update;

import it.polimi.ingsw.model.market.Market;

public class MarketMessageDTO extends UpdateMessageDTO{
    private Market market;

    public void setMarket(Market market) {
        this.market = market;
    }

    public Market getMarket() {
        return market;
    }
}
