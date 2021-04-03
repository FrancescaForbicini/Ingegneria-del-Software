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

    @Override
    public boolean isSatisfied() {
        return super.isSatisfied();
    }
}
