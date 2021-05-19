package it.polimi.ingsw.client;

import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.message.action_message.production_message.ChooseAnyInputOutputDTO;
import it.polimi.ingsw.message.action_message.production_message.ChooseTradingRulesDTO;
import it.polimi.ingsw.message.action_message.production_message.InputFromWhereDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class ActivateProduction implements ClientAction{
    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer) {
        ClientPlayer player = clientGameObserverProducer.getPlayers().stream().filter(clientPlayer -> clientPlayer.getUsername().equals(clientGameObserverProducer.getUsername())).findAny().get();
        synchronized (clientGameObserverProducer){
            try{
                if(clientGameObserverProducer.getTurnActionMessageDTO().stream().anyMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ActivateProductionDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ActivateProductionDTO activateProduction = (ActivateProductionDTO) clientGameObserverProducer.getTurnActionMessageDTO().get();
        activateProduction = chooseTradingRules(clientConnector,view, clientGameObserverProducer,activateProduction);

        //next action
        synchronized (clientGameObserverProducer){
            try{
                if (clientGameObserverProducer.getTurnActionMessageDTO().isEmpty())
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        //see if the are "resources any" to choose
        if (clientGameObserverProducer.getTurnActionMessageDTO().stream().anyMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ChooseAnyInputOutputDTO.class))){
            activateProduction = chooseAnyInputOutput(clientConnector,view, clientGameObserverProducer,activateProduction);
            synchronized (clientGameObserverProducer){
                try{
                    if (clientGameObserverProducer.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(InputFromWhereDTO.class)))
                        wait();
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
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
    }

    private ActivateProductionDTO chooseTradingRules(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer, ActivateProductionDTO activateProduction){

        synchronized (clientGameObserverProducer){
            try{
                if (clientGameObserverProducer.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ChooseTradingRulesDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ChooseTradingRulesDTO tradingRulesMessage = (ChooseTradingRulesDTO) clientGameObserverProducer.getTurnActionMessageDTO().get();
        tradingRulesMessage.setChosenTradingRules(view.chooseTradingRulesToActivate(activateProduction.getTradingRulesToChoose()));
        clientConnector.sendMessage(tradingRulesMessage);
        //update active production for the next action
        synchronized (clientGameObserverProducer){
            try{
                if (clientGameObserverProducer.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ActivateProductionDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        return (ActivateProductionDTO) clientGameObserverProducer.getTurnActionMessageDTO().get();
    }

    private ActivateProductionDTO chooseAnyInputOutput(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer, ActivateProductionDTO activateProduction){
        ChooseAnyInputOutputDTO chooseAnyInputOutput = (ChooseAnyInputOutputDTO) clientConnector.receiveMessage(ChooseAnyInputOutputDTO.class).get();
        chooseAnyInputOutput.setChosenInputAny(view.chooseAnyInput(activateProduction.getInputAnyToChoose()));
        chooseAnyInputOutput.setChosenOutputAny(view.chooseAnyOutput(activateProduction.getOutputAnyToChoose()));
        clientConnector.sendMessage(chooseAnyInputOutput);
        //update active production for the next action
        synchronized (clientGameObserverProducer){
            try{
                if (clientGameObserverProducer.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ActivateProductionDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        return (ActivateProductionDTO) clientGameObserverProducer.getTurnActionMessageDTO().get();

    }
    private void inputFromWhere(SocketConnector clientConnector, View view, ClientGameObserverProducer clientGameObserverProducer, ActivateProductionDTO activateProduction){
        InputFromWhereDTO inputFromWhere = new InputFromWhereDTO();
        inputFromWhere.setInputFromStrongbox(view.inputFromStrongbox(activateProduction.getInputFromStrongBoxToChoose()));
        inputFromWhere.setInputFromWarehouse(view.inputFromWarehouse(activateProduction.getInputFromWarehouseToChoose()));
        clientConnector.sendMessage(inputFromWhere);
    }
}
