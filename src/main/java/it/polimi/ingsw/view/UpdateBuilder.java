package it.polimi.ingsw.view;

import it.polimi.ingsw.message.update.*;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;
import java.util.stream.Stream;

public class UpdateBuilder {
    public static PlayerMessageDTO mkPlayerMessage(Player player) {
        return new PlayerMessageDTO(
                player.getUsername(),
                player.getActiveLeaderCards(),
                player.getLeaderCards().size() - player.getActiveLeaderCards().size(),
                player.getWarehouse(),
                player.getStrongbox(),
                player.getDevelopmentSlots());
    }

    public static MarketMessageDTO mkMarketMessage(Market market) {
        return new MarketMessageDTO(market);
    }

    public static FaithTrackMessageDTO mkFaithTrackMessage(FaithTrack faithTrack) {
        return new FaithTrackMessageDTO(faithTrack);
    }

    public static PlayersMessageDTO mkPlayersMessage(Stream<Player> players) {
        return new PlayersMessageDTO(players.map(UpdateBuilder::mkPlayerMessage));
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
}
