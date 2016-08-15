package com.bethyueshi.oasis2;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;


import com.viewpagerindicator.IconPageIndicator;

public class MapActivity extends FragmentActivity {


    private MapPagerAdapter mMapPagerAdaper;
    private ViewPager mViewPager;
    public static final int test0 = 0;
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 2;
    private static final String TAG = "Map";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        checkLocationPermission();

        //Set the pager with an adapter
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setAdapter(new MapPagerAdapter(getSupportFragmentManager()));

        //Bind the icon indicator to the adapter
        IconPageIndicator iconIndicator = (IconPageIndicator)findViewById(R.id.icons);
        iconIndicator.setViewPager(mViewPager);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MapActivity.this, SelectTest.class);
                intent.putExtra("test_num", test0);
                startActivity(intent);
            }
        });
    }

    private void checkLocationPermission() {
        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {//||
                //ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                //ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_LOCATION_EXTRA_COMMANDS) != PackageManager.PERMISSION_GRANTED) {


            //} else {
            Log.d(TAG, "no permission request");

            // No explanation needed, we can request the permission.

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION);

            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
            // app-defined int constant. The callback method gets the
            // result of the request.
            //}
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    // Create an instance of Camere
                    //getCameraInstance();
                    Log.d(TAG, "try location done");

                } else {
                    //TODO
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                Log.d(TAG, "get permission");
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}


