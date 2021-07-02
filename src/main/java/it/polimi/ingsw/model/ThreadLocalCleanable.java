package it.polimi.ingsw.model;

public interface ThreadLocalCleanable {
    /**
     * Cleans eventual instances of ThreadLocal variables
     */
    void clean();
}
