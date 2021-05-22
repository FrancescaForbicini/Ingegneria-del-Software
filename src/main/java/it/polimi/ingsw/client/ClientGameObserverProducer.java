package it.polimi.ingsw.client;

import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.client.solo_game_action.SoloGameAction;
import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.message.action_message.solo_game_message.SoloTokenDTO;
import it.polimi.ingsw.message.update.GameFinishedDTO;
import it.polimi.ingsw.message.update.UpdateMessageDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.model.solo_game.SoloToken;
import it.polimi.ingsw.model.turn_taker.Opponent;
import it.polimi.ingsw.server.SocketConnector;

import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ClientGameObserverProducer implements Runnable{
    private String username;
    private Market market;
    private FaithTrack faithTrack;
    private ArrayList<DevelopmentCard> developmentCards;
    private ArrayList<ClientPlayer> players;
    private SocketConnector clientConnector;
    private ArrayList<LeaderCard> leaderCards;
    private ConcurrentLinkedDeque<SoloGameAction> soloGameActions;
    private ConcurrentLinkedDeque<SoloTokenDTO> soloTokensDTO;
    private Opponent opponent;

    // "Concurrent" data structures used by this runnable to PUBLISH updates
    private final ConcurrentLinkedDeque<TurnActionMessageDTO> pendingTurnDTOs;
    private final ConcurrentLinkedDeque<ClientAction> actions;

    public ClientGameObserverProducer(SocketConnector clientConnector, String username){
        this.username = username;
        this.clientConnector = clientConnector;
        actions = new ConcurrentLinkedDeque<>();
        pendingTurnDTOs = new ConcurrentLinkedDeque<>();
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

    public ConcurrentLinkedDeque<TurnActionMessageDTO> getPendingTurnDTOs(){
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
                break;
            }
            else if (messageDTO instanceof UpdateMessageDTO) {
                // TODO Update this object,
                //  maybe change actions
            }
            else if (messageDTO instanceof TurnActionMessageDTO) {
                pub((TurnActionMessageDTO) messageDTO);
            } else {
              System.exit(1);
            }
        }
    }
    private void pub(TurnActionMessageDTO turnActionMessageDTO) {
        synchronized (pendingTurnDTOs) {
            assert pendingTurnDTOs.size() == 0;
            pendingTurnDTOs.push(turnActionMessageDTO);
        }
    }
}
