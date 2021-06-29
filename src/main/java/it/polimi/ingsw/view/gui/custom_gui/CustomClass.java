package it.polimi.ingsw.view.gui.custom_gui;

import javafx.scene.Node;
import javafx.scene.image.ImageView;

public abstract class CustomClass {
    public abstract ImageView getModifiedImageView();
    public abstract Node getToModify();
    public abstract Modifiable getModified();
}
