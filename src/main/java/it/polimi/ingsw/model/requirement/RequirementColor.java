package it.polimi.ingsw.model.requirement;

import it.polimi.ingsw.model.turn_taker.Player;

public class RequirementColor extends Requirement {
    private int level;
    private int quantity;
    private DevelopmentColor color;

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
     *
     * @param player the player against which the requirement is checked
     * @return true if a player has the right resources, false otherwise
     */
    @Override
    public boolean isSatisfied(Player player) {
        int level = player.getMaxDevelopmentLevel(color);
        int quantity = player.getDevelopmentQuantity(color);
        return quantity > this.quantity && level > this.level;
    }
}
