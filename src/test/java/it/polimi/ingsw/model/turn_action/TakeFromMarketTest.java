package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.market.MarketAxis;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TakeFromMarketTest {
    private ArrayList<Marble> marbles;
    private MarketAxis marketAxis;
    private int line;
    private Map<ResourceType, ArrayList<Integer>> resourceToDepot;
    private Player player;
    private ArrayList<ResourceType> whiteMarbleChosen;
    private ArrayList<ResourceType> resourceChosen;
    private ArrayList<Integer> depotsChosen;
    private TakeFromMarket takeFromMarket;
    private Game game;

    @Before
    public void setUp() throws Exception {
        game = Game.getInstance();
        player = new Player("username");
        player.loadFromSettings();
        depotsChosen = new ArrayList<>();
        resourceToDepot = new HashMap<>();
        whiteMarbleChosen = new ArrayList<>();
        resourceChosen = new ArrayList<>();
        marketAxis = MarketAxis.ROW;
        line = 2;
        //creates market
        marbles = new ArrayList<>();
        Marble marble = new Marble(MarbleType.White);
        marbles.add(marble);
        marble = new Marble(MarbleType.White);
        marbles.add(marble);
        marble = new Marble(MarbleType.Purple);
        marbles.add(marble);
        marble = new Marble(MarbleType.Red);
        marbles.add(marble);
        marble = new Marble(MarbleType.Grey);
        marbles.add(marble);
        marble = new Marble(MarbleType.Blue);
        marbles.add(marble);
        marble = new Marble(MarbleType.Purple);
        marbles.add(marble);
        marble = new Marble(MarbleType.Grey);
        marbles.add(marble);
        marble = new Marble(MarbleType.Red);
        marbles.add(marble);
        marble = new Marble(MarbleType.Yellow);
        marbles.add(marble);
        marble = new Marble(MarbleType.White);
        marbles.add(marble);
        marble = new Marble(MarbleType.Yellow);
        marbles.add(marble);
        marble = new Marble(MarbleType.Blue);
        marbles.add(marble);
        game.addPlayer("username");
        game.getMarket().setActualMarket(marbles);
    }


    @Test
    public void takeResourcesFromMarket(){
        depotsChosen = new ArrayList<>();
        depotsChosen.add(1);
        resourceToDepot.put(ResourceType.Shields,depotsChosen);
        depotsChosen = new ArrayList<>();
        depotsChosen.add(2);
        depotsChosen.add(2);
        resourceToDepot.put(ResourceType.Stones,depotsChosen);
        depotsChosen = new ArrayList<>();
        depotsChosen.add(3);
        resourceToDepot.put(ResourceType.Servants,depotsChosen);
        takeFromMarket = new TakeFromMarket(marketAxis,line,resourceToDepot,whiteMarbleChosen);
        takeFromMarket.play(player);
        assertEquals(player.getWarehouse().getDepot(1).get().getResourceType(),ResourceType.Shields);
        assertEquals(player.getWarehouse().getDepot(2).get().getResourceType(),ResourceType.Stones);
        assertEquals(player.getWarehouse().getDepot(3).get().getResourceType(),ResourceType.Servants);
    }

    @Test
    public void takeResourcesFromMarketWithOneWhiteMarbleConversion(){
        marketAxis = MarketAxis.COL;
        int line = 3;
        player.getActiveWhiteConversions().add(ResourceType.Stones);
        whiteMarbleChosen.add(ResourceType.Stones);
        depotsChosen = new ArrayList<>();
        depotsChosen.add(2);
        depotsChosen.add(2);
        resourceToDepot.put(ResourceType.Servants,depotsChosen);
        depotsChosen = new ArrayList<>();
        depotsChosen.add(3);
        resourceToDepot.put(ResourceType.Stones,depotsChosen);
        takeFromMarket = new TakeFromMarket(marketAxis,line,resourceToDepot,whiteMarbleChosen);
        takeFromMarket.play(player);
        assertEquals(player.getWarehouse().getDepot(1).get().getResourceType(),ResourceType.Any);
        assertEquals(player.getWarehouse().getDepot(2).get().getResourceType(),ResourceType.Servants);
        assertEquals(player.getWarehouse().getDepot(3).get().getResourceType(),ResourceType.Stones);
    }


    @Test
    public void takeResourcesFromMarketWithTwoWhiteMarbleConversion(){
        //player has two conversions but activates only one
        marketAxis = MarketAxis.COL;
        line = 3;
        player.getActiveWhiteConversions().add(ResourceType.Stones);
        player.getActiveWhiteConversions().add(ResourceType.Coins);
        whiteMarbleChosen.add(ResourceType.Coins);
        depotsChosen = new ArrayList<>();
        depotsChosen.add(1);
        resourceToDepot.put(ResourceType.Coins,depotsChosen);
        depotsChosen = new ArrayList<>();
        depotsChosen.add(2);
        depotsChosen.add(2);
        resourceToDepot.put(ResourceType.Servants,depotsChosen);
        takeFromMarket = new TakeFromMarket(marketAxis,line,resourceToDepot,whiteMarbleChosen);
        takeFromMarket.play(player);
        assertEquals(player.getWarehouse().getDepot(1).get().getResourceType(),ResourceType.Coins);
        assertEquals(player.getWarehouse().getDepot(2).get().getResourceType(),ResourceType.Servants);
        assertEquals(player.getWarehouse().getDepot(3).get().getResourceType(),ResourceType.Any);
    }

    @Test
    public void takeResourcesFromMarketWithTwoWhiteMarbleConversionAndUsesBoth(){
        //player has two conversions and activates both and there is a red marble
        //that moves the player of one step
        FaithTrack faithTrack = game.getFaithTrack();
        marketAxis = MarketAxis.ROW;
        line = 1;
        player.getActiveWhiteConversions().add(ResourceType.Stones);
        player.getActiveWhiteConversions().add(ResourceType.Coins);
        whiteMarbleChosen.add(ResourceType.Coins);
        whiteMarbleChosen.add(ResourceType.Stones);
        depotsChosen = new ArrayList<>();
        depotsChosen.add(1);
        resourceToDepot.put(ResourceType.Coins,depotsChosen);
        depotsChosen = new ArrayList<>();
        depotsChosen.add(2);
        resourceToDepot.put(ResourceType.Servants,depotsChosen);
        depotsChosen = new ArrayList<>();
        depotsChosen.add(3);
        resourceToDepot.put(ResourceType.Stones,depotsChosen);
        takeFromMarket = new TakeFromMarket(marketAxis,line,resourceToDepot,whiteMarbleChosen);
        takeFromMarket.play(player);
        faithTrack.move(player,1);
        assertEquals(player.getWarehouse().getDepot(1).get().getResourceType(),ResourceType.Coins);
        assertEquals(player.getWarehouse().getDepot(2).get().getResourceType(),ResourceType.Servants);
        assertEquals(player.getWarehouse().getDepot(3).get().getResourceType(),ResourceType.Stones);
        assertEquals(game.getFaithTrack().getPosition(player),faithTrack.getPosition(player));
    }
    @After
    public void clean(){
        Game.getInstance().clean();
    }
}