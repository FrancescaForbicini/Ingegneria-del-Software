package it.polimi.ingsw.client;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;
import it.polimi.ingsw.message.action_message.market_message.*;
import it.polimi.ingsw.message.update.PlayerMessageDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.Map;

public class TakeFromMarket implements ClientAction{

    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGame clientGame)  {
        ClientPlayer player = clientGame.getPlayers().stream().filter(clientPlayer -> clientPlayer.getUsername().equals(clientGame.getUsername())).findAny().get();
        synchronized (clientGame){
            try{
                if(clientGame.getTurnActionMessageDTO().isEmpty()) {
                    wait();
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        if (clientGame.getTurnActionMessageDTO().stream().anyMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(SortWarehouseDTO.class))){
            sortWarehouse(clientConnector,view,clientGame);
        }
        chooseLine(clientConnector,view,player,clientGame);
        //update resource any to choose or resource to put in the warehouse
        synchronized (clientGame) {
            try {
                if (clientGame.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(TakeFromMarketDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        TakeFromMarketDTO takeFromMarket = (TakeFromMarketDTO) clientGame.getTurnActionMessageDTO().get();
        //Checks if there are Resource.Any to choose
        if (takeFromMarket.getResourceAnyToChoose().size()!=0){
            chooseResourceAny(clientConnector,view,takeFromMarket,clientGame);
            //update for the new resources to put in the warehouse
            synchronized (clientGame){
                try{
                    if (clientGame.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(TakeFromMarketDTO.class)))
                        wait();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        resourceToDepot(clientConnector,view,takeFromMarket,clientGame);

        //OK message
        synchronized (clientGame){
            try{
                if (clientConnector.receiveAnyMessage().isEmpty())
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private void sortWarehouse(SocketConnector clientConnector, View view, ClientGame clientGame) {
        if (view.askSortWarehouse()) {
            SortWarehouseDTO sortWarehouseMessage = new SortWarehouseDTO();
            sortWarehouseMessage.setWarehouse(view.sortWarehouse(sortWarehouseMessage.getWarehouse()));
            clientConnector.sendMessage(sortWarehouseMessage);
            //TODO il server fa upgrade del warehouse quando mando il nuovo warehouse per il sortWarehouse
            synchronized (clientGame){
                try{
                    if (clientGame.getUpdateMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(PlayerMessageDTO.class)))
                        wait();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private void chooseLine(SocketConnector clientConnector, View view, ClientPlayer player, ClientGame clientGame){
        synchronized (clientGame){
            try{
                if (clientGame.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ChooseLineDTO.class))) {
                    wait();
                }
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        ChooseLineDTO chooseLine = (ChooseLineDTO) clientGame.getTurnActionMessageDTO().get();
        Map<String, Integer> line = view.chooseLine();
        chooseLine.setRc(line.keySet().toString());
        chooseLine.setNum(line.get(chooseLine.getRc()));
        clientConnector.sendMessage(chooseLine);
    }

    private void chooseResourceAny(SocketConnector clientConnector, View view, TakeFromMarketDTO takeFromMarket, ClientGame clientGame){
        ChooseResourceAnyDTO chooseResourceAny = new ChooseResourceAnyDTO();
        chooseResourceAny.setChosenResourceAny(view.chooseResourceAny(takeFromMarket.getResourceAnyToChoose(), takeFromMarket.getActiveWhiteMarbleConversion()));
        clientConnector.sendMessage(chooseResourceAny);
    }

    private void resourceToDepot(SocketConnector clientConnector, View view, TakeFromMarketDTO takeFromMarket, ClientGame clientGame){
        synchronized (clientGame){
            try{
                if (clientGame.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ResourceToDepotDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ResourceToDepotDTO resourceToDepot = (ResourceToDepotDTO) clientGame.getTurnActionMessageDTO().get();
        resourceToDepot.setResourceToDepot(view.resourceToDepot(takeFromMarket.getResourcesTaken()));
        clientConnector.sendMessage(resourceToDepot);
        //TODO come in sortWarehouse
        synchronized (clientGame){
            try{
                if (clientGame.getUpdateMessageDTO().stream().noneMatch(updateMessageDTO -> updateMessageDTO.getClass().equals(PlayerMessageDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }
}
