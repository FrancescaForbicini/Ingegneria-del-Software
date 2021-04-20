package it.polimi.ingsw.model.market;


import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.Map;

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

    public String toShortString(){ return type.toShortString();}

    public boolean equals(Marble m){
        return type.equals(m.getType());
    }

    /**
     * Convert a marble to the own resource
     * @param marble the marble that has to be convert to the correspondent resource
     * @return the resource type that corresponds to the marble
     */
    public static ResourceType conversion (MarbleType marble){
        switch(marble){
            case White:
                return ResourceType.Any;
            case Blue:
                return ResourceType.Shields;
            case Grey:
                return ResourceType.Stones;
            case Yellow:
                return ResourceType.Coins;
            case Purple:
                return ResourceType.Servants;
        }
        return null;
    }
}
