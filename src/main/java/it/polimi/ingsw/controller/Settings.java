package it.polimi.ingsw.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSerializer;
import it.polimi.ingsw.client.turn_taker.ClientTurnTaker;
import it.polimi.ingsw.controller.adapter.ClientTurnTakerAdapter;
import it.polimi.ingsw.controller.adapter.LeaderCardAdapter;
import it.polimi.ingsw.controller.adapter.RequirementAdapter;
import it.polimi.ingsw.controller.adapter.SoloTokenAdapter;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.Cell;
import it.polimi.ingsw.model.faith.CellGroup;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.requirement.Requirement;
import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.solo_game.SoloToken;

import java.io.*;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

public class Settings {
    private static final String SETTINGS_PATH = "etc/settings.json";
    private static final String CUSTOM_SETTINGS_PATH_TEMPLATE = "etc/settings_%s.json";
    private final static Logger LOGGER = Logger.getLogger(Settings.class.getName());
    private Settings settings;
    private int joinTimeout;
    private static final ThreadLocal<Settings> instance = ThreadLocal.withInitial(Settings::load);
    private static Gson gson;

    private ArrayList<Marble> marbles;
    private ArrayList<DevelopmentCard> developmentCards;
    private ArrayList<LeaderCard> leaderCards;
    private ArrayList<Cell> cells;
    private ArrayList<SoloToken> soloTokens;
    private ArrayList<CellGroup> groups;
    private TradingRule basicProduction;

    public ArrayList<SoloToken> getSoloTokens() {
        return soloTokens;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public TradingRule getBasicProduction() {
        return basicProduction;
    }

    public int getJoinTimeout() {
        return joinTimeout;
    }

    public ArrayList<Cell> getCells() {
        return cells;
    }

    public ArrayList<CellGroup> getGroups() {
        return groups;
    }

    public static Settings getInstance() {
        return instance.get();
    }

    public static Settings load() {
        String theadName = Thread.currentThread().getName();
        LOGGER.info(String.format("Loading Settings for thread: %s", theadName));
        String settingsFilePath = String.format(CUSTOM_SETTINGS_PATH_TEMPLATE, theadName);
        File settingsFile = new File(settingsFilePath);
        if (!settingsFile.exists()) {
            LOGGER.info("Custom settings not provided. Loading Settings with 'default' rules");
            settingsFile = new File(SETTINGS_PATH);
        } else {
            LOGGER.info("Found custom settings: loading custom Settings");
        }

        try {
            return getGson().fromJson(new FileReader(settingsFile), Settings.class);
        } catch (FileNotFoundException e) {
            LOGGER.warning("FATAL. No game settings found: game thread interrupted");
            Thread.currentThread().interrupt();
            return null;
        }
    }

    public static void writeCustomSettings(Optional<Settings> customSettings){
        if (customSettings.isEmpty())
            return;
        String threadName = Thread.currentThread().getName();
        try {
            FileWriter fw = new FileWriter(String.format(CUSTOM_SETTINGS_PATH_TEMPLATE, threadName));
            Settings.getGson().toJson(customSettings.get(), fw);
        } catch (IOException e) {
            LOGGER.warning("FATAL. Error writing custom settings: game thread interrupted");
            Thread.currentThread().interrupt();
        }
    }

    public ArrayList<Marble> getMarbles(){
        return marbles;
    }


    public void print(){//just for test
        System.out.println("\nmarble " + marbles.size() + "\ndev " + developmentCards.size() +"\ncell " +  cells.size() + "\ngg " + groups.size());
    }

    public static Gson getGson(){
        if (gson == null) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            JsonSerializer<LeaderCard> leaderCardJsonSerializer = new LeaderCardAdapter();
            gsonBuilder.registerTypeAdapter(LeaderCard.class, leaderCardJsonSerializer);

            JsonSerializer<Requirement> requirementJsonSerializer = new RequirementAdapter();
            gsonBuilder.registerTypeAdapter(Requirement.class, requirementJsonSerializer);

            JsonSerializer<ClientTurnTaker> clientTurnTakerJsonSerializer = new ClientTurnTakerAdapter();
            gsonBuilder.registerTypeAdapter(ClientTurnTaker.class, clientTurnTakerJsonSerializer);

            JsonSerializer<SoloToken> soloTokenAdapterJsonSerializer = new SoloTokenAdapter();
            gsonBuilder.registerTypeAdapter(SoloToken.class, soloTokenAdapterJsonSerializer);

            gson = gsonBuilder.create();
        }

        return gson;
    }
}
