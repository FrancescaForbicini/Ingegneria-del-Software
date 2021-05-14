package it.polimi.ingsw.model;

import org.junit.Test;

import static org.junit.Assert.*;

public class SettingsTest {
    private Settings settings;
    @Test
    public void testLoad(){
        settings = Settings.load();
        settings.print();//TODO fix appearance
    }

}