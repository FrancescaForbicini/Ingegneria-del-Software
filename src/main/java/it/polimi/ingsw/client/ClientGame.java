package it.polimi.ingsw.client;

import it.polimi.ingsw.message.MessageDTO;
import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.message.update.UpdateMessageDTO;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.market.Market;
import it.polimi.ingsw.server.SocketConnector;

import java.util.ArrayList;
import java.util.Optional;

public class ClientGame implements Runnable{
    private String username;
    private Market market;
    private FaithTrack faithTrack;
    private ArrayList<DevelopmentCard> developmentCards;
    private ArrayList<ClientPlayer> players;
    private SocketConnector clientConnector;
    private Optional<TurnActionMessageDTO> turnActionMessageDTO;
    private Optional<UpdateMessageDTO> updateMessageDTO;

    public Optional<UpdateMessageDTO> getUpdateMessageDTO(){
        return this.updateMessageDTO;
    }
    public Optional<TurnActionMessageDTO> getTurnActionMessageDTO() {
        return turnActionMessageDTO;
    }

    public void setTurnActionMessageDTO(Optional<TurnActionMessageDTO> turnActionMessageDTO) {
        this.turnActionMessageDTO = turnActionMessageDTO;
    }

    public ClientGame(SocketConnector clientConnector, String username){
        this.clientConnector = clientConnector;
        this.username = username;
    }

    public String getUsername(){
        return this.username;
    }
    public Market getMarket() {
        return market;
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

    public void setDevelopmentCards(ArrayList<DevelopmentCard> developmentCards) {
        this.developmentCards = developmentCards;
    }

    public void setPlayers(ArrayList<ClientPlayer> players){
        this.players = players;
    }

    @Override
    public void run(){
        MessageDTO messageDTO ;
        do {
            messageDTO = clientConnector.receiveAnyMessage().get();
        }while(messageDTO.getClass() == null);
       notifyAll();
    }
}
