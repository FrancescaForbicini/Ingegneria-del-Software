package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.TurnActionMessageDTO;

public class ChooseLineDTO extends TurnActionMessageDTO {
    String rc;
    int num;

    public void setRc(String rc) {
        this.rc = rc;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getRc() {
        return rc;
    }

    public int getNum() {
        return num;
    }
}
