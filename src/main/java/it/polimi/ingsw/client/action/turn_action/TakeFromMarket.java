package it.polimi.ingsw.client.action.turn_action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.action.ClientAction;
import it.polimi.ingsw.message.action_message.market_message.TakeFromMarketDTO;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.model.warehouse.WarehouseDepot;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Map;

public class TakeFromMarket extends ClientAction {
    private Player player;
    private String rc;
    private int line;

    public TakeFromMarket(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }


    @Override
    public boolean isDoable() {
        return true;
    }

    @Override
    public void doAction() {
        TakeFromMarketDTO takeFromMarketDTO = new TakeFromMarketDTO();
        ArrayList<MarbleType> marblesTaken;
        ArrayList<ResourceType> resourceToChoose = new ArrayList<>();
        int faithPoints = 0;
        player = clientGameObserverProducer.getCurrentPlayer();
        //ask to sort warehouse only if the warehouse is not empty
        if (player.getWarehouse().getWarehouseDepots().stream().noneMatch(WarehouseDepot::isEmpty) && view.askSortWarehouse()) {
            takeFromMarketDTO.setWarehouse(sortWarehouse());
        }
        else
            takeFromMarketDTO.setWarehouse(player.getWarehouse());
        chooseLine();
        takeFromMarketDTO.setLine(line);
        takeFromMarketDTO.setRc(rc);
        marblesTaken = clientGameObserverProducer.getMarket().getMarblesFromLine(rc,line);
        if (marblesTaken.contains(MarbleType.White)){
            resourceToChoose.addAll(chooseWhiteMarble( (int) marblesTaken.stream().filter(marbleType -> marbleType.equals(MarbleType.White)).count() , player.getActiveWhiteConversions()));
            marblesTaken.remove(MarbleType.White);
            takeFromMarketDTO.setResourceAnyChosen(resourceToChoose);
            if (resourceToChoose.contains(ResourceType.Any)) {
                faithPoints = (int) resourceToChoose.stream().filter(resourceType -> resourceType.equals(ResourceType.Any)).count();
                resourceToChoose.remove(ResourceType.Any);
                marblesTaken.remove(MarbleType.Red);
                takeFromMarketDTO.setFaithPoints(faithPoints);
            }
        }
        //Add resource to choose
        marblesTaken.forEach(marbleType -> resourceToChoose.add(marbleType.conversion()));
        //Set the resource chosen
        takeFromMarketDTO.setResourcesTaken(resourceToDepot(resourceToChoose,takeFromMarketDTO.getWarehouse()));
        clientConnector.sendMessage(takeFromMarketDTO);
    }

    private Warehouse sortWarehouse(){
        return view.sortWarehouse(player.getWarehouse());
    }
    private void chooseLine(){
        Map<String,Integer> lineChosen;
        lineChosen = view.chooseLine();
        rc = lineChosen.keySet().toString();
        line = lineChosen.get(rc);
    }

    private ArrayList<ResourceType> chooseWhiteMarble(int amount, ArrayList<ResourceType> activeWhiteConversions){
        return view.chooseWhiteMarble(amount,activeWhiteConversions);
    }
    private Map<ResourceType,Integer> resourceToDepot(ArrayList<ResourceType> resourceTypes, Warehouse warehouse){
        return view.resourceToDepot(resourceTypes, warehouse);
    }

}
