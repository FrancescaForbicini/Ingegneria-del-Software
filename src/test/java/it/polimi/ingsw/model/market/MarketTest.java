package it.polimi.ingsw.model.market;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Random;
import java.util.logging.Logger;

import static org.junit.Assert.*;

public class MarketTest {
    private Market market;
    private String rc;
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
        rc = null;
    }

    @Test
    public void testGetMarblesFromRightRow() {
        rc = "row";
        num = 2;
        marbleTaken.addAll(market.getRow(num));
        marbleTypes = market.getMarblesFromLine(rc,num);
        assertEquals(marbleTypes,marbleTaken);
    }
    @Test
    public void testGetMarblesFromRightColumn() {
        rc = "column";
        num = 2;
        marbleTaken.addAll(market.getColumn(num));
        marbleTypes = market.getMarblesFromLine(rc,num);
        assertEquals(marbleTypes,marbleTaken);
    }
    @Test
    public void testGetMarblesFromWrongRow(){
        rc = "row";
        num = 5;
        try{
            marbleTypes = market.getMarblesFromLine(rc,num);
        }catch(IndexOutOfBoundsException ignored){
        }
        assertEquals(marbleTypes.size(),0);
    }
    @Test
    public void testGetMarblesFromWrongColumn(){
        rc = "column";
        num = 5;
        try{
            marbleTypes = market.getMarblesFromLine(rc,num);
        }catch(IndexOutOfBoundsException ignored){
        }
        assertEquals(marbleTypes.size(),0);
    }

    @Test
    public void testUpdateMarketColumn(){
        rc = "column";
        num = 3;
        marbleTypes = market.getMarblesFromLine(rc,num);
        extraMarble = market.getExtraMarble();

        assertEquals(marbleTypes.get(marbleTypes.size()-1),extraMarble.getType());
    }
    @Test
    public void testUpdateMarketRow(){
        rc = "row";
        num = 2;
        marbleTypes = market.getMarblesFromLine(rc,num);
        extraMarble = market.getExtraMarble();
        assertEquals(marbleTypes.get(marbleTypes.size()-1),extraMarble.getType());
    }
    @Test
    public void testGetMarket(){
        System.out.println(market.toString());
        System.out.println(market.getMarket());
    }

 }