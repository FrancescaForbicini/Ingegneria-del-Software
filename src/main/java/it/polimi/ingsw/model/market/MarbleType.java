package it.polimi.ingsw.model.market;

public enum MarbleType {
    White ("W"),
    Blue ("B"),
    Grey ("G"),
    Yellow ("Y"),
    Purple ("P"),
    Red ("R");

    private final String abbreviation;
    MarbleType(String abbreviation){
        this.abbreviation=abbreviation;
    }
    public String toShortString(){
        return abbreviation;
    }
}
