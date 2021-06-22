package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.action_message.PickStartingLeaderCardsDTO;
import it.polimi.ingsw.message.action_message.PickStartingResourcesDTO;
import it.polimi.ingsw.message.action_message.development_message.BuyDevelopmentCardDTO;
import it.polimi.ingsw.message.action_message.leader_message.ActivateLeaderCardDTO;
import it.polimi.ingsw.message.action_message.leader_message.DiscardLeaderCardsDTO;
import it.polimi.ingsw.message.action_message.market_message.SortWarehouseDTO;
import it.polimi.ingsw.message.action_message.market_message.TakeFromMarketDTO;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.message.game_status.GameStatus;
import it.polimi.ingsw.message.game_status.GameStatusDTO;
import it.polimi.ingsw.message.update.*;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.turn_action.*;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.turn_taker.TurnTaker;
import it.polimi.ingsw.server.SocketConnector;

import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.stream.Collectors;


public class VirtualView {
    private final static Logger LOGGER = Logger.getLogger(VirtualView.class.getName());
    private final ConcurrentHashMap<String, SocketConnector> usersSocketConnectors;
    private static final ThreadLocal<VirtualView> instance = ThreadLocal.withInitial(VirtualView::new);
    private  GameController gameController;
    private final ArrayList<Consumer<Player>> setupsPerPlayerOrder;
    private final Map<Class<? extends ActionMessageDTO>, Function<ActionMessageDTO, TurnAction>> actionsPerMessages;
    private Game game;

    /**
     * Returns the thread local singleton instance
     */
    public static VirtualView getInstance() {
        return instance.get();
    }

    public void setGameController(GameController gameController) {
        this.gameController = gameController;
    }

    private VirtualView() {
        usersSocketConnectors = new ConcurrentHashMap<>();
        game = Game.getInstance();
        setupsPerPlayerOrder = new ArrayList<>();
        setupFunctions();
        actionsPerMessages = new HashMap<>();
        setupActions();
    }

    public boolean addPlayer(String username , SocketConnector playerSocket){
        if (usersSocketConnectors.containsKey(username)) {
            LOGGER.info(String.format("Cannot log '%s' in the game, there is another player with the same username", username));
            return false;
        }
        usersSocketConnectors.put(username, playerSocket);
        LOGGER.info(String.format("Adding '%s' to the game.", username));
        gameController.addPlayer(username);
        return true;
    }

    private void sendMessageTo(String username, MessageDTO message) {
        usersSocketConnectors.get(username).sendMessage(message);
    }

    private Optional<MessageDTO> receiveMessageFrom(String username, Type typeOfMessage){
        return usersSocketConnectors.get(username).receiveMessage(typeOfMessage);
    }

    private Optional<MessageDTO> receiveAnyMessageFrom(String username){
        return usersSocketConnectors.get(username).receiveAnyMessage();
    }
    public void serveCards(Player player, List<LeaderCard> proposedCards){
        sendMessageTo(player.getUsername(), new PickStartingLeaderCardsDTO(proposedCards));
    }

    public Map<String, PickStartingLeaderCardsDTO> getSelectedLeaderCards(){
        return game.getPlayers()
                .stream()
                .collect(Collectors.toMap(
                        Player::getUsername,
                        player -> (PickStartingLeaderCardsDTO) receiveMessageFrom(
                                player.getUsername(), PickStartingLeaderCardsDTO.class
                        ).get())); // TODO assuming it is present
    }

    public boolean isGameStarted(){
        return gameController.isGameStarted();
    }


    public void playTurn(Player player) {
        String username = player.getUsername();
        sendMessageTo(username, new GameStatusDTO(GameStatus.YOUR_TURN));
        do {
            MessageDTO messageDTO = receiveAnyMessageFrom(username).get();
            if (messageDTO.getClass().equals(GameStatusDTO.class) && ((GameStatusDTO) messageDTO).getStatus() == GameStatus.TURN_FINISHED)
                break;
            assert messageDTO instanceof ActionMessageDTO;
            handleActionMessage((ActionMessageDTO) messageDTO, player);
            notifyGameData();
        } while (true);
        notifyGameData();
    }

    private void handleActionMessage(ActionMessageDTO actionMessageDTO, Player player) {
        getTurnAction(actionMessageDTO).play(player);
    }


    public void notifyGameData(GameStatus status) {
        game.getPlayers().forEach(player -> sendMessageTo(player.getUsername(), new GameStatusDTO(status)));
    }
    public void notifyGameData() {
        TurnTakersMessageDTO turnTakersMessageDTO = UpdateBuilder.mkTurnTakersMessage(game.getTurnTakers());
        MarketMessageDTO marketMessageDTO = UpdateBuilder.mkMarketMessage(game.getMarket());
        FaithTrackMessageDTO faithTrackMessageDTO = UpdateBuilder.mkFaithTrackMessage(game.getFaithTrack());
        DevelopmentCardsMessageDTO developmentCardsMessageDTO = UpdateBuilder.mkDevelopmentCardsMessage(game.getDevelopmentCardColumns());
        ArrayList<UpdateMessageDTO> updateMessages = new ArrayList<>(Arrays.asList(
                marketMessageDTO,
                turnTakersMessageDTO,
                faithTrackMessageDTO,
                developmentCardsMessageDTO
        ));

        game.getPlayers().forEach(player -> {
            updateMessages.forEach(updateMessage ->
                    sendMessageTo(player.getUsername(), updateMessage));
            sendMessageTo(player.getUsername(), UpdateBuilder.mkCurrentPlayerMessage(player));
        });
    }

    public void notifyGameFinished() {
        Optional<TurnTaker> winner = game.computeWinner();
        String winnerUsername = winner.map(TurnTaker::getUsername).orElse(null);
        game.getPlayers().forEach(player -> {
            sendMessageTo(player.getUsername(), new GameStatusDTO(winnerUsername, GameStatus.FINISHED));
        });
    }
    private TurnAction getTurnAction(ActionMessageDTO actionMessageDTO) {
        return actionsPerMessages.get(actionMessageDTO.getClass()).apply(actionMessageDTO);
    }


    public void askForStartingResources() {
        LOGGER.info("Asking for players to pick the starting resources");
        List<Player> players = game.getPlayers();
        for (int i = 0; i < game.getMaxPlayers(); i++) {
            setupsPerPlayerOrder.get(i).accept(players.get(i));
        }
        LOGGER.info("Waiting for players to pick the starting resources");
    }


    public void addStartingResources() {
        PickStartingResourcesDTO resourceToDepotDTO;
        List<Player> players = game.getPlayers();
        for (Player player :
                players) {
            resourceToDepotDTO = (PickStartingResourcesDTO) receiveMessageFrom(player.getUsername(), PickStartingResourcesDTO.class).get();
            player.getPersonalBoard().addStartingResourcesToWarehouse(resourceToDepotDTO.getPickedResources());
        }
    }

        private void setupActions() {
            actionsPerMessages.put(ActivateProductionDTO.class, (msg) -> {
                ActivateProductionDTO ap = (ActivateProductionDTO) msg;
                return new ActivateProduction(
                        ap.getDevelopmentCardChosen(),ap.getAdditionalTradingRulesChosen(),ap.getInputChosenFromWarehouse(),ap.getInputChosenFromStrongbox(),
                        ap.getInputAny(),ap.getOutputAny());
            });
            actionsPerMessages.put(BuyDevelopmentCardDTO.class, (msg) -> {
                BuyDevelopmentCardDTO bdm = (BuyDevelopmentCardDTO)msg;
                return new BuyDevelopmentCard(bdm.getCard(), bdm.getSlotID(),bdm.getResourcesChosenFromWarehouse(),bdm.getResourcesChosenFromStrongbox());
            });
            actionsPerMessages.put(TakeFromMarketDTO.class, (msg) -> {
                TakeFromMarketDTO tfm = (TakeFromMarketDTO)msg;
                return new TakeFromMarket(tfm.getMarketAxis(), tfm.getLine(), tfm.getResourceToDepot(),tfm.getWhiteMarbleChosen());
            });
            actionsPerMessages.put(SortWarehouseDTO.class, (msg) ->{
                SortWarehouseDTO swm = (SortWarehouseDTO)msg;
                return new SortWarehouse(swm.getDepotID1(), swm.getDepotID2());
            });
            actionsPerMessages.put(ActivateLeaderCardDTO.class, (msg) -> new ActivateLeaderCard((((ActivateLeaderCardDTO)msg).getLeaderCardsToActivate())));
            actionsPerMessages.put(DiscardLeaderCardsDTO.class, (msg) -> new DiscardLeaderCard((((DiscardLeaderCardsDTO)msg).getLeaderCardToDiscard())));
        }

        private void setupFunctions() {
            setupsPerPlayerOrder.add(player -> sendMessageTo(player.getUsername(), new PickStartingResourcesDTO(0, null)));
            setupsPerPlayerOrder.add(player -> sendMessageTo(player.getUsername(), new PickStartingResourcesDTO(1, null)));
            setupsPerPlayerOrder.add(player -> {
                sendMessageTo(player.getUsername(), new PickStartingResourcesDTO(1, null));
                game.getFaithTrack().move(player, 1);
            });
            setupsPerPlayerOrder.add(player -> {
                sendMessageTo(player.getUsername(), new PickStartingResourcesDTO(2, null));
                game.getFaithTrack().move(player, 1);
            });
        }

    }
