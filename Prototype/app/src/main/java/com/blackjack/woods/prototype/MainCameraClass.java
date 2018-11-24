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
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class MainCameraClass extends MainActivity implements NavigationView.OnNavigationItemSelectedListener , CameraBridgeViewBase.CvCameraViewListener2{

    //defining the menu variables
    private DrawerLayout mDrawerLayout;
    //open cv variables
    CameraBridgeViewBase base;
    BaseLoaderCallback callback;
    Mat mat1, mat2, mat3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set content view to activity_main so that findViewByID finds the correct references
        setContentView(R.layout.activity_main);

        View decorView = getWindow().getDecorView();
// Hide the status bar.
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);


        //defining the menu
        mDrawerLayout = findViewById(R.id.drawer);
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
        NavigationView navigationView = findViewById(R.id.navigationLayout);
        navigationView.setNavigationItemSelectedListener(this);
    }


    //this function runs when the menu is opened and items are clicked
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        switch (item.getItemId()) {

            case R.id.betUnits: {
                startActivity(new Intent(MainCameraClass.this,BetUnitPopup.class));
                break;
            }
            case R.id.cardsInShoe:{
                startActivity(new Intent(MainCameraClass.this,DeckUnitsPopup.class));
                break;
            }
            case R.id.settings:{
                startActivity(new Intent(MainCameraClass.this,SettingsPopup.class));
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

        //takes the camera frame
        mat1 = theFrame.rgba();

        //defines the mats to use to manipulate the mat1 frame image
        Mat gray = new Mat();
        Mat edges = new Mat();
        Mat dest = new Mat();

//      Core.transpose(mat1, mat2);
//      Imgproc.resize(mat2, mat3, mat3.size(), 0, 0, 0);
//      Core.flip(mat3, mat1, 1);
//      Imgproc.ctvColor(mat1, new Mat(), Imgproc.COLOR_RGBA2GRAY);

        //turns mat1s frame image into black and white and saves it into the gray mat
        Imgproc.cvtColor(mat1, gray, Imgproc.COLOR_RGBA2GRAY);

        //blurs the gray mat and saves it as the edges mat
        Imgproc.blur(gray, edges, new org.opencv.core.Size(3,3));

        //this function finds all of the edges within the edges and re-writes itself to show found contours
        Imgproc.Canny(edges, edges, 100, 200, 3);
        Core.add(dest, Scalar.all(0), dest);

        //mask the edges found in the Canny function to the dest mat
        mat1.copyTo(dest,edges);

        //copy the dest mat to mat1
        dest.copyTo(mat1);

        //releases the data within the mats which was causing the memory leak
        edges.release();
        dest.release();
        gray.release();

        //return mat1 instead of dest so that we can release the values in the dest mat
        return mat1;
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
