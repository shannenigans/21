package com.blackjack.woods.prototype;

import android.os.Bundle;
import android.util.DisplayMetrics;

public class DeckUnitsPopup extends MainActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this sets the layout to the popup screen
        setContentView(R.layout.deckunitpopup);

        //gets the dimensions of the phone's screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //declares width and heights
        int width = displayMetrics.widthPixels;
        int hieght = displayMetrics.heightPixels;

        //set the layout size so the popup isn't fullscreen
        getWindow().setLayout((int)(width*.9), (int) (hieght*.8));

    }
}
