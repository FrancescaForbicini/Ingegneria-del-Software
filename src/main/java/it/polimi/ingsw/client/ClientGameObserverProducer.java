package it.polimi.ingsw.client;

import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.show.ShowDevelopmentCards;
import it.polimi.ingsw.client.show.ShowFaithTrack;
import it.polimi.ingsw.client.show.ShowMarket;
import it.polimi.ingsw.client.show.ShowPlayer;
import it.polimi.ingsw.client.solo_game_action.SoloGameAction;
import it.polimi.ingsw.message.GameFinishedDTO;
import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.ActionMessageDTO;
import it.polimi.ingsw.message.action_message.solo_game_message.SoloTokenDTO;
import it.polimi.ingsw.message.update.*;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;

public class ClientGameObserverProducer implements Runnable{
    private String username;
    private Market market;
    private FaithTrack faithTrack;
    private View view;
    private ArrayList<DevelopmentCard> developmentCards;
    private ArrayList<ClientPlayer> players;
    private SocketConnector clientConnector;
    private ArrayList<LeaderCard> leaderCards;
    private ConcurrentLinkedDeque<SoloGameAction> soloGameActions;
    private ConcurrentLinkedDeque<SoloTokenDTO> soloTokensDTO;
    private Opponent opponent;

    // "Concurrent" data structures used by this runnable to PUBLISH updates
    private final ConcurrentLinkedDeque<ActionMessageDTO> pendingTurnDTOs;
    private final ConcurrentLinkedDeque<ClientAction> actions;

    public ClientGameObserverProducer(SocketConnector clientConnector, View view, String username){
        this.username = username;
        this.clientConnector = clientConnector;
        this.view = view;
        actions = new ConcurrentLinkedDeque<>();
        pendingTurnDTOs = new ConcurrentLinkedDeque<>();
        addUpdateActions();
    }

    private void addUpdateActions() {
        actions.push(new ShowMarket(clientConnector, view, this));
        actions.push(new ShowDevelopmentCards(clientConnector, view, this));
        actions.push(new ShowFaithTrack(clientConnector, view, this));
        actions.push(new ShowPlayer(clientConnector, view, this));
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

    public ArrayList<ClientPlayer> getPlayers() {
        return players;
    }

    public void setMarket(Market market) {
        this.market = market;
    }

    public void setFaithTrack(FaithTrack faithTrack) {
        this.faithTrack = faithTrack;
    }

    public ArrayList<LeaderCard> getLeaderCards() {
        return leaderCards;
    }

    public void setLeaderCards(ArrayList<LeaderCard> leaderCards) {
        this.leaderCards = leaderCards;
    }

    public ConcurrentLinkedDeque<ActionMessageDTO> getPendingTurnDTOs(){
        return this.pendingTurnDTOs;
    }

    public ConcurrentLinkedDeque<SoloGameAction> getSoloGameActions(){ return this.soloGameActions;}

    public void setOpponent(Opponent opponent) {
        this.opponent = opponent;
    }
    public Opponent getOpponent() {
        return this.opponent;
    }

    public void setSoloGameActions(ConcurrentLinkedDeque<SoloGameAction> soloGameActions) {
        this.soloGameActions = soloGameActions;
    }

    public void setSoloTokensDTO(ConcurrentLinkedDeque<SoloTokenDTO> soloTokensDTO) {
        this.soloTokensDTO = soloTokensDTO;
    }

    public ConcurrentLinkedDeque<SoloTokenDTO> getSoloTokensDTO() {
        return soloTokensDTO;
    }

    public void run(){
        MessageDTO messageDTO;
        while(true) {
            messageDTO = clientConnector.receiveAnyMessage().get();
            if (messageDTO instanceof GameFinishedDTO) {
                view.showWinner(((GameFinishedDTO) messageDTO).getWinnerUsername());
                // TODO cleanup actions, no action are possible...
                break;
            }
            else if (messageDTO instanceof UpdateMessageDTO) {
                update((UpdateMessageDTO) messageDTO);
            }
            else if (messageDTO instanceof ActionMessageDTO) {
                ActionMessageDTO actionMessageDTO = (ActionMessageDTO)messageDTO;
                if (!actionMessageDTO.couldBeAnAction())
                    return;
                pub((ActionMessageDTO) messageDTO);
                view.notifyNewActions();
            } else {
              System.exit(1);
            }
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

    // TODO avoid instance of?
    private void update(UpdateMessageDTO updateMessageDTO){
        if (updateMessageDTO instanceof DevelopmentCardsMessageDTO) {
            developmentCards = ((DevelopmentCardsMessageDTO) updateMessageDTO).getAvailableCards();
        }
        else if (updateMessageDTO instanceof FaithTrackMessageDTO) {
            faithTrack = ((FaithTrackMessageDTO) updateMessageDTO).getFaithTrack();
        }
        else if (updateMessageDTO instanceof MarketMessageDTO) {
            market = ((MarketMessageDTO) updateMessageDTO).getMarket();
        }
        else if (updateMessageDTO instanceof PlayerMessageDTO) {
            // TODO:
            // find correct player
            // replace with ((PlayerMessageDTO) updateMessageDTO).getClientPlayer()

        } else if (updateMessageDTO instanceof PlayersMessageDTO) {
            players = (ArrayList<ClientPlayer>) ((PlayersMessageDTO) updateMessageDTO)
                    .getPlayerMessageDTOList().stream().map(PlayerMessageDTO::getClientPlayer)
                    .collect(Collectors.toList());

        } else if (updateMessageDTO instanceof TurnMessageDTO) {
            // TODO
        } else {
            System.exit(1);
        }
    }
}
