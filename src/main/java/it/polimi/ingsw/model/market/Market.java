package it.polimi.ingsw.model.market;


import it.polimi.ingsw.controller.Settings;

import java.util.ArrayList;
import java.util.Collections;

/**
 * It represents the market structure used by players to get new resources.
 */
public class Market {
    private ArrayList<Marble> actualMarket = new ArrayList<>();
    private Marble extraMarble;
    private final int numRow = 3;
    private final int numCol = 4;
    private static final ThreadLocal<Market> instance = ThreadLocal.withInitial(Market::new);

    public Market(ArrayList<Marble> marbles) {
        Collections.shuffle(marbles);
        extraMarble = marbles.get(0);
        marbles.remove(extraMarble);
        actualMarket.addAll(marbles);
    }
    public Market() { this(Settings.getInstance().getMarbles()); }



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
     * @param rc boolean which represents the player's choice of row or column, 'false' means row, 'true' means column
     * @param num number of row or column chosen. It ranges between 1 and numRow or numCol
     */
    private void updateMarket(String rc,int num){
        int r=rc.toLowerCase().charAt(0)=='c'?0:1;
        int c=rc.toLowerCase().charAt(0)=='c'?1:0;
        Marble tmpMarble;
        for(int i=0;i<(r*numCol+c*numRow);i++){
            int j = (num-1)*(r*numCol+c)+i*(r+c*numCol);
            tmpMarble = actualMarket.get(j);
            actualMarket.set(j,extraMarble);
            extraMarble = tmpMarble;
        }
    }

    /**
     * Returns each MarbleType contained in the line chosen by the player.
     * @param rc boolean which represents the player's choice of row or column, 'false' means row, 'true' means column
     * @param num number of the chosen line. It ranges between 1 and numRow or numCol
     * @return MarbleType contained in the chosen line
     */
    public ArrayList<MarbleType> getMarblesFromLine(String rc,int num) throws IndexOutOfBoundsException{
        int r=rc.toLowerCase().charAt(0)=='c'?0:1;
        int c=rc.toLowerCase().charAt(0)=='c'?1:0;
        ArrayList<MarbleType> line = new ArrayList<>();
        if(0<num && num<=(r*numRow+c*numCol)) {
            for (int i = 0; i < (r * numCol + c * numRow); i++) {
                line.add(actualMarket.get((num - 1) * (r * numCol + c) + i * (r + c * numCol)).getType());
            }
            this.updateMarket(rc, num);
        }
        else
            throw new IndexOutOfBoundsException("Chosen line exceeds market dimension");
        return line;
    }

    /**
     * Gets the row of the market
     * @param num the number of the row
     * @return the marbles in the row selected
     */
    public ArrayList<MarbleType> getRow (int num){
        ArrayList<MarbleType> line = new ArrayList<>();
        for (int i = 0; i <  numCol ; i++) {
            line.add(actualMarket.get((num - 1) * numCol  + i ).getType());
        }
        return line;
    }

    /**
     * Gets the column of the market
     * @param num the number of the column
     * @return the marbles in the column
     */
    public ArrayList<MarbleType> getColumn (int num){
        ArrayList<MarbleType> line = new ArrayList<>();
        for (int i = 0; i < numRow; i++) {
            line.add(actualMarket.get((num - 1) + i * numCol).getType());
        }
        return line;
    }

    @Override
    public String toString() {
        return "Market{" +
                "actualMarket=" + actualMarket +
                ", extraMarble=" + extraMarble +
                "}";
    }
}
