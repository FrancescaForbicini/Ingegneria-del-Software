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
    private final ArrayList<ResourceType> resourcesTaken;
    private final Map<ResourceType,ArrayList<Integer>> resourceToDepot;
    private int faithPoints;
    private int whiteMarble;
    private int discard;
    private final ArrayList<ResourceType> whiteMarbleChosen;


    public TakeFromMarket(MarketAxis marketAxis, int line, Map<ResourceType, ArrayList<Integer>> resourceToDepot, ArrayList<ResourceType> whiteMarbleChosen){
        this.marketAxis = marketAxis;
        this.num = line;
        this.resourceToDepot = resourceToDepot;
        this.whiteMarbleChosen = whiteMarbleChosen;
        this.faithPoints = 0;
        this.whiteMarble = 0;
        this.resourcesTaken = new ArrayList<>();
        this.discard = 0;
    }

    @Override
    public void play(Player player) {
        getResourceFromMarket(marketAxis, num);
        int quantityAdd = 0;
        if (!addWhiteMarbleChosen(player)){
            //TODO esplodi
        }
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
        }
        assignFaithPoints(player);
    }

    /**
     * Checks if the conversion of the white marble is correct and adds the white marble chosen to the resource to put
     *
     * @param player the player that has to choose the conversion of the white marble
     * @return true iff the conversion of the white marble is correct
     */
    private boolean addWhiteMarbleChosen(Player player){
        if (whiteMarble != 0){
            if (whiteMarble != whiteMarbleChosen.size())
                return false;
            for (ResourceType resourceType: whiteMarbleChosen){
                if (player.getActiveWhiteConversions().stream().noneMatch(resource -> resource.equals(resourceType))) {
                    return false;
                }
            }
            resourcesTaken.addAll(whiteMarbleChosen);
        }
        return true;
    }


    /**
     * Gets resources from the market
     *
     * @param marketAxis the row or the column chosen
     * @param num the number of the row or the column chosen
     */
    private void getResourceFromMarket(MarketAxis marketAxis, int num){
        ArrayList<MarbleType> marbles = Game.getInstance().getMarket().getMarblesFromLine(marketAxis,num,true);
        faithPoints = (int) marbles.stream().filter(marble -> marble.equals(MarbleType.Red)).count();
        whiteMarble = (int) marbles.stream().filter(marble -> marble.equals(MarbleType.White)).count();
        marbles = (ArrayList<MarbleType>) marbles.stream().filter(marbleType -> !marbleType.equals(MarbleType.Red) && !marbleType.equals(MarbleType.White)).collect(Collectors.toList());
        marbles.forEach(marble -> resourcesTaken.add(marble.convertToResource()));
    }

    /**
     * Assigns the faith points to the players
     *
     * @param player the player to assign faith points
     */
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
