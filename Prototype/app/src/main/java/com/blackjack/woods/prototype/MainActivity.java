package com.blackjack.woods.prototype;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.core.Core;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener , CameraBridgeViewBase.CvCameraViewListener2{


    //defining the menu variables
    private DrawerLayout mDrawerLayout;
    //open cv variables
    CameraBridgeViewBase base;
    BaseLoaderCallback callback;
    Mat mat1, mat2, mat3;

    private static final int CAMERA_REQUEST = 1888;


    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get camera permissions

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED)
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST);


        //menu stuff
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            //declares width and heights
            int width = displayMetrics.widthPixels;
            int hieght = displayMetrics.heightPixels;

            //set the layout to fullscreen
            getWindow().setLayout(width,hieght);


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
                startActivity(new Intent(MainActivity.this,BetUnitPopup.class));
                break;
            }
            case R.id.cardsInShoe:{
                startActivity(new Intent(MainActivity.this,DeckUnitsPopup.class));
                break;
            }
            case R.id.settings:{
                startActivity(new Intent(MainActivity.this,SettingsPopup.class));
                break;
            }
            case R.id.resetGame:{

                break;
            }
        }
        return true;
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
