package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.faith.Cell;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CustomCell extends CustomClass{
    private Cell originalCell;
    private Node cellToModify;
    private Spinner<Integer> modifiableVictoryPoints;
    private Cell modifiedCell;

    public CustomCell(Cell cell, boolean toModify){
        if(toModify){
            this.originalCell = cell;
            modifiableVictoryPoints = new Spinner<>();
        }
        else {
            this.modifiedCell = cell;
        }
    }
    @Override
    public Node getModifiedNodeToShow() {
        return null;
    }

    @Override
    public Node getNodeToModify() {

        HBox modifiableCell = new HBox();
        VBox lines = new VBox();
        Label cellLabel = new Label("Cell " + originalCell.getCellID());
        lines.getChildren().add(cellLabel);

        //pts
        VBox pointsPart = new VBox();
        Label victoryPointsLabel = new Label("Victory Points");
        modifiableVictoryPoints = new Spinner<>();
        modifiableVictoryPoints.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 12, originalCell.getCellVictoryPoints()));
        pointsPart.getChildren().add(victoryPointsLabel);
        pointsPart.getChildren().add(modifiableVictoryPoints);
        lines.getChildren().add(pointsPart);
        modifiableCell.getChildren().add(lines);
        cellToModify = modifiableCell;
        return cellToModify;
    }

    @Override
    public Modifiable getModified() {
        int vpts;
        if(modifiableVictoryPoints.getValue()==null){
            vpts = originalCell.getCellVictoryPoints();
        } else {
            vpts = modifiableVictoryPoints.getValue();
        }
        modifiedCell = new Cell(originalCell.getCellID(), vpts, originalCell.isPopeCell());
        return modifiedCell;
    }
}
