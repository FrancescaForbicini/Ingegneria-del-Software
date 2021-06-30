package it.polimi.ingsw.model.turn_taker;

public class TurnTakerScore implements Comparable {
    private int victoryPoint;
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

    /**
     * Checks if the player has more then five resource
     *
     * @param victoryPoints victory points of the player
     * @param resourceAmount total amount of resources
     * @return new value of victory points iff the player has more than five resources
     */
    private int addingPointsForFiveResources(int victoryPoints,int resourceAmount){
        if (resourceAmount >= 5 )
             return victoryPoints + resourceAmount/5;
        return victoryPoints;
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
        victoryPoint = addingPointsForFiveResources(victoryPoint,resourceCount);
        other.victoryPoint = addingPointsForFiveResources(other.getVictoryPoint(),other.getResourceCount());
        int res = Integer.compare(victoryPoint,other.getVictoryPoint());
        if (res != 0)
            return res;
        return Integer.compare(resourceCount,other.getResourceCount());
    }
}

