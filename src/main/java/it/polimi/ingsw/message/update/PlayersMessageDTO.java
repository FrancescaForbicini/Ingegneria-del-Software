package it.polimi.ingsw.message.update;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlayersMessageDTO extends UpdateMessageDTO{
    private List<PlayerMessageDTO> playerMessageDTOList;

    public List<PlayerMessageDTO> getPlayerMessageDTOList() {
        return playerMessageDTOList;
    }

    public PlayersMessageDTO(Stream<PlayerMessageDTO> playerMessageDTOList) {
        this.playerMessageDTOList = playerMessageDTOList.collect(Collectors.toList());
    }
}
