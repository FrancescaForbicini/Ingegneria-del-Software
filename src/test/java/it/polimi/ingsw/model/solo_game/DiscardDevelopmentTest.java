package it.polimi.ingsw.model.solo_game;

import it.polimi.ingsw.model.cards.DevelopmentCardColumn;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.requirement.DevelopmentColor;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;


public class DiscardDevelopmentTest {
    private Game game;
    private DiscardDevelopment discardDevelopment;
    private DevelopmentCardColumn[] developmentCardColumn;
    private ArrayList<DevelopmentCard> developmentCards;
    private DevelopmentColor colorToRemove;

    @Before
    public void setUp(){
        game = Game.getInstance();
        colorToRemove = DevelopmentColor.Blue;
        discardDevelopment = new DiscardDevelopment(colorToRemove,2);
        developmentCardColumn = game.getDevelopmentCardColumns();
        developmentCards = new ArrayList<>();
    }

    @Test
    public void discardDevelopment(){
        int column = 0;
        for (int i = 0; i < 4; i++) {
            if (developmentCardColumn[i].getColor().equals(colorToRemove)) {
                developmentCards.addAll(developmentCardColumn[i].getVisibleCards());
                column = i;
                break;
            }
        }
        developmentCardColumn[column].removeIfPresent(developmentCards.get(0));
        developmentCardColumn[column].removeIfPresent(developmentCards.get(1));
        discardDevelopment.use();
        assertEquals(developmentCardColumn.length,game.getDevelopmentCardColumns().length);
    }
    @After
    public void clean(){
        Game.getInstance().clean();
    }

}