package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;

public class ChooseLineDTO extends TurnActionMessageDTO {
    private final String rc;
    private final int num;

    public String getRc() {
        return rc;
    }

    public int getNum() {
        return num;
    }

    public ChooseLineDTO(String rc, int num) {
        this.rc = rc;
        this.num = num;
    }
}
