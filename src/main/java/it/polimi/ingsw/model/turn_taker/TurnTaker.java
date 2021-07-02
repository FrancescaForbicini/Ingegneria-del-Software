package it.polimi.ingsw.model.turn_taker;

/**
 * An entity that can take a turn in a game
 */
public interface TurnTaker {

    /**
     * Plays the turn
     */
    void playTurn();

    /**
     * Adds victory points to this TurnTaker
     *
     * @param victoryPoints to add
     */
    void addPersonalVictoryPoints(int victoryPoints);

    /**
     * Gets the username of this, for the Opponent is by default "Lorenzo Il Magnifico"
     *
     * @return username
     */
    String getUsername();

    /**
     * Compute the TurnTakerScore used to determine the winner
     *
     * @return computed TurnTakerScore
     */
    TurnTakerScore computeScore();
}
