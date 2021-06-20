package it.polimi.ingsw.view;

import it.polimi.ingsw.client.turn_taker.ClientOpponent;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.client.turn_taker.ClientTurnTaker;
import it.polimi.ingsw.message.update.*;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.turn_taker.TurnTaker;

import java.util.ArrayList;
import java.util.List;


// TODO move inside models?
public class UpdateBuilder {
    public static ClientPlayer mkClientPlayer(Player player) {
        return new ClientPlayer(
                player.getUsername(),
                player.getActiveLeaderCards(),
                player.getWarehouse(),
                player.getStrongbox(),
                player.getDevelopmentSlots()
                );
    }

    public static ClientOpponent mkClientOpponent(Opponent opponent) {
        return new ClientOpponent(opponent.getUsername());
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

    public static DevelopmentCardsMessageDTO mkDevelopmentCardsMessage(ArrayList<ArrayList<Deck<DevelopmentCard>>> developmentCardDecks) {
        ArrayList<DevelopmentCard> availableCards = new ArrayList<>();
        for (ArrayList<Deck<DevelopmentCard>> cardsPerLevel:
             developmentCardDecks) {
            for (Deck<DevelopmentCard> developmentCardDeck:
                 cardsPerLevel) {
                availableCards.add(developmentCardDeck.showFirstCard().get());
            }
        }
        return new DevelopmentCardsMessageDTO(availableCards);
    }

    public static CurrentPlayerDTO mkCurrentPlayerMessage(Player player) {
        return new CurrentPlayerDTO(player);
    }
}
