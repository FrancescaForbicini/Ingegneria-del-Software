package it.polimi.ingsw.message.update;

import java.util.List;

public class TurnTakersMessageDTO extends UpdateMessageDTO{
    private List<TurnTakerMessageDTO> turnTakerMessageDTOs;

    public List<TurnTakerMessageDTO> getPlayerMessageDTOList() {
        return turnTakerMessageDTOs;
    }

    public TurnTakersMessageDTO(List<TurnTakerMessageDTO> turnTakerMessageDTOs) {
        this.turnTakerMessageDTOs = turnTakerMessageDTOs;
    }
}
