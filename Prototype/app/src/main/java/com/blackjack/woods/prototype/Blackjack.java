package com.blackjack.woods.prototype;
import java.util.HashMap;
import java.util.Map.Entry;

public class Blackjack {

    private int count;
    private int size;
    private float chanceForHigh,chanceForLow,chanceForNeutral;
    private int[] shoe;

    Blackjack(){
        chanceForHigh = 0.0f; chanceForLow = 0.0f; chanceForNeutral = 0.0f;
        count = 0;
        size = 0;
        shoe = new int[3];
        fillShoe(1);
    }

    public void fillShoe(int decks){
        size = decks * 52;
        shoe[0] = decks * 20;
        shoe[1] = decks * 12;
        shoe[2] = decks * 20;
    }

    public int getCount(){
        return count;
    }

    public float[] getChances() {
        return new float[]{chanceForLow,chanceForNeutral,chanceForHigh};
    }

    public int updateCount(int[] c){
        for(int i : c) {
            if(i > 0)
                shoe[0]--;
            else if (i == 0)
                shoe[1]--;
            else
                shoe[2]--;

            count += i;
        }
        size -= c.length;
        updateChances();
        return count;
    }

    public void updateChances(){
        chanceForHigh = ((float)shoe[2])/size;
        chanceForNeutral = ((float)shoe[1])/size;
        chanceForLow = ((float)shoe[0])/size;
    }


    public void reset(){
        count = 0;
        size = 0;
        shoe = new int[3];
    }


    public class CardOutOfStockException extends Exception{
        CardOutOfStockException(String card){
            super(card + " has no more entries in the shoe.");
        }
    }

}