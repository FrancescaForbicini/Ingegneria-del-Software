package it.polimi.ingsw.client;

//TODO javadoc
public abstract class ReactiveObserver {
    protected ClientGameObserverProducer clientGameObserverProducer;
    public ReactiveObserver(ClientGameObserverProducer clientGameObserverProducer){
        this.clientGameObserverProducer = clientGameObserverProducer;
        clientGameObserverProducer.subscribe(this);
    }

    public abstract void update();
}
