package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.turn_taker.Player;

public class RequirementColor extends Requirement {
    private DevelopmentColor color;
    private int level;
    private int quantity;

    /**Initializes the Requirement Color
     *
     * @param level: used to specify the level of the Development Card
     * @param quantity: used to specify the number of the Development Card
     * @param color: used to specify the color od the Development Card
     */
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
    @Override
    public String toString(){
        return "Requirements: Color : " + color + " Level: " + level + " Quantity : " +quantity;
    }
}
