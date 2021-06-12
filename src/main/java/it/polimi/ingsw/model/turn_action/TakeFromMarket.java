package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.message.action_message.market_message.MarketAxis;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.market.MarbleType;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class TakeFromMarket implements TurnAction{
    private final MarketAxis marketAxis;
    private final int num;
    private ArrayList<ResourceType> resourcesTaken;
    private boolean takeFromMarket;
    private final Map<ResourceType,ArrayList<Integer>> resourceToDepot;
    private int faithPoints;
    private int discard;
    private final ArrayList<ResourceType> whiteMarbleChosen;


    public TakeFromMarket(MarketAxis marketAxis, int line, Map<ResourceType, ArrayList<Integer>> resourceToDepot, ArrayList<ResourceType> whiteMarbleChosen){
        this.marketAxis = marketAxis;
        this.num = line;
        this.takeFromMarket = false;
        this.resourceToDepot = resourceToDepot;
        this.whiteMarbleChosen = whiteMarbleChosen;
        this.faithPoints = 0;
        this.resourcesTaken = new ArrayList<>();
        this.discard = 0;
    }

    @Override
    public boolean isFinished(){
        return takeFromMarket;
    }

    @Override
    public void play(Player player) {
        getResourceFromMarket(marketAxis, num);
        int quantityAdd = 0;
        if (!whiteMarbleChosen.isEmpty())
            resourcesTaken.addAll(whiteMarbleChosen);
        assignFaithPoints(player);
        for (ResourceType resourceType : resourcesTaken) {
            int amount = (int) resourcesTaken.stream().filter(resource -> resource.equals(resourceType)).count();
            if (resourceToDepot.containsKey(resourceType)) {
                for (Integer depotID : resourceToDepot.get(resourceType)) {
                    if (depotID == -1) {
                        discard++;
                    } else
                        if (player.getPersonalBoard().addResourceToWarehouse(resourceType, 1, depotID)) {
                            quantityAdd ++;
                    }
                }
                if (quantityAdd < amount){
                    discard += amount - quantityAdd;
                }
                quantityAdd = 0;
            }
            takeFromMarket = true;
        }
    }

    /**
     * Gets resources from the market
     * @param marketAxis the row or the column chosen
     * @param num the number of the row or the column chosen
     */
    public void getResourceFromMarket(MarketAxis marketAxis, int num){
        ArrayList<MarbleType> marbles = Game.getInstance().getMarket().getMarblesFromLine(marketAxis,num,true);
        faithPoints += (int) marbles.stream().filter(marble -> marble.equals(MarbleType.Red)).count();
        marbles = (ArrayList<MarbleType>) marbles.stream().filter(marbleType -> !marbleType.equals(MarbleType.Red) && !marbleType.equals(MarbleType.White)).collect(Collectors.toList());
        marbles.forEach(marble -> resourcesTaken.add(marble.conversion()));
    }
    private void assignFaithPoints(Player player){
        if (faithPoints > 0)
            Game.getInstance().getFaithTrack().move(player,faithPoints);
        if (discard > 0){
            for(Player player1 : Game.getInstance().getPlayers()){
                if (!player1.getUsername().equals(player.getUsername()))
                    Game.getInstance().getFaithTrack().move(player1,discard);
            }
        }
    }

}
