package it.polimi.ingsw.model.turn_action;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.cards.AdditionalTradingRule;
import it.polimi.ingsw.model.cards.DevelopmentCard;
import it.polimi.ingsw.model.cards.TradingRule;
import it.polimi.ingsw.model.requirement.*;
import it.polimi.ingsw.model.turn_taker.Player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Activate some chosen production
 */
public class ActivateProduction implements TurnAction, RequireInputToRemove {
    private int faithPoints;
    private final ArrayList<DevelopmentCard> chosenDevelopmentCards;
    private final ArrayList<AdditionalTradingRule> chosenAdditionalTradingRules;
    private final Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse;
    private final Map<ResourceType,Integer> inputFromStrongbox;
    private final ArrayList<ResourceType> outputAnyChosen;
    private final ArrayList<ResourceType> inputAnyChosen;


    public ActivateProduction(ArrayList<DevelopmentCard> chosenDevelopmentCards, ArrayList<AdditionalTradingRule> chosenAdditionalTradingRules, Map<ResourceType,Map<Integer,Integer>> inputFromWarehouse, Map<ResourceType,Integer> inputFromStrongbox, ArrayList<ResourceType> inputAnyChosen, ArrayList<ResourceType> outputAnyChosen) {
        this.chosenDevelopmentCards = chosenDevelopmentCards;
        this.chosenAdditionalTradingRules = chosenAdditionalTradingRules;
        this.inputFromWarehouse = inputFromWarehouse;
        this.inputFromStrongbox = inputFromStrongbox;
        this.inputAnyChosen = inputAnyChosen;
        this.outputAnyChosen = outputAnyChosen;
        faithPoints = 0;
    }


    /**
     * {@inheritDoc}
     *
     * Activates some productions from the trading rules chosen by the player
     *
     * @param player the player that chooses the trading rules to activate
     */
    @Override
    public void play (Player player) {
        if(isUserInputCorrect(player)) {
            for (DevelopmentCard developmentCard : chosenDevelopmentCards) {
                takeResourcesFrom(player, developmentCard.getTradingRule());
                addResourcesTo(player, developmentCard.getTradingRule());
            }
            for (AdditionalTradingRule additionalTradingRule : chosenAdditionalTradingRules) {
                takeResourcesFrom(player, additionalTradingRule.getAdditionalTradingRule());
                addResourcesTo(player, additionalTradingRule.getAdditionalTradingRule());
            }
            addFaithPoints(player);
        }
        else {
            //if the game is corrupted, the game will end
            Game.getInstance().setEnded();
            Game.getInstance().setCorrupted();
        }
    }

    /**
     * Adds the faithPoints attribute to the passed player
     *
     * @param player to add faithPoints
     */
    private void addFaithPoints(Player player){
        if (faithPoints > 0)
            Game.getInstance().getFaithTrack().move(player,faithPoints);
    }

    /**
     * Removes the input of the TradingRule from the player's storages
     * @param player to remove resources to
     * @param tradingRule to get the input to remove
     */
    private void takeResourcesFrom(Player player, TradingRule tradingRule) {
        int amountToTake;
        for (ResourceType resourceToTake : tradingRule.getInput().keySet()) {
            amountToTake = tradingRule.getInput().get(resourceToTake);
            if (!resourceToTake.equals(ResourceType.Any)) {
                RequireInputToRemove.removeResources(resourceToTake, player, inputFromWarehouse, inputFromStrongbox);
            } else {
                while (amountToTake != 0) {
                    RequireInputToRemove.removeResources(inputAnyChosen.get(0), player, inputFromWarehouse, inputFromStrongbox);
                    inputAnyChosen.remove(0);
                    amountToTake--;
                }
            }
        }
    }


    /**
     * Adds Resources and faithPoints to the player from the output of the TradingRule
     *
     * @param player to add resources and faithPoints to
     * @param tradingRule to get the output to add
     */
    private void addResourcesTo(Player player, TradingRule tradingRule){
        if (!tradingRule.getOutput().isEmpty()) {
            insertOutput(player, tradingRule);
        }
        if (tradingRule.getFaithPoints() > 0)
            faithPoints += tradingRule.getFaithPoints();
    }

    /**
     * Adds Resources from TradingRule's output to player's storages
     *
     * @param player to add resources to
     * @param tradingRule to get Resources to add
     */
    private void insertOutput(Player player, TradingRule tradingRule){
        int amountToAdd;
        for (ResourceType resourceType: tradingRule.getOutput().keySet()){
            amountToAdd = tradingRule.getOutput().get(resourceType);
            if (resourceType.equals(ResourceType.Any)){
                if (amountToAdd < outputAnyChosen.size())
                    return;
                while (amountToAdd != 0){
                    player.getStrongbox().merge(outputAnyChosen.get(0),1,Integer::sum);
                    outputAnyChosen.remove(0);
                    amountToAdd --;
                }
            }
            else{
                player.getStrongbox().merge(resourceType,amountToAdd,Integer::sum);
            }
        }
    }


    /**
     * {@inheritDoc}
     *
     * Checks if the input from client is correct wrt player's state
     *
     * @param player to check on
     * @return true iff all the input is correct
     */
    @Override
    public boolean isUserInputCorrect(Player player){
        if(!allPresentAndUsable(player)){
            return false;
        }
        Map<ResourceType, Integer> totalInput = getTotalInput();
        return enoughResourcesInRightPlaces(player, totalInput);
    }

    /**
     * Checks if all chosen DevelopmentCards and LeaderCards to activate are present, active and their TradingRule usable
     *
     * @param player to check on
     * @return true iff all cards are present, active and eligible
     */
    private boolean allPresentAndUsable(Player player){
        Collection<Requirement> requirementsBasicProduction = new ArrayList<>();
        requirementsBasicProduction.add(new RequirementResource(0,ResourceType.Any));
        DevelopmentCard basicProduction = new DevelopmentCard(requirementsBasicProduction, DevelopmentColor.Any,0,0,player.getPersonalBoard().getBasicProduction(),"");
        for(DevelopmentCard chosenDevelopmentCard : chosenDevelopmentCards){
            if(!chosenDevelopmentCard.equals(basicProduction) &&
                    (!player.hasDevelopmentCard(chosenDevelopmentCard) || !chosenDevelopmentCard.getTradingRule().isUsable(player))) {
                //the chosenDevelopmentCard doesn't represent the basic production and it's not present or the TradingRule is not usable
                return false;
            }
        }
        for(AdditionalTradingRule chosenAdditionalTradingRule : chosenAdditionalTradingRules){
            if(!player.hasActiveLeaderCard(chosenAdditionalTradingRule) || !chosenAdditionalTradingRule.getAdditionalTradingRule().isUsable(player)){
                //the chosen LeaderCard it is not active or the TradingRule is not usable
                return false;
            }
        }
        return true;
    }

    /**
     * Unify the input from all chosen TradingRules into one
     *
     * @return unified input
     */
    private Map<ResourceType, Integer> getTotalInput(){
        Map<ResourceType, Integer> totalInput = new HashMap<>();
        ArrayList<TradingRule> allChosenTradingRules = new ArrayList<>();
        for(DevelopmentCard chosenDevelopmentCard : chosenDevelopmentCards) {
            allChosenTradingRules.add(chosenDevelopmentCard.getTradingRule());
        }
        for(AdditionalTradingRule chosenAdditionalTradingRule : chosenAdditionalTradingRules){
            allChosenTradingRules.add(chosenAdditionalTradingRule.getAdditionalTradingRule());
        }
        for(TradingRule chosenTradingRule : allChosenTradingRules){
            for(ResourceType inputResource : chosenTradingRule.getInput().keySet()){
                if(inputResource.equals(ResourceType.Any)){
                    //conversion from Any is needed
                    int inputAnyIndex = 0;
                    while(inputAnyIndex < inputAnyChosen.size()) {
                        //user passed at least the minimum amount of Resources to convert the Anys
                        totalInput.merge(inputAnyChosen.get(inputAnyIndex), 1, Integer::sum);
                        inputAnyIndex++;
                    }
                } else {
                    totalInput.merge(inputResource, chosenTradingRule.getInput().get(inputResource), Integer::sum);
                }
            }
        }
        return totalInput;
    }

    /**
     * Checks if the amounts of resources are valid and the relatives places (i.e. strongbox and Warehouse's depots)
     *
     * @param player to check on
     * @param totalInput unified input to check
     * @return true iff all amount and places are correct
     */
    private boolean enoughResourcesInRightPlaces(Player player, Map<ResourceType, Integer> totalInput){
        for(ResourceType inputResource : totalInput.keySet()){
            if(player.getResourceQuantity(inputResource) < totalInput.get(inputResource) ){
                //player has less resources than required
                return false;
            }
            if(!RequireInputToRemove.isInputQuantityRight(inputResource, totalInput.get(inputResource), inputFromWarehouse, inputFromStrongbox)){
                return false;
            }
            if(!RequireInputToRemove.arePlacesRight(inputResource, player, inputFromWarehouse, inputFromStrongbox)){
                return false;
            }
        }
        return true;
    }


}
