package it.polimi.ingsw.client.action;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.client.ClientPlayer;
import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.message.action_message.production_message.ChooseAnyInputOutputDTO;
import it.polimi.ingsw.message.action_message.production_message.ChooseTradingRulesDTO;
import it.polimi.ingsw.message.action_message.production_message.InputFromWhereDTO;
import it.polimi.ingsw.model.requirement.ResourceType;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

import java.util.ArrayList;


public class ActivateProduction extends ClientAction {
    public ActivateProduction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        super(clientConnector, view, clientGameObserverProducer);
    }

    @Override
    public void doAction() {
        ClientPlayer player = clientGameObserverProducer.getPlayers().stream().filter(clientPlayer -> clientPlayer.getUsername().equals(clientGameObserverProducer.getUsername())).findAny().get();
        synchronized (clientGameObserverProducer.getPendingTurnDTOs()){
            try{
                if (!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(ActivateProductionDTO.class))
                    clientGameObserverProducer.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ActivateProductionDTO activateProduction = (ActivateProductionDTO) clientGameObserverProducer.getPendingTurnDTOs().getLast();
        chooseTradingRules(clientConnector,view, clientGameObserverProducer,activateProduction);

        //update of the trading rules chosen
        synchronized (clientGameObserverProducer){
            try{
                if (!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(ActivateProductionDTO.class))
                    clientGameObserverProducer.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        //see if the are "resources any" to choose
        if (activateProduction.getInputAnyToChoose().size()!=0){
            chooseAnyInputOutput(clientConnector,view, clientGameObserverProducer,activateProduction);
            //update of resources chosen
            synchronized (clientGameObserverProducer){
                try{
                    if (!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(ActivateProductionDTO.class))
                        clientGameObserverProducer.wait();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        inputFromWhere(clientConnector,view, clientGameObserverProducer,activateProduction);

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

    private void chooseTradingRules(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer, ActivateProductionDTO activateProduction){

        synchronized (clientGameObserverProducer.getPendingTurnDTOs()){
            try{
                if (clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(ChooseTradingRulesDTO.class))
                    clientGameObserverProducer.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ChooseTradingRulesDTO tradingRulesMessage = new ChooseTradingRulesDTO(view.chooseTradingRulesToActivate(activateProduction.getTradingRulesToChoose()));
        clientConnector.sendMessage(tradingRulesMessage);
    }

    private void chooseAnyInputOutput(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer, ActivateProductionDTO activateProduction){
        ArrayList<ResourceType> chooseInput = new ArrayList<>();
        ArrayList<ResourceType> chooseOutput = new ArrayList<>();
        synchronized (clientGameObserverProducer.getPendingTurnDTOs()){
            try{
                if (!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(ChooseAnyInputOutputDTO.class))
                    clientGameObserverProducer.wait();
            }catch (InterruptedException e){
                e.printStackTrace();
            }
        }
        if (activateProduction.getInputAnyToChoose().size()!=0)
            chooseInput = view.chooseAnyInput(activateProduction.getInputAnyToChoose());
        else
            chooseInput = null;
        if (activateProduction.getOutputAnyToChoose().size()!=0)
            chooseOutput = view.chooseAnyOutput(activateProduction.getOutputAnyToChoose());
        else
            chooseOutput = null;
        ChooseAnyInputOutputDTO chooseAnyInputOutput = new ChooseAnyInputOutputDTO (chooseInput,chooseOutput);
        clientConnector.sendMessage(chooseAnyInputOutput);
    }


    private void inputFromWhere(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer, ActivateProductionDTO activateProduction){
        synchronized (clientGameObserverProducer.getPendingTurnDTOs()){
            try{
                if (!clientGameObserverProducer.getPendingTurnDTOs().getLast().getClass().equals(InputFromWhereDTO.class))
                    clientGameObserverProducer.wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        InputFromWhereDTO inputFromWhere = new InputFromWhereDTO(view.inputFromWarehouse(activateProduction.getInputFromWarehouseToChoose()),view.inputFromStrongbox(activateProduction.getInputFromStrongBoxToChoose()));
        clientConnector.sendMessage(inputFromWhere);
    }
}
