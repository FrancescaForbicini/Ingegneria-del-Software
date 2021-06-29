package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.leader_cards.AssignWhiteMarble;
import it.polimi.ingsw.model.cards.leader_cards.Discount;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class DiscardLeaderCardTest {
    private Discount discount;
    private Collection<Requirement> requirements;
    private Player player;
    private DiscardLeaderCard discardLeaderCard;

    @Before
    public void setUp(){
        requirements = new ArrayList<>();
        requirements.add(new RequirementResource(2,ResourceType.Coins));
        discount = new Discount(2,ResourceType.Stones,1,requirements,"");
        player = new Player("username");
        discardLeaderCard = new DiscardLeaderCard(discount);
    }

    @Test
    public void discardALeaderCard(){
        //player has only one card and wants to discard it
        discardLeaderCard.play(player);
        assertEquals(player.getNonActiveLeaderCards().size(),0);
        assertEquals(player.getActiveLeaderCards().size(),0);
    }

    @Test
    public void discardOnlyOne(){
        //player has only one card and wants to discard only one card
        AssignWhiteMarble assignWhiteMarble = new AssignWhiteMarble(2,ResourceType.Servants,requirements,"");
        player.getNonActiveLeaderCards().add(assignWhiteMarble);
        player.getNonActiveLeaderCards().add(discount);
        discardLeaderCard.play(player);
        assertEquals(player.getNonActiveLeaderCards().size(),1);
        assertEquals(player.getActiveLeaderCards().size(),0);
    }

    @Test
    public void discardBothCards(){
        //player has only one card and wants to activate only one card
        AssignWhiteMarble assignWhiteMarble = new AssignWhiteMarble(2,ResourceType.Servants,requirements,"");
        player.getNonActiveLeaderCards().add(assignWhiteMarble);
        player.getNonActiveLeaderCards().add(discount);
        discardLeaderCard.play(player);
        assertEquals(player.getNonActiveLeaderCards().size(),1);
        assertEquals(player.getActiveLeaderCards().size(),0);
        discardLeaderCard = new DiscardLeaderCard(assignWhiteMarble);
        discardLeaderCard.play(player);
        assertEquals(player.getNonActiveLeaderCards().size(),0);
        assertEquals(player.getActiveLeaderCards().size(),0);
    }

}