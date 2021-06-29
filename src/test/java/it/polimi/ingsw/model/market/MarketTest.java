package it.polimi.ingsw.model.market;

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
        for(int i=4;i<8;i++) {
            marbleTaken.add(market.getMarket().get(i).getType());
        }
        marbleTypes = market.getMarblesFromLine(marketAxis,num,false);
        assertEquals(marbleTypes,marbleTaken);
    }
    @Test
    public void testGetMarblesFromRightColumn() {
        marketAxis = MarketAxis.COL;
        num = 2;
        for(int i=1;i<10;i+=4) {
            marbleTaken.add(market.getMarket().get(i).getType());
        }
        marbleTypes = market.getMarblesFromLine(marketAxis,num,false);
        assertEquals(marbleTypes,marbleTaken);
    }
    @Test
    public void testGetMarblesFromWrongRow(){
        marketAxis = MarketAxis.ROW;
        num = 5;
        try{
            marbleTypes = market.getMarblesFromLine(marketAxis,num,false);
        }catch(IndexOutOfBoundsException ignored){
        }
        assertEquals(marbleTypes.size(),0);
    }
    @Test
    public void testGetMarblesFromWrongColumn(){
        marketAxis = MarketAxis.COL;
        num = 5;
        try{
            marbleTypes = market.getMarblesFromLine(marketAxis,num,false);
        }catch(IndexOutOfBoundsException ignored){
        }
        assertEquals(marbleTypes.size(),0);
    }

    @Test
    public void testUpdateMarketColumn(){
        marketAxis = MarketAxis.COL;
        num = 3;
        marbleTypes = market.getMarblesFromLine(marketAxis,num,true);
        extraMarble = market.getExtraMarble();
        assertEquals(marbleTypes.get(0),extraMarble.getType());
    }
    @Test
    public void testUpdateMarketRow(){
        marketAxis = MarketAxis.ROW;
        num = 2;
        marbleTypes = market.getMarblesFromLine(marketAxis,num,true);
        extraMarble = market.getExtraMarble();
        assertEquals(marbleTypes.get(0),extraMarble.getType());
    }

 }