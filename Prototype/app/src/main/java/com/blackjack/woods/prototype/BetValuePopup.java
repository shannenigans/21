package com.blackjack.woods.prototype;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class BetValuePopup extends MainActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //this sets the layout to the popup screen
        setContentView(R.layout.betunitspopup);

        //gets the dimensions of the phone's screen
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //declares width and heights
        int width = displayMetrics.widthPixels;
        int hieght = displayMetrics.heightPixels;

        //set the layout size so the popup isn't fullscreen
        getWindow().setLayout((int)(width*.85), (int) (hieght*.85));


//            String thing = "Currently have " + deck + " decks";
//            Log.d("thingy", "inputButtonClicked: " + thing);



        Intent intent = getIntent();
        String var1 = intent.getStringExtra("deckValue");
        Log.d("thingy", "inputButtonClicked: " + var1);
        ((TextView)findViewById(R.id.textView6)).setText(var1);
//            String outputDeck = getString(R.string.deck, deck);
//            String.format(outputDeck,deck);
//            Toast.makeText(getApplicationContext(), outputDeck, Toast.LENGTH_LONG).show();

    }

    public void closeBetValuePopup(View v){
        super.onBackPressed();
    }


}
