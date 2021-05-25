package it.polimi.ingsw.view.gui;

public class GUIController  {
    private LoginScene loginScene;
    private static GUIController instance = null;

    public static GUIController getInstance(){
        if (instance == null)
            instance = new GUIController();
        return instance;
    }



}
