package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.market.Market;
import org.junit.Test;

import java.util.ArrayList;

public class SettingsTest {
    private Settings settings;
    @Test
    public void testLoad(){
        settings = Settings.load();
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
        Market market = new Market(marbles);
        settings.print();
    }

}