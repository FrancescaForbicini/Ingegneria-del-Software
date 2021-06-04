package it.polimi.ingsw.view.gui.scene_controller;

import it.polimi.ingsw.model.faith.Cell;
import it.polimi.ingsw.model.faith.CellGroup;
import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.view.gui.GUIController;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Map;


public class ShowFaithTrackController {
    @FXML
    private HBox faithTrackBox;

    public void initialize(){
        FaithTrack faithTrack = GUIController.getInstance().getFaithTrack();
        VBox cellBox;
        Label victoryPointsLabel;
        Rectangle cellBodyShape;
        VBox groupParametersBox;
        Rectangle groupMembershipShape;
        Label groupVictoryPointsLabel;
        VBox playersInCellBox;
        for(Cell cell : faithTrack.getCells()){
            int cellID = cell.getCellID();
            cellBox = new VBox();
            cellBodyShape = new Rectangle();
            groupParametersBox = new VBox();
            groupMembershipShape = new Rectangle();
            groupVictoryPointsLabel = new Label();
            victoryPointsLabel = new Label();
            playersInCellBox = new VBox();
            victoryPointsLabel.setText(String.valueOf(cell.getCellVictoryPoints()));
            if(cell.isPopeCell()){
                cellBodyShape.setFill(Color.GREEN);
            }
            if (faithTrack.getGroups().stream().anyMatch(group -> group.contains(cellID))){
                groupMembershipShape.setFill(Color.BROWN);
                groupParametersBox.getChildren().add(groupMembershipShape);
                if(cell.isPopeCell()){
                    groupVictoryPointsLabel.setText(String.valueOf(faithTrack.getGroupByCell(cellID).getTileVictoryPoints()));
                    groupParametersBox.getChildren().add(groupVictoryPointsLabel);
                }
            }
            Map<String,Integer> markers = faithTrack.getMarkers();
            Label playerUsername;
            for(String player : markers.keySet()){
                if(markers.get(player).equals(cellID)){
                    playerUsername = new Label();
                    playerUsername.setText(player);
                    playersInCellBox.getChildren().add(playerUsername);
                }
            }
            cellBox.getChildren().add(victoryPointsLabel);
            cellBox.getChildren().add(cellBodyShape);
            cellBox.getChildren().add(groupParametersBox);
            faithTrackBox.getChildren().add(cellBox);
        }
    }
}
