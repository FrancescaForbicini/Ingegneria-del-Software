package it.polimi.ingsw.client;

import it.polimi.ingsw.message.action_message.market_message.MarketAxis;

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
