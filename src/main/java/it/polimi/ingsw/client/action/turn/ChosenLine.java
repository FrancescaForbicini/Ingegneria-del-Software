package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.model.market.MarketAxis;

/**
 * Line and the row or the column chosen to take the resources from the market
 */
public class ChosenLine {
    private final MarketAxis marketAxis;
    private final int line;

    public ChosenLine(MarketAxis marketAxis, int line) {
        this.marketAxis = marketAxis;
        this.line = line;
    }

    public MarketAxis getMarketAxis() {
        return marketAxis;
    }

    public int getLine() {
        return line;
    }

}
