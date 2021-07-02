package it.polimi.ingsw.view.cli;

/**
 * Enumeration of color to print objects in the view
 *
 */
public enum Color
{
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_WHITE("\u001B[17m"),
    ANSI_GREY("\u001B[37m");

    public static final String RESET = "\u001B[0m";

    private String escape;

    Color(String escape)
    {
        this.escape = escape;
    }

    @Override
    public String toString()
    {
        return escape;
    }
}
