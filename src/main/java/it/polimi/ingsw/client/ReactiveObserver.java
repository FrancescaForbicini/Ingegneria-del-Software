package it.polimi.ingsw.client;

/**
 * Abstract class for GUI reactive controllers, those are observers which reactively updates the view
 */
public abstract class ReactiveObserver {
    protected ClientGameObserverProducer clientGameObserverProducer;
    public ReactiveObserver(ClientGameObserverProducer clientGameObserverProducer){
        this.clientGameObserverProducer = clientGameObserverProducer;
        clientGameObserverProducer.subscribe(this);
    }

    public abstract void update();
}
