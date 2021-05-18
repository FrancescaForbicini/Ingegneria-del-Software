package it.polimi.ingsw.client;

import it.polimi.ingsw.message.action_message.market_message.*;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.Map;

public class TakeFromMarket implements ClientAction{

    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGame clientGame)  {
        ClientPlayer player = clientGame.getPlayers().stream().filter(clientPlayer -> clientPlayer.getUsername().equals(clientGame.getUsername())).findAny().get();
        if (view.sortWarehouse()) {
            SortWarehouseDTO sortWarehouseMessage = new SortWarehouseDTO();
            sortWarehouseMessage.setWarehouse(view.sortWarehouse(player.getWarehouse()));
            clientConnector.sendMessage(sortWarehouseMessage);
        }
        synchronized (clientGame){
            try{
                if(clientGame.getTurnActionMessageDTO().isEmpty())
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }

        }
        ChooseLineDTO chooseLine = (ChooseLineDTO) clientConnector.receiveMessage(ChooseLineDTO.class).get();
        Map<String, Integer> line = view.chooseLine();
        chooseLine.setRc(line.keySet().toString());
        chooseLine.setNum(line.get(chooseLine.getRc()));
        clientConnector.sendMessage(chooseLine);
        ChooseResourceAnyDTO chooseResourceAny = (ChooseResourceAnyDTO) clientConnector.receiveMessage(ChooseResourceAnyDTO.class).get();
        chooseResourceAny.setChosenResourceAny(view.chooseResourceAny(takeFromMarket.getResourceAnyToChoose(), takeFromMarket.getActiveWhiteMarbleConversion()));
        clientConnector.sendMessage(chooseResourceAny);
        ResourceToDepotDTO resourceToDepot = (ResourceToDepotDTO) clientConnector.receiveMessage(ResourceToDepotDTO.class).get();
        resourceToDepot.setResourceToDepot(view.resourceToDepot(takeFromMarket.getResourcesTaken()));
        clientConnector.sendMessage(resourceToDepot);
    }
}
