package it.polimi.ingsw.client.action.turn;

import it.polimi.ingsw.client.ChosenLine;
import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.message.action_message.market_message.MarketAxis;
import it.polimi.ingsw.message.action_message.market_message.TakeFromMarketDTO;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;
import it.polimi.ingsw.model.warehouse.Warehouse;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class TakeFromMarket extends TurnAction {
    private Player player;
    private MarketAxis marketAxis;
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
        ArrayList<MarbleType> marblesTaken;
        ArrayList<ResourceType> resourceToChoose = new ArrayList<>();
        ArrayList<ResourceType> whiteMarbleChosen = new ArrayList<>();
        player = clientGameObserverProducer.getCurrentPlayer();
        chooseLine();
        marblesTaken = clientGameObserverProducer.getMarket().getMarblesFromLine(marketAxis,line);
        marblesTaken = (ArrayList<MarbleType>) marblesTaken.stream().filter(resourceType -> !resourceType.equals(MarbleType.Red)).collect(Collectors.toList());
        if (marblesTaken.contains(MarbleType.White) && !player.getActiveWhiteConversions().isEmpty())
            whiteMarbleChosen.addAll(chooseWhiteMarble( (int) marblesTaken.stream().filter(marbleType -> marbleType.equals(MarbleType.White)).count() , player.getActiveWhiteConversions()));
        marblesTaken = (ArrayList<MarbleType>) marblesTaken.stream().filter(resourceType -> !resourceType.equals(MarbleType.White)).collect(Collectors.toList());
        if (!whiteMarbleChosen.isEmpty())
            resourceToChoose.addAll(whiteMarbleChosen);
        marblesTaken.forEach(marbleType -> resourceToChoose.add(marbleType.conversion()));
        Map<ResourceType, Integer> resourceToDepot = resourceToDepot(resourceToChoose,player.getWarehouse());
        clientConnector.sendMessage(new TakeFromMarketDTO(marketAxis, line, resourceToDepot,whiteMarbleChosen));
    }

    private void chooseLine(){
        ChosenLine chooseLine = view.chooseLine();
        line = chooseLine.getLine();
        marketAxis = chooseLine.getMarketAxis();
    }

    private ArrayList<ResourceType> chooseWhiteMarble(int amount, ArrayList<ResourceType> activeWhiteConversions){
        return view.chooseWhiteMarble(amount,activeWhiteConversions);
    }
    private Map<ResourceType,Integer> resourceToDepot(ArrayList<ResourceType> resourceTypes, Warehouse warehouse){
        return view.resourceToDepot(resourceTypes, warehouse);
    }

}
