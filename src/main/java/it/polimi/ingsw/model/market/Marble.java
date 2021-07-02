package it.polimi.ingsw.model.market;


/**
 * Abstraction to define which resource (faith points included) a player gets from the market
 */
public class Marble {
    private final MarbleType type;

    public Marble(MarbleType type){
        this.type=type;
    }

    public MarbleType getType() {
        return type;
    }

    public String toString(){
        return type.toString();
    }

    public boolean equals(Marble m){
        return type.equals(m.getType());
    }



}
