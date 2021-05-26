package it.polimi.ingsw.message.update;

import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.Warehouse;

import java.util.List;
import java.util.Map;

public class PlayerMessageDTO extends UpdateMessageDTO{
    private String username;
    private List<LeaderCard> activeLeaderCards;
    private int numberOfNonActiveCards;
    private Warehouse warehouse;
    private Map<ResourceType, Integer> strongbox;
    private DevelopmentSlot developmentSlots[]; // TODO check order

    public PlayerMessageDTO(String username, List<LeaderCard> activeLeaderCards, int numberOfNonActiveCards, Warehouse warehouse, Map<ResourceType, Integer> strongbox, DevelopmentSlot[] developmentSlots) {
        this.username = username;
        this.activeLeaderCards = activeLeaderCards;
        this.numberOfNonActiveCards = numberOfNonActiveCards;
        this.warehouse = warehouse;
        this.strongbox = strongbox;
        this.developmentSlots = developmentSlots;
    }

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

    public DevelopmentSlot[] getDevelopmentSlots() {
        return developmentSlots;
    }

    public void setDevelopmentSlots(DevelopmentSlot[] developmentSlots) {
        this.developmentSlots = developmentSlots;
    }
}
