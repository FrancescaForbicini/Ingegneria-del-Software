package it.polimi.ingsw.model.turn_taker;

public class TurnTakerScore implements Comparable {
    private final int victoryPoint;
    private final int resourceCount;
    private final boolean winner;

    public TurnTakerScore(int victoryPoint, int resourceCount) {
        this.victoryPoint = victoryPoint;
        this.resourceCount = resourceCount;
        winner = false;
    }

    public TurnTakerScore(boolean winner) {
        this.victoryPoint = 0;
        this.resourceCount = 0;
        this.winner = winner;
    }

    public int getVictoryPoint() {
        return victoryPoint;
    }

    public int getResourceCount() {
        return resourceCount;
    }

    @Override
    public int compareTo(Object o) {
        if(!o.getClass().equals(TurnTakerScore.class)) {
            return 0;
        }
        TurnTakerScore other = (TurnTakerScore)o;
        if (winner)
            return 1;
        if (other.winner)
            return -1;
        int res = Integer.compare(other.getVictoryPoint(), victoryPoint);
        if (res != 0)
            return res;
        return Integer.compare(other.getResourceCount(), resourceCount);
    }
}

