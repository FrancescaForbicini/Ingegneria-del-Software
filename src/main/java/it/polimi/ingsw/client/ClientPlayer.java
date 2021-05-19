package it.polimi.ingsw.client;

import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientPlayer {
    private String username;
    private List<LeaderCard> activeLeaderCards;
    private int numberOfNonActiveCards;
    private Warehouse warehouse;
    private Map<ResourceType, Integer> strongbox;
    private ArrayList<DevelopmentSlot> developmentSlots;

    public List<LeaderCard> getActiveLeaderCards() {
        return activeLeaderCards;
    }

    public void setActiveLeaderCards(List<LeaderCard> activeLeaderCards) {
        this.activeLeaderCards = activeLeaderCards;
    }

    public int getNumberOfNonActiveCards() {
        return numberOfNonActiveCards;
    }

    public String getUsername(){
        return this.username;
    }

    public void setNumberOfNonActiveCards(int numberOfNonActiveCards) {
        this.numberOfNonActiveCards = numberOfNonActiveCards;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public Map<ResourceType, Integer> getStrongbox() {
        return strongbox;
    }

    public void setStrongbox(Map<ResourceType, Integer> strongbox) {
        this.strongbox = strongbox;
    }

    public ArrayList<DevelopmentSlot> getDevelopmentSlots() {
        return developmentSlots;
    }

    public void setDevelopmentSlots(ArrayList<DevelopmentSlot> developmentSlots) {
        this.developmentSlots = developmentSlots;
    }
}
