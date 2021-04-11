package it.polimi.ingsw.model;

public class RequirementColor extends Requirement{
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

    /**Used to check if a player has the right colors of the Development Card
     *
     * @param player: used to specify the player
     * @return true if a player has the right resources, false if not.
     */
    @Override
    public boolean isSatisfied(Player player) {
        int count=0;
        for(DevelopmentSlot developmentSlot : player.getPersonalBoard().getDevelopmentSlots()){
            count+=developmentSlot.checkColor(color);
        }
        if(count>=quantity && quantity!=0)
            return true;
        else
            return false;
    }
}
