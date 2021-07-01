package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.faith.CellGroup;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class CustomCellGroup extends CustomClass{
    private CellGroup originalCellGroup;
    private Node cellGroupToModify;
    private Spinner<Integer> modifiableVictoryPoints;
    private CellGroup modifiedCellGroup;

    public CustomCellGroup(CellGroup cellGroup, boolean toModify){
        if(toModify){
            this.originalCellGroup = cellGroup;
            modifiableVictoryPoints = new Spinner<>();
        }
        else {
            this.modifiedCellGroup = cellGroup;
        }
    }
    @Override
    public Node getNodeToShow() {
        return null;
    }

    @Override
    public Node getNodeToModify() {

        HBox modifiableGroup = new HBox();
        VBox lines = new VBox();
        Label cellGroupLabel = new Label("Cell Group " + originalCellGroup.getCellIDs());
        lines.getChildren().add(cellGroupLabel);

        //pts
        VBox pointsPart = new VBox();
        Label victoryPointsLabel = new Label("Victory Points");
        modifiableVictoryPoints = new Spinner<>();
        modifiableVictoryPoints.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 12, originalCellGroup.getTileVictoryPoints()));
        pointsPart.getChildren().add(victoryPointsLabel);
        pointsPart.getChildren().add(modifiableVictoryPoints);
        lines.getChildren().add(pointsPart);
        modifiableGroup.getChildren().add(lines);
        cellGroupToModify = modifiableGroup;
        return cellGroupToModify;
    }

    @Override
    public Modifiable getModified() {
        int vpts;
        if(modifiableVictoryPoints.getValue()==null){
            vpts = originalCellGroup.getTileVictoryPoints();
        } else {
            vpts = modifiableVictoryPoints.getValue();
            if(vpts!= originalCellGroup.getTileVictoryPoints()){
                modified = true;
            }
        }
        modifiedCellGroup = new CellGroup(originalCellGroup.getCellIDs(), vpts);
        return modifiedCellGroup;
    }
}
