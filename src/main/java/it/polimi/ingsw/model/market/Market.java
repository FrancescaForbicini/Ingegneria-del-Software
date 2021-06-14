package it.polimi.ingsw.model.market;


import it.polimi.ingsw.controller.Settings;
import it.polimi.ingsw.message.action_message.market_message.MarketAxis;
import it.polimi.ingsw.model.Cleanable;
import it.polimi.ingsw.view.cli.Color;

import java.util.ArrayList;
import java.util.Collections;

/**
 * It represents the market structure used by players to get new resources.
 */
public class Market implements Cleanable {
    private ArrayList<Marble> actualMarket = new ArrayList<>();
    private Marble extraMarble;
    private final int numRow = 3;
    private final int numCol = 4;
    private static ThreadLocal<Market> instance = ThreadLocal.withInitial(Market::load);

    public Market(ArrayList<Marble> marbles) {
        Collections.shuffle(marbles);
        extraMarble = marbles.get(0);
        marbles.remove(extraMarble);
        actualMarket.addAll(marbles);
    }
    private static Market load() {
        return new Market(Settings.getInstance().getMarbles());
    }

    public static Market getInstance() {
        return instance.get();
    }

    public ArrayList<Marble> getMarket() {
        return actualMarket;
    }

    public Marble getExtraMarble() {
        return extraMarble;
    }


    public void setActualMarket (ArrayList<Marble> actualMarket){ this.actualMarket = actualMarket;}

    public void setExtraMarble(Marble extraMarble) {
        this.extraMarble = extraMarble;
    }

    /**
     * Updates the market state after a withdraw of resource is done: the extraMarble is put on top of chosen row/column,
     * all marbles after that are shifted of one place and the last one is put into the extraMarble slot.
     * @param marketAxis MarketAxis which represents the player's choice of row or column,
     * @param num number of row or column chosen. It ranges between 1 and numRow or numCol
     */
    private void updateMarket(MarketAxis marketAxis, int num){
        int r = marketAxis.equals(MarketAxis.ROW)?1:0;
        int c = marketAxis.equals(MarketAxis.COL)?1:0;
        Marble tmpMarble;
        for(int i=(r*numCol+c*numRow)-1;i>=0;i--){
            int j = (num-1)*(r*numCol+c)+i*(r+c*numCol);
            tmpMarble = actualMarket.get(j);
            actualMarket.set(j,extraMarble);
            extraMarble = tmpMarble;
        }
    }

    /**
     * Returns each MarbleType contained in the line chosen by the player.
     * @param marketAxis represents the player's choice of row or column
     * @param num number of the chosen line. It ranges between 1 and numRow or numCol
     * @param update if true do the update of the market
     * @return MarbleType contained in the chosen line
     */
    public ArrayList<MarbleType> getMarblesFromLine(MarketAxis marketAxis,int num, boolean update) throws IndexOutOfBoundsException{
        int r = marketAxis.equals(MarketAxis.ROW)?1:0;
        int c = marketAxis.equals(MarketAxis.COL)?1:0;
        ArrayList<MarbleType> line = new ArrayList<>();
        if(0<num && num<=(r*numRow+c*numCol)) {
            for (int i = 0; i < (r * numCol + c * numRow); i++) {
                line.add(actualMarket.get((num - 1) * (r * numCol + c) + i * (r + c * numCol)).getType());
            }
            if(update) {
                this.updateMarket(marketAxis, num);
            }
        }
        else
            throw new IndexOutOfBoundsException("Chosen line exceeds market dimension");
        return line;
    }

   /**
     * Prints the market
     * @return the string to print the market
     */
    @Override
    public String toString() {
        StringBuilder print = new StringBuilder();
        for (int i = 0; i < numRow ; i++ ) {
            getMarblesFromLine(MarketAxis.ROW,i+1,false).forEach(marbleType -> print.append(marbleType.convertColor()).append(" "));
            print.append(i+1);
            print.append(" <");
            print.append("\n");
        }
        print.append("1 2 3 4 \n");
        print.append("^ ^ ^ ^ \n");
        print.append("Extra Marble: ").append(getExtraMarble().getType().convertColor());
        print.append("\n");
        print.append(Color.RESET);
        return print.toString();
    }

    @Override
    public void clean() {
        instance = null;
    }
}
