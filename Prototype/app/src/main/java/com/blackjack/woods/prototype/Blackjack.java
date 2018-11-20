package com.blackjack.woods.prototype;
import java.util.HashMap;
import java.util.Map.Entry;

public class Blackjack {
    //RANKS and SUITS are used to generate the shoe
    private static final String[] RANKS = new String[]{"2","3","4","5","6","7","8","9","T","J","Q","K","A"};
    private static final String[] SUITS = new String[]{"C","D","H","S"};

    //cardRankValues is used to quickly access the value of a card
    private static final HashMap<String, Integer> cardRankValues;

    static{
        cardRankValues = new HashMap<>();
        cardRankValues.put("A", 1);
        cardRankValues.put("2", 2);
        cardRankValues.put("3", 3);
        cardRankValues.put("4", 4);
        cardRankValues.put("5", 5);
        cardRankValues.put("6", 6);
        cardRankValues.put("7", 7);
        cardRankValues.put("8", 8);
        cardRankValues.put("9", 9);
        cardRankValues.put("T", 10);
        cardRankValues.put("J", 10);
        cardRankValues.put("Q", 10);
        cardRankValues.put("K", 10);
    }

    //Count is based on the number of high and low cards already seen
    private int count;
    //How many cards are left in the shoe
    private int size;
    //Value of the players hand
    private int playerHandValue;
    //Chance to bust on the next hit
    private float chanceToBust;
    //Keeps track of all the cards in the shoe
    private HashMap<String,Integer> shoe;
    //How many cards are left based on their value
    private int[] cardStockByValue;

    Blackjack(){
        cardStockByValue = new int[10];
        playerHandValue = 0;
        chanceToBust = 0.0f;
        count = 0;
        size = 0;
        shoe = new HashMap<>();
        populateShoe();
        fillShoe(1);

    }

    private void populateShoe(){
        String rank,suit;
        for(int i = 0; i < (RANKS.length * SUITS.length);i++){
            rank = RANKS[i%RANKS.length];
            suit = SUITS[i%SUITS.length];
            shoe.put(rank+suit,0);


        }
    }

    private int cardCountValue(String card){
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
        String rank;
        Integer numOfCards;

        for(String s : cards){
            numOfCards = shoe.get(s);

            if (numOfCards > 0) {
                shoe.put(s, shoe.get(s) - 1);
                rank = s.substring(0,1);

                cardStockByValue[cardRankValues.get(rank)-1]--;
                updateCount(s);
                size--;
            }
        }

        UpdateChanceToBust();


        return true;
    }

    private void UpdateChanceToBust(){
        if(size == 0) return;

        int upperBoundOfBust = 21 - playerHandValue;
        if(upperBoundOfBust > 10){
            chanceToBust = 0.0f;
            return;
        }

        float nonBustCards = 0;
        for(int i =0; i < upperBoundOfBust; i++){
            nonBustCards += cardStockByValue[i];
        }

        chanceToBust = 1-(nonBustCards/size);

    }

    public int getCount(){
        return count;
    }

    public float getChanceToBust() {
        return chanceToBust;
    }

    public int updateCount(String card){
        int cardVal = cardCountValue(card);
        count += cardVal;
        return cardVal;
    }

    public void printShoe(){
        try {
            String all = "[";
            for (Entry<String, Integer> i : shoe.entrySet()) {
                if (i.getValue() != 0)
                    all += i.getKey() + ":" + i.getValue() + ", ";
            }
            if(all.length() >= 2)
                System.out.print(all.substring(0, all.length() - 2));
            System.out.println("[]");

            all = "[";
            for (int i : cardStockByValue) {
                all += i + ", ";
            }

            if(all.length() >= 2)
                System.out.print(all.substring(0, all.length() - 2));
            System.out.println("]");
        }
        catch(java.lang.StringIndexOutOfBoundsException e){}
    }

    public void fillShoe(int numOfDecks){
        size = numOfDecks * 52;
        for(Entry<String,Integer> i : shoe.entrySet()){
            i.setValue(numOfDecks);
        }

        for(int i = 0; i < cardStockByValue.length; i++) {
            if(i == 9)
                cardStockByValue[i] = 16 * numOfDecks;
            else
                cardStockByValue[i] = 4 * numOfDecks;
        }
    }


    public void reset(){
        count = 0;
        for(Entry<String,Integer> i : shoe.entrySet()){
            i.setValue(0);
        }

        cardStockByValue = new int[10];
    }

    public void setPlayerHandValue(int i){
        playerHandValue = i;
    }
}