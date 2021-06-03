package it.polimi.ingsw.client.solo_game_action;

// TODO this cannot be done client side
public class MoveBlackCross {
//    @Override
//    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
//        synchronized (clientGameObserverProducer.getSoloTokensDTO()){
//            try{
//                if (!clientGameObserverProducer.getSoloTokensDTO().getLast().getClass().equals(MoveBlackCrossDTO.class))
//                    wait();
//            }catch(InterruptedException e){
//                e.printStackTrace();
//            }
//        }
//        MoveBlackCrossDTO moveBlackCrossDTO = (MoveBlackCrossDTO) clientGameObserverProducer.getSoloTokensDTO().getLast();
//
//        MoveBlackCrossDTO moveBlackCrossUpdated = new MoveBlackCrossDTO(moveBlackCross(clientGameObserverProducer));
//        view.showMoveBlackCross(moveBlackCrossUpdated.getFaithTrack());
//        clientConnector.sendMessage(moveBlackCrossDTO);
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
//
//    }
//
//    private FaithTrack moveBlackCross(ClientGameObserverProducer clientGameObserverProducer){
//        clientGameObserverProducer.getFaithTrack().move(clientGameObserverProducer.getOpponent(),2);
//        return clientGameObserverProducer.getFaithTrack();
//    }
}

