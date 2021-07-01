package it.polimi.ingsw.view.gui.custom_gui;

import javafx.scene.Node;

public abstract class CustomClass {
    protected boolean modified = false;
    public abstract Node getNodeToShow();
    public abstract Modifiable getModified();
    public abstract Node getNodeToModify();

    public boolean isModified(){
        return modified;
    }
}
