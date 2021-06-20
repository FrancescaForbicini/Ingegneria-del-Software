package it.polimi.ingsw.client.turn_taker;

import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class ClientPlayer extends ClientTurnTaker {
    private List<LeaderCard> activeLeaderCards;
    private List<LeaderCard> nonActiveLeaderCards;
    private int numberOfNonActiveLeaderCards;
    private Warehouse warehouse;
    private Map<ResourceType, Integer> strongbox;
    private DevelopmentSlot[] developmentSlots;

    public ClientPlayer(String username) {
        super(username);
        nonActiveLeaderCards = new ArrayList<>();
    }

    public List<LeaderCard> getActiveLeaderCards() {
        return activeLeaderCards;
    }

    public void setActiveLeaderCards(List<LeaderCard> activeLeaderCards) {
        this.activeLeaderCards = activeLeaderCards;
    }

    public List<LeaderCard> getNonActiveLeaderCards() {
        return nonActiveLeaderCards;
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

    public void setNonActiveLeaderCards(List<LeaderCard> nonActiveLeaderCards) {
        this.nonActiveLeaderCards = nonActiveLeaderCards;
    }

    @Override
    public String toString(){
        ArrayList<DevelopmentSlot> arrayListSlots = new ArrayList<>(Arrays.asList(developmentSlots));
        StringBuilder print = new StringBuilder();
        print.append(getUsername()).append("\n");
        print.append(getFaithTrack().toString());
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
