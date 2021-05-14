package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.Cell;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.faith.CellGroup;
import it.polimi.ingsw.model.market.Marble;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

// TODO complete, clean-up
public class Settings {
    private static final String SETTINGS_PATH = "etc/settings.json";
    private static final String CUSTOM_SETTINGS_PATH_TEMPLATE = "etc/settings_%s.json";
    private final static Logger LOGGER = Logger.getLogger(Settings.class.getName());
    private Settings settings;
    private int maxPlayers;
    private int joinTimeout;
    private static final ThreadLocal<Settings> instance = ThreadLocal.withInitial(Settings::load);

    private ArrayList<Marble> marbles;
    private ArrayList<DevelopmentCard> developmentCards;
    //private ArrayList<LeaderCard> leaderCards;
    private ArrayList<Cell> cells;
    private ArrayList<CellGroup> groups;
    private boolean soloGame;


    public int getMaxPlayers() {
        return maxPlayers;
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
        String settingsFilePath = String.format(CUSTOM_SETTINGS_PATH_TEMPLATE, theadName); // TODO document this convention
        File settingsFile = new File(settingsFilePath);
        if (!settingsFile.exists()) {
            LOGGER.info("Custom settings not provided. Loading Settings with 'default' rules");
            settingsFile = new File(SETTINGS_PATH);
        } else {
            LOGGER.info("Found custom settings: loading custom Settings");
        }

        try {
            return new Gson().fromJson(new FileReader(settingsFile), Settings.class);
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
            new Gson().toJson(customSettings.get(), fw);
        } catch (IOException e) {
            LOGGER.warning("FATAL. Error writing custom settings: game thread interrupted");
            Thread.currentThread().interrupt();
        }
    }

    public ArrayList<Marble> getMarbles(){
        return marbles;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    /*public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }*/

    public FaithTrack getFaithTrack() {
        return new FaithTrack(cells,groups);
    }

    public boolean isSoloGame() {
        return soloGame;
    }

    public void print(){//just for test
        System.out.println("\nmarble " + marbles.size() + "\ndev " + developmentCards.size() +"\ncell " +  cells.size() + "\ngg " + groups.size());
    }
}
