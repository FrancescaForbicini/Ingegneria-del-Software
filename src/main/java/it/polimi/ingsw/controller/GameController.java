package it.polimi.ingsw.controller;

import it.polimi.ingsw.message.LoginMessageDTO;
import it.polimi.ingsw.message.update.*;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.GamesRegistry;
import it.polimi.ingsw.view.UpdateBuilder;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class GameController {
    private final static Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private static final ThreadLocal<GameController> instance = ThreadLocal.withInitial(GameController::new);
    private Settings settings;
    private Game game;
    private final VirtualView virtualView;


    /**
     * Returns the thread local singleton instance
     */

    public static GameController getInstance() {
        return instance.get();
    }

    private GameController() {
        virtualView = VirtualView.getInstance();
        virtualView.setGameController(this);
    }

    private boolean waitForPlayers(){
        LOGGER.info("Waiting for players to join");
        try {
            synchronized (game) {
                while (game.getPlayersNumber() < settings.getMaxPlayers()) {
                    LOGGER.info("Not enough players, waiting for players to join...");
                    LOGGER.info(String.format("Waiting players: %s", game.getPlayersNames().collect(Collectors.joining(", "))));
                    game.wait();
                }
            }
            return true;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    // TODO ENTRY POINT OF THE GAME, WHEN THIS METHOD ENDS, THE THREAD DIES
    public void runGame(String gameId){
        Thread.currentThread().setName(gameId);
        game = Game.getInstance();
        settings = Settings.getInstance();
        GamesRegistry.getInstance().addThreadLocalGame(gameId);
        startGame();
    }

    private void startGame() {
        if (!waitForPlayers()) {
            LOGGER.info("Game interrupted. Exiting...");
            return;
        }
        LOGGER.info("\n\n--- SETTING UP GAME ---\n\n");

        LOGGER.info(String.format("Players are: %s", game.getPlayersNames().collect(Collectors.joining(", "))));
        setUpGame();
        LOGGER.info("\n\n--- GAME STARTED ---\n\n");
        playGame();
        LOGGER.info("\n\n--- GAME FINISHED ---\n\n");
        // TODO VERY IMPORTANT CLEAN thread locals
    }


    private void setUpGame() {
        LOGGER.info(String.format("Setting up game with id: '%s'", game.getGameID()));
        serveCards();
        game.initializeGame();
//        pickStartingResources(); // TODO ASK TO RESPECTIVE PLAYERS RESOURCE (2nd, 3dr, 4th)
        notifyStart();
    }
    private void serveCards() {
        Deck<LeaderCard> leaderCardDeck = game.getLeaderCards();
        leaderCardDeck.shuffle();
        // TODO constraint on leadercards length
        List<LoginMessageDTO> loginMessageDTOList = game.getPlayers().map(player ->
                new LoginMessageDTO(
                        player.getUsername(),
                        game.getGameID(),
                        settings,
                        leaderCardDeck.drawFourCards())).collect(Collectors.toList());

        LOGGER.info("Proposing cards to players");

        // TODO handle "bad connections"? Here we assume all clients are good!
        loginMessageDTOList.forEach(loginMessageDTO -> virtualView.sendMessageTo(loginMessageDTO.getUsername(), loginMessageDTO));

        LOGGER.info("Waiting for players to pick the cards");
        Map<String, LoginMessageDTO> loginMessageDTOs = game.getPlayers()
                .collect(Collectors.toMap(
                        Player::getUsername,
                        player -> (LoginMessageDTO) virtualView.receiveMessageFrom(player.getUsername(), LoginMessageDTO.class).get())); // TODO assuming it is present, ask DC

        //  TODO check that leader exists, picked are 2 in 4 proposed, validate
        LOGGER.info("Setting picked cards to related players");
        loginMessageDTOs.forEach((username, loginMessageDTO) -> game.getPlayerByUsername(username).get().setLeaderCards(loginMessageDTO.getCards()));
    }

    private void pickStartingResources() {
        // TODO
    }

    private void notifyStart() {
        PlayersMessageDTO playersMessageDTO = UpdateBuilder.mkPlayersMessage(game.getPlayers());
        MarketMessageDTO marketMessageDTO = UpdateBuilder.mkMarketMessage(game.getMarket());
        FaithTrackMessageDTO faithTrackMessageDTO = UpdateBuilder.mkFaithTrackMessage(game.getFaithTrack());
        DevelopmentCardsMessageDTO developmentCardsMessageDTO = UpdateBuilder.mkDevelopmentCardsMessage(game.getDevelopmentCards());
        ArrayList<UpdateMessageDTO> updateMessages = new ArrayList<>(Arrays.asList(
                marketMessageDTO,
                playersMessageDTO,
                faithTrackMessageDTO,
                developmentCardsMessageDTO
        ));

        game.getPlayers().forEach(player ->
                updateMessages.forEach(updateMessage ->
                        virtualView.sendMessageTo(player.getUsername(), updateMessage)));
    }

    public void playGame() {
        // TODO main cycle, while game is not finished
    }

    public void addPlayer(String username) {
        game.addPlayer(username);
    }

}
