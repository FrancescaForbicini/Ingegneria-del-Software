package it.polimi.ingsw.model.market;


import it.polimi.ingsw.model.faith.FaithTrack;
import it.polimi.ingsw.model.requirement.ResourceType;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * It represents the market structure used by players to get new resources.
 */
public class Market {
    private final ArrayList<Marble> actualMarket = new ArrayList<>();
    private Marble extraMarble;

    private final int numRow=3;
    private final int numCol=4;

    public Market(ArrayList<Marble> marbles) {
        Collections.shuffle(marbles);
        extraMarble = marbles.get(0);
        marbles.remove(extraMarble);
        actualMarket.addAll(marbles);
    }
    // TODO ThreadLocal Singleton
    //private static ThreadLocal<Market> instance = ThreadLocal.withInitial(() -> new Market());

    public Collection<Marble> getMarket() {
        return actualMarket;
    }

    public Marble getExtraMarble() {
        return extraMarble;
    }

    public void setExtraMarble(Marble extraMarble) {
        this.extraMarble = extraMarble;
    }



    /*
    private Market()  {
        LinkedList<Marble> marbles = (LinkedList<Marble>) Settings.getInstance().getMarbles();
        Collections.shuffle(marbles);
        extraMarble = marbles.poll();
        this.market = marbles.toArray(new Marble[numRow*numCol]);
    }*/

    /*
    public static Market getInstance(){
        //TODO: sistemare ThreadLocal Singletons
        String threadName = Thread.currentThread().getName();
        if(instances.get(threadName)==null) {
            instances.put(threadName, new Market());
        }
        return instances.get(threadName);
    }

     */

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

/*    public void print(){
        for(int i=0;i<numRow*numCol;i++){
            System.out.print(market.get(i).toString() + "\t");
            if(i%4==3){
                System.out.print("\n");
            }
        }
        System.out.println("ExtraMarble: " + extraMarble.toString() + "\n");
    }
*/
    public void shortPrint(){
        for(int i=0;i<numRow*numCol;i++){
            System.out.print(actualMarket.get(i).toShortString() + "\t");
            if(i%4==3){
                System.out.print("\n");
            }
        }
        System.out.println("ExtraMarble: " + extraMarble.toShortString() + "\n");
    }
}