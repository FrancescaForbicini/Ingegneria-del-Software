package it.polimi.ingsw.model;

import java.util.Optional;

public class Requirement implements RequirementType {

    private final RequirementType requirementType;
    private final int quantity;
    private final Optional<Integer> level;

    /** Initializes the Requirement in order to get or use a DevelopmentCard or a LeaderCard
     *
     * @param requirementType: used to specify the type of the requirement that it could be a DevelopmentColor or a Resourcetype
     * @param quantity: used to specify the number of requirementType
     * @param level: in order to specify the level of the DevelopmentCard , if the requirementType is a DevelopmentColor
     *
     */
    public Requirement(RequirementType requirementType, int quantity, Optional<Integer> level) {
        this.requirementType = requirementType;
        this.quantity = quantity;
        this.level = level;
    }
    public RequirementType getRequirementType(){
        return this.requirementType;
    }
    public Integer getQuantity() {
        return this.quantity;
    }
    public Optional<Integer> getLevel(){
        return this.level;
    }

}
