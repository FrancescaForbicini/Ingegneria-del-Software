package it.polimi.ingsw.model;

import java.util.Collection;

public interface Usable {
     Collection<Requirement> requirements = null;
     public Collection<Requirement> getRequirements();
     public boolean isUsable();
}
