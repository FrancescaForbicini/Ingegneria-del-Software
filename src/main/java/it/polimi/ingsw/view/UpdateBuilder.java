package it.polimi.ingsw.view;

import it.polimi.ingsw.client.turn_taker.ClientOpponent;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.client.turn_taker.ClientTurnTaker;
import it.polimi.ingsw.message.update.*;
import it.polimi.ingsw.model.cards.DevelopmentCardColumn;
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

/**
 * Updates
 */
public class UpdateBuilder {
    /**
     * Updates the client player
     *
     * @param player player updated
     * @return client player updated
     */
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

    /**
     * Updates the opponent
     * @param opponent opponent updated
     * @return opponent updated
     */
    public static ClientOpponent mkClientOpponent(Opponent opponent) {
        return new ClientOpponent(opponent.getUsername(), opponent.getLastAction());
    }

    /**
     * Updates the market
     * @param market market updated
     * @return message of market updated
     */
    public static MarketMessageDTO mkMarketMessage(Market market) {
        return new MarketMessageDTO(market);
    }

    /**
     * Updates the faith track
     * @param faithTrack faith track updated
     * @return message of faith track updated
     */
    public static FaithTrackMessageDTO mkFaithTrackMessage(FaithTrack faithTrack) {
        return new FaithTrackMessageDTO(faithTrack);
    }

    /**
     * Updates the turn takers
     * @param turnTakers lists of turn taker updated
     * @return message of the turn takers updated
     */
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

    /**
     * Updates the lists of the development cards
     * @param developmentCardColumns lists of the development cards updated
     * @return message of all the development cards updated
     */
    public static DevelopmentCardsMessageDTO mkDevelopmentCardsMessage(DevelopmentCardColumn[] developmentCardColumns) {
        return new DevelopmentCardsMessageDTO((ArrayList<DevelopmentCard>) Arrays.stream(developmentCardColumns)
                        .flatMap(developmentCardColumn -> developmentCardColumn.getVisibleCards().stream())
                        .collect(Collectors.toList()));
    }

    /**
     * Updates the current player
     * @param player player updated
     * @return message of the player updated
     */
    public static CurrentPlayerDTO mkCurrentPlayerMessage(Player player) {
        return new CurrentPlayerDTO(player);
    }
}
