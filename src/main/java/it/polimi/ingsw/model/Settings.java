package it.polimi.ingsw.model;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

// TODO complete, clean-up
public class Settings {
    public static final String FILENAME = "settings.json";
    private static final Map<String,Settings> instances = new ConcurrentHashMap<>();
    /*
    private List<Marble> marbles;
    private List<DevelopmentCard> developmentCards;
    private List<LeaderCard> leaderCards;
    private FaithTrack faithTrack;
    private List<WarehouseDepot> warehouseDepots;
    //*/
    public static Settings getInstance() {
        String threadID = Thread.currentThread().getName();
        if(instances.get(threadID)==null){
            Gson gson = new Gson();
            try{
                instances.put(threadID,gson.fromJson(new FileReader("settings"+threadID+".json"), Settings.class));
            }catch (IOException e1){
                try{
                    instances.put(threadID,gson.fromJson(new FileReader(FILENAME), Settings.class));
                } catch (IOException e2){
                    throw new RuntimeException("Settings file is missing");
                }
            }
        }
        return instances.get(threadID);



    }
/*
    public List<Marble> getMarbles(){
        return marbles;
    }

    public List<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }

    public List<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }


    public List<WarehouseDepot> getWarehouseDepots() {
        return warehouseDepots;
    }
*/
}
