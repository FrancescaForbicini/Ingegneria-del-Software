package it.polimi.ingsw.client;

import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ClientPlayer {
    private boolean opponent;
    private final String username;
    private List<LeaderCard> activeLeaderCards;
    private int numberOfNonActiveLeaderCards;
    private Warehouse warehouse;
    private Map<ResourceType, Integer> strongbox;
    private DevelopmentSlot[] developmentSlots;
    private FaithTrack faithTrack;

    public ClientPlayer(String username) {
        this.username = username;
    }

    public boolean isOpponent() {
        return opponent;
    }

    public void setOpponent(boolean opponent) {
        this.opponent = opponent;
    }


    public List<LeaderCard> getActiveLeaderCards() {
        return activeLeaderCards;
    }

    public void setActiveLeaderCards(List<LeaderCard> activeLeaderCards) {
        this.activeLeaderCards = activeLeaderCards;
    }

    public int getNumberOfNonActiveLeaderCards() {
        return numberOfNonActiveLeaderCards;
    }

    public String getUsername(){
        return this.username;
    }

    public void setNumberOfNonActiveLeaderCards(int numberOfNonActiveLeaderCards) {
        this.numberOfNonActiveLeaderCards = numberOfNonActiveLeaderCards;
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

    public DevelopmentSlot[] getDevelopmentSlots() {
        return developmentSlots;
    }

    public void setDevelopmentSlots(DevelopmentSlot[] developmentSlots) {
        this.developmentSlots = developmentSlots;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public void setFaithTrack(FaithTrack faithTrack) {
        this.faithTrack = faithTrack;
    }

    @Override
    public String toString(){
        ArrayList<DevelopmentSlot> arrayListSlots = new ArrayList<>(Arrays.asList(developmentSlots));
        StringBuilder print = new StringBuilder();
        print.append(username).append("\n");
        print.append(faithTrack.toString());
        for(DevelopmentSlot slot : arrayListSlots){
            print.append(slot.toString()).append("\n");
        }
        print.append(warehouse.toString()).append("\n");
        print.append("STRONGBOX").append("\n").append(printStrongbox()).append("\n");
        print.append("Active Leader Cards: ").append("\n");
        for(LeaderCard leaderCard : activeLeaderCards){
            print.append(leaderCard.toString());
        }
        print.append("Non active Leader Cards: ").append(numberOfNonActiveLeaderCards).append("\n");
        return print.toString();
    }

    private String printStrongbox(){
        StringBuilder print = new StringBuilder();
        for (ResourceType resourceType: strongbox.keySet())
            print.append(resourceType.convertColor()).append(": ").append(strongbox.get(resourceType)).append(",  ");
        return print.toString();
    }
}
