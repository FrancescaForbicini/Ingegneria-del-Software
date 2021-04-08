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
    private final Market market = new Market();
    @Before
    public void setUp(){
        ArrayList<Marble> marbles = new ArrayList<>();
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
        Collections.shuffle(marbles);
        market.setMarket(marbles);

        Marble extra = new Marble (MarbleType.Yellow);
        market.setExtraMarble(extra);

        market.shortPrint();
    }

    @Test
    public void testGetMarblesFromLine() {
        Random rnd = new Random();
        String rc;
        int num;
        if (rnd.nextBoolean()) {
            rc = "Column";
            num = rnd.nextInt(4)+1;
        } else {
            rc = "Row";
            num = rnd.nextInt(3)+1;
        }
        System.out.println(rc + " number: " + num);
        Collection<MarbleType> marbleTypes = market.getMarblesFromLine(rc,num);
        market.shortPrint();
        System.out.println(marbleTypes.toString());
    }
}