package it.polimi.ingsw.model;

public class DiscardDevelopment implements SoloTokenStrategy {
    /**Used to discard a DevelopmentCard
     *
     */
    @Override
    public void use() {
        Game.getdevelopmentCardDecks().drawLastCard();
    }
}
