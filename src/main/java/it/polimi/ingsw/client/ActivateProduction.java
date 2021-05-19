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
        ActivateProductionDTO activateProduction ;
        ChooseTradingRulesDTO tradingRulesMessage = (ChooseTradingRulesDTO) clientConnector.receiveMessage(ChooseTradingRulesDTO.class).get();
        tradingRulesMessage.setChosenTradingRules(view.chooseTradingRulesToActivate(activateProduction.getTradingRulesToChoose()));
        clientConnector.sendMessage(tradingRulesMessage);
        ChooseAnyInputOutputDTO chooseAnyInputOutput = (ChooseAnyInputOutputDTO) clientConnector.receiveMessage(ChooseAnyInputOutputDTO.class).get();
        chooseAnyInputOutput.setChosenInputAny(view.chooseAnyInput(activateProduction.getInputAnyToChoose()));
        chooseAnyInputOutput.setChosenOutputAny(view.chooseAnyOutput(activateProduction.getOutputAnyToChoose()));
        clientConnector.sendMessage(chooseAnyInputOutput);
        InputFromWhereDTO inputFromWhere = (InputFromWhereDTO) clientConnector.receiveMessage(InputFromWhereDTO.class).get();
        inputFromWhere.setInputFromStrongbox(view.inputFromStrongbox(activateProduction.getInputFromStrongBoxToChoose()));
        inputFromWhere.setInputFromWarehouse(view.inputFromWarehouse(activateProduction.getInputFromWarehouseToChoose()));
        clientConnector.sendMessage(inputFromWhere);
    }
}
