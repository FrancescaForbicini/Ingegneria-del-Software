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
    private final List<LeaderCard> activeLeaderCards;
    private final List<LeaderCard> nonActiveLeaderCards;
    private final int numberOfNonActiveLeaderCards;
    private final Warehouse warehouse;
    private final Map<ResourceType, Integer> strongbox;
    private final DevelopmentSlot[] developmentSlots;

    public ClientPlayer(String username,
                        List<LeaderCard> activeLeaderCards,
                        List<LeaderCard> nonActiveLeaderCards,
                        int numberOfNonActiveLeaderCards,
                        Warehouse warehouse,
                        Map<ResourceType, Integer> strongbox,
                        DevelopmentSlot[] developmentSlots) {
        super(username);
        this.activeLeaderCards = activeLeaderCards;
        this.nonActiveLeaderCards = nonActiveLeaderCards;
        this.numberOfNonActiveLeaderCards = numberOfNonActiveLeaderCards;
        this.warehouse = warehouse;
        this.strongbox = strongbox;
        this.developmentSlots = developmentSlots;
    }

    public List<LeaderCard> getActiveLeaderCards() {
        return activeLeaderCards;
    }

    public List<LeaderCard> getNonActiveLeaderCards() {
        return nonActiveLeaderCards;
    }

    public int getNumberOfNonActiveLeaderCards() {
        return numberOfNonActiveLeaderCards;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public Map<ResourceType, Integer> getStrongbox() {
        return strongbox;
    }

    public DevelopmentSlot[] getDevelopmentSlots() {
        return developmentSlots;
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