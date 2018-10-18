package com.blackjack.woods.prototype;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;

public class AmountOfCardsPopup extends MainActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this sets the layout to the popup screen
        setContentView(R.layout.amountofdeckspop);

        //gets the dimensions of the phone's screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //declares width and heights
        int width = displayMetrics.widthPixels;
        int hieght = displayMetrics.heightPixels;

        //set the layout size so the popup isn't fullscreen
        getWindow().setLayout((int)(width*.85), (int) (hieght*.85));

    }

    public void closeAmountOfCardsPopup(View v){
        finish();
    }
}
