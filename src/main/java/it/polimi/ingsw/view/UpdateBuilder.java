package it.polimi.ingsw.view;

import it.polimi.ingsw.client.turn_taker.ClientOpponent;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.client.turn_taker.ClientTurnTaker;
import it.polimi.ingsw.message.update.*;
import it.polimi.ingsw.model.DevelopmentCardColumn;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.turn_taker.TurnTaker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


// TODO move inside models?
public class UpdateBuilder {
    public static ClientPlayer mkClientPlayer(Player player) {
        return new ClientPlayer(
                player.getUsername(),
                player.getActiveLeaderCards(),
                player.getWarehouse(),
                player.getStrongbox(),
                player.getDevelopmentSlots(),
                player.getPersonalVictoryPoints()
                );
    }

    public static ClientOpponent mkClientOpponent(Opponent opponent) {
        return new ClientOpponent(opponent.getUsername(), opponent.getLastAction());
    }

    public static MarketMessageDTO mkMarketMessage(Market market) {
        return new MarketMessageDTO(market);
    }

    public static FaithTrackMessageDTO mkFaithTrackMessage(FaithTrack faithTrack) {
        return new FaithTrackMessageDTO(faithTrack);
    }

    public static TurnTakersMessageDTO mkTurnTakersMessage(List<TurnTaker> turnTakers) {
        List<ClientTurnTaker> turnTakerMessageDTOs =  new ArrayList<>();
        turnTakers.stream()
                .filter(turnTaker -> turnTaker.getClass().equals(Opponent.class))
                .findFirst()
                .map(turnTaker -> UpdateBuilder.mkClientOpponent((Opponent) turnTaker))
                .ifPresent(turnTakerMessageDTOs::add);
        turnTakers.stream()
                .filter(turnTaker -> turnTaker.getClass().equals(Player.class))
                .map(turnTaker -> UpdateBuilder.mkClientPlayer((Player) turnTaker))
                .forEach(turnTakerMessageDTOs::add);
        return new TurnTakersMessageDTO(turnTakerMessageDTOs);
    }

    public static DevelopmentCardsMessageDTO mkDevelopmentCardsMessage(DevelopmentCardColumn[] developmentCardColumns) {
        return new DevelopmentCardsMessageDTO((ArrayList<DevelopmentCard>) Arrays.stream(developmentCardColumns)
                        .flatMap(developmentCardColumn -> developmentCardColumn.getVisibleCards().stream())
                        .collect(Collectors.toList()));
    }

    public static CurrentPlayerDTO mkCurrentPlayerMessage(Player player) {
        return new CurrentPlayerDTO(player);
    }
}
