package it.polimi.ingsw.model.turn_taker;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.cards.Deck;
import it.polimi.ingsw.model.ThreadLocalCleanable;
import it.polimi.ingsw.model.solo_game.SoloToken;
import it.polimi.ingsw.view.VirtualView;

/**
 * Representation of adversary during Solo Game
 */
public class Opponent implements TurnTaker, ThreadLocalCleanable {
    public static String USERNAME = "Lorenzo Il Magnifico";
    private Deck<SoloToken> soloTokens;
    private static final ThreadLocal<Opponent> instance = ThreadLocal.withInitial(Opponent::new);
    private int personalVictoryPoints;
    private boolean winner;
    private String lastAction;

    /**
     * Initializes the opponent using appropriate settings
     */
    private Opponent() {
        this.soloTokens = new Deck(Settings.getInstance().getSoloTokens());
        winner = false;
    }

    public static Opponent getInstance(){
        return instance.get();
    }

    /**
     * {@inheritDoc}
     *
     * Plays the solo game card taken and add it to the deck of discard cards
     */
    @Override
    public void playTurn(){
        SoloToken pickedSoloToken = soloTokens.drawAndPutBelowFirst().get();
        lastAction = pickedSoloToken.toString();
        pickedSoloToken.use();
        VirtualView.getInstance().notifyGameData();
    }

    /**
     * {@inheritDoc}
     *
     * @param victoryPoints to be added, Opponent's victory points are not used
     */
    @Override
    public void addPersonalVictoryPoints(int victoryPoints) {
    }

    @Override
    public String getUsername(){
        return Opponent.USERNAME;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public TurnTakerScore computeScore() {
        return new TurnTakerScore(winner);
    }

    public Deck<SoloToken> getSoloTokens() { return soloTokens; }

    /**
     * Puts together the discard cards and the cards that aren't discarded yet.
     *
     * @return reset deck
     */
    public Deck<SoloToken> resetDeck() {
        soloTokens = new Deck<>(Settings.getInstance().getSoloTokens());
        soloTokens.shuffle();
        return soloTokens;
    }

    public String getLastAction() {
        return lastAction;
    }

    public void setWinner() {
        this.winner = true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void clean() {
        instance.remove();
    }
}
