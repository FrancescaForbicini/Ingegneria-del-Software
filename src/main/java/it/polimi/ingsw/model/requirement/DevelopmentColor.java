package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.view.cli.Color;

public enum DevelopmentColor {
    Green,
    Blue,
    Yellow,
    Purple;


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
            default:
                return null;
        }
    }
}