package com.blackjack.woods.prototype;

import android.os.Bundle;
import android.preference.PreferenceManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;


import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuItemView;
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
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.EditText;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;

import java.util.Locale;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , CameraBridgeViewBase.CvCameraViewListener2{

    private Blackjack game;
    private TextView test;
    //defines the navigation drawer
    private DrawerLayout mDrawerLayout;
    public int totalDeckNumber;
    public int totalBetNumber;
    public String deck;
    public String bet;

    public String deckSentence;

    CameraBridgeViewBase base;
    BaseLoaderCallback callback;
    Mat mat1, mat2, mat3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.inputscreen);

        //menu stuff
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //declares width and heights
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        //set the layout to fullscreen
        getWindow().setLayout(width,height);

        game = new Blackjack();
        test = findViewById(R.id.testView);

        //defining the menu
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        setNavigationViewListener();
        //end menu stuff

        //open cv stuff
        if (OpenCVLoader.initDebug()) {
            Toast.makeText(getApplicationContext(), "OpenCV ran successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Nope, it don't work", Toast.LENGTH_SHORT).show();
        }
        base = (JavaCameraView)findViewById(R.id.myCameraView);
        base.setVisibility(SurfaceView.VISIBLE);
        base.setCvCameraViewListener(this);
        callback = new BaseLoaderCallback(this) {
            @Override
            public void onManagerConnected(int status) {

                switch (status) {
                    case BaseLoaderCallback.SUCCESS:
                        base.enableView();
                        break;

                    default:
                        super.onManagerConnected(status);
                        break;
                }
            }
        };
        //end open cv stuff
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

    public void pictureButtonOnClick(View v){
        //insert functionality
    }

    public void readTableButtonOnClick(View v){
        //insert functionality
        game.setPlayerHandValue(16);
        game.readCards(new String[]{"AH", "3S", "TD", "TS", "QD","4H", "KD"});
        String str = String.format(Locale.getDefault(),
                "Chance to bust: %f",
                game.getChanceToBust());
        test.setText(str);

    }

    //
    private void setNavigationViewListener() {
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigationLayout);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.betUnits: {

                Intent passDeck = new Intent(this,BetValuePopup.class);
                passDeck.putExtra("deckValue", deckSentence);
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
                game.reset();
                test.setText(Integer.toString(game.getCount()));
                break;
            }
        }
        //close navigation drawer
        return true;
    }

    //checks to see if the navigation is being used
    public void inputButtonClicked(View v) {
        EditText deckInput = (EditText) findViewById(R.id.decksInput);
        deck = deckInput.getText().toString();
        totalDeckNumber = Integer.parseInt(deck);

        EditText betInput = (EditText) findViewById(R.id.betInput);
        bet = betInput.getText().toString();
        totalBetNumber = Integer.parseInt(bet);

        deckSentence = "There are " + deck + " decks";

        Log.d("inputs", "inputButtonClicked: totalDeckNumber: " + totalDeckNumber + " totalBetNumber: " + totalBetNumber);
        setContentView(R.layout.activity_main);
        menu();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame theFrame) {
        mat1 = theFrame.rgba();
        //Core.transpose(mat1, mat2);
        //Imgproc.resize(mat2, mat3, mat3.size(), 0, 0, 0);
        //Core.flip(mat3, mat1, 1);
        Mat gray = new Mat(), edges = new Mat(), dest = new Mat();
//        Imgproc.ctvColor(mat1, new Mat(), Imgproc.COLOR_RGBA2GRAY);
        Imgproc.cvtColor(mat1, gray, Imgproc.COLOR_RGBA2GRAY);
        Imgproc.blur(gray, edges, new org.opencv.core.Size(3,3));
        Imgproc.Canny(edges, edges, 100, 200, 3);
        Core.add(dest, Scalar.all(0), dest);
        mat1.copyTo(dest, edges);


        return dest;
    }

    @Override
    public void onCameraViewStopped() {
        mat1.release();
        mat2.release();
        mat3.release();
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mat1 = new Mat(width, height, CvType.CV_8UC4);
        mat2 = new Mat(width, height, CvType.CV_8UC4);
        mat3 = new Mat(width, height, CvType.CV_8UC4);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (base != null) {
            base.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Toast.makeText(getApplicationContext(), "Nope, it don't work again", Toast.LENGTH_SHORT).show();
        } else {
            callback.onManagerConnected(BaseLoaderCallback.SUCCESS);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (base != null) {
            base.disableView();
        }
    }

}
