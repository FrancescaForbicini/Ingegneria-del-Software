package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.turn_taker.Player;

/**
 * Requirements of colors
 */
public class RequirementColor extends Requirement {
    private final  DevelopmentColor color;
    private final int level;
    private final int quantity;


    public RequirementColor(int level, int quantity, DevelopmentColor color) {
        this.level = level;
        this.quantity = quantity;
        this.color = color;
    }

    public int getQuantity(){
        return quantity;
    }
    public int getLevel(){
        return level;
    }
    public DevelopmentColor getColor() {
        return color;
    }

    /**
     * Checks if a player has the right colors of the Development Card
     * @param player the player against which the requirement is checked
     * @return true if a player has the right resources, false otherwise
     */
    @Override
    public boolean isSatisfied(Player player) {
        int quantity;
        //level == 0 means that in the requirements the level is not specified
        if (this.level == 0)
            quantity = player.getDevelopmentQuantity(this.color);
        else
            quantity = player.getDevelopmentQuantity(this.color,this.level);
        return quantity >= this.quantity;
    }

    /**
     * Prints the requirements
     *
     * @return string to show the requirements
     */
    @Override
    public String toString(){
        return quantity + " " + color.convertColor() + " " + (( level== 0 )?" Any level " : " Level: " + level);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RequirementColor that = (RequirementColor) o;

        if (level != that.level) return false;
        if (quantity != that.quantity) return false;
        return color == that.color;
    }
}
