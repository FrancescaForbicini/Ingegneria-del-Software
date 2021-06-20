package it.polimi.ingsw.model.turn_taker;

import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.solo_game.SoloToken;

public class Opponent implements TurnTaker{
    private Deck<SoloToken> soloTokens;
    private Deck<SoloToken> discardedSoloTokens;
    private int victoryPoints; // TODO remove add victory points??

    /**
     * Initializes the opponent using appropriate settings
     */
    public Opponent() {
//        discardedSoloTokens = new Deck<>();
//        soloTokens = Game.getInstance().getSoloToken();
    }

    /**
     * Plays the solo game card taken and add it to the deck of discard cards
     */
    @Override
    public void playTurn(){
        SoloToken card;
        card = soloTokens.drawFirstCard().get();
        card.use( this);
        discardedSoloTokens.addCard(card);
    }

    /**
     * Adds victory Points
     * @param victoryPoints the amount of victory points to add
     */
    @Override
    public void addPersonalVictoryPoints(int victoryPoints){
        this.victoryPoints+=victoryPoints;
    }


    @Override
    public String getUsername(){
        return "Lorenzo Il Magnifico";
    }

    @Override
    public TurnTakerScore computeScore() {
        return new TurnTakerScore(0,0);
    }

    public int getVictoryPoints(){ return this.victoryPoints; }

    public Deck<SoloToken> getDiscardedSoloTokens() { return discardedSoloTokens; }

    public Deck<SoloToken> getSoloTokens() { return soloTokens; }

    /**
     * Puts together the discard cards and  the cards that aren't discarded yet.
     */
    public Deck<SoloToken> resetDecks() {
        soloTokens.addAll(discardedSoloTokens);
        return soloTokens;
    }

    public void setSoloTokens(Deck<SoloToken> soloTokens) {
        this.soloTokens = soloTokens;
    }

    public void setDiscardedSoloTokens(Deck<SoloToken> discardedSoloTokens) {
        this.discardedSoloTokens = discardedSoloTokens;
    }

    public void setVictoryPoints(int victoryPoints) {
        this.victoryPoints = victoryPoints;
    }
}
