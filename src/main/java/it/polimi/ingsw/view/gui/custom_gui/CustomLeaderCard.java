package it.polimi.ingsw.view.gui.custom_gui;

import it.polimi.ingsw.model.cards.LeaderCard;
import it.polimi.ingsw.model.requirement.RequirementResource;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.Map;

public class CustomLeaderCard extends CustomClass {
    protected Node cardToModify;
    protected Spinner<Integer> modifiableVictoryPoints;


    @Override
    public ImageView getModifiedImageView() {
        return null;
    }

    @Override
    public Node getToModify() {
        return cardToModify;
    }



    @Override
    public Modifiable getModified() {
        return null;
    }
}


