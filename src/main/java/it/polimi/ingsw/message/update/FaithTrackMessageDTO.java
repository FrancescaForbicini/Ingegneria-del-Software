package it.polimi.ingsw.message.update;

import it.polimi.ingsw.model.faith.FaithTrack;

public class FaithTrackMessageDTO extends UpdateMessageDTO{
    private FaithTrack faithTrack;

    public FaithTrack getFaithTrack() {
        return faithTrack;
    }

    public void setFaithTrack(FaithTrack faithTrack) {
        this.faithTrack = faithTrack;
    }
}
