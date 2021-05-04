package it.polimi.ingsw.model;

import com.google.gson.Gson;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCardStrategy;
import it.polimi.ingsw.model.faith.Cell;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.faith.GroupCell;
import it.polimi.ingsw.model.market.Marble;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO complete, clean-up
public class Settings {
    public static final String FILENAME = "settings.json";
    private Settings settings;
    //private static ThreadLocal<Settings> instance = ThreadLocal.withInitial(() -> new Settings());

    private ArrayList<Marble> marbles;
    private ArrayList<DevelopmentCard> developmentCards;
    private ArrayList<LeaderCardStrategy> leaderCards;
    private ArrayList<Cell> cells;
    private ArrayList<GroupCell> groups;
    private List<WarehouseDepot> warehouseDepots;
    private boolean soloGame;


    public Settings(String fileName){
        Gson gson = new Gson();
        try{
            settings = gson.fromJson(new FileReader(fileName + ".json"), Settings.class);
        }catch (IOException e1){
            try{
                settings = gson.fromJson(new FileReader(FILENAME), Settings.class);
            } catch (IOException e2){
                throw new RuntimeException("Settings file is missing");
            }
        }
    }

    public ArrayList<Marble> getMarbles(){
        return marbles;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public List<LeaderCardStrategy> getLeaderCards() {
        return leaderCards;
    }

    public FaithTrack getFaithTrack() {
        return new FaithTrack(cells,groups);
    }


    public List<WarehouseDepot> getWarehouseDepots() {
        return warehouseDepots;
    }

    public boolean isSoloGame() {
        return soloGame;
    }
}
