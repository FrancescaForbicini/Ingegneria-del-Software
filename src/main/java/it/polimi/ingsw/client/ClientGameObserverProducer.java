package it.polimi.ingsw.client;

import it.polimi.ingsw.client.action.ActionUtils;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.action.FinishTurn;
import it.polimi.ingsw.client.action.turn.SortWarehouse;
import it.polimi.ingsw.client.action.leader.ActivateLeaderCard;
import it.polimi.ingsw.client.action.leader.DiscardLeaderCard;
import it.polimi.ingsw.client.action.show.ShowDevelopmentCards;
import it.polimi.ingsw.client.action.show.ShowMarket;
import it.polimi.ingsw.client.action.show.ShowOpponentLastAction;
import it.polimi.ingsw.client.action.show.ShowPlayer;
import it.polimi.ingsw.client.action.turn.ActivateProduction;
import it.polimi.ingsw.client.action.turn.BuyDevelopmentCard;
import it.polimi.ingsw.client.action.turn.TakeFromMarket;
import it.polimi.ingsw.client.turn_taker.ClientOpponent;
import it.polimi.ingsw.client.turn_taker.ClientPlayer;
import it.polimi.ingsw.client.turn_taker.ClientTurnTaker;
import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.game_status.GameStatus;
import it.polimi.ingsw.message.game_status.GameStatusDTO;
import it.polimi.ingsw.message.update.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

//TODO javadoc
public class ClientGameObserverProducer implements Runnable{
    private final String username;
    private Market market;
    private FaithTrack faithTrack;
    private final View view;
    private ArrayList<DevelopmentCard> developmentCards;
    private ArrayList<ClientTurnTaker> turnTakers;
    private final SocketConnector clientConnector;
    private Optional<String> winner;
    private boolean gameActive = true;
    private final ActionUtils actionUtils;
    private Player currentPlayer;
    private ArrayList<ReactiveObserver> reactiveObservers;

    // "Concurrent" data structures used by this runnable to PUBLISH updates
    private final ConcurrentLinkedDeque<ActionMessageDTO> pendingTurnDTOs;
    private final ConcurrentLinkedDeque<ClientAction> actions;

    public ClientGameObserverProducer(SocketConnector clientConnector, View view, String username){
        this.username = username;
        this.clientConnector = clientConnector;
        this.view = view;
        actions = new ConcurrentLinkedDeque<>();
        pendingTurnDTOs = new ConcurrentLinkedDeque<>();
        actionUtils = ActionUtils.getInstance();
        winner = Optional.empty();
        gameActive = true;
        reactiveObservers = new ArrayList<>();
    }

    public ArrayList<ClientPlayer> getPlayers() {
        return (ArrayList<ClientPlayer>) turnTakers.stream()
                .filter(turnTaker -> turnTaker.getClass().equals(ClientPlayer.class))
                .map(turnTaker -> (ClientPlayer)turnTaker)
                .collect(Collectors.toList());
    }


    // TODO Refactor
    private synchronized void initActions() {
        actions.push(new ShowMarket(clientConnector, view, this));
        actions.push(new ShowDevelopmentCards(clientConnector, view, this));
        actions.push(new ShowPlayer(clientConnector, view, this));
        if (turnTakers.stream().anyMatch(turnTaker -> turnTaker.getClass().equals(ClientOpponent.class)))
            actions.push(new ShowOpponentLastAction(clientConnector,view,this));
        notifyAll();
    }

    // TODO Refactor
    private void initTurn() {
        actions.push(new DiscardLeaderCard(clientConnector, view, this));
        actions.push(new ActivateLeaderCard(clientConnector, view, this));
        actions.push(new ActivateProduction(clientConnector, view, this));
        actions.push(new BuyDevelopmentCard(clientConnector, view, this));
        actions.push(new SortWarehouse(clientConnector,view,this));
        actions.push(new TakeFromMarket(clientConnector, view, this));
        actions.push(new FinishTurn(clientConnector, view, this));
    }

    public String getUsername() {
        return username;
    }

    public Market getMarket() {
        return market;
    }

    public ConcurrentLinkedDeque<ClientAction> getActions() {
        return actions;
    }

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public ArrayList<DevelopmentCard> getDevelopmentCards() {
        return developmentCards;
    }


    public Optional<String> getWinner() {
        return winner;
    }

    public boolean isGameActive() { return gameActive; }

    public void setMarket(Market market) {
        this.market = market;
    }

    public void setFaithTrack(FaithTrack faithTrack) {
        this.faithTrack = faithTrack;
    }

    public ConcurrentLinkedDeque<ActionMessageDTO> getPendingTurnDTOs(){
        return this.pendingTurnDTOs;
    }

    public Optional<ClientOpponent> getOpponent() {
        if (turnTakers == null)
            return Optional.empty();
        return turnTakers.stream()
                .filter(turnTaker -> turnTaker.getClass().equals(ClientOpponent.class))
                .findAny()
                .map(clientTurnTaker -> (ClientOpponent) clientTurnTaker);
    }


    public void run(){
        MessageDTO messageDTO;
        while(true) {
            messageDTO = clientConnector.receiveAnyMessage().get();
            if (messageDTO instanceof GameStatusDTO) {
                handleStatus((GameStatusDTO) messageDTO);
            }
            else if (messageDTO instanceof UpdateMessageDTO) {
                update((UpdateMessageDTO) messageDTO);
            }
            else if (messageDTO instanceof ActionMessageDTO) {
                handleAction((ActionMessageDTO) messageDTO);
            } else {
              System.exit(1);
            }
        }
    }

    private void handleAction(ActionMessageDTO actionMessageDTO) {
        Optional<MessageDTO> omessageDTO = actionUtils.noUserRequiredAction(actionMessageDTO);
        if (omessageDTO.isPresent()) {
            clientConnector.sendMessage(omessageDTO.get());
            return;
        }
        pub(actionMessageDTO);
        view.notifyNewActions();
        synchronized (actions) {
            actions.notifyAll();
        }

    }
    private void pub(ActionMessageDTO actionMessageDTO) {
        synchronized (pendingTurnDTOs) {
            assert pendingTurnDTOs.size() == 0;
            try {
                Class klass = Class.forName(actionMessageDTO.getRelatedAction());
                Constructor constructor = klass.getConstructor(SocketConnector.class, View.class, ClientGameObserverProducer.class);
                actions.push((ClientAction) constructor.newInstance(clientConnector, view, this));
                pendingTurnDTOs.push(actionMessageDTO);
            } catch (ClassNotFoundException | NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                System.exit(1);
            }

        }
    }

    private void handleStatus(GameStatusDTO gameStatusDTO) {
        GameStatus gameStatus = gameStatusDTO.getStatus();
        switch (gameStatus) {
            case SETUP:
                initActions();
                break;
            case YOUR_TURN:
                initTurn();
                view.notifyNewActions();
                break;

            case START:
                break;

            case FINISHED:
                winner = Optional.ofNullable(gameStatusDTO.getWinnerUsername());
                gameActive = false;
                break;
        }
        updateObservers();
    }

    // TODO avoid instance of?, refactor
    private void update(UpdateMessageDTO updateMessageDTO){
        if (updateMessageDTO instanceof DevelopmentCardsMessageDTO) {
            developmentCards = ((DevelopmentCardsMessageDTO) updateMessageDTO).getAvailableCards();
        }
        else if (updateMessageDTO instanceof FaithTrackMessageDTO) {
            faithTrack = ((FaithTrackMessageDTO) updateMessageDTO).getFaithTrack();
        }
        else if (updateMessageDTO instanceof MarketMessageDTO) {
            market = ((MarketMessageDTO) updateMessageDTO).getMarket();
        } else if (updateMessageDTO instanceof TurnTakersMessageDTO) {
            turnTakers = (ArrayList<ClientTurnTaker>) ((TurnTakersMessageDTO) updateMessageDTO)
                    .getClientTurnTakers();
        } else if (updateMessageDTO instanceof CurrentPlayerDTO) {
            currentPlayer = ((CurrentPlayerDTO) updateMessageDTO).getCurrentPlayer();
            ClientPlayer clientPlayer = (ClientPlayer) turnTakers.stream()
                    .filter(turnTaker -> turnTaker.getUsername().equals(currentPlayer.getUsername()))
                    .findAny().get();
            clientPlayer.setNonActiveLeaderCards(currentPlayer.getNonActiveLeaderCards());
        } else
        {
            System.exit(1);
        }

        updateObservers();
    }
    public void subscribe(ReactiveObserver reactiveObserver) {
        reactiveObservers.add(reactiveObserver);
    }

    private void updateObservers(){
        reactiveObservers.forEach(ReactiveObserver::update);
    }
    public void consumeAction(ClientAction action) {
        action.consumeFrom(actions);
        updateObservers();
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public synchronized void waitForActions() {
        while (actions.size() == 0){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
