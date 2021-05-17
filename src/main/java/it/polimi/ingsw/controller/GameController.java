package it.polimi.ingsw.controller;

import it.polimi.ingsw.message.LoginMessageDTO;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.GamesRegistry;
import it.polimi.ingsw.view.VirtualView;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class GameController {
    private final static Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private static final ThreadLocal<GameController> instance = ThreadLocal.withInitial(GameController::new);
    private final Settings settings;
    private final Game game;
    private final VirtualView virtualView;


    /**
     * Returns the thread local singleton instance
     */

    public static GameController getInstance() {
        return instance.get();
    }

    private GameController() {
        settings = Settings.getInstance();
        game = Game.getInstance();
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
        GamesRegistry.getInstance().addThreadLocalGame(gameId);
        startGame();
    }

    private void startGame() {
        if (!waitForPlayers()) {
            LOGGER.info("Game interrupted. Exiting...");
            return;
        }
        LOGGER.info("\n\n--- GAME STARTED ---\n\n");
        LOGGER.info(String.format("Players are: %s", game.getPlayersNames().collect(Collectors.joining(", "))));
        setUpGame();
//
//        pickLeaderCards();
//
//        notifyPlayers();
//
//        // TODO notify that game is started, send initial state to players!
//        playGame();
        LOGGER.info("\n\n--- GAME FINISHED ---\n\n");


        // TODO SUPER TODO CLEAN thread locals
    }

    private void setUpGame(){
        LOGGER.info(String.format("Setting up game with id: '%s'", game.getGameID()));
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

        // HERE players has the ack
        // TODO FINISH SETUP
        //  1 - create decks
        //  2 - create opponent
//        createDevelopmentCardDecks(settings.getDevelopmentCards());
        // TODO
        //  2 - create opponent
//        if(settings.isSoloGame()){
//            opponent = Optional.oO f(new Opponent());
//        }
    }

    private void pickLeaderCards() {
        // TODO:
        //  1) shuffles players
        //  2) give players leader cards
        //  3) notify players
        //  4) wait for responses
    }


    private void notifyPlayers() {
        // TODO:
        //  1) Notify players of the starts
    }


    public void playGame() {
        // TODO main cycle, while game is not finished
    }

    public void addPlayer(String username) {
        game.addPlayer(username);
    }

}
