package it.polimi.ingsw.client.solo_game_action;


// TODO this action CANNOT be done client-side
public class DiscardDevelopmentCards {
//
//    public DiscardDevelopmentCards(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
//        super(clientConnector, view, clientGameObserverProducer);
//    }
//
//    @Override
//    public void doAction() {
//        //TODO a livello server prima vedo se riesco a scartare le due development cards
//        synchronized (clientGameObserverProducer.getSoloTokensDTO()){
//            try{
//                if (!clientGameObserverProducer.getSoloTokensDTO().getLast().getClass().equals(DiscardDevelopmentCardsDTO.class))
//                    wait();
//            }catch(InterruptedException e){
//                e.printStackTrace();
//            }
//        }
//        DiscardDevelopmentCardsDTO discardDevelopmentCardsDTO = (DiscardDevelopmentCardsDTO) clientGameObserverProducer.getSoloTokensDTO().getLast();
//        DiscardDevelopmentCardsDTO discardDevelopmentCardsUpdated= new DiscardDevelopmentCardsDTO(view.DevelopmentCardsToDiscard(discardDevelopmentCardsDTO.getDevelopmentCardsAvailable(),discardDevelopmentCardsDTO.getDevelopmentCardsToDiscard()),null);
//        clientConnector.sendMessage(discardDevelopmentCardsUpdated);
//
//        //OK message
//        synchronized (clientConnector.receiveAnyMessage()){
//            try{
//                if (clientConnector.receiveAnyMessage().isEmpty())
//                    wait();
//            }catch(InterruptedException e){
//                e.printStackTrace();
//            }
//        }
//    }
}
