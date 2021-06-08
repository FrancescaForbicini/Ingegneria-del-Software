package it.polimi.ingsw.model.market;

import it.polimi.ingsw.model.requirement.ResourceType;

import static it.polimi.ingsw.view.cli.Color.*;

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

    /**
     * Converts the marble to the correspondent color
     * @return the color of the marble
     */
    public StringBuilder convertColor(){
        String circle ="֎";
        switch(this){
            case Blue:
                return new StringBuilder().append(ANSI_BLUE).append(circle).append(RESET);
            case White:
                return new StringBuilder().append(ANSI_WHITE).append(circle).append(RESET);
            case Grey:
                return new StringBuilder().append(ANSI_GREY).append(circle).append(RESET);
            case Yellow:
                return new StringBuilder().append(ANSI_YELLOW).append(circle).append(RESET);
            case Purple:
                return new StringBuilder().append(ANSI_PURPLE).append(circle).append(RESET);
            case Red:
                return new StringBuilder().append(ANSI_RED).append(circle).append(RESET);
            default: return null;
        }
    }
    /**
     * Convert a marble to the own resource
     * @return the resource type that corresponds to the marble
     */
    public ResourceType conversion (){
        switch(this){
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
