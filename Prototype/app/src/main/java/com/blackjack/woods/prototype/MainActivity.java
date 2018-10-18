package com.blackjack.woods.prototype;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{



    //defines the navigation drawer
    private DrawerLayout mDrawerLayout;
    public int totalDeckNumber;
    public int totalBetNumber;
    public String deck;
    public String bet;

    public String deckSentance;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputscreen);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //declares width and heights
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;


        //set the layout to fullscreen
        getWindow().setLayout(width,height);



    }

    //this starts the listener to see if the menu items are clicked
    public void menu(){
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        setNavigationViewListener();
    }

    //if the menu button is clicked then open up the menu drawer
    public void menuButtonOnClick(View v){
        //opens up the sliding menu
        mDrawerLayout.openDrawer(Gravity.START);
    }

    //
    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationLayout);
        navigationView.setNavigationItemSelectedListener(this);
    }


    //this function runs when the menu is opened and items are clicked
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.betUnits: {

                Intent passDeck = new Intent(this,BetValuePopup.class);
                passDeck.putExtra("deckValue", deckSentance);
                startActivity(passDeck);
                break;
            }
            case R.id.cardsInShoe:{
                startActivity(new Intent(this,AmountOfCardsPopup.class));
                break;
            }
            case R.id.settings:{
                startActivity(new Intent(this,SettingsPopup.class));
                break;
            }
            case R.id.resetGame:{

                break;
            }
        }
        //close navigation drawer
        return true;
    }

    //will take picture of table when fully implemented
    public void pictureButtonOnClick(View v){
        //insert functionality
    }

    //will read entirety of table when fully implemented
    public void readTableButtonOnClick(View v){
        //insert functionality
    }


    //checks to see if the navigation is being used
    public void inputButtonClicked(View v){
        EditText deckInput = (EditText) findViewById(R.id.decksInput);
        deck = deckInput.getText().toString();
        totalDeckNumber =  Integer.parseInt(deck);

        EditText betInput = (EditText) findViewById(R.id.betInput);
        bet = betInput.getText().toString();
        totalBetNumber = Integer.parseInt(bet);

        deckSentance = "There are " + deck + " decks";

        Log.d("inputs", "inputButtonClicked: totalDeckNumber: " + totalDeckNumber + " totalBetNumber: " + totalBetNumber);
        setContentView(R.layout.activity_main);
        menu();
    }

}
