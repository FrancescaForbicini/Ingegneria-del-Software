package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.view.cli.Color;
import javafx.scene.paint.Paint;

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

    /**
     * Convert the DevelopmentColor into its relative paint color of javafx
     *
     * @return relative paint color
     */
    public Paint toPaint(){
        switch (this) {
            case Green:
                return javafx.scene.paint.Color.GREEN;
            case Blue:
                return javafx.scene.paint.Color.BLUE;
            case Yellow:
                return javafx.scene.paint.Color.YELLOW;
            case Purple:
                return javafx.scene.paint.Color.PURPLE;
            case Any :
                return javafx.scene.paint.Color.WHITE;
            default:
                return javafx.scene.paint.Color.BLACK;
        }
    }
}