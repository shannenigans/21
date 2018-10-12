package com.blackjack.woods.prototype;
import java.util.HashMap;
import java.util.Map.Entry;

public class Blackjack {
    private static final String[] RANKS = new String[]{"2","3","4","5","6","7","8","9","T","J","Q","K","A"};
    private static final String[] SUITS = new String[]{"C","D","H","S"};

    private int count;
    private int size;
    private int playerHand;
    private float chanceToBust;
    private HashMap<String,Integer> shoe;
    private int[] cardStockByValue;

    Blackjack(){
        cardStockByValue = new int[10];
        playerHand = 0;
        chanceToBust = 0.0f;
        count = 0;
        size = 0;
        shoe = new HashMap<>();
        populateShoe();
    }

    private void populateShoe(){
        String rank,suit;
        for(int i = 0; i < (RANKS.length * SUITS.length);i++){
            rank = RANKS[i%RANKS.length];
            suit = SUITS[i%SUITS.length];
            shoe.put(rank+suit,0);


        }
    }

    private int cardValue(String card){
        String rank = card.substring(0,1);
        switch(rank){
            case "2": case "3": case "4": case "5": case "6":
                return 1;
            case "T": case "J": case "Q": case "K": case "A":
                return -1;
            default:
                return 0;
        }
    }

    public boolean readCards(String[] cards){
        Integer numOfCards;

        for(String s : cards){

            numOfCards = shoe.get(s);

            if(numOfCards == null)
                shoe.put(s,shoe.get(s)-1);

            updateCount(s);
        }


        return true;
    }

    public int getCount(){
        return count;
    }

    public int updateCount(String card){
        int cardVal = cardValue(card);
        count += cardVal;
        return cardVal;
    }

    public void printShoe(){
        for(Entry<String,Integer> i : shoe.entrySet()){
            System.out.println(i.getKey());
        }
    }

    public void fillShoe(int numOfDecks){
        size = numOfDecks * shoe.size();
        for(Entry<String,Integer> i : shoe.entrySet()){
            i.setValue(numOfDecks);
        }
    }

    public void reset(){
        count = 0;
        for(Entry<String,Integer> i : shoe.entrySet()){
            i.setValue(0);
        }
    }
}