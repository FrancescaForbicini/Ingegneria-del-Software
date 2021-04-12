package it.polimi.ingsw.model.turn_taker;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.solo_game.SoloTokenStrategy;

public class Opponent implements TurnTaker{
    private Deck<SoloTokenStrategy> soloTokens;
    private Deck<SoloTokenStrategy> discardedSoloTokens;

    // TODO test in a real multi thread env
    private static ThreadLocal<Opponent> instance = ThreadLocal.withInitial(() -> new Opponent());

    /**
     * Initializes the opponent using appropriate settings
     */
    private Opponent() {
        // TODO properly initialize with SETTINGS (set solotokens)
        discardedSoloTokens = new Deck<>();
    }

    /**
     * Returns the thread local singleton instance
     */
    public static Opponent getInstance() {
        return instance.get();
    }

    @Override
    public void playTurn() {
        // TODO
    }

    public Deck<SoloTokenStrategy> getDiscardedSoloTokens() {
        return discardedSoloTokens;
    }

    public Deck<SoloTokenStrategy> getSoloTokens() {
        return soloTokens;
    }

    public void setDiscardedSoloTokens(Deck<SoloTokenStrategy> discardedSoloTokens) {
        this.discardedSoloTokens = discardedSoloTokens;
    }

    public void setSoloTokens(Deck<SoloTokenStrategy> soloTokens) {
        this.soloTokens = soloTokens;
    }

    public void resetDecks() {
        // TODO reset soloTokens using settings
        discardedSoloTokens = new Deck<>();
    }
}
