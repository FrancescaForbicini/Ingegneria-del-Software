package it.polimi.ingsw.message;

public class PickStartingResourcesDTO extends MessageDTO{
    public final int number;

    public int getNumber() {
        return number;
    }

    public PickStartingResourcesDTO(int number) {
        this.number = number;
    }
}
