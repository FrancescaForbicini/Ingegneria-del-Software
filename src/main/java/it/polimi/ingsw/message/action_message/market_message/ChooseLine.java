package it.polimi.ingsw.message.action_message.market_message;

import it.polimi.ingsw.message.action_message.ActionMessage;

public class ChooseLine extends ActionMessage {
    private static final long serialVersionUID = -8699441976953637797L;
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
