package it.polimi.ingsw.model.turn_taker;

public class TurnTakerScore implements Comparable {
    private final int victoryPoint;
    private final int resourceCount;

    public TurnTakerScore(int victoryPoint, int resourceCount) {
        this.victoryPoint = victoryPoint;
        this.resourceCount = resourceCount;
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
        int res = Integer.compare(other.getVictoryPoint(), victoryPoint);
        if (res != 0)
            return res;
        return Integer.compare(other.getResourceCount(), resourceCount);
    }
}

