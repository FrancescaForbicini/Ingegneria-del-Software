package it.polimi.ingsw.view;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.game_status.GameStatus;
import it.polimi.ingsw.message.game_status.GameStatusDTO;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


public class VirtualView {
    private final static Logger LOGGER = Logger.getLogger(VirtualView.class.getName());
    private final ConcurrentHashMap<String, SocketConnector> usersSocketConnectors;
    private static final ThreadLocal<VirtualView> instance = ThreadLocal.withInitial(VirtualView::new);
    private  GameController gameController;

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

    public void sendMessageTo(String username, MessageDTO message) {
        usersSocketConnectors.get(username).sendMessage(message);
    }

    public Optional<MessageDTO> receiveMessageFrom(String username, Type typeOfMessage){
        return usersSocketConnectors.get(username).receiveMessage(typeOfMessage);
    }

    public Optional<MessageDTO> receiveAnyMessageFrom(String username){
        return usersSocketConnectors.get(username).receiveAnyMessage();
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
            notifyGameStatus();
        } while (true);
        notifyGameStatus();
    }

    private void handleActionMessage(ActionMessageDTO actionMessageDTO, Player player) {
        getTurnAction(actionMessageDTO).play(player);
    }


    public void notifyGameStatus() {
        TurnTakersMessageDTO turnTakersMessageDTO = UpdateBuilder.mkTurnTakersMessage(game.getTurnTakers());
        MarketMessageDTO marketMessageDTO = UpdateBuilder.mkMarketMessage(game.getMarket());
        FaithTrackMessageDTO faithTrackMessageDTO = UpdateBuilder.mkFaithTrackMessage(game.getFaithTrack());
        DevelopmentCardsMessageDTO developmentCardsMessageDTO = UpdateBuilder.mkDevelopmentCardsMessage(game.getDevelopmentCards());
        ArrayList<UpdateMessageDTO> updateMessages = new ArrayList<>(Arrays.asList(
                marketMessageDTO,
                turnTakersMessageDTO,
                faithTrackMessageDTO,
                developmentCardsMessageDTO
        ));

        game.getPlayers().forEach(player -> {
            updateMessages.forEach(updateMessage ->
                    virtualView.sendMessageTo(player.getUsername(), updateMessage));
            virtualView.sendMessageTo(player.getUsername(), UpdateBuilder.mkCurrentPlayerMessage(player));
        });
    }

    public void notifyGameFinished() {
        Optional<TurnTaker> winner = game.computeWinner();
        String winnerUsername = winner.map(TurnTaker::getUsername).orElse(null);
        game.getPlayers().forEach(player -> {
            virtualView.sendMessageTo(player.getUsername(), new GameStatusDTO(winnerUsername, GameStatus.FINISHED));
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
