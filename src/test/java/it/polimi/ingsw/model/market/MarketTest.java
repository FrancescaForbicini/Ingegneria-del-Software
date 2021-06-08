package it.polimi.ingsw.model.market;

import it.polimi.ingsw.message.action_message.market_message.MarketAxis;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class MarketTest {
    private Market market;
    private MarketAxis marketAxis;
    private int num;
    private ArrayList<MarbleType> marbleTypes;
    private ArrayList<MarbleType> marbleTaken;
    private Marble extraMarble;
    @Before
    public void setUp(){
        ArrayList<Marble> marbles = new ArrayList<>();
        marbles.add(new Marble (MarbleType.Yellow));
        marbles.add(new Marble(MarbleType.White));
        marbles.add(new Marble(MarbleType.White));
        marbles.add(new Marble(MarbleType.White));
        marbles.add(new Marble(MarbleType.White));
        marbles.add(new Marble(MarbleType.Red));
        marbles.add(new Marble(MarbleType.Blue));
        marbles.add(new Marble(MarbleType.Blue));
        marbles.add(new Marble(MarbleType.Grey));
        marbles.add(new Marble(MarbleType.Grey));
        marbles.add(new Marble(MarbleType.Purple));
        marbles.add(new Marble(MarbleType.Purple));
        marbles.add(new Marble(MarbleType.Yellow));
        market = new Market(marbles);
        marbleTypes = new ArrayList<>();
        marbleTaken = new ArrayList<>();
        num = 0;
        marketAxis = null;
    }

    @Test
    public void testGetMarblesFromRightRow() {
        marketAxis = MarketAxis.ROW;
        num = 2;
        marbleTaken.addAll(market.getRow(num));
        marbleTypes = market.getMarblesFromLine(marketAxis,num);
        assertEquals(marbleTypes,marbleTaken);
    }
    @Test
    public void testGetMarblesFromRightColumn() {
        marketAxis = MarketAxis.COL;
        num = 2;
        marbleTaken.addAll(market.getColumn(num));
        marbleTypes = market.getMarblesFromLine(marketAxis,num);
        assertEquals(marbleTypes,marbleTaken);
    }
    @Test
    public void testGetMarblesFromWrongRow(){
        marketAxis = MarketAxis.ROW;
        num = 5;
        try{
            marbleTypes = market.getMarblesFromLine(marketAxis,num);
        }catch(IndexOutOfBoundsException ignored){
        }
        assertEquals(marbleTypes.size(),0);
    }
    @Test
    public void testGetMarblesFromWrongColumn(){
        marketAxis = MarketAxis.COL;
        num = 5;
        try{
            marbleTypes = market.getMarblesFromLine(marketAxis,num);
        }catch(IndexOutOfBoundsException ignored){
        }
        assertEquals(marbleTypes.size(),0);
    }

    @Test
    public void testUpdateMarketColumn(){
        marketAxis = MarketAxis.COL;
        num = 3;
        marbleTypes = market.getMarblesFromLine(marketAxis,num);
        extraMarble = market.getExtraMarble();

        assertEquals(marbleTypes.get(marbleTypes.size()-1),extraMarble.getType());
    }
    @Test
    public void testUpdateMarketRow(){
        marketAxis = MarketAxis.ROW;
        num = 2;
        marbleTypes = market.getMarblesFromLine(marketAxis,num);
        extraMarble = market.getExtraMarble();
        assertEquals(marbleTypes.get(marbleTypes.size()-1),extraMarble.getType());
    }
    @Test
    public void testGetMarket(){
        System.out.println(market.toString());
        System.out.println(market.getMarket());
    }

 }