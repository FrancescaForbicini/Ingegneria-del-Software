package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class SettingsTest {
    private Settings settings;
    @Test
    public void testLoad(){
        settings = Settings.load();
        settings.print();//TODO fix appearance
    }

}