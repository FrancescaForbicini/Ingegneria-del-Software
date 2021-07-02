package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.view.cli.Color;

/**
 * Enumeration to represent all the possible colors of the Development Cards
 */
public enum DevelopmentColor {
    Green,
    Blue,
    Yellow,
    Purple,
    Any;


    /**
     * Converts this into a colored String
     * @return builder to get the string
     */
    public StringBuilder convertColor() {
        switch (this) {
            case Green:
                return new StringBuilder().append(Color.ANSI_GREEN).append(this).append(Color.RESET);
            case Blue:
                return new StringBuilder().append(Color.ANSI_BLUE).append(this).append(Color.RESET);
            case Yellow:
                return new StringBuilder().append(Color.ANSI_YELLOW).append(this).append(Color.RESET);
            case Purple:
                return new StringBuilder().append(Color.ANSI_PURPLE).append(this).append(Color.RESET);
            case Any :
                return new StringBuilder().append(this);
                default:
                return null;
        }
    }
}