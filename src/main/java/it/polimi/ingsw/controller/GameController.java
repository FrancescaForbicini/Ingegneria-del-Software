package it.polimi.ingsw.controller;

import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.action_message.PickStartingLeaderCardsDTO;
import it.polimi.ingsw.message.action_message.PickStartingResourcesDTO;
import it.polimi.ingsw.message.action_message.development_message.BuyDevelopmentCardDTO;
import it.polimi.ingsw.message.action_message.leader_message.ActivateLeaderCardDTO;
import it.polimi.ingsw.message.action_message.leader_message.DiscardLeaderCardsDTO;
import it.polimi.ingsw.message.action_message.market_message.TakeFromMarketDTO;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.message.game_status.GameStatus;
import it.polimi.ingsw.message.game_status.GameStatusDTO;
import it.polimi.ingsw.message.update.*;
import it.polimi.ingsw.model.Deck;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_action.*;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.GamesRegistry;
import it.polimi.ingsw.view.UpdateBuilder;
import it.polimi.ingsw.view.VirtualView;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;


// TODO move "notify" methods on virtual view??

public class GameController {
    private final static Logger LOGGER = Logger.getLogger(GameController.class.getName());
    private static final ThreadLocal<GameController> instance = ThreadLocal.withInitial(GameController::new);
    private Settings settings;
    private Game game;
    private final VirtualView virtualView;
    private final Map<Integer, Consumer<Player>> setupsPerPlayerOrder; // TODO THIS IS A LIST
    private final Map<Class<? extends ActionMessageDTO>, Function<ActionMessageDTO, TurnAction>> actionsPerMessages;

    /**
     * Returns the thread local singleton instance
     */

    public static GameController getInstance() {
        return instance.get();
    }

    private GameController() {
        virtualView = VirtualView.getInstance();
        virtualView.setGameController(this);
        setupsPerPlayerOrder = new HashMap<>();
        setupFunctions();
        actionsPerMessages = new HashMap<>();
        setupActions();
    }

    private void setupActions() {
        actionsPerMessages.put(ActivateProductionDTO.class, (msg) -> {
            ActivateProductionDTO ap = (ActivateProductionDTO) msg;
            return new ActivateProduction(
                    ap.getDevelopmentCardChosen(),
                    ap.getInputAnyChosen(),
                    ap.getOutputAnyChosen(),
                    ap.getInputChosenFromWarehouse(),
                    ap.getInputChosenFromStrongbox());
        });
        actionsPerMessages.put(BuyDevelopmentCardDTO.class, (msg) -> new BuyDevelopmentCard()); // TODO
        actionsPerMessages.put(TakeFromMarketDTO.class, (msg) -> {
            TakeFromMarketDTO tfm = (TakeFromMarketDTO)msg;
            return new TakeFromMarket(tfm.getMarketAxis(), tfm.getLine(), tfm.getResourceToDepot());
        });
        actionsPerMessages.put(ActivateLeaderCardDTO.class, (msg) -> new ActivateLeaderCard((((ActivateLeaderCardDTO)msg).getLeaderCardsToActivate())));
        actionsPerMessages.put(DiscardLeaderCardsDTO.class, (msg) -> new DiscardLeaderCard((((DiscardLeaderCardsDTO)msg).getLeaderCardToDiscard())));
    }

    private void setupFunctions() {
        setupsPerPlayerOrder.put(0, player -> virtualView.sendMessageTo(player.getUsername(), new PickStartingResourcesDTO(0, null)));
        setupsPerPlayerOrder.put(1, player -> virtualView.sendMessageTo(player.getUsername(), new PickStartingResourcesDTO(1, null)));
        setupsPerPlayerOrder.put(2, player -> {
            virtualView.sendMessageTo(player.getUsername(), new PickStartingResourcesDTO(1, null));
            game.getFaithTrack().move(player, 1);
        });
        setupsPerPlayerOrder.put(3, player -> {
            virtualView.sendMessageTo(player.getUsername(), new PickStartingResourcesDTO(2, null));
            game.getFaithTrack().move(player, 1);
        });
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
        LOGGER.info("Shuffling players");
        Collections.shuffle(game.getPlayers());
        LOGGER.info("Notifying initial state of the game");
        notifyGameStatus();
        notifyGameStatus(GameStatus.SETUP);
        LOGGER.info("Serving cards");
        serveCards();
        notifyGameStatus();

        LOGGER.info("Serving starting resources");
        pickStartingResources();
        notifyGameStatus();

        LOGGER.info("Starting the game");
        notifyGameStatus(GameStatus.START);
    }

    private void notifyGameStatus(GameStatus status) {
        game.getPlayers().forEach(player -> virtualView.sendMessageTo(player.getUsername(), new GameStatusDTO(status)));
    }


    private void serveCards() {
        Deck<LeaderCard> leaderCardDeck = game.getLeaderCards();
        leaderCardDeck.shuffle();

/**/        // TODO handle "bad connections"? Here we assume all clients are good!
        game.getPlayers().forEach(player -> virtualView.sendMessageTo(
                        player.getUsername(), new PickStartingLeaderCardsDTO(leaderCardDeck.drawFourCards())));


        LOGGER.info("Waiting for players to pick the cards");
        Map<String, PickStartingLeaderCardsDTO> pickLeaderCardsDTOs = game.getPlayers()
                .stream()
                .collect(Collectors.toMap(
                        Player::getUsername,
                        player -> (PickStartingLeaderCardsDTO) virtualView.receiveMessageFrom(player.getUsername(), PickStartingLeaderCardsDTO.class).get())); // TODO assuming it is present

        //  TODO check that leader exists, picked are 2 in 4 proposed, validate
        LOGGER.info("Setting picked cards to related players");
        pickLeaderCardsDTOs.forEach((username, pickStartingLeaderCardsDTO) -> game.getPlayerByUsername(username).get().setNonActiveLeaderCards(pickStartingLeaderCardsDTO.getCards()));
    }

    private void pickStartingResources() {
        askForStartingResources();
        addStartingResources();
    }

    private void askForStartingResources() {
        LOGGER.info("Asking for players to pick the starting resources");
        List<Player> players = game.getPlayers();
        for (int i = 0; i < settings.getMaxPlayers(); i++) {
            setupsPerPlayerOrder.get(i).accept(players.get(i));
        }
        LOGGER.info("Waiting for players to pick the starting resources");
    }

    private void addStartingResources() {
        PickStartingResourcesDTO resourceToDepotDTO;
        List<Player> players = game.getPlayers();
        for (Player player:
                players) {
            resourceToDepotDTO = (PickStartingResourcesDTO) virtualView
                    .receiveMessageFrom(player.getUsername(), PickStartingResourcesDTO.class).get();
            player.getPersonalBoard().addStartingResourcesToWarehouse(resourceToDepotDTO.getPickedResources());
        }
    }

    private void notifyGameStatus() {
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

        game.getPlayers().forEach(player -> {
            updateMessages.forEach(updateMessage ->
                    virtualView.sendMessageTo(player.getUsername(), updateMessage));
            virtualView.sendMessageTo(player.getUsername(), UpdateBuilder.mkCurrentPlayerMessage(player));
        });
    }

    public void playGame() {
        // main loop, msg to player who have to play the turn
        while (!game.isEnded()) {
            game.getPlayers().forEach(this::playTurn);
        }
        // TODO notify game finished
    }

    private void playTurn(Player player) {
        String username = player.getUsername();
        virtualView.sendMessageTo(username, new GameStatusDTO(GameStatus.YOUR_TURN));
        do {
            MessageDTO messageDTO = virtualView.receiveAnyMessageFrom(username).get();
            if (messageDTO.getClass().equals(GameStatusDTO.class) && ((GameStatusDTO) messageDTO).getStatus() == GameStatus.TURN_FINISHED)
                break;
            handleActionMessage((ActionMessageDTO) messageDTO, player);
            notifyGameStatus();
        } while (true);
        notifyGameStatus();
    }

    private void handleActionMessage(ActionMessageDTO actionMessageDTO, Player player) {
            getTurnAction(actionMessageDTO).play(player);
    }

    private TurnAction getTurnAction(ActionMessageDTO actionMessageDTO) {
        return actionsPerMessages.get(actionMessageDTO.getClass()).apply(actionMessageDTO);
    }


    public void addPlayer(String username) {
        game.addPlayer(username);
    }

}
