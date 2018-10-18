package com.blackjack.woods.prototype;

import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Size;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.TextView;

import java.util.Collections;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{


    //declaring class variables
    private DrawerLayout mDrawerLayout;
    private Blackjack game;
    private TextView test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        //Initialize Blackjack state keeper
        game = new Blackjack();

        test = findViewById(R.id.testView);

        //declares width and heights
        int width = displayMetrics.widthPixels;
        int hieght = displayMetrics.heightPixels;

        //set the layout to fullscreen
        getWindow().setLayout(width,hieght);


        //defining the menu
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer);
        setNavigationViewListener();

    }



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

    //checks to see if the navigation is being used
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
                startActivity(new Intent(MainActivity.this,Pop.class));
                break;
            }
            case R.id.cardsInShoe:{

                break;
            }
            case R.id.settings:{

                break;
            }
            case R.id.resetGame:{
                game.reset();
                test.setText(Integer.toString(game.getCount()));
                break;
            }
        }
        //close navigation drawer
        mDrawerLayout.closeDrawer(Gravity.START);
        return true;
    }

}
