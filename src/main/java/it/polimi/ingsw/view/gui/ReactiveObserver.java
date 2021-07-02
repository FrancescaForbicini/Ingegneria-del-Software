package it.polimi.ingsw.view.gui;

import it.polimi.ingsw.client.ClientGameObserverProducer;
import it.polimi.ingsw.view.gui.scene_controller.SceneController;

/**
 * Abstract class for GUI reactive controllers, those are observers which reactively updates the view
 */
public abstract class ReactiveObserver implements SceneController {
    protected ClientGameObserverProducer clientGameObserverProducer;
    public ReactiveObserver(ClientGameObserverProducer clientGameObserverProducer){
        this.clientGameObserverProducer = clientGameObserverProducer;
        clientGameObserverProducer.subscribe(this);
    }

    /**
     * Updates
     */
    public abstract void update();
}
