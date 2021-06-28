package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.cards.AssignWhiteMarble;
import it.polimi.ingsw.model.cards.Discount;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.requirement.RequirementResource;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

public class ActivateLeaderCardTest {
    private Discount discount;
    private Collection<Requirement> requirements;
    private Player player;
    private ActivateLeaderCard activateLeaderCard;

    @Before
    public void setUp(){
        requirements = new ArrayList<>();
        requirements.add(new RequirementResource(2,ResourceType.Coins));
        discount = new Discount(2,ResourceType.Stones,1,requirements,"");
        player = new Player("username");
        player.loadFromSettings();
        activateLeaderCard = new ActivateLeaderCard(discount);
    }

    @Test
    public void canActivateALeaderCard(){
        //player has only one card
        player.getWarehouse().addResource(ResourceType.Coins,2,2);
        activateLeaderCard.play(player);
        assertEquals(player.getNonActiveLeaderCards().size(),0);
        assertEquals(player.getActiveLeaderCards().size(),1);
    }

    @Test
    public void canActivateOnlyOne(){
        //player has only one card and wants to activate only one card
        AssignWhiteMarble assignWhiteMarble = new AssignWhiteMarble(2,ResourceType.Servants,requirements,"");
        player.getNonActiveLeaderCards().add(assignWhiteMarble);
        player.getNonActiveLeaderCards().add(discount);
        player.getWarehouse().addResource(ResourceType.Coins,2,2);
        activateLeaderCard.play(player);
        assertEquals(player.getNonActiveLeaderCards().size(),1);
        assertEquals(player.getActiveLeaderCards().size(),1);
    }

    @Test
    public void canActivateBothCards(){
        //player has only one card and wants to activate only one card
        AssignWhiteMarble assignWhiteMarble = new AssignWhiteMarble(2,ResourceType.Servants,requirements,"");
        player.getNonActiveLeaderCards().add(assignWhiteMarble);
        player.getNonActiveLeaderCards().add(discount);
        player.getWarehouse().addResource(ResourceType.Coins,2,2);
        activateLeaderCard.play(player);
        assertEquals(player.getNonActiveLeaderCards().size(),1);
        assertEquals(player.getActiveLeaderCards().size(),1);
        activateLeaderCard = new ActivateLeaderCard(assignWhiteMarble);
        activateLeaderCard.play(player);
        assertEquals(player.getNonActiveLeaderCards().size(),0);
        assertEquals(player.getActiveLeaderCards().size(),2);
    }

}