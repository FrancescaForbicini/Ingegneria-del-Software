package it.polimi.ingsw.client;

import it.polimi.ingsw.message.action_message.production_message.ActivateProductionDTO;
import it.polimi.ingsw.message.action_message.production_message.ChooseAnyInputOutputDTO;
import it.polimi.ingsw.message.action_message.production_message.ChooseTradingRulesDTO;
import it.polimi.ingsw.message.action_message.production_message.InputFromWhereDTO;
import it.polimi.ingsw.server.SocketConnector;
import it.polimi.ingsw.view.View;

public class ActivateProduction implements ClientAction{
    @Override
    public void doAction(SocketConnector clientConnector, View view, ClientGame clientGame) {
        ClientPlayer player = clientGame.getPlayers().stream().filter(clientPlayer -> clientPlayer.getUsername().equals(clientGame.getUsername())).findAny().get();
        synchronized (clientGame){
            try{
                if(clientGame.getTurnActionMessageDTO().stream().anyMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ActivateProductionDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ActivateProductionDTO activateProduction = (ActivateProductionDTO) clientGame.getTurnActionMessageDTO().get();
        activateProduction = chooseTradingRules(clientConnector,view,clientGame,activateProduction);

        //next action
        synchronized (clientGame){
            try{
                if (clientGame.getTurnActionMessageDTO().isEmpty())
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }

        //see if the are "resources any" to choose
        if (clientGame.getTurnActionMessageDTO().stream().anyMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ChooseAnyInputOutputDTO.class))){
            activateProduction = chooseAnyInputOutput(clientConnector,view,clientGame,activateProduction);
            synchronized (clientGame){
                try{
                    if (clientGame.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(InputFromWhereDTO.class)))
                        wait();
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
        }
        inputFromWhere(clientConnector,view,clientGame,activateProduction);
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

    private ActivateProductionDTO chooseTradingRules(SocketConnector clientConnector, View view, ClientGame clientGame, ActivateProductionDTO activateProduction){

        synchronized (clientGame){
            try{
                if (clientGame.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ChooseTradingRulesDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        ChooseTradingRulesDTO tradingRulesMessage = (ChooseTradingRulesDTO) clientGame.getTurnActionMessageDTO().get();
        tradingRulesMessage.setChosenTradingRules(view.chooseTradingRulesToActivate(activateProduction.getTradingRulesToChoose()));
        clientConnector.sendMessage(tradingRulesMessage);
        //update active production for the next action
        synchronized (clientGame){
            try{
                if (clientGame.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ActivateProductionDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        return (ActivateProductionDTO) clientGame.getTurnActionMessageDTO().get();
    }

    private ActivateProductionDTO chooseAnyInputOutput(SocketConnector clientConnector, View view, ClientGame clientGame, ActivateProductionDTO activateProduction){
        ChooseAnyInputOutputDTO chooseAnyInputOutput = (ChooseAnyInputOutputDTO) clientConnector.receiveMessage(ChooseAnyInputOutputDTO.class).get();
        chooseAnyInputOutput.setChosenInputAny(view.chooseAnyInput(activateProduction.getInputAnyToChoose()));
        chooseAnyInputOutput.setChosenOutputAny(view.chooseAnyOutput(activateProduction.getOutputAnyToChoose()));
        clientConnector.sendMessage(chooseAnyInputOutput);
        //update active production for the next action
        synchronized (clientGame){
            try{
                if (clientGame.getTurnActionMessageDTO().stream().noneMatch(turnActionMessageDTO -> turnActionMessageDTO.getClass().equals(ActivateProductionDTO.class)))
                    wait();
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        return (ActivateProductionDTO) clientGame.getTurnActionMessageDTO().get();

    }
    private void inputFromWhere(SocketConnector clientConnector, View view, ClientGame clientGame, ActivateProductionDTO activateProduction){
        InputFromWhereDTO inputFromWhere = new InputFromWhereDTO();
        inputFromWhere.setInputFromStrongbox(view.inputFromStrongbox(activateProduction.getInputFromStrongBoxToChoose()));
        inputFromWhere.setInputFromWarehouse(view.inputFromWarehouse(activateProduction.getInputFromWarehouseToChoose()));
        clientConnector.sendMessage(inputFromWhere);
    }
}
