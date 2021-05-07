package it.polimi.ingsw.message.action_message.MarketMessage;

import it.polimi.ingsw.message.action_message.ActionMessage;

public class ChooseLine extends ActionMessage {
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
