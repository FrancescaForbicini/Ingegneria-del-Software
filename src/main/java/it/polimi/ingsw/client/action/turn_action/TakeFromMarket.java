package it.polimi.ingsw.client.action.turn_action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.action_message.market_message.*;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.Map;

public class TakeFromMarket extends ClientAction {
    private ClientPlayer player;

    public TakeFromMarket(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        player = clientGameObserverProducer.getPlayers().stream().filter(clientPlayer -> clientPlayer.getUsername().equals(clientGameObserverProducer.getUsername())).findAny().get();
        synchronized (clientGameObserverProducer.getPendingTurnDTOs()){
            try{
                if(clientGameObserverProducer.getPendingTurnDTOs().isEmpty()) {
                    clientGameObserverProducer.wait();
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        TakeFromMarketDTO takeFromMarketDTO = (TakeFromMarketDTO) clientGameObserverProducer.getPendingTurnDTOs().getLast();
        //Choose sortWarehouse or chooseLine
        if (view.askSortWarehouse()){
            sortWarehouse(clientConnector,view,takeFromMarketDTO, clientGameObserverProducer);
            //upgrade warehouse
            //upgrade warehouse
            synchronized (clientGameObserverProducer){
                try{
                    if (!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(TakeFromMarketDTO.class))
                        clientGameObserverProducer.wait();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        chooseLine(clientConnector,view,player, clientGameObserverProducer);
        //update resource any to choose or resource to put in the warehouse
        synchronized (clientGameObserverProducer) {
            try {
                if (!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(TakeFromMarketDTO.class))
                    clientGameObserverProducer.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        TakeFromMarketDTO takeFromMarket = (TakeFromMarketDTO) clientGameObserverProducer.getPendingTurnDTOs().getLast();
        //Checks if there are Resource.Any to choose
        if (takeFromMarket.getResourcesAnyToChoose().size()!=0){
            chooseResourceAny(clientConnector,view,takeFromMarket, clientGameObserverProducer);
            //update for the new resources to put in the warehouse
            synchronized (clientGameObserverProducer.getPendingTurnDTOs()){
                try{
                    if (!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(TakeFromMarketDTO.class))
                        clientGameObserverProducer.wait();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        resourceToDepot(clientConnector,view,takeFromMarket, clientGameObserverProducer);

        //OK message
        synchronized (clientGameObserverProducer){
            try{
                if (clientConnector.receiveAnyMessage().isEmpty())
                    clientGameObserverProducer.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void sortWarehouse(SocketConnector clientConnector, View view, TakeFromMarketDTO takeFromMarketDTO,ClientGameObserverProducer clientGameObserverProducer) {
            SortWarehouseDTO sortWarehouseMessage = new SortWarehouseDTO(view.sortWarehouse(takeFromMarketDTO.getWarehouse()));
            clientConnector.sendMessage(sortWarehouseMessage);
    }

    private void chooseLine(SocketConnector clientConnector, View view, ClientPlayer player, ClientGameObserverProducer clientGameObserverProducer){
        synchronized (clientGameObserverProducer.getPendingTurnDTOs()){
            try{
                if (!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(ChooseLineDTO.class)) {
                    clientGameObserverProducer.wait();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        Map<String, Integer> line = view.chooseLine();
        String rc = line.keySet().toString();
        int num = line.get(rc);
        ChooseLineDTO chooseLine = new ChooseLineDTO(rc,num);
        clientConnector.sendMessage(chooseLine);
    }

    private void chooseResourceAny(SocketConnector clientConnector, View view, TakeFromMarketDTO takeFromMarket, ClientGameObserverProducer clientGameObserverProducer){
        ChooseResourceAnyDTO chooseResourceAny = new ChooseResourceAnyDTO(view.chooseResourceAny(takeFromMarket.getResourcesAnyToChoose(), takeFromMarket.getActiveWhiteMarbleConversion()));
        clientConnector.sendMessage(chooseResourceAny);
    }

    private void resourceToDepot(SocketConnector clientConnector, View view, TakeFromMarketDTO takeFromMarket, ClientGameObserverProducer clientGameObserverProducer){
        synchronized (clientGameObserverProducer.getPendingTurnDTOs()){
            try{
                if (clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(ResourceToDepotDTO.class))
                    clientGameObserverProducer.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ResourceToDepotDTO resourceToDepot = new ResourceToDepotDTO(view.resourceToDepot(takeFromMarket.getResourcesTaken(),player));
        clientConnector.sendMessage(resourceToDepot);
    }
}
