package it.polimi.ingsw.model.turn_taker;

import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.ThreadLocalCleanable;
import it.polimi.ingsw.model.solo_game.SoloToken;
import it.polimi.ingsw.view.VirtualView;

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
     * Plays the solo game card taken and add it to the deck of discard cards
     */
    @Override
    public void playTurn(){
        SoloToken pickedSoloToken = soloTokens.drawAndPutBelowFirst().get();
        lastAction = pickedSoloToken.toString();
        pickedSoloToken.use();
        VirtualView.getInstance().notifyGameData();
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
        return new TurnTakerScore(winner);
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

    public String getLastAction() {
        return lastAction;
    }

    public void setWinner() {
        this.winner = true;
    }


    @Override
    public void clean() {
        instance.remove();
    }
}
