package it.polimi.ingsw.model.turn_taker;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.ThreadLocalCleanable;
import it.polimi.ingsw.model.solo_game.SoloToken;

public class Opponent implements TurnTaker, ThreadLocalCleanable {
    private Deck<SoloToken> soloTokens;
    public static String USERNAME = "Lorenzo Il Magnifico";
    private static final ThreadLocal<Opponent> instance = ThreadLocal.withInitial(Opponent::new);
    public int personalVictoryPoints;
    /**
     * Initializes the opponent using appropriate settings
     */
    public Opponent() {
        this.soloTokens = new Deck(Settings.getInstance().getSoloTokens());
    }

    public static Opponent getInstance(){
        return instance.get();
    }

    /**
     * Plays the solo game card taken and add it to the deck of discard cards
     */
    @Override
    public void playTurn(){
        soloTokens.drawAndPutBelowFirst().get().use();
    }

    @Override
    public void addPersonalVictoryPoints(int victoryPoints) {
        personalVictoryPoints+=victoryPoints;
    }

    @Override
    public String getUsername(){
        return Opponent.USERNAME;
    }

    @Override
    public TurnTakerScore computeScore() {
        return new TurnTakerScore(0,0);
    }

    @Override
    public String getFaithId() {
        return getUsername();
    }

    public Deck<SoloToken> getSoloTokens() { return soloTokens; }

    /**
     * Puts together the discard cards and  the cards that aren't discarded yet.
     */
    public Deck<SoloToken> resetDecks() {
        soloTokens = new Deck<>(Settings.getInstance().getSoloTokens());
        soloTokens.shuffle();
        return soloTokens;
    }

    @Override
    public void clean() {
        instance.remove();
    }
}
