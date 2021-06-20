package it.polimi.ingsw.message.update;

import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.model.board.DevelopmentSlot;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.warehouse.Warehouse;

import java.util.List;
import java.util.Map;

public class PlayerMessageDTO extends TurnTakerMessageDTO {
    private ClientPlayer clientPlayer;

    public PlayerMessageDTO(String username, List<LeaderCard> activeLeaderCards, int numberOfNonActiveCards, Warehouse warehouse, Map<ResourceType, Integer> strongbox, DevelopmentSlot[] developmentSlots) {
        clientPlayer = new ClientPlayer(username);
        clientPlayer.setActiveLeaderCards(activeLeaderCards);
        clientPlayer.setNumberOfNonActiveLeaderCards(numberOfNonActiveCards);
        clientPlayer.setWarehouse(warehouse);
        clientPlayer.setStrongbox(strongbox);
        clientPlayer.setDevelopmentSlots(developmentSlots);
    }

    public ClientPlayer getClientPlayer() {
        return clientPlayer;
    }
}

